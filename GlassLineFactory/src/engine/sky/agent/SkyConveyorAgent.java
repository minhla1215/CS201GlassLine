package engine.sky.agent;

import java.util.ArrayList;

import engine.agent.Agent;
import engine.interfaces.ConveyorFamily;
import engine.util.GlassType;

import transducer.TChannel;
import transducer.TEvent;
import transducer.Transducer;

public class SkyConveyorAgent extends Agent implements ConveyorFamily {
	
	/** DATA */
	private ConveyorFamily postCF;
	private ConveyorFamily preCF;
	public ConveyorState myState;
	
	private int myGuiIndex;
	
	public enum ConveyorState {Idle, Waiting, ReadyToMove, Moving, ReadyToPass};
	private boolean informed;
	private boolean PopUpAvailable;
	private ArrayList<GlassType> myGlasses;
	private String name;
	
	public SkyConveyorAgent(ConveyorFamily post, ConveyorFamily pre, int guiIndex, String n, Transducer tr) {
		super(n,tr);
		
		name = n;
		postCF = post;
		preCF = pre;
		myState = ConveyorState.Idle;
		informed = false;
		PopUpAvailable = false;
		myGlasses = new ArrayList<GlassType>();
		myGuiIndex = guiIndex;
		
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

	public void msgGlassEntering() {
		myState = ConveyorState.ReadyToMove;
		stateChanged();
	}
	
	public void msgGlassExiting() {
		myState = ConveyorState.ReadyToPass;
		stateChanged();
	}

	/** Scheduler */
	
	@Override
	public boolean pickAndExecuteAnAction() {
		if ((myGlasses.size()==0 || myState == ConveyorState.Moving) && !informed) {
			informAvailability();
			return true;
		}
		
		if (myGlasses.size()>0) {
			if (myState == ConveyorState.ReadyToMove) {
				moveGlassDownConveyor();
				return true;
			}
			
			if (myState == ConveyorState.ReadyToPass) {
				stopConveyor();
				if (PopUpAvailable) {
					passGlass(myGlasses.remove(0));
					return true;
				}

			}
			
			if(myState == ConveyorState.Waiting) {
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
		postCF.msgPassingGlass(gt);
		myState = ConveyorState.ReadyToMove;
		stateChanged();
		
	}

	private void stopConveyor() {
		myState = ConveyorState.Waiting;
				Object[] args = new Object[1];
		args[0] = myGuiIndex;
		transducer.fireEvent(TChannel.CONVEYOR, TEvent.CONVEYOR_DO_STOP, args);
		stateChanged();
		
	}

	private void moveGlassDownConveyor() {
		myState = ConveyorState.Moving;
		Object[] args = new Object[1];
		args[0] = myGuiIndex;
		transducer.fireEvent(TChannel.CONVEYOR, TEvent.CONVEYOR_DO_START, args);
		stateChanged();
		
	}


	private void informAvailability() {
		informed = true;
		preCF.msgIAmAvailable();
		stateChanged();
	}
	
	/** Utilities **/
	
	@Override
	public String getName() {
		return name;
	}
	
	public ArrayList<GlassType> getGlasses() {
		return myGlasses;
	}
	
	public boolean getInformed() {
		return informed;
	}
	
	public boolean isPopUpAvailable() {
		return PopUpAvailable;
	}

	@Override
	public void eventFired(TChannel channel, TEvent event, Object[] args) {
		// TODO Auto-generated method stub

	}

}
