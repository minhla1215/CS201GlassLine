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

	
	
	
	
	
	
	//Scheduler///////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public boolean pickAndExecuteAnAction() {
		int glassOnConveyor = glassPanes.size() + conveyor.glassPanes.size() + conveyor.frontSensor.glassPanes.size();
		
		if(sensorState == SensorState.RELEASED && glassPanes.isEmpty() && glassOnConveyor <= 2){
			sendIAmAvailable();
			sensorState = SensorState.DONOTHING;
			return true;
		}
		
//		if(conveyor.glassPanes.size() <= conveyor.conveyorCapacity){
//			stopConveyor();
//		}
		
		if(sensorState == SensorState.PRESSED && passingGlass){
			moveConveyor();
			sensorState = SensorState.DONOTHING;
			return true;
		}
		
		if(sensorState == SensorState.RELEASED && passingGlass && !glassPanes.isEmpty()){
			passGlass();
			return true;
		}
		
		return false;
	}


	
	
	
	
	//Actions///////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	void sendIAmAvailable(){
		inlineMachine.msgIAmAvailable();
	}
	
	
	void passGlass(){
		if(!glassPanes.isEmpty()){
			System.out.println(name + " passed glass.");
			conveyor.msgPassingGlass(glassPanes.remove());
			passingGlass = false;
			sensorState = SensorState.RELEASED;
		}
	}
	
	
	void moveConveyor(){
		Object[] arg = new Object[1];
		arg[0] = conveyor.conveyorNumber;
		transducer.fireEvent(TChannel.CONVEYOR, TEvent.CONVEYOR_DO_START, arg);
	}
	
	
	void stopConveyor(){
		Object[] arg = new Object[1];
		arg[0] = conveyor.conveyorNumber;
		transducer.fireEvent(TChannel.CONVEYOR, TEvent.CONVEYOR_DO_STOP, arg);
	}

	
	
	
	
	
	
	//EVENTS FIRED ///////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void eventFired(TChannel channel, TEvent event, Object[] args) {
		if (channel == TChannel.SENSOR && event == TEvent.SENSOR_GUI_PRESSED)
		{
			if (((Integer)args[0]) == sensorNumber){
				sensorState = SensorState.PRESSED;
				stateChanged();
				
				
				//This is for testing purposes
				if(sensorNumber == 29){
					if(glassPanes.peek().getInlineMachineProcessingHistory(3)){
						System.out.println(" Washer has processed the glass.");
					}
					if(glassPanes.peek().getInlineMachineProcessingHistory(4)){
						System.out.println(" Painter has processed the glass.");
					}
					if(glassPanes.peek().getInlineMachineProcessingHistory(5)){
						System.out.println(" UV_Lamp has processed the glass.");
					}
					if(glassPanes.peek().getInlineMachineProcessingHistory(6)){
						System.out.println(" Oven has processed the glass.");
					}
				}
				
			}
		}
		else if (channel == TChannel.SENSOR && event == TEvent.SENSOR_GUI_RELEASED)
		{
			if (((Integer)args[0]) == sensorNumber){
				//System.out.println("Sensor Released: " + sensorNumber);
				sensorState = SensorState.RELEASED;
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






	@Override
	public void msgIAmNotAvailable() {
		// TODO Auto-generated method stub
		
	}
}




