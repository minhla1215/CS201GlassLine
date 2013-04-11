package engine.alex.agent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import transducer.TChannel;
import transducer.TEvent;
import transducer.TReceiver;
import transducer.Transducer;
import engine.agent.Agent;
import engine.interfaces.ConveyorFamily;
import engine.util.GlassType;

public class AlexConveyorAgent extends Agent implements ConveyorFamily{

//Data
Integer[] conveyorNumber = new Integer[1];
//int conveyorNumber;
enum SensorStates {pressed,doingNothing,released};//,waitToPass};
SensorStates startSensorStates=SensorStates.released;
SensorStates endSensorStates=SensorStates.released;

boolean allowPass;
boolean conveyorOn;

BinAgent binAgent;
BreakoutAgent breakoutAgent;
SkyConveyorAgent conveyor5Agent;
AlexConveyorAgent conveyor1Agent,conveyor2Agent,conveyor4Agent;//,conveyor5Agent,conveyor9Agent,conveyor10Agent,conveyor12Agent,conveyor13Agent;
CutterAgent cutterAgent;
ManualBreakoutAgent manualBreakoutAgent;
//OvenAgent ovenAgent;
//PainterAgent painterAgent;
//PopUpAgent drillAgent,crossSeamerAgent,grinderAgent;
//TruckAgent truckAgent;
//UvLampAgent uvLampAgent;
//WasherAgent washerAgent;

private List<GlassType> glasses=Collections.synchronizedList(new ArrayList<GlassType>());
int gtCount;

Transducer t = transducer;

		
/*
public void setPreAgent(MockPreCF preAgent) {
	this.preAgent = preAgent;
}

public void setNextAgent(MockPopUpAgent nextAgent) {
	this.nextAgent = nextAgent;
}
*/
public AlexConveyorAgent(String name,Transducer transducer,int i){
	t=transducer;
	//t.register(this, TChannel.SENSOR);
	//t.register(this, TChannel.CONVEYOR);
	conveyorNumber[0]=i;
	this.name=name;
	gtCount=0;
	allowPass=false;
	conveyorOn=false;
	stateChanged();//to run the scheduler for the first time so it can send message to preCF
}

//message


public void msgStartSensorPressed() { //step 3 startSensor is pressed so we can turn on the conveyor
	startSensorStates=SensorStates.pressed;
	System.out.println("start sensor pressed");
	stateChanged();
}

public void msgStartSensorReleased() { 
	startSensorStates=SensorStates.released;
	System.out.println("start sensor released");
	stateChanged();
	
}

public void msgEndSensorPressed(){//step 4 the endSensor is pressed so we can turn off the conveyor
	endSensorStates=SensorStates.pressed;
	System.out.println("end sensor pressed");
	stateChanged();
}

public void msgEndSensorReleased(){
	endSensorStates=SensorStates.released;
	System.out.println("end sensor released");
	stateChanged();
	
}

@Override
public void msgPassingGlass(GlassType gt) {//step 2 preCF sending glass to conveyor
	// TODO Auto-generated method stub
	glasses.add(gt);
	gtCount++;		
	System.out.println("adding a glasstype");
	stateChanged();
	
}

@Override
public void msgIAmAvailable() {//step 6 receive message from popUp says ready
	// TODO Auto-generated method stub
	allowPass=true;
	System.out.println("glass is allowed to pass to popup");
	stateChanged();
}

@Override
public boolean pickAndExecuteAnAction() {
	// TODO Auto-generated method stub
if (startSensorStates==SensorStates.released){
		tellingPreCFImAvailable();
		startSensorStates=SensorStates.doingNothing;
		return true;
	}



if (startSensorStates==SensorStates.pressed){
	if (endSensorStates==SensorStates.released){
		TurnOnConveyor();
		startSensorStates=SensorStates.doingNothing;
		return true;
	}
}


if (endSensorStates==SensorStates.pressed){
	TurnOffConveyor();
	//askIfPopUpReady();
	//endSensorStates=SensorStates.waitToPass;
	//return true;
	if (allowPass==true){
		passingGlass();//step 7 sending glass to the popup and go back to Available status
		endSensorStates=SensorStates.doingNothing;
		return true;
	}
}

if (endSensorStates==SensorStates.released){
	TurnOnConveyor();
	return true;
}

/*
if (endSensorStates==SensorStates.waitToPass){
	if (allowPass==true){
		passingGlass();//step 7 sending glass to the popup and go back to Available status
		endSensorStates=SensorStates.doingNothing;
		return true;
	}

}*/
		
	return false;
}


public void tellingPreCFImAvailable() {//step1 telling previous CF im available
	if(conveyorNumber[0]==0){
		binAgent.msgIAmAvailable();	
		System.out.println("sending msg to binAgent saying I'm ready");
	}else if(conveyorNumber[0]==1){
		cutterAgent.msgIAmAvailable();	
		System.out.println("sending msg to cutterAgent saying I'm ready");
	}else if(conveyorNumber[0]==2){
		conveyor1Agent.msgIAmAvailable();	
		System.out.println("sending msg to conveyor1Agent saying I'm ready");
	}else if(conveyorNumber[0]==3){
		breakoutAgent.msgIAmAvailable();	
		System.out.println("sending msg to breakoutAgent saying I'm ready");
	}else if(conveyorNumber[0]==4){
		manualBreakoutAgent.msgIAmAvailable();	
		System.out.println("sending msg to manualBreakoutAgent saying I'm ready");
	}else if(conveyorNumber[0]==5){
		conveyor4Agent.msgIAmAvailable();	
		System.out.println("sending msg to conveyor4Agent saying I'm ready");
	}/*else if(conveyorNumber==6){
		drillAgent.msgIAmAvailable();	
		System.out.println("sending msg to drillAgent saying I'm ready");
	}else if(conveyorNumber==7){
		crossSeamerAgent.msgIAmAvailable();	
		System.out.println("sending msg to crossSeamerAgent saying I'm ready");
	}else if(conveyorNumber==8){
		grinderAgent.msgIAmAvailable();	
		System.out.println("sending msg to grinderAgent saying I'm ready");
	}else if(conveyorNumber==9){
		washerAgent.msgIAmAvailable();	
		System.out.println("sending msg to washerAgent saying I'm ready");
	}else if(conveyorNumber==10){
		conveyor9Agent.msgIAmAvailable();	
		System.out.println("sending msg to conveyor9Agent saying I'm ready");
	}else if(conveyorNumber==11){
		uvLampAgent.msgIAmAvailable();	
		System.out.println("sending msg to uvLampAgent saying I'm ready");
	}else if(conveyorNumber==12){
		painterAgent.msgIAmAvailable();	
		System.out.println("sending msg to painterAgent saying I'm ready");
	}else if(conveyorNumber==13){
		conveyor12Agent.msgIAmAvailable();	
		System.out.println("sending msg to conveyor12Agent saying I'm ready");
	}else if(conveyorNumber==14){
		ovenAgent.msgIAmAvailable();	
		System.out.println("sending msg to ovenAgent saying I'm ready");
	}
	*/
}


public void TurnOnConveyor(){
	conveyorOn=true;
	t.fireEvent(TChannel.CONVEYOR, TEvent.CONVEYOR_DO_START, conveyorNumber);

	System.out.println("Conveyor is on");
	//stateChanged();
}

public void TurnOffConveyor(){
	conveyorOn=false;
	t.fireEvent(TChannel.CONVEYOR, TEvent.CONVEYOR_DO_STOP, conveyorNumber);
	System.out.println("Conveyor is off");
	
	//stateChanged();
}

//public void askIfPopUpReady(){
	//nextAgent.msgAreYouReady(glasses.get(0));
	//System.out.println("sending msg to nextCF asking if ready");
//}


public void passingGlass(){//step 7 sending glass to the popup and go back to Available status
	GlassType temp=glasses.remove(0);
	if(conveyorNumber[0]==0){
		cutterAgent.msgPassingGlass(temp);	
		System.out.println("passing glass " + temp.getGlassID() + " to cutter");
	}else if(conveyorNumber[0]==1){
		conveyor2Agent.msgPassingGlass(temp);
		System.out.println("passing glass " + temp.getGlassID() + " to conveyor2");
	}else if(conveyorNumber[0]==2){
		breakoutAgent.msgPassingGlass(temp);
		System.out.println("passing glass " + temp.getGlassID()+ " to breakout");
	}else if(conveyorNumber[0]==3){
		manualBreakoutAgent.msgPassingGlass(temp);	
		System.out.println("passing glass " + temp.getGlassID()+ " to manualBreakout");
	}else if(conveyorNumber[0]==4){
		conveyor5Agent.msgPassingGlass(temp);
		System.out.println("passing glass " + temp.getGlassID()+ " to conveyor5");
	}/*else if(conveyorNumber==5){
		drillAgent.msgPassingGlass(temp);	
		System.out.println("passing glass " + temp.getGlassID()+ " to drill popup");
	}else if(conveyorNumber==6){
		crossSeamerAgent.msgPassingGlass(temp);
		System.out.println("passing glass " + temp.getGlassID()+ " to crossSeamer popup");
	}else if(conveyorNumber==7){
		grinderAgent.msgPassingGlass(temp);
		System.out.println("passing glass " + temp.getGlassID()+ " to grinder popup");
	}else if(conveyorNumber==8){
		washerAgent.msgPassingGlass(temp);
		System.out.println("passing glass " + temp.getGlassID()+ " to washer");
	}else if(conveyorNumber==9){
		conveyor10Agent.msgPassingGlass(temp);	
		System.out.println("passing glass " + temp.getGlassID()+ " to conveyor10");
	}else if(conveyorNumber==10){
		uvLampAgent.msgPassingGlass(temp);		
		System.out.println("passing glass " + temp.getGlassID()+ " to uvlamp");
	}else if(conveyorNumber==11){
		painterAgent.msgPassingGlass(temp);
		System.out.println("passing glass " + temp.getGlassID()+ " to painter");
	}else if(conveyorNumber==12){
		conveyor13Agent.msgPassingGlass(temp);
		System.out.println("passing glass " + temp.getGlassID()+ " to conveyor13");
	}else if(conveyorNumber==13){
		ovenAgent.msgPassingGlass(temp);	
		System.out.println("passing glass " + temp.getGlassID()+ " to oven");
	}else if(conveyorNumber==14){
		truckAgent.msgPassingGlass(temp);	
		System.out.println("passing glass " + temp.getGlassID()+ " to truck");
	}*/
	gtCount--;
	System.out.println("passing glass " + temp.getGlassID());
}

@Override
public void eventFired(TChannel channel, TEvent event, Object[] args) {
	// TODO Auto-generated method stub
	if (event == TEvent.SENSOR_GUI_PRESSED){
		Integer[] newArgs = new Integer[1];
		newArgs[0] = (Integer)args[0] / 2;//turn on the matching conveyor
		if (conveyorNumber[0]==(Integer)newArgs[0]){
		if (((Integer)args[0] % 2) == 0 ){//when it's start sensor pressed
			this.msgStartSensorPressed();
		}else if (((Integer)args[0] % 2) == 1){//when it's end sensor pressed
			this.msgEndSensorPressed();
		}
		}
	}else if (event == TEvent.SENSOR_GUI_RELEASED){
		Integer[] newArgs = new Integer[1];
		newArgs[0] = (Integer)args[0] / 2;//decide which machine to send message to based on the number of the start sensor
		if (conveyorNumber[0]==(Integer)newArgs[0]){
		if (((Integer)args[0] % 2) == 0){//when it's start sensor released	
			this.msgStartSensorReleased();
		}else if (((Integer)args[0] % 2) == 1){//when it's end sensor released
			this.msgEndSensorReleased();
			//do we need to fireEvent for next agent? or the animation does it automatically?
		}
		}
	}
}

public BinAgent getBinAgent() {
	return binAgent;
}

public void setBinAgent(BinAgent binAgent) {
	this.binAgent = binAgent;
}

public BreakoutAgent getBreakoutAgent() {
	return breakoutAgent;
}

public void setBreakoutAgent(BreakoutAgent breakoutAgent) {
	this.breakoutAgent = breakoutAgent;
}



public AlexConveyorAgent getConveyor1Agent() {
	return conveyor1Agent;
}

public void setConveyor1Agent(AlexConveyorAgent conveyor1Agent) {
	this.conveyor1Agent = conveyor1Agent;
}

public AlexConveyorAgent getConveyor2Agent() {
	return conveyor2Agent;
}

public void setConveyor2Agent(AlexConveyorAgent conveyor2Agent) {
	this.conveyor2Agent = conveyor2Agent;
}

public AlexConveyorAgent getConveyor4Agent() {
	return conveyor4Agent;
}

public void setConveyor4Agent(AlexConveyorAgent conveyor4Agent) {
	this.conveyor4Agent = conveyor4Agent;
}

public SkyConveyorAgent getConveyor5Agent() {
	return conveyor5Agent;
}

public void setConveyor5Agent(SkyConveyorAgent conveyor5Agent) {
	this.conveyor5Agent = conveyor5Agent;
}


/*
public AlexConveyorAgent getConveyor5Agent() {
	return conveyor5Agent;
}

public void setConveyor5Agent(AlexConveyorAgent conveyor5Agent) {
	this.conveyor5Agent = conveyor5Agent;
}

public AlexConveyorAgent getConveyor9Agent() {
	return conveyor9Agent;
}

public void setConveyor9Agent(AlexConveyorAgent conveyor9Agent) {
	this.conveyor9Agent = conveyor9Agent;
}

public AlexConveyorAgent getConveyor10Agent() {
	return conveyor10Agent;
}

public void setConveyor10Agent(AlexConveyorAgent conveyor10Agent) {
	this.conveyor10Agent = conveyor10Agent;
}

public AlexConveyorAgent getConveyor12Agent() {
	return conveyor12Agent;
}

public void setConveyor12Agent(AlexConveyorAgent conveyor12Agent) {
	this.conveyor12Agent = conveyor12Agent;
}

public AlexConveyorAgent getConveyor13Agent() {
	return conveyor13Agent;
}

public void setConveyor13Agent(AlexConveyorAgent conveyor13Agent) {
	this.conveyor13Agent = conveyor13Agent;
}

public CutterAgent getCutterAgent() {
	return cutterAgent;
}

public void setCutterAgent(CutterAgent cutterAgent) {
	this.cutterAgent = cutterAgent;
}

public ManualBreakoutAgent getManualBreakoutAgent() {
	return manualBreakoutAgent;
}

public void setManualBreakoutAgent(ManualBreakoutAgent manualBreakoutAgent) {
	this.manualBreakoutAgent = manualBreakoutAgent;
}

public OvenAgent getOvenAgent() {
	return ovenAgent;
}

public void setOvenAgent(OvenAgent ovenAgent) {
	this.ovenAgent = ovenAgent;
}

public PainterAgent getPainterAgent() {
	return painterAgent;
}

public void setPainterAgent(PainterAgent painterAgent) {
	this.painterAgent = painterAgent;
}

public PopUpAgent getDrillAgent() {
	return drillAgent;
}

public void setDrillAgent(PopUpAgent drillAgent) {
	this.drillAgent = drillAgent;
}

public PopUpAgent getCrossSeamerAgent() {
	return crossSeamerAgent;
}

public void setCrossSeamerAgent(PopUpAgent crossSeamerAgent) {
	this.crossSeamerAgent = crossSeamerAgent;
}

public PopUpAgent getGrinderAgent() {
	return grinderAgent;
}

public void setGrinderAgent(PopUpAgent grinderAgent) {
	this.grinderAgent = grinderAgent;
}

public TruckAgent getTruckAgent() {
	return truckAgent;
}

public void setTruckAgent(TruckAgent truckAgent) {
	this.truckAgent = truckAgent;
}

public UvLampAgent getUvLampAgent() {
	return uvLampAgent;
}

public void setUvLampAgent(UvLampAgent uvLampAgent) {
	this.uvLampAgent = uvLampAgent;
}

public WasherAgent getWasherAgent() {
	return washerAgent;
}

public void setWasherAgent(WasherAgent washerAgent) {
	this.washerAgent = washerAgent;
}
*/
}
