package engine.minh.agent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import transducer.TChannel;
import transducer.TEvent;
import transducer.Transducer;
import engine.agent.Agent;
import engine.interfaces.ConveyorFamily;
import engine.util.GlassType;

public class TruckAgent extends Agent implements ConveyorFamily {

	ConveyorFamily previousComponent;
	
	// The state of the truck
	TruckState state;
	
	// the size of the truck
	int glassSize = 1;

	enum TruckState{LOADING,LEAVING,DOINGNOTHING,LOADED, DISAPPEAR, REAPPEAR};
	
	List <GlassType> glasses = Collections.synchronizedList(new ArrayList<GlassType>());
	//List <GlassType> currentGlass = Collections.synchronizedList(new ArrayList<GlassType>());
	String name;

	public TruckAgent(Transducer t, String name){
		
		super(name, t);
		transducer.register(this, TChannel.TRUCK);
		previousComponent = null;
		state = TruckState.LOADING;
		stateChanged();
	}



	
	//Messages
	
	@Override
	public void msgPassingGlass(GlassType gt) {
	
		glasses.add(gt);
		transducer.fireEvent(TChannel.TRUCK, TEvent.TRUCK_DO_LOAD_GLASS, null);
		stateChanged();
	}

	
	@Override
	public void msgIAmAvailable() {
		// Empty method
	}
	
	
	public void msgGlassLoadedToTruck(){
		state = TruckState.LOADED;
		stateChanged();
	}
	
	public void msgTruckIsBack(){
		state = TruckState.LOADING;
		stateChanged();
	}
	
	public void msgTruckLeave(){
		state = TruckState.DISAPPEAR;
		stateChanged();
	}

	public void msgTruckReturn(){
		state = TruckState.REAPPEAR;
		stateChanged();
	}
	
	@Override
	public boolean pickAndExecuteAnAction() {
		
		if(state == TruckState.DISAPPEAR){
			previousComponent.msgIAmNotAvailable();
			transducer.fireEvent(TChannel.TRUCK, TEvent.TRUCK_DO_LEAVE, null);
			state = TruckState.DOINGNOTHING;
			return true;
		}
		
		if(state == TruckState.REAPPEAR){
			transducer.fireEvent(TChannel.TRUCK, TEvent.TRUCK_DO_RETURN, null);
			state = TruckState.DOINGNOTHING;
			return true;
		}
		
		if(state == TruckState.LOADED){
			transducer.fireEvent(TChannel.TRUCK, TEvent.TRUCK_DO_EMPTY, null);
			state = TruckState.DOINGNOTHING;
			return true;
		}
		
		if(state == TruckState.LOADING){
			tellIAmAvailable();
			state = TruckState.DOINGNOTHING;
			return true;
		}
		
		
		return false;
	}

	@Override
	public void eventFired(TChannel channel, TEvent event, Object[] args) {
		if(event == TEvent.TRUCK_GUI_EMPTY_FINISHED){
			this.msgTruckIsBack();
		}
		if(event == TEvent.TRUCK_GUI_LOAD_FINISHED){
			this.msgGlassLoadedToTruck();
		}
		if(event == TEvent.TRUCK_GUI_RETURN_FINISHED){
			this.msgTruckIsBack();
		}

	}

	// ACTION
	public void tellIAmAvailable(){	
		previousComponent.msgIAmAvailable();
	}

	public void setPreviousComponent(ConveyorFamily previousC){
		previousComponent = previousC;
	}




	@Override
	public void msgIAmNotAvailable() {
		// TODO Auto-generated method stub
		
	}
}
