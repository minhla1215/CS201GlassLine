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
	public enum SensorState {Informed, Entering, Exiting, Entered, Exited};
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
	public void msgGlassEntering() {
		state = SensorState.Entering;
		stateChanged();
	}
	
	public void msgGlassEntered() {
		state = SensorState.Entered;
		stateChanged();
	}

	public void msgGlassExiting() {
		state = SensorState.Exiting;
		stateChanged();
	}
	
	public void msgGlassExited() {
		state = SensorState.Exited;
	}

	/** Scheduler **/
	@Override
	public boolean pickAndExecuteAnAction() {
		if (state == SensorState.Entering && pos == Position.First) {
			informConveyorEntering();
			return true;
		}

		if (state == SensorState.Exiting && pos == Position.Second) {
			informConveyorExiting();
			return true;
		}
		
		if (state == SensorState.Entered && pos == Position.First) {
			informConveyorEntered();
			return true;
		}

		return false;
	}

	/** Actions **/
	private void informConveyorEntering() {
		System.out.println(this.name+" telling conveyor " + myConveyor.toString() + " glass entering");
		myConveyor.msgGlassEntering();

		state = SensorState.Informed;
//		stateChanged();
	}
	
	private void informConveyorEntered() {
		((SkyConveyorAgent) myConveyor).msgGlassEntered();
		state = SensorState.Informed;
//		stateChanged();
	}

	private void informConveyorExiting() {
		System.out.println(this.name+" telling conveyor " + myConveyor.toString() + " glass exiting");
		myConveyor.msgGlassExiting();
		
		state = SensorState.Informed;
//		stateChanged();

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
			if (myGuiIndex%2 == 0) {
				msgGlassEntering();
			}
			else {
				msgGlassExiting();
			}
		}
		
		if (channel == TChannel.SENSOR && event == TEvent.SENSOR_GUI_RELEASED &&((Integer)args[0]).equals(myGuiIndex)) {
			if (myGuiIndex%2==0) {
				msgGlassEntered();
			}
		}

//		if (channel == TChannel.SENSOR && event == TEvent.SENSOR_GUI_PRESSED && ((Integer)args[0]).equals(myGuiIndex)) {
//			msgGlassExited();
//		}
	}

}
