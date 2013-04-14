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
import engine.interfaces.JoshConveyor;

public class JoshBackSensorAgent extends Agent implements ConveyorFamily, JoshBackSensor{

	int glassCapacity;
	public JoshConveyorAgent conveyor;
	public JoshInlineMachineAgent inlineMachine;
	public Queue<GlassType> glassPanes;
	public Boolean atCapacity;
	public Boolean passingGlass;
	int sensorNumber;
	
	public Transducer transducer;

	
	
	
	
	//Initialize connections to null.  They'll all be set with setters later
	public JoshBackSensorAgent(String n, int sNum, Transducer t){
		glassCapacity = 1;
		conveyor = null;
		inlineMachine = null;
		glassPanes = new LinkedList<GlassType>();
		atCapacity = false;
		passingGlass = false;
		name = n;
		sensorNumber = sNum;
		
		transducer = t;
		transducer.register(this, TChannel.SENSOR);
	}
	
	
	
	
	
	
	
	//Messages
	
	//The machine or popup before this sensor sends it a piece of glass
	public void msgPassingGlass(GlassType gt) {
		glassPanes.add(gt);
		stateChanged();
	}

	//The conveyor in front of sensor tells it that it is available.
	public void msgIAmAvailable() {
		passingGlass = true;
		stateChanged();
	}

	
	
	
	
	//Scheduler
	

	public boolean pickAndExecuteAnAction() {
		//This sets the sensor to atCapacity = true;
		if(glassPanes.size() >= glassCapacity){
			holdSensor();
		}
		//This tells the popup or machine befor this sensor that the sensor is available.
		if(!atCapacity){
			releaseSensor();
		}
		//This passes glass to the conveyor.
		if(passingGlass){
			passGlass();
		}
		return false;
	}


	
	
	
	//Actions
	
	void holdSensor(){
		atCapacity = true;
	}
	
	
	void releaseSensor(){
		if(inlineMachine != null){
			inlineMachine.msgIAmAvailable();
		}
		atCapacity = false;
	}
	
	
	void passGlass(){
		if(!glassPanes.isEmpty()){
			
			//Backend: Note: The glass is actually passed once the piece slides off the sensor
			//This is done in the eventFired when we recieve the SENSOR_GUI_RELEASED.
			passingGlass = false;
			
			//FrontEnd
			Object[] arg = new Object[1];
			arg[0] = conveyor.conveyorNumber;
			transducer.fireEvent(TChannel.CONVEYOR, TEvent.CONVEYOR_DO_START, arg);
		}
	}

	
	
	
	
	
	
	//TReciever
	
	public void eventFired(TChannel channel, TEvent event, Object[] args) {
		
		if (channel == TChannel.SENSOR && event == TEvent.SENSOR_GUI_PRESSED)
		{
			if (((Integer)args[0]) == sensorNumber){
				//System.out.println("Sensor Pressed: " + sensorNumber);
				
				//This if is for testing purposes.  It places the first piece of glass into the sensor.
				if(sensorNumber == 16){
					
					//This slots in the first piece of glass for testing
					msgPassingGlass(new GlassType(true, true, true, "The Glass"));
					
					if(!glassPanes.peek().getInlineMachineProcessingHistory(3)){
						System.out.println(" Washer has yet to process the glass.");
					}
					if(!glassPanes.peek().getInlineMachineProcessingHistory(4)){
						System.out.println(" Painter has yet to process the glass.");
					}
					if(!glassPanes.peek().getInlineMachineProcessingHistory(5)){
						System.out.println(" UV_Lamp has yet to process the glass.");
					}
					if(!glassPanes.peek().getInlineMachineProcessingHistory(6)){
						System.out.println(" Oven has yet to process the glass.");
					}
					
				}
				System.out.println("Glass " + glassPanes.peek().getGlassID() + " is on Sensor " + name);
				
			}
		}
		else if (channel == TChannel.SENSOR && event == TEvent.SENSOR_GUI_RELEASED)
		{
			if (((Integer)args[0]) == sensorNumber){
				//System.out.println("Sensor Released: " + sensorNumber);
				
				//Once the glass is removed, it is passed to the next conveyor.
				if(!glassPanes.isEmpty()){
				conveyor.msgPassingGlass(glassPanes.remove());
				}
			}
		}
		
	}

	
	
	
	
	
	
	
	//Extra Functions
	
	public void set_conveyor(JoshConveyorAgent c){
		conveyor = c;
	}
	
	public void set_inlineMachine(JoshInlineMachineAgent i){
		inlineMachine = i;
	}
	
}
