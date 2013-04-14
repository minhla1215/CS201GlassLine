package engine.josh.agent;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

import transducer.TChannel;
import transducer.TEvent;
import transducer.Transducer;
import transducer.TransducerDebugMode;
import engine.agent.Agent;
import engine.interfaces.ConveyorFamily;
import engine.util.GlassType;
import engine.interfaces.JoshConveyor;
import engine.interfaces.JoshFrontSensor;
import engine.josh.agent.JoshBackSensorAgent.SensorState;
import engine.josh.agent.JoshConveyorAgent.ConveyorState;
import engine.josh.agent.JoshInlineMachineAgent.MachineState;


public class JoshFrontSensorAgent extends Agent implements ConveyorFamily, JoshFrontSensor{

	public ConveyorFamily inlineMachine;
	public JoshConveyorAgent conveyor;
	public Queue<GlassType> glassPanes;
	public Boolean passingGlass;
	public Boolean conveyorStopped;
	public int sensorNumber;
	enum SensorState{PRESSED, RELEASED, DONOTHING};
	SensorState sensorState;
	public Transducer transducer;
	Object[] args;

	
	
	
	public JoshFrontSensorAgent(String n, int sNum, Transducer t){
		inlineMachine = null;
		conveyor = null;
		glassPanes = new LinkedList<GlassType>();
		passingGlass = false;
		conveyorStopped = false;
		name = n;
		sensorNumber = sNum;
		sensorState = SensorState.RELEASED;
		transducer = t;
		transducer.register(this, TChannel.SENSOR);
	}
	
	
	
	
	
	
	//Messages///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	public void msgPassingGlass(GlassType gt) {
		glassPanes.add(gt);
		sensorState = SensorState.PRESSED;
		stateChanged();
	}


	public void msgIAmAvailable() {
		passingGlass = true;
		stateChanged();
	}

	
	
	
	
	
	//Scheduler///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	public boolean pickAndExecuteAnAction() {
		
		if(sensorState == SensorState.RELEASED && glassPanes.isEmpty()){
			sendIAmAvailable();
			sensorState = SensorState.DONOTHING;
			return true;
		}


		if(sensorState == SensorState.PRESSED && passingGlass){
			moveConveyor();
			sensorState = SensorState.DONOTHING;
			return true;
		}
		

//		if(inlineMachine.machineState == MachineState.LOADED && !conveyorStopped){
//			stopConveyor();
//			return true;
//		}
		
		
		if(sensorState == SensorState.RELEASED && !glassPanes.isEmpty()){
			if(passingGlass){
				moveConveyor();
				passGlass();
			}
			else
			{
				if(!conveyorStopped) {
					stopConveyor();
				}
			}

			return true;
		}

		
		return false;
	}


	
	
	
	
	
	//Actions///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	void sendIAmAvailable(){
		conveyor.msgIAmAvailable();
	}
	
	void passGlass(){
		if(!glassPanes.isEmpty()){
			System.out.println(name + " passed glass.");
			inlineMachine.msgPassingGlass(glassPanes.remove());
			sensorState = SensorState.RELEASED;
			passingGlass = false;
		}
	}
	
	void moveConveyor(){
		conveyorStopped = false;
		
		Object[] arg = new Object[1];
		arg[0] = conveyor.conveyorNumber;
		transducer.fireEvent(TChannel.CONVEYOR, TEvent.CONVEYOR_DO_START, arg);
	}
	
	void stopConveyor(){
		conveyorStopped = true;
		
		Object[] arg = new Object[1];
		arg[0] = conveyor.conveyorNumber;
		transducer.fireEvent(TChannel.CONVEYOR, TEvent.CONVEYOR_DO_STOP, arg);
	}

	
	
	
	
	
	
	//TRECIEVER STUFF///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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
				sensorState = SensorState.RELEASED;
				stateChanged();
			}
		}
	}

	
	
	
	
	
	

	
	//Extra Functions
	
	public void set_inlineMachine(ConveyorFamily i){
		inlineMachine = i;
	}
	
	public void set_conveyor(JoshConveyorAgent c){
		conveyor = c;
	}
}




