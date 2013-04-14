package engine.agent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import transducer.TChannel;
import transducer.TEvent;
import transducer.Transducer;
import engine.interfaces.ConveyorFamily;
import engine.util.GlassType;

public class TruckAgent extends Agent implements ConveyorFamily {

	ConveyorFamily previousComponent;
	
	// The state of the truck
	TruckState state;
	
	// the size of the truck
	int glassSize = 1;

	enum TruckState{LOADING,LEAVING,DOINGNOTHING,LOADED};
	
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



	@Override
	public void msgPassingGlass(GlassType gt) {
	
		 glasses.add(gt);
		//This is for testing purposes
		 if(glasses.get(0).getInlineMachineProcessingHistory(0)){
			 System.out.println(" Cutter has processed the glass.");
		 }
		 if(glasses.get(0).getInlineMachineProcessingHistory(1)){
			 System.out.println(" Breakout has processed the glass.");
		 }
		 if(glasses.get(0).getInlineMachineProcessingHistory(2)){
			 System.out.println(" ManualBreakout has processed the glass.");
		 }
		 if(glasses.get(0).getInlineMachineProcessingHistory(3)){
			 System.out.println(" Washer has processed the glass.");
		 }
		 if(glasses.get(0).getInlineMachineProcessingHistory(4)){
			 System.out.println(" Painter has processed the glass.");
		 }
		 if(glasses.get(0).getInlineMachineProcessingHistory(5)){
			 System.out.println(" UV_Lamp has processed the glass.");
		 }
		 if(glasses.get(0).getInlineMachineProcessingHistory(6)){
			 System.out.println(" Oven has processed the glass.");
		 }


		//currentGlass.add(gt);
		transducer.fireEvent(TChannel.TRUCK, TEvent.TRUCK_DO_LOAD_GLASS, null);
		//state = TruckState.LOADING;
		//stateChanged();
	}

	@Override
	public void msgIAmAvailable() {
		// Empty method
	}
	
	
	public void msgGlassLoadedToTruck(){
		System.out.println("i'm loaded");
		state = TruckState.LOADED;
		stateChanged();
	}
	
	public void msgTruckIsBack(){
		state = TruckState.LOADING;
		stateChanged();
	}

	@Override
	public boolean pickAndExecuteAnAction() {

		
		if(state == TruckState.LOADED){
			transducer.fireEvent(TChannel.TRUCK, TEvent.TRUCK_DO_EMPTY, null);
			System.out.println("Truck do empty");
			state = TruckState.DOINGNOTHING;
			return true;
		}
		
		if(state == TruckState.LOADING){
			tellIAmAvailable();
			state = TruckState.DOINGNOTHING;
			return true;
		}
		
//		if(state == TruckState.RETURNING){
//			state = TruckState.LOADING;
//			return true;
//		}
		
		
		return false;
	}

	@Override
	public void eventFired(TChannel channel, TEvent event, Object[] args) {
		// TODO Auto-generated method stub
		if(event == TEvent.TRUCK_GUI_EMPTY_FINISHED){
			//state = TruckState.RETURNING;
			this.msgTruckIsBack();
			System.out.println("u fired me! TRUCK_GUI_EMPTY_FINISHED");
		}
		if(event == TEvent.TRUCK_GUI_LOAD_FINISHED){
			//if(currentGlass.size() > 0){
				//glasses.add(currentGlass.remove(0));
				this.msgGlassLoadedToTruck();
				System.out.println("u fired me! TRUCK_GUI_LOAD_FINISHED");
			//}else
				//System.out.println("Loading error");
		}

	}

	// ACTION
	public void tellIAmAvailable(){	
		previousComponent.msgIAmAvailable();
		//state = TruckState.LOADING;
	}

	public void setPreviousComponent(ConveyorFamily previousC){
		previousComponent = previousC;
	}
}
