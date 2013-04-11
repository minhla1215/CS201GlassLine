package agent.test;
// Just write a comment
// COMMENT BY MINH

import static org.junit.Assert.*;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import junit.framework.TestCase;

import org.junit.Test;

import transducer.Transducer;
import transducer.TransducerDebugMode;

import engine.sky.agent.SkyConveyorAgent;
import engine.sky.agent.SkyConveyorAgent.ConveyorState;
import engine.util.GlassType;

import agent.mock.MockAnimation;
import agent.mock.MockPopUp;
import agent.mock.MockSensor;

public class ConveyorAgentTest extends TestCase{
	
	public MockPopUp prePopUp = new MockPopUp("Mock Pre PopUp");
	public MockPopUp postPopUp = new MockPopUp("Mock Post PopUp");
	public GlassType glass = new GlassType(false, false, false, "G001");
	public Transducer transducer = new Transducer();
	public MockAnimation animation= new MockAnimation("Mock Animation", transducer);
	
	
	public void testInitialCondition() {
		SkyConveyorAgent conveyor= new SkyConveyorAgent(postPopUp, prePopUp, 10, "Conveyor", transducer);
		assertEquals("Conveyor should originally have no glasses.",
				0, conveyor.getGlasses().size());
		assertTrue("Conveyor should originally have not informed anyone.",
				!conveyor.getInformed());
		assertEquals("Conveyor should originally be in Idle state",
				ConveyorState.Idle, conveyor.myState);
		
		conveyor.pickAndExecuteAnAction();
		//Should inform availability
		
		assertTrue("Conveyor should have informed availablity",
				conveyor.getInformed());
		assertTrue("previous PopUp should have got the message informing availability. Log reads :"
				+ prePopUp.log.toString(), 
				prePopUp.log.containsString("Received message msgIAmAvailable"));
	}
	public void testMsgPassingGlass() {
		SkyConveyorAgent conveyor= new SkyConveyorAgent(postPopUp, prePopUp, 10, "Conveyor", transducer);
		
		conveyor.msgPassingGlass(glass);
		
		assertEquals("Conveyor should now have a glass",
				1, conveyor.getGlasses().size());
		assertTrue("Conveyor still has not informed anyone yet",
				!conveyor.getInformed());
		
		conveyor.pickAndExecuteAnAction();
		//Nothing should get fired by the scheduler so far
		
		assertTrue("Conveyor still has not informed anyone yet",
				!conveyor.getInformed());
		assertEquals("Conveyor should still be in Idle state",
				ConveyorState.Idle, conveyor.myState);
	}
	
	public void testMsgIAmAvailable() {
		SkyConveyorAgent conveyor= new SkyConveyorAgent(postPopUp, prePopUp, 10, "Conveyor", transducer);		
		
		assertTrue("post popup is originally unavailable",
				!(conveyor.isPopUpAvailable()));
		
		conveyor.msgIAmAvailable();
		
		assertTrue("post popup is now available",
				conveyor.isPopUpAvailable());
	}
	
	public void testMsgGlassEntering() {
		SkyConveyorAgent conveyor= new SkyConveyorAgent(postPopUp, prePopUp, 10, "Conveyor", transducer);		
		transducer.startTransducer();
		conveyor.msgGlassEntering();
		conveyor.msgPassingGlass(glass);
		assertEquals("Conveyor should  be in ReadyToMove state",
				ConveyorState.ReadyToMove, conveyor.myState);
		
		conveyor.pickAndExecuteAnAction();
		assertEquals("Conveyor should  be in Moving state",
				ConveyorState.Moving, conveyor.myState);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertTrue("Mock Animation gets an event of conveyor start moving. Log reads: " 
				+animation.log.toString(),
				animation.log.containsString("CONVEYOR_DO_START")) ;
	}
	
	public void testMsgGlassExiting() {
		SkyConveyorAgent conveyor= new SkyConveyorAgent(postPopUp, prePopUp, 10, "Conveyor", transducer);		
		transducer.startTransducer();
		
		//there needs to be at least one glass on the conveyor before getting a msgGlassExiting
		conveyor.msgPassingGlass(glass);
		conveyor.msgGlassEntering();
		assertEquals("Conveyor should now have a glass",
				1, conveyor.getGlasses().size());
		assertEquals("Conveyor should  be in ReadyToMove state",
				ConveyorState.ReadyToMove, conveyor.myState);
		
		conveyor.pickAndExecuteAnAction();
		
		assertEquals("Conveyor should  be in Moving state",
				ConveyorState.Moving, conveyor.myState);
		
		conveyor.msgGlassExiting();
		assertEquals("Conveyor should  be in ReadyToPass state",
				ConveyorState.ReadyToPass, conveyor.myState);
		
		conveyor.msgIAmAvailable();
		conveyor.pickAndExecuteAnAction();
		assertEquals("Conveyor should  be in ReadyToMove state",
				ConveyorState.ReadyToMove, conveyor.myState);
		assertTrue("Mock postPopup gets a message of msgPassingGlass. Log reads:"
				+postPopUp.log.toString(),
				postPopUp.log.containsString("Received message msgPassingGlass"));
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertTrue("Mock Animation gets an event of conveyor stop moving. Log reads: " 
				+animation.log.toString(),
				animation.log.containsString("CONVEYOR_DO_STOP")) ;
	}
	
	public void testPassingInAnotherGlassWhileMoving() {
		SkyConveyorAgent conveyor= new SkyConveyorAgent(postPopUp, prePopUp, 10, "Conveyor", transducer);		
		transducer.startTransducer();
		
		assertEquals("Conveyor should originally have no glasses.",
				0, conveyor.getGlasses().size());
		
		conveyor.msgPassingGlass(glass);
		conveyor.msgGlassEntering();
		conveyor.pickAndExecuteAnAction();
	
		assertEquals("Conveyor should  be in Moving state",
				ConveyorState.Moving, conveyor.myState);
		assertEquals("Conveyor should now have 1 glasses.",
				1, conveyor.getGlasses().size());
		
		
		conveyor.msgPassingGlass(new GlassType(true, true, true, "G002"));
		conveyor.msgGlassEntering();
		conveyor.pickAndExecuteAnAction();
		
		assertEquals("Conveyor should  be in Moving state",
				ConveyorState.Moving, conveyor.myState);
		assertEquals("Conveyor should now have 2 glasses.",
				2, conveyor.getGlasses().size());
		
		conveyor.msgPassingGlass(new GlassType(true, true, true, "G003"));
		conveyor.msgGlassEntering();
		conveyor.pickAndExecuteAnAction();
		
		assertEquals("Conveyor should  be in Moving state",
				ConveyorState.Moving, conveyor.myState);
		assertEquals("Conveyor should now have 3 glasses.",
				3, conveyor.getGlasses().size());
	}
	
	public void testPostPopUpNotAvailable() {
		SkyConveyorAgent conveyor= new SkyConveyorAgent(postPopUp, prePopUp, 10, "Conveyor", transducer);		
		transducer.startTransducer();
		
		conveyor.msgPassingGlass(glass);
		conveyor.msgGlassEntering();
		conveyor.pickAndExecuteAnAction();
	
		assertEquals("Conveyor should  be in Moving state",
				ConveyorState.Moving, conveyor.myState);
		assertEquals("Conveyor should now have 1 glasses.",
				1, conveyor.getGlasses().size());
		
		conveyor.msgGlassExiting();
		conveyor.pickAndExecuteAnAction();
		
		assertEquals("Conveyor should  be in Waiting state",
				ConveyorState.Waiting, conveyor.myState);
		
		conveyor.msgIAmAvailable();
		conveyor.pickAndExecuteAnAction();
		
		assertEquals("Conveyor should now have 0 glasses.",
				0, conveyor.getGlasses().size());
		assertEquals("Conveyor should  be in ReadyToMove state",
				ConveyorState.ReadyToMove, conveyor.myState);
		
	}

}
