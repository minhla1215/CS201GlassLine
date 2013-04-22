package engine.alex.agent;

import transducer.TChannel;
import transducer.TEvent;
import transducer.TReceiver;
import transducer.Transducer;
import engine.agent.Agent;
import engine.interfaces.ConveyorFamily;
import engine.util.GlassType;

public class AlexInlineMachineAgent extends Agent implements ConveyorFamily{

	//data

	enum States {noparts,doingnothing,partsloaded,readytopass,machinebreak};

	ConveyorFamily preConveyor,nextConveyor;

	boolean allowPass;
	boolean tempallow;

	int machineNumber;
	GlassType glass;
	States myState,tempState;

	private boolean machineBreak;


	public AlexInlineMachineAgent(String name,Transducer t,int i){
		super(name,t);
		glass=null;
		machineNumber=i;
		allowPass=false;
		machineBreak=false;
		myState=States.noparts;
		tempState=States.noparts;
		if(machineNumber==0){
			t.register(this, TChannel.CUTTER);
		}else if(machineNumber==1){
			t.register(this, TChannel.BREAKOUT);
		}else if(machineNumber==2){
			t.register(this, TChannel.MANUAL_BREAKOUT);
		}else if(machineNumber==3){
			t.register(this, TChannel.WASHER);
		}else if(machineNumber==4){
			t.register(this, TChannel.PAINTER);
		}else if(machineNumber==5){
			t.register(this, TChannel.UV_LAMP);
		}else if(machineNumber==6){
			t.register(this, TChannel.OVEN);
		}
		
		
		
		stateChanged();//to run the scheduler for the first time so it can send message to preCF

	}



	//message
	
	public void msgInlineMachineBreak(){
		machineBreak=true;
		
		System.out.println(this + " : is break and tempState is " +tempState);
		
		myState=States.machinebreak;
		stateChanged();
	}
	public void msgInlineMachineUnbreak(){
		
		machineBreak=false;
		myState=tempState;
		System.out.println(this + " : is unbreak and myState is " +myState);
		stateChanged();
	}
	
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

//		System.out.println("glass is allowed to pass to next conveyor");
		stateChanged();
	}
	
	@Override
	public void msgIAmNotAvailable() {
		// TODO Auto-generated method stub
		allowPass=false;

//		System.out.println("glass is allowed to pass to next conveyor");

		stateChanged();
	}

	public void msgWorkStationGuiActionFinished(){
		myState=States.readytopass;
		tempState=myState;
		stateChanged();
	}

	public void msgWorkStationLoadFinished(){
		myState=States.partsloaded;
		tempState=myState;
		stateChanged();
	}

	public void msgWorkStationReleaseFinished(){
		myState=States.noparts;
		tempState=myState;
		stateChanged();
	}

	///schedule
	@Override
	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		if(!machineBreak){
		if (myState==States.noparts){
			tellingPreCFImAvailable();
			myState=States.doingnothing;
			return true;

		}


		if (myState==States.partsloaded){
			if(glass.getinlineMachineProcessingNeeded()[machineNumber]==true){
				glass.setInlineMachineProcessingHistory(machineNumber);
			workingStationDoAction();
			myState=States.doingnothing;
			return true;
			}else{
				noActionNeeded();
				return true;
			}
		}

		if (myState==States.readytopass){
			if (allowPass==true){
				passingGlass();
				myState=States.doingnothing;
				return true;
			}

		}
		}else {
			if(myState==States.machinebreak){
			tellingPreCFImNotAvailable();
			myState=States.doingnothing;
			return true;
			}
		}

		return false;
	}

	//action
	public void tellingPreCFImAvailable() {
		preConveyor.msgIAmAvailable();	
		System.out.println(this + ": tells "+ preConveyor + " tellingPreCFImAvailable()");
	}
	
	public void tellingPreCFImNotAvailable(){
		preConveyor.msgIAmNotAvailable();	
		System.out.println(this + ": tells "+ preConveyor + " tellingPreCFImNotAvailable()");
		
	}

	public void workingStationDoAction(){
		if(machineNumber==0){
			transducer.fireEvent(TChannel.CUTTER, TEvent.WORKSTATION_DO_ACTION, null );
		}else if(machineNumber==1){
			transducer.fireEvent(TChannel.BREAKOUT, TEvent.WORKSTATION_DO_ACTION, null);
		}else if(machineNumber==2){
			transducer.fireEvent(TChannel.MANUAL_BREAKOUT, TEvent.WORKSTATION_DO_ACTION, null);
		}else if(machineNumber==3){
			transducer.fireEvent(TChannel.WASHER, TEvent.WORKSTATION_DO_ACTION, null );
		}else if(machineNumber==4){
			transducer.fireEvent(TChannel.PAINTER, TEvent.WORKSTATION_DO_ACTION, null );
		}else if(machineNumber==5){
			transducer.fireEvent(TChannel.UV_LAMP, TEvent.WORKSTATION_DO_ACTION, null );
		}else if(machineNumber==6){
			transducer.fireEvent(TChannel.OVEN, TEvent.WORKSTATION_DO_ACTION, null );
		}
		//System.out.println("doing workstation animation ");
		System.out.println(this + ": workingStationDoAction()");
	}

	public void noActionNeeded(){
		myState=States.readytopass;
		System.out.println(this + ": noActionNeeded()");
		stateChanged();
		
//		System.out.println("no action needed in " + this.getName());
	}
	
	
	public void passingGlass(){
		nextConveyor.msgPassingGlass(glass);
		allowPass=false;
		if(machineNumber==0){
			transducer.fireEvent(TChannel.CUTTER, TEvent.WORKSTATION_RELEASE_GLASS, null );
		}else if(machineNumber==1){
			transducer.fireEvent(TChannel.BREAKOUT, TEvent.WORKSTATION_RELEASE_GLASS, null);
		}else if(machineNumber==2){
			transducer.fireEvent(TChannel.MANUAL_BREAKOUT, TEvent.WORKSTATION_RELEASE_GLASS, null);
		}else if(machineNumber==3){
			transducer.fireEvent(TChannel.WASHER, TEvent.WORKSTATION_RELEASE_GLASS, null );
		}else if(machineNumber==4){
			transducer.fireEvent(TChannel.PAINTER, TEvent.WORKSTATION_RELEASE_GLASS, null );
		}else if(machineNumber==5){
			transducer.fireEvent(TChannel.UV_LAMP, TEvent.WORKSTATION_RELEASE_GLASS, null );
		}else if(machineNumber==6){
			transducer.fireEvent(TChannel.OVEN, TEvent.WORKSTATION_RELEASE_GLASS, null );
		}
		glass=null;
		System.out.println(this + " passingGlass()");
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



	public ConveyorFamily getPreConveyor() {
		return preConveyor;
	}



	public void setPreConveyor(ConveyorFamily preConveyor) {
		this.preConveyor = preConveyor;
	}



	public ConveyorFamily getNextConveyor() {
		return nextConveyor;
	}



	public void setNextConveyor(ConveyorFamily nextConveyor) {
		this.nextConveyor = nextConveyor;
	}



	




}
