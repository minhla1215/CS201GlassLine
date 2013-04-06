package engine.agent;

import engine.interfaces.ConveyorFamily;
import engine.interfaces.Machine;
import engine.util.GlassType;
import transducer.TChannel;
import transducer.TEvent;

public class MachineAgent extends Agent implements ConveyorFamily, Machine {

	/* (non-Javadoc)
	 * @see engine.agent.Machine#msgPassingGlass(engine.agent.GlassType)
	 */
	
	private String name;
	private int index;
	private ConveyorFamily pairedPopUp;
	public MachineAgent (ConveyorFamily popUp, int in, String n) {
		pairedPopUp = popUp;
		index = in;
		name = n;
	}
	@Override
	
	public void msgPassingGlass(GlassType gt) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see engine.agent.Machine#msgIAmAvailable()
	 */
	@Override
	public void msgIAmAvailable() {
		// TODO Auto-generated method stub

	}
	
	/* (non-Javadoc)
	 * @see engine.agent.Machine#msgIAmReady()
	 */
	@Override
	public void msgIAmReady() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public void eventFired(TChannel channel, TEvent event, Object[] args) {
		// TODO Auto-generated method stub

	}

	

}
