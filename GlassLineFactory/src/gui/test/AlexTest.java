package gui.test;

import engine.alex.agent.AlexConveyorAgent;
import engine.alex.agent.BinAgent;
import engine.alex.agent.MachineAgent;
import engine.sky.agent.SkyConveyorAgent;
import transducer.TChannel;
import transducer.TEvent;
import transducer.TReceiver;
import transducer.Transducer;

public class AlexTest implements TReceiver{
	
	Transducer t;
	
	AlexConveyorAgent conveyor0,conveyor1,conveyor2,conveyor3,conveyor4;
	BinAgent binAgent;
	MachineAgent cutterAgent,breakoutAgent,ManualBreakoutAgent;
	SkyConveyorAgent conveyor5;
	
	public AlexTest(Transducer t){
		this.t=t;
	}

	@Override
	public void eventFired(TChannel channel, TEvent event, Object[] args) {
		// TODO Auto-generated method stub
		
	}

}
