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
import engine.josh.agent.JoshFrontSensorAgent.SensorState;

public class JoshConveyorAgent extends Agent implements ConveyorFamily, JoshConveyor{

	public JoshBackSensorAgent backSensor;
	public JoshFrontSensorAgent frontSensor;
	public Queue<GlassType> glassPanes;
	public Boolean passingGlass;
	public int conveyorNumber;
	public int conveyorCapacity = 2;
	enum ConveyorState{ON, OFF, DONOTHING};
	ConveyorState conveyorState;
	public Transducer transducer;
	Object[] args;
	
	
	
	public JoshConveyorAgent(String n, int cNum, Transducer t){
		backSensor = null;
		frontSensor = null;
		glassPanes = new LinkedList<GlassType>();
		passingGlass = false;
		name = n;
		conveyorNumber = cNum;
		conveyorState = ConveyorState.OFF;
		transducer = t;
		t.register(this, TChannel.CONVEYOR);
	}

	

	
	
	
	//Messages//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void msgPassingGlass(GlassType gt) {
		glassPanes.add(gt);
		conveyorState = ConveyorState.ON;
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

	
	
	
	
	
	
	//Scheduler///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public boolean pickAndExecuteAnAction() {
		if(conveyorState == ConveyorState.OFF){
			callIAmAvailable();
			conveyorState = ConveyorState.DONOTHING;
			return true;
		}
		
		if(passingGlass && conveyorState == ConveyorState.ON){
			passGlass();
			conveyorState = ConveyorState.OFF;
			return true;
		}
		
		/////////////////////NEW STUFF////////////////////////////////////
		
		
		
		
		
		
		
		
		
		
		
		
		////////////////////////////////////////////////////////////////////
		
		return false;
	}

	
	
	
	
	
	
	//Actions//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	void callIAmAvailable(){
		backSensor.msgIAmAvailable();
	}
	
	
	
	void passGlass(){
		if(!glassPanes.isEmpty()){
			System.out.println(name + " passed glass.");
			frontSensor.msgPassingGlass(glassPanes.remove());
			passingGlass = false;
		}
	}
	
	
	
	
	
	
	
	
	//TReciever///////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void eventFired(TChannel channel, TEvent event, Object[] args) {
		
	}
	
	
	
	
	
	//Extra Functions///////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void set_backSensor(JoshBackSensorAgent bs){
		backSensor = bs;
	}
	
	public void set_frontSensor(JoshFrontSensorAgent fs){
		frontSensor = fs;
	}




}
