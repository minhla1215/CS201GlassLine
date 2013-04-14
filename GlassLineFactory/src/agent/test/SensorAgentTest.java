package agent.test;


import org.junit.Test;

import engine.sky.agent.SkySensorAgent;
import engine.sky.agent.SkySensorAgent.Position;

import transducer.Transducer;

import agent.mock.MockAnimation;
import agent.mock.MockConveyor;

import junit.framework.TestCase;

public class SensorAgentTest extends TestCase {
	MockConveyor conveyor = new MockConveyor("Mock Conveyor");
	Transducer transducer = new Transducer();
	MockAnimation animation = new MockAnimation("Mock Animation", transducer);
	
	public void testInitialCondition() {
		SkySensorAgent sensor = new SkySensorAgent(conveyor, Position.First, 5, "Sensor5", transducer);
		
		assertTrue("Sensor should originally not inform any one until gets a gui message", sensor.getInformed());
		assertEquals("conveyor should originally have log size of 0", 0, conveyor.log.size());
		
		sensor.pickAndExecuteAnAction();
		
		assertTrue("Scheduler shouldn't have done anything at this moment", sensor.getInformed());
		assertEquals("conveyor should still have log size of 0", 0, conveyor.log.size());

	}
	
	public void testMsgGlassDetectedForFirstSensor() {
		SkySensorAgent sensor = new SkySensorAgent(conveyor, Position.First, 5, "Sensor5", transducer);
		sensor.msgGlassEntering();
		sensor.pickAndExecuteAnAction();
		
		assertTrue("the conveyor should have got a msgGlassEntering. Log reads: "
				+conveyor.log.toString(),
				conveyor.log.containsString("msgGlassEntering"));

	}
	
	public void testMsgGlassDetectedForSecondSensor() {
		SkySensorAgent sensor = new SkySensorAgent(conveyor, Position.First, 5, "Sensor5", transducer);
		sensor.msgGlassEntering();
		sensor.pickAndExecuteAnAction();
		
		assertTrue("the conveyor should have got a msgGlassExiting. Log reads: "
				+conveyor.log.toString(),
				conveyor.log.containsString("msgGlassExiting"));
	}
	
	public void testConsecutiveGlassesDetected() {
		SkySensorAgent sensor = new SkySensorAgent(conveyor, Position.First, 5, "Sensor5", transducer);

		assertEquals("conveyor should originally have log size of 0", 0, conveyor.log.size());
		
		sensor.msgGlassEntering();
		sensor.pickAndExecuteAnAction();
		
		assertTrue("the conveyor should have got a msgGlassEntering. Log reads: "
				+conveyor.log.toString(),
				conveyor.log.containsString("msgGlassEntering"));
		
		sensor.msgGlassEntering();
		sensor.pickAndExecuteAnAction();
		sensor.msgGlassEntering();
		sensor.pickAndExecuteAnAction();
		
		assertEquals("conveyor should  have log size of 3", 3, conveyor.log.size());

		
		
	}
}
