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
	public JoshBackSensorAgent backSensor;
	public JoshFrontSensorAgent frontSensor;
	public Queue<GlassType> glassPanes;
	public Boolean atCapacity;
	public Boolean passingGlass;
	public int conveyorNumber;
	
	public Transducer transducer;
	Object[] args;
	
	
	
	//Connections are set later.  For now, null
	public JoshConveyorAgent(String n, int cNum, Transducer t){
		glassCapacity = 5;
		backSensor = null;
		frontSensor = null;
		glassPanes = new LinkedList<GlassType>();
		atCapacity = false;
		passingGlass = false;
		name = n;
		conveyorNumber = cNum;
		
		transducer = t;
		t.register(this, TChannel.CONVEYOR);
	}

	
	
	//Messages
	
	//Conveyor recieves a piece of glass from the backSensor
	public void msgPassingGlass(GlassType gt) {
		glassPanes.add(gt);
		stateChanged();
	}

	//frontSensor tells conveyor that it is available.
	public void msgIAmAvailable() {
		passingGlass = true;
		stateChanged();	
	}

	
	
	//Scheduler
	
	public boolean pickAndExecuteAnAction() {
		//Conveyor stops if it cannot take more glass
		if(glassPanes.size() >= glassCapacity){
			stopConveyor();
		}
		//Tell the backSensor it is available if !atCapacity;
		if(!atCapacity){
			runConveyor();
		}
		//Passes glass to frontSensor
		if(passingGlass){
			passGlass();
		}
		return false;

	}

	
	
	//Actions
	
	//Stops all movement on the conveyor
	void stopConveyor(){
		//Backend
		atCapacity = true;
		
		//FrontEnd
		Object[] arg = new Object[1];
		arg[0] = conveyorNumber;
		//transducer.fireEvent(TChannel.CONVEYOR, TEvent.CONVEYOR_DO_STOP, arg);
	}
	
	
	//Tells backSensor that it can take more glass
	void runConveyor(){
		//Backend
		backSensor.msgIAmAvailable();
		atCapacity = false;
		
		//FrontEnd
		Object[] arg = new Object[1];
		arg[0] = conveyorNumber;
		transducer.fireEvent(TChannel.CONVEYOR, TEvent.CONVEYOR_DO_START, arg);
	}
	
	
	//Passes glass to frontSensor
	void passGlass(){
		if(!glassPanes.isEmpty()){
			System.out.println("Glass " + glassPanes.peek().getGlassID() + " is on Conveyor " + name);
			Reinitialize();
			frontSensor.msgPassingGlass(glassPanes.remove());
			passingGlass = false;
		}
	}
	
	void Reinitialize()
	{
		atCapacity = false;
		passingGlass = false;
	}
	
	
	
	
	//TReciever
	
	public void eventFired(TChannel channel, TEvent event, Object[] args) {
		if (channel == TChannel.CONVEYOR && event == TEvent.CONVEYOR_DO_START)
		{
			if (((Integer)args[0]) == conveyorNumber){
				//System.out.println("Conveyor Started: " + conveyorNumber);
			}
		}
		else if (channel == TChannel.CONVEYOR && event == TEvent.CONVEYOR_DO_STOP)
		{
			if (((Integer)args[0]) == conveyorNumber){
				//System.out.println("Conveyor Stopped: " + conveyorNumber);
			}
		}
		
	}
	
	
	
	
	//Extra Functions
	
	public void set_backSensor(JoshBackSensorAgent bs){
		backSensor = bs;
	}
	
	public void set_frontSensor(JoshFrontSensorAgent fs){
		frontSensor = fs;
	}

}
