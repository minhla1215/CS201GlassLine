package agent.mock;

import engine.interfaces.ConveyorFamily;
import engine.interfaces.SkyMachine;
import engine.util.GlassType;

public class MockPopUp extends MockAgent implements ConveyorFamily {

	public MockPopUp(String name) {
		super(name);
	}
	
	public EventLog log = new EventLog();
	@Override
	public void msgPassingGlass(GlassType gt) {
		System.out.println(this);
		log.add(new LoggedEvent(
				"Received message msgPassingGlass with Glass ID " 
						+ gt.getGlassID() + "."));
	}
	

	@Override
	public void msgIAmAvailable() {
		log.add(new LoggedEvent(
				"Received message msgIAmAvailable."));
	}
	
	public void msgGlassDone(SkyMachine machine, GlassType gt) {
		log.add(new LoggedEvent(
				"Received message msgGlassDone from machine " + machine.getName()
				+ "GlassType " + gt.getGlassID() + "."));
	}

}
