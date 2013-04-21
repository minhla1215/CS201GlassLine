<<<<<<< HEAD
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
	public GlassType theGlass;
	public Boolean passingGlass;
	public Boolean releaseGlass;
	public Boolean glassPaneProcessed;
	public Boolean isACorner;
	public Boolean machineIsEmpty;
	public int machineNumber;
	public TChannel myTChannel;
	public enum MachineState{LOADING, UNLOADING, EMPTY, DONOTHING};
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
		glassPaneProcessed = false;
		machineIsEmpty = true;
		myTChannel = tChannel;
		name = n;
		machineNumber = mNum;
		machineState = MachineState.EMPTY;
		transducer = t;
		transducer.register(this, myTChannel);
	}
	
	
	
	
	
	
	
	//Messages///////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void msgPassingGlass(GlassType gt) {
		glassPanes.add(gt);
		machineIsEmpty = false;
		if(isACorner){
			machineState = MachineState.LOADING;
		}
		stateChanged();
	}

	
	public void msgIAmAvailable() {
		passingGlass = true;
		stateChanged();	
	}
	
	
	public void msgIAmNotAvailable(){
		passingGlass = false;
		stateChanged();
	}
	
	
	
	
	
	
	
	//Scheduler///////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public boolean pickAndExecuteAnAction() {
		if(!isACorner){
			
			if(	machineState == MachineState.EMPTY && glassPanes.isEmpty() && machineIsEmpty){
				if(!backSensor.sensorPressed){
					checkFrontSensor();
					machineState = MachineState.DONOTHING;
				}				
				else{					
						frontSensor.msgIAmNotAvailable();
				}
				return true;
			}
			
			
			if(machineState == MachineState.LOADING && !glassPaneProcessed){
				if(glassPanes.peek().getinlineMachineProcessingNeeded()[machineNumber]){
					ProcessGlass();
					machineState = MachineState.DONOTHING;
				}
				else{
					ReleaseGlass();
					passGlass();
					machineState = MachineState.EMPTY;
				}
				return true;
			}
			
			
			if(machineState == MachineState.LOADING && releaseGlass && passingGlass){
				ReleaseGlass();
				passGlass();
				machineState = MachineState.EMPTY;
				return true;
			}
		}
		
		else{
			if(	machineState == MachineState.EMPTY && glassPanes.isEmpty() && passingGlass){
				if(!backSensor.sensorPressed && !backSensor.conveyor.frontSensor.sensorPressed){
					checkFrontSensor();
					System.out.println(name + " sent I am available");
					machineState = MachineState.DONOTHING;
				}
				else{
						frontSensor.msgIAmNotAvailable();
				}
				return true;
			}
			
			if(machineState == MachineState.LOADING && passingGlass){
				passGlass();
				ReleaseGlass();
				machineState = MachineState.EMPTY;
				
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

			transducer.fireEvent(myTChannel, TEvent.WORKSTATION_DO_ACTION, null);
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
				glassPaneProcessed = false;
				machineState = MachineState.LOADING;
				stateChanged();
		}
		else if (channel == myTChannel && event == TEvent.WORKSTATION_GUI_ACTION_FINISHED)
		{
				machineState = MachineState.LOADING;
				releaseGlass = true;
				stateChanged();
		}
		else if (channel == myTChannel && event == TEvent.WORKSTATION_RELEASE_FINISHED)
		{		
				machineState = MachineState.EMPTY;
				machineIsEmpty = true;
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
	



	

}
=======
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
	public GlassType theGlass;
	public Boolean passingGlass;
	public Boolean releaseGlass;
	public Boolean glassPaneProcessed;
	public Boolean isACorner;
	public Boolean machineIsEmpty;
	public int machineNumber;
	public TChannel myTChannel;
	public enum MachineState{LOADING, UNLOADING, EMPTY, DONOTHING};
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
		glassPaneProcessed = false;
		machineIsEmpty = true;
		myTChannel = tChannel;
		name = n;
		machineNumber = mNum;
		machineState = MachineState.EMPTY;
		transducer = t;
		transducer.register(this, myTChannel);
	}
	
	
	
	
	
	
	
	//Messages///////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void msgPassingGlass(GlassType gt) {
		glassPanes.add(gt);
		machineIsEmpty = false;
		if(isACorner){
			machineState = MachineState.LOADING;
		}
		stateChanged();
	}

	
	public void msgIAmAvailable() {
		passingGlass = true;
		stateChanged();	
	}
	
	
	public void msgIAmNotAvailable(){
		passingGlass = false;
		stateChanged();
	}
	
	
	
	
	
	
	
	//Scheduler///////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public boolean pickAndExecuteAnAction() {
//		if(!isACorner){
			
			if(	machineState == MachineState.EMPTY && glassPanes.isEmpty() && machineIsEmpty){
				if(!backSensor.sensorPressed){
					sendIAmAvailable();
					machineState = MachineState.DONOTHING;
				}				
				else{					
						frontSensor.msgIAmNotAvailable();
				}
				return true;
			}
			
			
			if(machineState == MachineState.LOADING && !glassPaneProcessed){
					if(glassPanes.peek().getinlineMachineProcessingNeeded()[machineNumber]){
						ProcessGlass();
						machineState = MachineState.DONOTHING;
					}
					else{
						ReleaseGlass();
						passGlass();
						machineState = MachineState.EMPTY;
					}
					return true;
			}
			
			
			if(machineState == MachineState.LOADING && releaseGlass && passingGlass){
				ReleaseGlass();
				passGlass();
				machineState = MachineState.EMPTY;
				return true;
			}
//		}
//		
//		else{
//			if(	machineState == MachineState.EMPTY && glassPanes.isEmpty() && passingGlass){
//				if(/*!backSensor.sensorPressed &&*/ !backSensor.conveyor.frontSensor.sensorPressed){
//					checkFrontSensor();
//					System.out.println(name + " sent I am available");
//					machineState = MachineState.DONOTHING;
//				}
//				else{
//						frontSensor.msgIAmNotAvailable();
//				}
//				return true;
//			}
//			
//			if(machineState == MachineState.LOADING && passingGlass){
//				passGlass();
//				ReleaseGlass();
//				machineState = MachineState.EMPTY;
//				
//				return true;
//			}
//			if(machineState == MachineState.EMPTY && !backSensor.sensorPressed){
//				checkFrontSensor();
//				machineState = MachineState.UNLOADING;
//				return true;
//			}
//			
//			if(machineState == MachineState.UNLOADING && passingGlass){
//				passGlass();
//				machineState = MachineState.EMPTY;
//				return true;
//			}
			
//		}
		
		return false;

	}

	
	
	
	
	
	
	//Actions///////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	void sendIAmAvailable(){
			frontSensor.msgIAmAvailable();
			
			System.out.println(name + " sendIAmAvailable");
	}
	

	void ProcessGlass(){
		if(glassPanes.peek().getinlineMachineProcessingNeeded()[machineNumber]){
			glassPanes.peek().setInlineMachineProcessingHistory(machineNumber);
			glassPaneProcessed = true;

			transducer.fireEvent(myTChannel, TEvent.WORKSTATION_DO_ACTION, null);
			
			System.out.println(name + " ProcessGlass");
		}
	}
	
	
	void ReleaseGlass(){
		releaseGlass = false;

		Object[] arg = new Object[1];
		arg[0] = frontSensor.sensorNumber;
		transducer.fireEvent(myTChannel, TEvent.WORKSTATION_RELEASE_GLASS, arg);
		
		System.out.println(name + " ReleaseGlass");
	}
	
	
	void passGlass(){
		if(!glassPanes.isEmpty()){
			backSensor.msgPassingGlass(glassPanes.remove());
			passingGlass = false;
			
			System.out.println(name + " passGlass");
		}
	}
	
	
	
	
	
	//TRECIEVER EVENTS///////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void eventFired(TChannel channel, TEvent event, Object[] args) {
		if (channel == myTChannel && event == TEvent.WORKSTATION_LOAD_FINISHED)
		{
				glassPaneProcessed = false;
				machineState = MachineState.LOADING;
				stateChanged();
		}
		else if (channel == myTChannel && event == TEvent.WORKSTATION_GUI_ACTION_FINISHED)
		{
				machineState = MachineState.LOADING;
				releaseGlass = true;
				stateChanged();
		}
		else if (channel == myTChannel && event == TEvent.WORKSTATION_RELEASE_FINISHED)
		{		
				machineState = MachineState.EMPTY;
				machineIsEmpty = true;
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
	



	

}
>>>>>>> ab4962e3bd3570a31346e874501257143bf83586
