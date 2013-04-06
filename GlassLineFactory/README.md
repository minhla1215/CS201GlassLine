personalGlassLine
=================
V.0:

Whitebox Unit testing classes are within package agent.test. Each class represents the test
for each agent. Unit tested with real transducer and a mock animation.

Added classes:
- 	Made package engine.agent, which contains the agents of a conveyor family. 
	They implement a common interface because they share part of the message 
	reception and sending.
	
-	Made package engine.mock, which contains the mock agents, implementing 
	the same interface.
	
-	Made package engine.test, which contains the Unit tests for each agent. 
	Preconditions, Postconditions, Scheduler, Actions and internal variables 
	are tested.

-	Made package engine.interfaces.
	- ConveyorFamily: the interface for core agent communication
	- Conveyor: the interface for conveyors with message reception from sensors
	- Machine: the interface for workstations
	
