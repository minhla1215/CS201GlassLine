package agent.test;


import org.junit.Test;

import engine.agent.SensorAgent;
import engine.agent.SensorAgent.Position;

import transducer.Transducer;

import agent.mock.MockAnimation;
import agent.mock.MockConveyor;

import junit.framework.TestCase;

public class SensorAgentTest extends TestCase {
	MockConveyor conveyor = new MockConveyor("Mock Conveyor");
	Transducer transducer = new Transducer();
	MockAnimation animation = new MockAnimation("Mock Animation", transducer);
	
	public void testInitialCondition() {
		SensorAgent sensor = new SensorAgent(conveyor, Position.First, 5);
		
		assertTrue("Sensor should originally not inform any one until gets a gui message", sensor.getInformed());
		assertEquals("conveyor should originally have log size of 0", 0, conveyor.log.size());
		
		sensor.pickAndExecuteAnAction();
		
		assertTrue("Scheduler shouldn't have done anything at this moment", sensor.getInformed());
		assertEquals("conveyor should still have log size of 0", 0, conveyor.log.size());

	}
	
	public void testMsgGlassDetectedForFirstSensor() {
		SensorAgent sensor = new SensorAgent(conveyor, Position.First, 5);
		sensor.msgGlassDetected();
		sensor.pickAndExecuteAnAction();
		
		assertTrue("the conveyor should have got a msgGlassEntering. Log reads: "
				+conveyor.log.toString(),
				conveyor.log.containsString("msgGlassEntering"));

	}
	
	public void testMsgGlassDetectedForSecondSensor() {
		SensorAgent sensor = new SensorAgent(conveyor, Position.Second, 5);
		sensor.msgGlassDetected();
		sensor.pickAndExecuteAnAction();
		
		assertTrue("the conveyor should have got a msgGlassExiting. Log reads: "
				+conveyor.log.toString(),
				conveyor.log.containsString("msgGlassExiting"));
	}
	
	public void testConsecutiveGlassesDetected() {
		SensorAgent sensor = new SensorAgent(conveyor, Position.First, 5);

		assertEquals("conveyor should originally have log size of 0", 0, conveyor.log.size());
		
		sensor.msgGlassDetected();
		sensor.pickAndExecuteAnAction();
		
		assertTrue("the conveyor should have got a msgGlassEntering. Log reads: "
				+conveyor.log.toString(),
				conveyor.log.containsString("msgGlassEntering"));
		
		sensor.msgGlassDetected();
		sensor.pickAndExecuteAnAction();
		sensor.msgGlassDetected();
		sensor.pickAndExecuteAnAction();
		
		assertEquals("conveyor should  have log size of 3", 3, conveyor.log.size());

		
		
	}
}
