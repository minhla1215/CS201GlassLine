package agent.mock;

import engine.interfaces.ConveyorFamily;
import engine.util.GlassType;

public class MockSensor extends MockAgent {

	public MockSensor(String name) {
		super(name);
	}

	EventLog log = new EventLog();
	
	public void msgGlassDetected() {
		log.add(new LoggedEvent(
				"Received message msgGlassDetected."));
	}

}
