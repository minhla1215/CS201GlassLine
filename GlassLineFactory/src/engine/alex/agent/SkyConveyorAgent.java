package engine.alex.agent;

import transducer.TChannel;
import transducer.TEvent;
import transducer.TReceiver;
import engine.agent.Agent;
import engine.interfaces.ConveyorFamily;
import engine.util.GlassType;

public class SkyConveyorAgent extends Agent implements ConveyorFamily {

	@Override
	public void msgPassingGlass(GlassType gt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgIAmAvailable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void eventFired(TChannel channel, TEvent event, Object[] args) {
		// TODO Auto-generated method stub
		
	}

}