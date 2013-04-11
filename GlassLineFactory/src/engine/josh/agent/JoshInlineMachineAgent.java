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

	public JoshFrontSensor frontSensor;
	public JoshBackSensor backSensor;
	public Queue<GlassType> glassPanes;
	public Boolean passingGlass;
	public int machineNumber;
	
	public Transducer transducer;
	Object[] args;

	public JoshInlineMachineAgent(int mNum){
		frontSensor = null;
		backSensor = null;
		glassPanes = new LinkedList<GlassType>();
		passingGlass = false;
		machineNumber = mNum;
		
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

		return true;

	}

	
	
	//Actions
	
	void checkFrontSensor(){
		frontSensor.msgIAmAvailable();
	}
	
	
	public void eventFired(TChannel channel, TEvent event, Object[] args) {
		// TODO Auto-generated method stub
	}
	
	
	//Extra Functions
	
	public void set_frontSensor(JoshFrontSensor fs){
		frontSensor = fs;
	}
	
	public void set_backSensor(JoshBackSensor bs){
		backSensor = bs;
	}
	
	public int get_machineNumber(){
		return machineNumber;
	}

}
