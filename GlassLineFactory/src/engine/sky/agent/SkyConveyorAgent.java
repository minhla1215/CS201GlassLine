package engine.sky.agent;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import engine.agent.Agent;
import engine.alex.agent.AlexConveyorAgent;
import engine.interfaces.ConveyorFamily;
import engine.interfaces.SkyConveyor;
import engine.util.GlassType;

import transducer.TChannel;
import transducer.TEvent;
import transducer.Transducer;

public class SkyConveyorAgent extends Agent implements ConveyorFamily,SkyConveyor {

	/** DATA */
	private ConveyorFamily postCF;
	private ConveyorFamily preCF;
	public ConveyorState myState;

	private int myGuiIndex;

	public enum ConveyorState {Idle, Stopped, ReadyToMove, Moving, ReadyToPass};
	private boolean informed;
	private boolean okToSkip;
	private boolean PopUpAvailable;
	private boolean frontSensorReleased;
	private ArrayList<GlassType> myGlasses;

	public SkyConveyorAgent(ConveyorFamily post, ConveyorFamily pre, int guiIndex, String n, Transducer tr) {
		super(n,tr);

		name = n;
		postCF = post;
		preCF = pre;
		myState = ConveyorState.Idle;
		informed = false;
		PopUpAvailable = false;
		okToSkip = false;
		myGlasses = new ArrayList<GlassType>();
		myGuiIndex = guiIndex;

	}

	public SkyConveyorAgent(int guiIndex, String n, Transducer tr) {
		super (n,tr);
		name = n;
		myState = ConveyorState.Idle;
		informed = false;
		PopUpAvailable = false;
		okToSkip = false;
		myGlasses = new ArrayList<GlassType>();
		myGuiIndex = guiIndex;
		frontSensorReleased = true;
	}

	/** Messages */

	@Override
	public void msgPassingGlass(GlassType gt) {
		myGlasses.add(gt);
		informed = false;
		stateChanged();
	}

	@Override
	public void msgIAmAvailable() {
		PopUpAvailable = true;
		stateChanged();
	}
	
	public void msgOkToSkip() {
		okToSkip = true;
		stateChanged();
	}

//	public void msgIAmBusy() {
//		PopUpAvailable = false;
//		stateChanged();
//	}

//	public void msgFullyLoaded() {
//		popUpLoaded = true;
//		stateChanged();
//	}

	public void msgGlassEntering() {
		if (myState != ConveyorState.Stopped ) {
			myState = ConveyorState.ReadyToMove;
		}


		frontSensorReleased = false;
		
		preCF.msgIAmNotAvailable();
		informed = false;

		stateChanged();
	}

	public void msgGlassExiting() {
		myState = ConveyorState.ReadyToPass;
		stateChanged();
	}

	public void msgGlassEntered() {
		frontSensorReleased = true;
		stateChanged();
	}

	public void msgGlassExited() {

	}

	/** Scheduler */
	@Override
	public boolean pickAndExecuteAnAction() {
		if ((myGlasses.size()==0 || myState == ConveyorState.Moving) && frontSensorReleased && !informed) {
			informAvailability();
			return true;
		}

		if (myState == ConveyorState.ReadyToMove) {
			startConveyor();
			return true;
		}

		if (myGlasses.size()>0) {	


			if (myState == ConveyorState.ReadyToPass) {
				if (PopUpAvailable || (!myGlasses.get(0).getConfig(myGuiIndex-5) && okToSkip)) {
					passGlass(myGlasses.remove(0));
					return true;
				}
				else {
					stopConveyor();
					return true;
				}
			}

			if(myState == ConveyorState.Stopped) {
				if (PopUpAvailable) {
					passGlass(myGlasses.remove(0));
					return true;
				}
			}
		}
		return false;
	}

	/** Actions */

	private void passGlass(GlassType gt) {
		System.out.println(this + ": action - passGlass");
		postCF.msgPassingGlass(gt);
		myState = ConveyorState.ReadyToMove;
		PopUpAvailable = false;
		okToSkip = false;
		//		stateChanged();
	}

	private void stopConveyor() {
		System.out.println(this + ": action - stopConveyor");
		myState = ConveyorState.Stopped;
		Object[] args = new Object[1];
		args[0] = myGuiIndex;
		transducer.fireEvent(TChannel.CONVEYOR, TEvent.CONVEYOR_DO_STOP, args);

		//		if (preCF instanceof AlexConveyorAgent) {
		//			((AlexConveyorAgent) preCF).msgIAmNotAvailable();
		System.out.println(this + " telling " + preCF + " IAmNotAvailable");
		preCF.msgIAmNotAvailable();
		informed = false;
		//		}
		//		stateChanged();

	}

	private void startConveyor() {
		System.out.println(this + ": action - startConveyor");
		myState = ConveyorState.Moving;
		Object[] args = new Object[1];
		args[0] = myGuiIndex;
		transducer.fireEvent(TChannel.CONVEYOR, TEvent.CONVEYOR_DO_START, args);
		//		stateChanged();

	}


	private void informAvailability() {
		System.out.println(this + ": action - informAvailability");
		informed = true;
		new Timer().schedule(new TimerTask(){
		    public void run(){//this routine is like a message reception    
		    	preCF.msgIAmAvailable();
			    }
			}, 100);
		//		stateChanged();
	}

	/** Utilities **/

	public ArrayList<GlassType> getGlasses() {
		return myGlasses;
	}

	public boolean getInformed() {
		return informed;
	}

	public boolean isPopUpAvailable() {
		return PopUpAvailable;
	}


	public void connectAgents(ConveyorFamily pre, ConveyorFamily post) {
		preCF = pre;
		postCF = post;
	}

	@Override
	public void eventFired(TChannel channel, TEvent event, Object[] args) {
		// TODO Auto-generated method stub

	}

	@Override
	public void msgIAmNotAvailable() {
		PopUpAvailable = false;
		okToSkip = false;
		stateChanged();

	}

}
