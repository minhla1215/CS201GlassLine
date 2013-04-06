package agent.test;


import org.junit.Test;

import transducer.Transducer;
import transducer.TransducerDebugMode;

import engine.agent.PopUpAgent;
import engine.agent.PopUpAgent.ConveyorState;
import engine.agent.PopUpAgent.GlassState;
import engine.agent.PopUpAgent.MachineState;
import engine.util.GlassType;

import agent.mock.MockAnimation;
import agent.mock.MockConveyor;
import agent.mock.MockMachine;

import junit.framework.TestCase;

public class PopUpAgentTest extends TestCase {
	public MockConveyor preConveyor = new MockConveyor("Mock Pre Conveyor");
	public MockConveyor postConveyor = new MockConveyor("Mock Post Conveyor");
	public GlassType glass2 = new GlassType (false, true, false, "G002");
	public GlassType glass3 = new GlassType (false,false,false, "G003");
	public Transducer transducer = new Transducer();
	public MockAnimation animation = new MockAnimation("Mock Animation",transducer);
	public MockMachine firstMachine = new MockMachine("first machine");
	public MockMachine secondMachine = new MockMachine("second machine");

	public void testInitialCondition() {
		PopUpAgent popUp = new PopUpAgent(preConveyor, postConveyor, firstMachine, secondMachine, 1, "PopUp", transducer);
		assertTrue("PopUp should originally not inform anyone.", !popUp.getInformed());
		assertEquals("First Machine should be Idle", MachineState.Idle,popUp.getFirstMachineState());
		assertEquals("Second Machine should be Idle", MachineState.Idle,popUp.getSecondMachineState());
		assertEquals("postConveyor should be not available", ConveyorState.UnAvailable,popUp.getPostConveyorState());
		
		popUp.pickAndExecuteAnAction();
		assertTrue("PopUp should have informed preConveyor its availablilty.", popUp.getInformed());
		assertTrue("previous Conveyor should have got msgIAmAvailable. Log reads:" 
		+preConveyor.log.toString(), preConveyor.log.containsString("msgIAmAvailable"));
	}
	
	public void testMsgPassingGlass() {
		PopUpAgent popUp = new PopUpAgent(preConveyor, postConveyor, firstMachine, secondMachine, 1, "PopUp", transducer);
		transducer.startTransducer();
		transducer.setDebugMode(TransducerDebugMode.EVENTS_AND_ACTIONS_AND_SCHEDULER);
		
		popUp.msgPassingGlass(glass2);
		assertEquals("popUp should get a glass by now with glass ID G002", "G002", 
				popUp.getGlass().getGlassID().toString());
		assertEquals("currentGlass should be onBoard", GlassState.OnBoard, popUp.getGlassState());
		assertTrue("PopUp should update inform status to false", !popUp.getInformed());
		
		//When scheduler gets called, the glass will be handed to the machine
		popUp.pickAndExecuteAnAction();
		assertTrue("first machine should get the glass G002. Log reads: "
				+firstMachine.log.toString(),
				firstMachine.log.containsString("msgPassingGlass"));
		assertEquals("First Machine should be Processing", MachineState.Processing,
				popUp.getFirstMachineState());
	}
	
	public void testMsgGlassDone() {
		PopUpAgent popUp = new PopUpAgent(preConveyor, postConveyor, firstMachine, secondMachine, 1, "PopUp", transducer);
		transducer.startTransducer();
		
		popUp.msgGlassDone(firstMachine, glass2);
		assertEquals("First Machine should be Done", MachineState.Done,
				popUp.getFirstMachineState());
		assertEquals("currentGlass should be Processing", GlassState.Processing, popUp.getGlassState());

		
		popUp.pickAndExecuteAnAction();
		assertTrue("first machine should get the msgIAmReady. Log reads: "
				+firstMachine.log.toString(),
				firstMachine.log.containsString("msgIAmReady"));
		assertEquals("First Machine should be Called", MachineState.Called,
				popUp.getFirstMachineState());
	}
	
	public void testMsgReturningGlass() {
		PopUpAgent popUp = new PopUpAgent(preConveyor, postConveyor, firstMachine, secondMachine, 1, "PopUp", transducer);
		
		popUp.msgReturningGlass(firstMachine, glass2);
		assertEquals("currentGlass should be Processed", GlassState.Processed, popUp.getGlassState());
		assertEquals("popUp should get a glass by now with glass ID G002", "G002", 
				popUp.getGlass().getGlassID().toString());
		assertEquals("First Machine should be Idle", MachineState.Idle,popUp.getFirstMachineState());
		
		popUp.msgIAmAvailable();
		popUp.pickAndExecuteAnAction();
		
		assertTrue("Post Conveyor should get the message msgPassingGlass. Log reads: " 
				+ postConveyor.log.toString(),
				postConveyor.log.containsString("msgPassingGlass"));
		
	}
	
	public void testMsgIAmAvailable() {
		PopUpAgent popUp = new PopUpAgent(preConveyor, postConveyor, firstMachine, secondMachine, 1, "PopUp", transducer);
		assertEquals("postConveyor should be not available", ConveyorState.UnAvailable,popUp.getPostConveyorState());
		
		popUp.msgIAmAvailable();
		assertEquals("postConveyor should be available", ConveyorState.Available,popUp.getPostConveyorState());

	}
	
	public void testPassingGlassWithoutProcessing() {
		PopUpAgent popUp = new PopUpAgent(preConveyor, postConveyor, firstMachine, secondMachine, 1, "PopUp", transducer);
		popUp.msgPassingGlass(glass3);
		popUp.msgIAmAvailable();
		popUp.pickAndExecuteAnAction();
		
		assertTrue("Post Conveyor should get the message msgPassingGlass. Log reads: " 
				+ postConveyor.log.toString(),
				postConveyor.log.containsString("msgPassingGlass"));
	}
	
	public void testGlassProcessedButPostConveyorUnavailable() {
		PopUpAgent popUp = new PopUpAgent(preConveyor, postConveyor, firstMachine, secondMachine, 1, "PopUp", transducer);
		
		popUp.msgReturningGlass(firstMachine, glass2);
		popUp.pickAndExecuteAnAction();
		assertEquals("Post conveyor should have any messages because he's not available", 0, postConveyor.log.size());
		
		popUp.msgIAmAvailable();
		popUp.pickAndExecuteAnAction();
		assertTrue("Post Conveyor should get the message msgPassingGlass. Log reads: " 
				+ postConveyor.log.toString(),
				postConveyor.log.containsString("msgPassingGlass"));
	}
	
}
