TEAM 8 GLASSLINE
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
	




V.1:

The goal of V.1 is to have the entire factory line up and running.  It must cover all normative scenarios.  Our agents are placed into separate packages.

Alex worked on all conveyor families before the popups.  Sky worked on all popup conveyor families.  Josh worked on all conveyor families after the popups.  Minh worked on the GUI and converted his personal agents to make the Bin and Truck classes.

THE PACKAGE NAMES:

-	engine.josh.agent
-	engine.minh.agent
-	engine.sky.agent
-	engine.alex.agent

ADDED CLASSES:
-	In engine.util we added classes that pertain  to Glass information.
-	In gui.panels.subcontrolpanels we added classes to refine the GUI interface and customize the types of glass we send down the line.

HOW TO USE THE GUI:
-	Under the Glass Select tab, enter a name for the GlassType and check all of the boxes for the machines you want it to be processed by.  Press Create the Glass.
-	Switch to the Produce Config. tab you can now view all of the GlassTypes you have created.
-	Enter a number next to each GlassType then click Produce to have each of those GlassTypes placed onto the line.
-	Clicking delete will erase the associated GlassType.  You will have to switch tabs to have the display refresh.

NOTE:
-	All agents are initialized inside FactoryPanel, under gui.panels

