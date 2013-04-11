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
	public JoshConveyor conveyor;
	//public Popup popup;
	public Queue<GlassType> glassPanes;
	public Boolean atCapacity;
	public Boolean passingGlass;
	
	public Transducer transducer;
	Object[] args;

	public JoshBackSensorAgent(){
		glassCapacity = 1;
		conveyor = null;
		//popup = null;
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
			holdSensor();
		}
		if(!atCapacity){
			releaseSensor();
		}
		if(passingGlass){
			passGlass();
		}
		return false;
	}


	
	//Actions
	
	void holdSensor(){
		atCapacity = true;
		transducer.fireEvent(TChannel.SENSOR, TEvent.SENSOR_GUI_PRESSED, args);
	}
	void releaseSensor(){
		//popup.msgIAmAvailable();
		atCapacity = false;
		transducer.fireEvent(TChannel.SENSOR, TEvent.SENSOR_GUI_RELEASED, args);
	}
	void passGlass(){
		conveyor.msgPassingGlass(glassPanes.remove());
		passingGlass = false;
	}

	public void eventFired(TChannel channel, TEvent event, Object[] args) {
		// TODO Auto-generated method stub
		
	}

	
	
	//Extra Functions
	
	public void set_conveyor(JoshConveyor c){
		conveyor = c;
	}
	
	//public void set_popup(Popup p){
		//popup = p;
	//}
	
}
