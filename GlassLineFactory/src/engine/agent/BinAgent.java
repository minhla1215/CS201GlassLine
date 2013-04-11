package engine.agent;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import transducer.TChannel;
import transducer.TEvent;
import transducer.Transducer;
import engine.interfaces.ConveyorFamily;
import engine.josh.agent.JoshFrontSensorAgent;
import engine.util.Config;
import engine.util.GlassType;

enum ConveyorState {
	NOTHING,
	READY,
	WAITING
}
public class BinAgent extends Agent implements ConveyorFamily {

	public class NextComponent{
		ConveyorFamily nextComponent;
		ConveyorState state;		
		public NextComponent(ConveyorFamily p, ConveyorState s){
			this.nextComponent = p;
			this.state = s;
		}
	}
	
	NextComponent nextComponent;
	List <GlassType> glasses = new ArrayList<GlassType>();
	Transducer transducer;
	Timer timer = new Timer();
	
	public BinAgent(Transducer t, ConveyorFamily nextC){
		this.transducer = t;
		nextComponent = new NextComponent(nextC, ConveyorState.NOTHING);
	}
	
	
	

	@Override
	public void msgPassingGlass(GlassType gt) {
		// EMPTY METHOD
	}

	@Override
	public void msgIAmAvailable() {
		nextComponent.state = ConveyorState.WAITING;
		stateChanged();
	}

	@Override
	public boolean pickAndExecuteAnAction() {
		// the popup is waiting for glass.
		if(nextComponent.state == ConveyorState.WAITING && glasses.size() > 0){
			sendingGlass();
			return true;
		}
		return false;
	}

	@Override
	public void eventFired(TChannel channel, TEvent event, Object[] args) {
		// TODO Auto-generated method stub

	}

	// ACTION=
	public void sendingGlass(){
		nextComponent.nextComponent.msgPassingGlass(glasses.get(0));
		glasses.remove(0);
		nextComponent.state = ConveyorState.NOTHING;
		stateChanged();
	}
	
	public void hereIsConfig(ArrayList<Config> configs){
		for(int i = 0; i < configs.size();i++){
			glasses.add(new GlassType(configs.get(0).getConfig(0),configs.get(0).getConfig(1),
					configs.get(0).getConfig(2), configs.get(0).getConfigID()));
			transducer.fireEvent(TChannel.BIN, TEvent.BIN_CREATE_PART, null);
			timer.schedule(new TimerTask(){
				public void run(){//this routine is like a message reception    
					;
				}
			}, (int)(1000));
		}
	}

}
