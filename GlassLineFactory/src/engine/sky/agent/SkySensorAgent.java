package engine.sky.agent;

import engine.agent.Agent;
import engine.interfaces.SkyConveyor;
import engine.interfaces.ConveyorFamily;
import transducer.TChannel;
import transducer.TEvent;
import transducer.Transducer;

public class SkySensorAgent extends Agent {

	/** Data **/
	private Position pos;
	private SkyConveyor myConveyor;
	private SensorState state;

	private int myGuiIndex;
	public enum SensorState {Informed, Detected, Released};
	public enum Position {First, Second};

	public SkySensorAgent(SkyConveyor c, Position p, int guiIndex, String name, Transducer tr) {
		super(name,tr);
		myConveyor = c;
		pos = p;
		myGuiIndex = guiIndex;
		state = SensorState.Informed;
		transducer.register(this, TChannel.SENSOR);

	}

	public SkySensorAgent(Position p, int guiIndex, String name, Transducer tr) {
		super(name,tr);
		pos = p;
		myGuiIndex = guiIndex;
		state = SensorState.Informed;
		transducer.register(this, TChannel.SENSOR);

	}

	/** Messages **/
	public void msgGlassDetected() {
		state = SensorState.Detected;
		stateChanged();
	}

	public void msgGlassExited() {
		state = SensorState.Released;
		stateChanged();
	}

	/** Scheduler **/
	@Override
	public boolean pickAndExecuteAnAction() {
		if (state == SensorState.Detected && pos == Position.First) {
			informConveyorEntering();
			return true;
		}

		if (state == SensorState.Released && pos == Position.Second) {
			informConveyorExiting();
			return true;
		}

		return false;
	}

	/** Actions **/
	private void informConveyorEntering() {
		System.out.println(this.name+" telling conveyor " + myConveyor.toString() + " glass entering");
		myConveyor.msgGlassEntering();

		state = SensorState.Informed;
		stateChanged();
	}

	private void informConveyorExiting() {
		System.out.println(this.name+" telling conveyor " + myConveyor.toString() + " glass exiting");
		myConveyor.msgGlassExiting();
		
		state = SensorState.Informed;
		stateChanged();

	}

	/** Utilities **/
	public boolean getInformed() {
		if (state == SensorState.Informed) {
			return true;
		}
		return false;
	}

	public void connectAgents(SkyConveyor cf) {
		myConveyor = cf;
	}

	@Override
	public void eventFired(TChannel channel, TEvent event, Object[] args) {
		if (channel == TChannel.SENSOR && event == TEvent.SENSOR_GUI_PRESSED && ((Integer)args[0]).equals(myGuiIndex)) {
			msgGlassDetected();
		}

//		if (channel == TChannel.SENSOR && event == TEvent.SENSOR_GUI_RELEASED && ((Integer)args[0]).equals(myGuiIndex)) {
//			msgGlassExited();
//		}
	}

}
