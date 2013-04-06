package agent.mock;

import engine.interfaces.Conveyor;
import engine.interfaces.ConveyorFamily;
import engine.util.GlassType;

public class MockConveyor extends MockAgent implements Conveyor, ConveyorFamily {

	public MockConveyor(String name) {
		super(name);
	}

	public EventLog log = new EventLog();
	@Override
	public void msgPassingGlass(GlassType gt) {
		log.add(new LoggedEvent(
				"Received message msgPassingGlass with Glass ID " 
						+ gt.getGlassID() + "."));

	}

	@Override
	public void msgIAmAvailable() {
		log.add(new LoggedEvent(
				"Received message msgIAmAvailable."));
	}
	
	public void msgGlassEntering() {
		log.add(new LoggedEvent(
				"Received message msgGlassEntering."));
	}
	
	public void msgGlassExiting() {
		log.add(new LoggedEvent(
				"Received message msgGlassExiting."));
	}

}
