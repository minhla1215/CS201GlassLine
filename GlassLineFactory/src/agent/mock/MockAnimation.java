package agent.mock;

import transducer.TChannel;
import transducer.TEvent;
import transducer.TReceiver;
import transducer.Transducer;

public class MockAnimation extends MockAgent implements TReceiver{
	private Transducer transducer;
	public EventLog log = new EventLog();

	public MockAnimation(String name,Transducer tr) {
		super(name);
		transducer = tr;
		tr.register(this, TChannel.CONVEYOR);
		tr.register(this, TChannel.POPUP);
	}

	@Override
	public void eventFired(TChannel channel, TEvent event, Object[] args) {
		if (channel == TChannel.CONVEYOR && event == TEvent.CONVEYOR_DO_START) {
			log.add(new LoggedEvent("event fired with channel = " + channel.toString()
					+ ", event = " + event.toString()));
		}
		
		if (channel == TChannel.CONVEYOR && event == TEvent.CONVEYOR_DO_STOP) {
			log.add(new LoggedEvent("event fired with channel = " + channel.toString()
					+ ", event = " + event.toString()));
		}
		
		if (channel == TChannel.POPUP && event == TEvent.POPUP_DO_MOVE_UP) {
			log.add(new LoggedEvent("event fired with channel = " + channel.toString()
					+ ", event = " + event.toString()));
			Object[] obj = new Object[1];
			obj[0] = new Integer(1);
			transducer.fireEvent(TChannel.POPUP, TEvent.POPUP_GUI_MOVED_UP, obj);
		}
		
		if (channel == TChannel.POPUP && event == TEvent.POPUP_DO_MOVE_DOWN) {
			log.add(new LoggedEvent("event fired with channel = " + channel.toString()
					+ ", event = " + event.toString()));
			Object[] obj = new Object[1];
			obj[0] = new Integer(1);
			transducer.fireEvent(TChannel.POPUP, TEvent.POPUP_GUI_MOVED_DOWN, obj);
		}
		
		if (channel == TChannel.POPUP && event == TEvent.WORKSTATION_DO_LOAD_GLASS) {
			log.add(new LoggedEvent("event fired with channel = " + channel.toString()
					+ ", event = " + event.toString()));
		}
		
	}

}
