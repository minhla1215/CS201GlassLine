package engine.agent;

import engine.interfaces.Conveyor;
import engine.interfaces.ConveyorFamily;
import transducer.TChannel;
import transducer.TEvent;

public class SensorAgent extends Agent {
	
	/** Data **/
	private Position pos;
	private Conveyor myConveyor;
	
	private int myGuiIndex;
	private boolean informed;
	public enum Position {First, Second};
	
	public SensorAgent(Conveyor c, Position p, int guiIndex) {
		myConveyor = c;
		pos = p;
		myGuiIndex = guiIndex;
		informed = true;
	}
	
	/** Messages **/
	public void msgGlassDetected() {
		informed = false;
		stateChanged();
	}
	
	/** Scheduler **/
	@Override
	public boolean pickAndExecuteAnAction() {
		if (!informed) {
			informConveyor ();
			return true;
		}
		return false;
	}
	
	/** Actions **/
	private void informConveyor() {
		if (pos == Position.First) {
			 myConveyor.msgGlassEntering();
		}
		else {
			 myConveyor.msgGlassExiting();
		}
		informed = true;
		stateChanged();
	}
	
	/** Utilities **/
	public boolean getInformed() {
		return informed;
	}

	@Override
	public void eventFired(TChannel channel, TEvent event, Object[] args) {
		if (channel == TChannel.SENSOR && event == TEvent.SENSOR_GUI_PRESSED && ((Integer)args[0]).equals(myGuiIndex)) {
			msgGlassDetected();
		}

	}

}
