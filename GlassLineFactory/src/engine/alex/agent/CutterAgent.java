package engine.alex.agent;

import transducer.TChannel;
import transducer.TEvent;
import transducer.TReceiver;
import engine.agent.Agent;
import engine.interfaces.ConveyorFamily;
import engine.util.GlassType;

public class CutterAgent extends Agent implements ConveyorFamily{

	//data
	
	enum States {nothing,partsloaded,partsreleased};
	
	
	
	
	//message
	@Override
	public void msgPassingGlass(GlassType gt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgIAmAvailable() {
		// TODO Auto-generated method stub
		
	}

	
	
	///schedule
	@Override
	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}

	//action
	
	
	
	
	@Override
	public void eventFired(TChannel channel, TEvent event, Object[] args) {
		// TODO Auto-generated method stub
		
	}



}
