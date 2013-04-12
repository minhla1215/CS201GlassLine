package engine.sky.agent;

import java.util.concurrent.Semaphore;

import engine.agent.Agent;
import engine.interfaces.ConveyorFamily;
import engine.interfaces.SkyMachine;
import engine.util.GlassType;

import transducer.TChannel;
import transducer.TEvent;
import transducer.Transducer;

public class SkyPopUpAgent extends Agent implements ConveyorFamily {
	
	/** Data **/
	
	private MyConveyor postConveyor;
	private MyConveyor preConveyor;
	private MyMachine firstMachine;
	private MyMachine secondMachine;
	private MyGlass currentGlass;
	
	private int myGuiIndex;
	private Semaphore waitAnimation = new Semaphore(0,true);
	private boolean informed;
	
	public enum GlassState {Idle, OnBoard, Processing, Processed};
	public enum MachineState {Idle, Processing, Done, Called};
	public enum ConveyorState {Available, UnAvailable};
	
	private class MyGlass {
		GlassType gt;
		GlassState state;
		
		public MyGlass(GlassType g, GlassState s) {
			gt = g;
			state = s;
		}
	}
	
	private class MyConveyor {
		ConveyorFamily conveyor;
		ConveyorState state;
		
		public MyConveyor(ConveyorFamily c, ConveyorState s) {
			conveyor = c;
			state = s;
		}
	}
	
	private class MyMachine {
		SkyMachine machine;
		MachineState state;
		
		public MyMachine(SkyMachine m, MachineState s) {
			machine = m;
			state = s;
		}
	}
	
	/** Constructor **/
	public SkyPopUpAgent(ConveyorFamily pre, ConveyorFamily post, SkyMachine first, SkyMachine second, int guiIndex, String n, Transducer tr) {
		
		super(n,tr);
		preConveyor = new MyConveyor(pre, ConveyorState.UnAvailable);
		postConveyor = new MyConveyor(post, ConveyorState.UnAvailable);
		firstMachine = new MyMachine (first, MachineState.Idle);
		secondMachine = new MyMachine (second, MachineState.Idle);
		myGuiIndex = guiIndex;
		
		transducer.register(this, TChannel.POPUP);
		informed = false;
	}
	
	public SkyPopUpAgent(int guiIndex, String n, Transducer tr) {
		super(n,tr);
		myGuiIndex = guiIndex;
		
		transducer.register(this, TChannel.POPUP);
		informed = false;
}
	
	/** Messages **/

	@Override
	public void msgPassingGlass(GlassType gt) {
		currentGlass = new MyGlass (gt, GlassState.Idle);
		informed = false;
		
	}

	@Override
	public void msgIAmAvailable() {
		postConveyor.state = ConveyorState.Available;
		stateChanged();
	}
	
	public void msgLoadFinished() {
		System.out.println("Popup received msgLoadFinished().");
		waitAnimation.release();
	}
	
	public void msgGlassDone(SkyMachine machine, GlassType gt) {
		if (machine == firstMachine.machine) {
			firstMachine.state = MachineState.Done;
		} else if (machine == secondMachine.machine) {
			secondMachine.state =MachineState.Done;
		}
		currentGlass = new MyGlass(gt,GlassState.Processing);
		stateChanged();
	}
	
	public void msgReturningGlass(SkyMachine machine, GlassType gt) {
		currentGlass = new MyGlass (gt, GlassState.Processed);
		if (machine == firstMachine.machine) {
			firstMachine.state = MachineState.Idle;
		} else if (machine == secondMachine.machine) {
			secondMachine.state =MachineState.Idle;
		}
		stateChanged();
		
	}
	
	/** Scheduler **/

	@Override
	public boolean pickAndExecuteAnAction() {
		if (currentGlass != null) {
			if ((currentGlass.state == GlassState.Processed || !currentGlass.gt.getConfig(myGuiIndex)) && postConveyor.state == ConveyorState.Available) {
				passGlass(currentGlass);
				return true;
			}
			if (currentGlass.gt.getConfig(myGuiIndex) && currentGlass.state == GlassState.OnBoard) {
				if (firstMachine.state == MachineState.Idle) {
					popUpAndPass(currentGlass, firstMachine);
					return true;
				} else if (secondMachine.state == MachineState.Idle) {
					popUpAndPass(currentGlass, secondMachine);
					return true;
				}
			}

			if (firstMachine.state == MachineState.Done) {
				popUpAndSayReady(firstMachine);
				return true;
			} else if (secondMachine.state == MachineState.Done) {
				popUpAndSayReady(secondMachine);
				return true;
			}
		} else if ((firstMachine.state == MachineState.Idle || secondMachine.state == MachineState.Idle) &&currentGlass==null && !informed) {
			informIAmAvailable();
			return true;
		}
		return false;
	}
	
	/**Actions **/
	
	private void informIAmAvailable() {
		informed = true;
		preConveyor.conveyor.msgIAmAvailable();
		stateChanged();
	}

	private void popUpAndSayReady(MyMachine mm) {
		System.out.println("Calling popUpAndSayReady");
		Object[] args = new Object[1];
		args[0] = myGuiIndex;
		transducer.fireEvent(TChannel.POPUP, TEvent.POPUP_DO_MOVE_UP, args);
		try {
			waitAnimation.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		mm.machine.msgIAmReady();
		mm.state = MachineState.Called;
		stateChanged();
	}

	private void popUpAndPass(MyGlass mg, MyMachine mm) {

		Object[] args = new Object[1];
		args[0] = myGuiIndex;
		
		transducer.fireEvent(TChannel.POPUP, TEvent.POPUP_DO_MOVE_UP, args);
		try {
			waitAnimation.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		mm.machine.msgPassingGlass(mg.gt);
		mm.state = MachineState.Processing;
//		mg.state = GlassState.Processing;
		mg = null;
		
		try {
			waitAnimation.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
		transducer.fireEvent(TChannel.POPUP, TEvent.POPUP_DO_MOVE_DOWN, args);
		try {
			waitAnimation.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		stateChanged();
		
	}

	private void passGlass(MyGlass mg) {
		postConveyor.conveyor.msgPassingGlass(mg.gt);
		currentGlass = null;
		stateChanged();
	}
	
	/** Utilities **/

	
	public GlassType getGlass() {
		return currentGlass.gt;
	}
	
	public GlassState getGlassState() {
		return currentGlass.state;
	}
	
	public boolean getInformed() {
		return informed;
	}
	
	public MachineState getFirstMachineState() {
		return firstMachine.state;
	}
	
	public MachineState getSecondMachineState() {
		return secondMachine.state;
	}
	
	public ConveyorState getPostConveyorState() {
		return postConveyor.state;
	}
	
	public void connectAgents(ConveyorFamily pre, ConveyorFamily post, SkyMachine first, SkyMachine second) {
		preConveyor = new MyConveyor(pre, ConveyorState.UnAvailable);
		postConveyor = new MyConveyor(post, ConveyorState.UnAvailable);
		firstMachine = new MyMachine (first, MachineState.Idle);
		secondMachine = new MyMachine (second, MachineState.Idle);
	}

	@Override
	public void eventFired(TChannel channel, TEvent event, Object[] args) {
		if (channel == TChannel.POPUP && event == TEvent.POPUP_GUI_LOAD_FINISHED &&((Integer)args[0]).equals(myGuiIndex)) {
			currentGlass.state = GlassState.OnBoard;
			stateChanged();
		}
		
		if (channel == TChannel.POPUP && event == TEvent.POPUP_GUI_MOVED_UP && ((Integer)args[0]).equals(myGuiIndex)) {
			System.out.println("POPUP GUI MOVED UP and waitAnimation released");
			waitAnimation.release();
		}
		
		if (channel == TChannel.POPUP && event == TEvent.POPUP_GUI_MOVED_DOWN && ((Integer)args[0]).equals(myGuiIndex)) {
			waitAnimation.release();
		}
		
	}

}
