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
import engine.josh.agent.JoshFrontSensorAgent.SensorState;

public class JoshInlineMachineAgent extends Agent implements ConveyorFamily{

	public JoshFrontSensorAgent frontSensor;
	public JoshBackSensorAgent backSensor;
	public Queue<GlassType> glassPanes;
	public Boolean passingGlass;
	public Boolean releaseGlass;
	public Boolean glassPaneProcessed;
	public Boolean isACorner;
	public Boolean GUIPass;
	public int machineNumber;
	public TChannel myTChannel;
	public enum MachineState{LOADED, UNLOADED, DONOTHING};
	public MachineState machineState;
	public Transducer transducer;
	Object[] args;

	
	
	public JoshInlineMachineAgent(Boolean isCorner, TChannel tChannel, String n, int mNum, Transducer t){
		isACorner = isCorner;
		frontSensor = null;
		backSensor = null;
		glassPanes = new LinkedList<GlassType>();
		passingGlass = false;
		releaseGlass = false;
		glassPaneProcessed = true;
		GUIPass = false;
		myTChannel = tChannel;
		name = n;
		machineNumber = mNum;
		machineState = MachineState.UNLOADED;
		transducer = t;
		transducer.register(this, myTChannel);
	}
	
	
	
	
	
	
	
	//Messages///////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void msgPassingGlass(GlassType gt) {
		glassPanes.add(gt);
		machineState = MachineState.LOADED;
		stateChanged();
	}

	
	public void msgIAmAvailable() {
		passingGlass = true;
		stateChanged();	
	}
	
	
	
	
	
	
	
	//Scheduler///////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public boolean pickAndExecuteAnAction() {
		if(!isACorner){
			System.out.println(name + " " + machineState.toString());
			if(	machineState == MachineState.UNLOADED && glassPanes.isEmpty()){
				checkFrontSensor();
				machineState = MachineState.DONOTHING;
				return true;
			}
			
			if(machineState == MachineState.LOADED && !glassPaneProcessed){
				ProcessGlass();
				machineState = MachineState.DONOTHING;
				return true;
			}
			
			if(machineState == MachineState.LOADED && releaseGlass){
					passGlass();
					ReleaseGlass();
					//machineState = MachineState.UNLOADED;
				return true;
			}
		}
		
		else{
			if(	machineState == MachineState.UNLOADED && glassPanes.isEmpty()){
				checkFrontSensor();
				machineState = MachineState.DONOTHING;
				return true;
			}
			
			if(machineState == MachineState.LOADED && passingGlass){
				passGlass();
				ReleaseGlass();
				machineState = MachineState.UNLOADED;
				
				passingGlass = false;
				releaseGlass = false;
				glassPaneProcessed = true;
				
				return true;
			}
		}
		
		return false;

	}

	
	
	
	
	
	
	//Actions///////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	void checkFrontSensor(){
		frontSensor.msgIAmAvailable();
	}
	

	void ProcessGlass(){
		if(glassPanes.peek().getinlineMachineProcessingNeeded()[machineNumber]){
			glassPanes.peek().setInlineMachineProcessingHistory(machineNumber);
			glassPaneProcessed = true;

			Object[] arg = new Object[1];
			arg[0] = frontSensor.sensorNumber;
			transducer.fireEvent(myTChannel, TEvent.WORKSTATION_DO_ACTION, arg);
		}
		else{
			ReleaseGlass();
		}
	}
	
	
	void ReleaseGlass(){
		releaseGlass = false;

		Object[] arg = new Object[1];
		arg[0] = frontSensor.sensorNumber;
		transducer.fireEvent(myTChannel, TEvent.WORKSTATION_RELEASE_GLASS, arg);
	}
	
	
	void passGlass(){
		if(!glassPanes.isEmpty()){
			System.out.println(name + " passed glass.");
			backSensor.msgPassingGlass(glassPanes.remove());
			passingGlass = false;
		}
	}
	
	
	
	
	
	//TRECIEVER EVENTS///////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void eventFired(TChannel channel, TEvent event, Object[] args) {
		if (channel == myTChannel && event == TEvent.WORKSTATION_LOAD_FINISHED)
		{
				System.out.println("WORKSTATION_LOAD_FINISHED: " + myTChannel.toString());
				glassPaneProcessed = false;
				//machineState = MachineState.LOADED;
				stateChanged();
		}
		else if (channel == myTChannel && event == TEvent.WORKSTATION_GUI_ACTION_FINISHED)
		{
				System.out.println("WORKSTATION_GUI_ACTION_FINISHED: " + myTChannel.toString());
				machineState = MachineState.LOADED;
				releaseGlass = true;
				stateChanged();
		}
		else if (channel == myTChannel && event == TEvent.WORKSTATION_RELEASE_FINISHED)
		{		
				machineState = MachineState.UNLOADED;
				stateChanged();
		}

	}
	
	
	
	
	
	//Extra Functions///////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void set_frontSensor(JoshFrontSensorAgent fs){
		frontSensor = fs;
	}
	
	public void set_backSensor(JoshBackSensorAgent bs){
		backSensor = bs;
	}
	
	public boolean get_GUIPass(){
		return GUIPass;
	}
	

}
