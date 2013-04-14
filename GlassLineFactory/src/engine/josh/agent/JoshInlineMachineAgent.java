package engine.josh.agent;

import java.util.LinkedList;
import java.util.Queue;

import transducer.TChannel;
import transducer.TEvent;
import transducer.Transducer;
import transducer.TransducerDebugMode;
import engine.agent.Agent;
import engine.interfaces.ConveyorFamily;
import engine.util.GlassType;
import engine.interfaces.JoshBackSensor;
import engine.interfaces.JoshFrontSensor;

public class JoshInlineMachineAgent extends Agent implements ConveyorFamily{

	public JoshFrontSensorAgent frontSensor;
	public JoshBackSensorAgent backSensor;
	public Queue<GlassType> glassPanes;
	public Boolean passingGlass;
	public Boolean glassInWorkstation;
	public Boolean glassReleased;
	public Boolean glassPaneProcessed;
	public Boolean isACorner;
	public int machineNumber;
	public TChannel myTChannel;
	
	public Transducer transducer;
	Object[] args;

	public JoshInlineMachineAgent(Boolean isCorner, TChannel tChannel, String n, int mNum, Transducer t){
		isACorner = isCorner;
		frontSensor = null;
		backSensor = null;
		glassPanes = new LinkedList<GlassType>();
		passingGlass = false;
		glassInWorkstation = false;
		glassReleased = true;
		glassPaneProcessed = false;
		myTChannel = tChannel;
		name = n;
		machineNumber = mNum;
		
		transducer = t;
		transducer.register(this, myTChannel);
	}
	
	
	
	//Messages

	public void msgPassingGlass(GlassType gt) {
		glassPanes.add(gt);
		glassReleased = false;
		stateChanged();
	}

	public void msgIAmAvailable() {
		passingGlass = true;
		stateChanged();	
	}
	
	
	//Scheduler
	
	public boolean pickAndExecuteAnAction() {
		if(!isACorner){
			if(glassPanes.isEmpty()){
				checkFrontSensor();
			}
			if(!glassPanes.isEmpty() && glassInWorkstation && !glassPaneProcessed){
				ProcessGlass();
			}
			if(glassPaneProcessed){
				ReleaseGlass();
			}
			if(glassReleased && passingGlass){
				passGlass();
			}
			return false;
		}
		else{
			if(glassPanes.isEmpty()){
				checkFrontSensor();
			}
			if(!glassPanes.isEmpty()){
				passGlass();
			}
			return false;
		}

	}

	
	
	//Actions
	
	void checkFrontSensor(){
		frontSensor.msgIAmAvailable();
	}
	
	void ProcessGlass(){
		//BackEnd
		glassPanes.peek().setInlineMachineProcessingHistory(machineNumber);
		
		//FrontEnd
		Object[] arg = new Object[1];
		arg[0] = frontSensor.sensorNumber;
		transducer.fireEvent(myTChannel, TEvent.WORKSTATION_DO_ACTION, arg);
	}
	
	void ReleaseGlass(){
		glassPaneProcessed = false;
		
		Object[] arg = new Object[1];
		arg[0] = frontSensor.sensorNumber;
		transducer.fireEvent(myTChannel, TEvent.WORKSTATION_RELEASE_GLASS, arg);
	}
	
	void passGlass(){
		if(!glassPanes.isEmpty()){
			backSensor.msgPassingGlass(glassPanes.remove());
			glassReleased = false;
		}
	}
	
	
	
	
	
	
	
	
	public void eventFired(TChannel channel, TEvent event, Object[] args) {
		if (channel == myTChannel && event == TEvent.WORKSTATION_DO_ACTION)
		{
				//System.out.println("WORKSTATION_DO_ACTION: " + myTChannel.toString());
		}
		else if (channel == myTChannel && event == TEvent.WORKSTATION_RELEASE_GLASS)
		{
				//System.out.println("WORKSTATION_RELEASE_GLASS: " + myTChannel.toString());
				glassReleased = true;
				glassInWorkstation = false;
				stateChanged();
		}
		else if (channel == myTChannel && event == TEvent.WORKSTATION_GUI_ACTION_FINISHED)
		{
				//System.out.println("WORKSTATION_GUI_ACTION_FINISHED: " + myTChannel.toString());
				glassPaneProcessed = true;
				stateChanged();
		}
		else if (channel == myTChannel && event == TEvent.WORKSTATION_DO_LOAD_GLASS)
		{
				//System.out.println("WORKSTATION_DO_LOAD_GLASS: " + myTChannel.toString());
		}
		else if (channel == myTChannel && event == TEvent.WORKSTATION_LOAD_FINISHED)
		{
				//System.out.println("WORKSTATION_LOAD_FINISHED: " + myTChannel.toString());
				System.out.println("Glass " + glassPanes.peek().getGlassID() + " is in Workstation " + name);
				glassInWorkstation = true;
				stateChanged();
		}
		else if (channel == myTChannel && event == TEvent.WORKSTATION_RELEASE_FINISHED)
		{
				//System.out.println("WORKSTATION_RELEASE_FINISHED: " + myTChannel.toString());
				stateChanged();
		}

	}
	
	
	//Extra Functions
	
	public void set_frontSensor(JoshFrontSensorAgent fs){
		frontSensor = fs;
	}
	
	public void set_backSensor(JoshBackSensorAgent bs){
		backSensor = bs;
	}
	

}
