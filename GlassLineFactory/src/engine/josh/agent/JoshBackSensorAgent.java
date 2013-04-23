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
import engine.interfaces.JoshConveyor;
import engine.interfaces.JoshFrontSensor;
import engine.josh.agent.JoshConveyorAgent.ConveyorState;


public class JoshBackSensorAgent extends Agent implements ConveyorFamily, JoshFrontSensor{
	
	ConveyorFamily inlineMachine;
	public JoshConveyorAgent conveyor;
	public Queue<GlassType> glassPanes;
	public Boolean passingGlass;
	int sensorNumber;
	public Boolean sensorPressed = false;
	public Boolean conveyorMoving = false;
	public Boolean sentIAmAvailable = false;
	enum SensorState{PRESSED, RELEASED, DONOTHING};
	SensorState sensorState;
	public Transducer transducer;
	Object[] args;
	
	
	public JoshBackSensorAgent(String n, int sNum, Transducer t){
		inlineMachine = null;
		conveyor = null;
		glassPanes = new LinkedList<GlassType>();
		passingGlass = false;
		name = n;
		sensorNumber = sNum;
		sensorState = SensorState.RELEASED;
		transducer = t;
		transducer.register(this, TChannel.SENSOR);
	}
	
	
	//Messages///////////////////////////////////////////////////////////////////////////////////////////////////////////
	

	public void msgPassingGlass(GlassType gt) {
		glassPanes.add(gt);
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
	
	public void msgFrontSensorReleased(){
		stateChanged();
	}
	
	public void msgConveyorChangedState(){
		stateChanged();
	}

	
	//Scheduler///////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public boolean pickAndExecuteAnAction() {
		
		//This part of the Scheduler is specific to corners.////////////////
		if(sensorNumber == 20 || sensorNumber == 26 || sensorNumber == 16){
			if(!conveyor.isMoving){
				if(sentIAmAvailable){
					sendIAmNotAvailable();
					return true;
				}
			}

			if(conveyor.isMoving && !conveyor.frontSensor.sensorPressed){
				if(!sentIAmAvailable){
					sendIAmAvailable();
					return true;
				}
			}
			
//			if(glassPanes.isEmpty() && conveyor.glassPanes.isEmpty() && conveyor.frontSensor.glassPanes.isEmpty()){
//				if(!sentIAmAvailable){
//					sendIAmAvailable();
//					if(!conveyor.isMoving){
//						moveConveyor();
//					}
//					return true;
//				}
//			}
		}
		////////////////////////////////////////////////////////////////////	
		
		
		
		
		
		if(sensorState == SensorState.RELEASED ){
				sendIAmAvailable();
				sensorState = SensorState.DONOTHING;
				return true;
		}
		
		
		if(sensorState == SensorState.PRESSED && conveyorMoving && !passingGlass){
			stopConveyor();
			sensorState = SensorState.DONOTHING;
			return true;
		}
		
		
		if(sensorState == SensorState.PRESSED && passingGlass){
			if(!conveyorMoving){
				moveConveyor();
			}
			passGlass();
			sensorState = SensorState.RELEASED;
			return true;
		}
		
		return false;
	}


	
	
	
	
	//Actions///////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	void sendIAmAvailable(){
		System.out.println(name + " sendIAmAvailable");
		inlineMachine.msgIAmAvailable();
		sentIAmAvailable = true;

	}
	
	void sendIAmNotAvailable(){
		System.out.println(name + " sendIAmNotAvailable");
		inlineMachine.msgIAmNotAvailable();
		sentIAmAvailable = false;
		

	}
	
	
	void passGlass(){
		if(!glassPanes.isEmpty()){
				System.out.println(name + " passGlass");
			conveyor.msgPassingGlass(glassPanes.remove());
			passingGlass = false;
			

		}
	}
	
	
	void moveConveyor(){
		System.out.println(name + " moveConveyor");
		conveyorMoving = true;
		
		Object[] arg = new Object[1];
		arg[0] = conveyor.conveyorNumber;
		transducer.fireEvent(TChannel.CONVEYOR, TEvent.CONVEYOR_DO_START, arg);
		
		conveyor.set_isMoving(true);
		

	}
	
	
	void stopConveyor(){
		System.out.println(name + " stopConveyor");
		
		conveyorMoving = false;
		
		Object[] arg = new Object[1];
		arg[0] = conveyor.conveyorNumber;
		transducer.fireEvent(TChannel.CONVEYOR, TEvent.CONVEYOR_DO_STOP, arg);
		
		conveyor.set_isMoving(false);
	}

	
	
	
	
	
	
	//EVENTS FIRED ///////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void eventFired(TChannel channel, TEvent event, Object[] args) {
		if (channel == TChannel.SENSOR && event == TEvent.SENSOR_GUI_PRESSED)
		{
			if (((Integer)args[0]) == sensorNumber){
				sensorState = SensorState.PRESSED;
				sensorPressed = true;
				stateChanged();
				
			}
		}
		else if (channel == TChannel.SENSOR && event == TEvent.SENSOR_GUI_RELEASED)
		{
			if (((Integer)args[0]) == sensorNumber){;
				sensorState = SensorState.RELEASED;
				sensorPressed = false;
				stateChanged();
			}
		}
		
	}

	
	
	
	//Extra Functions///////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void set_inlineMachine(ConveyorFamily i){
		inlineMachine = i;
	}
	
	public void set_conveyor(JoshConveyorAgent c){
		conveyor = c;
	}





}




