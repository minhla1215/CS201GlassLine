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
import engine.interfaces.JoshFrontSensor;

public class JoshConveyorAgent extends Agent implements ConveyorFamily, JoshConveyor{

	public int glassCapacity;
	public JoshBackSensor backSensor;
	public JoshFrontSensor frontSensor;
	public Queue<GlassType> glassPanes;
	public Boolean atCapacity;
	public Boolean passingGlass;
	
	public Transducer transducer;
	Object[] args;
	
	public JoshConveyorAgent(){
		glassCapacity = 5;
		backSensor = null;
		frontSensor = null;
		glassPanes = new LinkedList<GlassType>();
		atCapacity = false;
		passingGlass = false;
		
		transducer = new Transducer();
		transducer.setDebugMode(TransducerDebugMode.EVENTS_AND_ACTIONS);
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
			stopConveyor();
		}
		if(!atCapacity){
			runConveyor();
		}
		if(passingGlass){
			passGlass();
		}
		return false;

	}

	
	
	//Actions
	
	void stopConveyor(){
		atCapacity = true;
		transducer.fireEvent(TChannel.CONVEYOR, TEvent.CONVEYOR_DO_STOP, args);
	}
	void runConveyor(){
		backSensor.msgIAmAvailable();
		atCapacity = false;
		transducer.fireEvent(TChannel.CONVEYOR, TEvent.CONVEYOR_DO_START, args);
	}
	void passGlass(){
		frontSensor.msgPassingGlass(glassPanes.remove());
		passingGlass = false;
	}

	public void eventFired(TChannel channel, TEvent event, Object[] args) {
		// TODO Auto-generated method stub
		
	}
	
	
	//Extra Functions
	
	public void set_backSensor(JoshBackSensor bs){
		backSensor = bs;
	}
	
	public void set_frontSensor(JoshFrontSensor fs){
		frontSensor = fs;
	}

}
