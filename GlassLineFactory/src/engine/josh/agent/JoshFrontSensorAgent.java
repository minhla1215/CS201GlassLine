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


public class JoshFrontSensorAgent extends Agent implements ConveyorFamily, JoshFrontSensor{

	int glassCapacity;
	public JoshInlineMachineAgent inlineMachine;
	public JoshConveyorAgent conveyor;
	public Queue<GlassType> glassPanes;
	public Boolean atCapacity;
	public Boolean passingGlass;
	public Boolean sensorPressed;
	public int sensorNumber;
	
	public Transducer transducer;
	Object[] args;

	
	
	
	public JoshFrontSensorAgent(String n, int sNum, Transducer t){
		glassCapacity = 1;
		inlineMachine = null;
		conveyor = null;
		glassPanes = new LinkedList<GlassType>();
		atCapacity = false;
		passingGlass = false;
		sensorPressed = false;
		name = n;
		sensorNumber = sNum;
		
		transducer = t;
		transducer.register(this, TChannel.SENSOR);
	}
	
	
	
	
	
	
	//Messages
	

	public void msgPassingGlass(GlassType gt) {
		glassPanes.add(gt);
		stateChanged();
	}


	public void msgIAmAvailable() {
		passingGlass = true;
		stateChanged();
	}

	
	
	
	
	
	//Scheduler
	
	public boolean pickAndExecuteAnAction() {
		if(glassPanes.size() >= glassCapacity){
			holdSensor();
		}
		if(!atCapacity){
			releaseSensor();
		}
		if(passingGlass && sensorPressed){
			passGlass();
		}
		return false;
	}


	
	
	
	
	//Actions
	
	void holdSensor(){
		atCapacity = true;
		//transducer.fireEvent(TChannel.SENSOR, TEvent.SENSOR_GUI_PRESSED, args);
	}
	
	void releaseSensor(){
		conveyor.msgIAmAvailable();
		atCapacity = false;
		//transducer.fireEvent(TChannel.SENSOR, TEvent.SENSOR_GUI_RELEASED, args);
	}
	
	void passGlass(){
		//BackEnd
		if(!glassPanes.isEmpty()){
			inlineMachine.msgPassingGlass(glassPanes.remove());
			passingGlass = false;
		}
		
		//FrontEnd
		Object[] arg = new Object[1];
		arg[0] = conveyor.conveyorNumber;
		transducer.fireEvent(TChannel.CONVEYOR, TEvent.CONVEYOR_DO_START, arg);
	}

	
	
	
	
	
	
	
	public void eventFired(TChannel channel, TEvent event, Object[] args) {
		if (channel == TChannel.SENSOR && event == TEvent.SENSOR_GUI_PRESSED)
		{
			if (((Integer)args[0]) == sensorNumber){
				//System.out.println("Sensor Pressed: " + sensorNumber);
				sensorPressed = true;
				stateChanged();
				System.out.println("Glass " + glassPanes.peek().getGlassID() + " is on Sensor " + name);
				
				
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
				sensorPressed = false;
			}
		}
		
	}

	
	
	//Extra Functions
	
	public void set_inlineMachine(JoshInlineMachineAgent i){
		inlineMachine = i;
	}
	
	public void set_conveyor(JoshConveyorAgent c){
		conveyor = c;
	}
}




