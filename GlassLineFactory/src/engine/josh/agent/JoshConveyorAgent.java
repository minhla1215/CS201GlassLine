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
import engine.interfaces.JoshConveyor;
import engine.interfaces.JoshFrontSensor;
import engine.josh.agent.JoshFrontSensorAgent.SensorState;

public class JoshConveyorAgent extends Agent implements ConveyorFamily, JoshConveyor{

	public JoshBackSensorAgent backSensor;
	public JoshFrontSensorAgent frontSensor;
	public Queue<GlassType> glassPanes;
	public Boolean passingGlass;
	public Boolean isMoving;
	public Boolean isJammed;
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
		isMoving = true;
		isJammed = false;
		name = n;
		conveyorNumber = cNum;
		conveyorState = ConveyorState.OFF;
		transducer = t;
		t.register(this, TChannel.CONVEYOR);
		
		Object[] arg = new Object[1];
		arg[0] = conveyorNumber;
		transducer.fireEvent(TChannel.CONVEYOR, TEvent.CONVEYOR_DO_START, arg);
	}

	

	
	
	
	//Messages//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//NORMATIVE
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

	
	//NONNORMATIVE
	public void msgConveyorJammed(){
		isJammed = true;
		stateChanged();
	}
	
	
	public void msgConveyorUnjammed(){
		isJammed = false;
		moveConveyor();
		
		//REINITIALIZING
		passingGlass = false;
		isMoving = true;
		isJammed = false;
		conveyorState = ConveyorState.OFF;
		
		stateChanged();
	}
	
	
	
	
	
	//Scheduler///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public boolean pickAndExecuteAnAction() {
		if(isJammed){
			stopConveyor();
		}
		else{
			if(conveyorState == ConveyorState.OFF){
				sendIAmAvailable();
				conveyorState = ConveyorState.DONOTHING;
				return true;
			}

			if(passingGlass && conveyorState == ConveyorState.ON){
				passGlass();
				conveyorState = ConveyorState.OFF;
				return true;
			}

		}

		return false;
	}

	
	
	
	
	
	
	//Actions//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	void sendIAmAvailable(){
		System.out.println(name + " sendIAmAvailable");
		backSensor.msgIAmAvailable();
	
	}
	
	
	
	void passGlass(){
		if(!glassPanes.isEmpty()){
			System.out.println(name + " passGlass");
			frontSensor.msgPassingGlass(glassPanes.remove());
			passingGlass = false;
		}
	}
	
	
	
	void moveConveyor(){
		System.out.println(name + " moveConveyor");
		
		Object[] arg = new Object[1];
		arg[0] = conveyorNumber;
		transducer.fireEvent(TChannel.CONVEYOR, TEvent.CONVEYOR_DO_START, arg);
		
		set_isMoving(true);
		

	}
	
	
	void stopConveyor(){
		System.out.println(name + " stopConveyor");
		
		Object[] arg = new Object[1];
		arg[0] = conveyorNumber;
		transducer.fireEvent(TChannel.CONVEYOR, TEvent.CONVEYOR_DO_STOP, arg);
		
		set_isMoving(false);
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
	
	public void set_isMoving(Boolean b){
		isMoving = b;
		frontSensor.msgConveyorChangedState();
		backSensor.msgConveyorChangedState();
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
import engine.interfaces.JoshConveyor;
import engine.interfaces.JoshFrontSensor;
import engine.josh.agent.JoshFrontSensorAgent.SensorState;

public class JoshConveyorAgent extends Agent implements ConveyorFamily, JoshConveyor{

	public JoshBackSensorAgent backSensor;
	public JoshFrontSensorAgent frontSensor;
	public Queue<GlassType> glassPanes;
	public Boolean passingGlass;
	public Boolean isMoving;
	public Boolean isJammed;
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
		isMoving = true;
		isJammed = false;
		name = n;
		conveyorNumber = cNum;
		conveyorState = ConveyorState.OFF;
		transducer = t;
		t.register(this, TChannel.CONVEYOR);
		
		Object[] arg = new Object[1];
		arg[0] = conveyorNumber;
		transducer.fireEvent(TChannel.CONVEYOR, TEvent.CONVEYOR_DO_START, arg);
	}

	

	
	
	
	//Messages//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//NORMATIVE
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

	
	//NONNORMATIVE
	public void msgConveyorJammed(){
		isJammed = true;
		stateChanged();
	}
	
	
	public void msgConveyorUnjammed(){
		isJammed = false;
		moveConveyor();
		
		//REINITIALIZING
		passingGlass = false;
		isMoving = true;
		isJammed = false;
		conveyorState = ConveyorState.OFF;
		
		stateChanged();
	}
	
	
	
	
	
	//Scheduler///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public boolean pickAndExecuteAnAction() {
		if(isJammed){
			stopConveyor();
		}
		else{
			if(conveyorState == ConveyorState.OFF){
				sendIAmAvailable();
				conveyorState = ConveyorState.DONOTHING;
				return true;
			}

			if(passingGlass && conveyorState == ConveyorState.ON){
				passGlass();
				conveyorState = ConveyorState.OFF;
				return true;
			}

		}

		return false;
	}

	
	
	
	
	
	
	//Actions//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	void sendIAmAvailable(){
		System.out.println(name + " sendIAmAvailable");
		backSensor.msgIAmAvailable();
	
	}
	
	
	
	void passGlass(){
		if(!glassPanes.isEmpty()){
			System.out.println(name + " passGlass");
			frontSensor.msgPassingGlass(glassPanes.remove());
			passingGlass = false;
		}
	}
	
	
	
	void moveConveyor(){
		System.out.println(name + " moveConveyor");
		
		Object[] arg = new Object[1];
		arg[0] = conveyorNumber;
		transducer.fireEvent(TChannel.CONVEYOR, TEvent.CONVEYOR_DO_START, arg);
		
		set_isMoving(true);
		

	}
	
	
	void stopConveyor(){
		System.out.println(name + " stopConveyor");
		
		Object[] arg = new Object[1];
		arg[0] = conveyorNumber;
		transducer.fireEvent(TChannel.CONVEYOR, TEvent.CONVEYOR_DO_STOP, arg);
		
		set_isMoving(false);
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
	
	public void set_isMoving(Boolean b){
		isMoving = b;
		frontSensor.msgConveyorChangedState();
		backSensor.msgConveyorChangedState();
	}




}
>>>>>>> b52f3d4c66b06fe79e6cf91e2663c32be39e9073
