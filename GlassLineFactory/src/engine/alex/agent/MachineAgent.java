package engine.alex.agent;

import transducer.TChannel;
import transducer.TEvent;
import transducer.TReceiver;
import transducer.Transducer;
import engine.agent.Agent;
import engine.interfaces.ConveyorFamily;
import engine.util.GlassType;

public class MachineAgent extends Agent implements ConveyorFamily{

	//data
	
	enum States {noparts,doingnothing,partsloaded,readytopass};
	
	AlexConveyorAgent preConveyor,nextConveyor;
	
	boolean allowPass;
	
	Transducer t;
	int machineNumber;
	GlassType glass;
	States myState;
	
	
	public MachineAgent(String name,Transducer t,int i){
		this.t=t;
		machineNumber=i;
		allowPass=false;
		myState=States.noparts;
		stateChanged();//to run the scheduler for the first time so it can send message to preCF
		
	}
	
	
	
	//message
	@Override
	public void msgPassingGlass(GlassType gt) {
		// TODO Auto-generated method stub
		glass=gt;
		System.out.println("adding a glasstype");
		stateChanged();
	}

	@Override
	public void msgIAmAvailable() {
		// TODO Auto-generated method stub
		allowPass=true;
		System.out.println("glass is allowed to pass to next conveyor");
		stateChanged();
	}

	public void msgWorkStationGuiActionFinished(){
		myState=States.readytopass;
		stateChanged();
	}
	
	public void msgWorkStationLoadFinished(){
		myState=States.partsloaded;
		stateChanged();
	}
	
	public void msgWorkStationReleaseFinished(){
		myState=States.noparts;
		stateChanged();
	}
	
	///schedule
	@Override
	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		
		if (myState==States.noparts){
			tellingPreCFImAvailable();
			myState=States.doingnothing;
			return true;
		
		}
		
		
		if (myState==States.partsloaded){
			workingStationDoAction();
			myState=States.doingnothing;
			return true;
		}
		
		if (myState==States.readytopass){
			if (allowPass==true){
				passingGlass();
				myState=States.doingnothing;
				return true;
			}
			
		}
		
		return false;
	}

	//action
	public void tellingPreCFImAvailable() {//step1 telling previous CF im available
			preConveyor.msgIAmAvailable();	
			System.out.println("sending msg to preConveyor saying I'm ready");
	}
	
	public void workingStationDoAction(){
		t.fireEvent(TChannel.CUTTER, TEvent.WORKSTATION_DO_ACTION, null );
		System.out.println("doing workstation animation ");
	}
	
	public void passingGlass(){
		nextConveyor.msgPassingGlass(glass);
		t.fireEvent(TChannel.CUTTER, TEvent.WORKSTATION_RELEASE_GLASS, null);
	}
	
	@Override
	public void eventFired(TChannel channel, TEvent event, Object[] args) {
		// TODO Auto-generated method stub
		if (event==TEvent.WORKSTATION_GUI_ACTION_FINISHED){
			
			this.msgWorkStationGuiActionFinished();
			
		}else if(event==TEvent.WORKSTATION_LOAD_FINISHED){
			
			this.msgWorkStationLoadFinished();
			
		}else if(event==TEvent.WORKSTATION_RELEASE_FINISHED){
			
			this.msgWorkStationReleaseFinished();
			
		}
		
		
		
	}



	public AlexConveyorAgent getPreConveyor() {
		return preConveyor;
	}



	public void setPreConveyor(AlexConveyorAgent preConveyor) {
		this.preConveyor = preConveyor;
	}



	public AlexConveyorAgent getNextConveyor() {
		return nextConveyor;
	}



	public void setNextConveyor(AlexConveyorAgent nextConveyor) {
		this.nextConveyor = nextConveyor;
	}



}
