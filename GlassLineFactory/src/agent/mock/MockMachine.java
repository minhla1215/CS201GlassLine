package agent.mock;

import engine.interfaces.SkyMachine;
import engine.util.GlassType;

public class MockMachine extends MockAgent implements SkyMachine {

	public MockMachine(String name) {
		super(name);
		// TODO Auto-generated constructor stub
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

	@Override
	public void msgIAmReady() {
		log.add(new LoggedEvent(
				"Received message msgIAmReady."));
	}

}
