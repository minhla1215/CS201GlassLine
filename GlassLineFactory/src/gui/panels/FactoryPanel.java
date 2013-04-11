
package gui.panels;

import engine.agent.BinAgent;
import engine.alex.agent.AlexConveyorAgent;
import engine.alex.agent.MachineAgent;
import engine.sky.agent.SkyConveyorAgent;
import engine.sky.agent.SkyMachineAgent;
import engine.sky.agent.SkyPopUpAgent;
import engine.sky.agent.SkySensorAgent;
import engine.sky.agent.SkySensorAgent.Position;
import gui.drivers.FactoryFrame;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import transducer.Transducer;

/**
 * The FactoryPanel is highest level panel in the actual kitting cell. The
 * FactoryPanel makes all the back end components, connects them to the
 * GuiComponents in the DisplayPanel. It is responsible for handing
 * communication between the back and front end.
 */
@SuppressWarnings("serial")
public class FactoryPanel extends JPanel
{
	/** The frame connected to the FactoryPanel */
	private FactoryFrame parent;

	/** The control system for the factory, displayed on right */
	private ControlPanel cPanel;

	/** The graphical representation for the factory, displayed on left */
	private DisplayPanel dPanel;

	/** Allows the control panel to communicate with the back end and give commands */
	private Transducer transducer;

	// A reference of Bin Agent for the Control Panel
	BinAgent bin;

	/**
	 * Constructor links this panel to its frame
	 */
	public FactoryPanel(FactoryFrame fFrame)
	{
		parent = fFrame;

		// initialize transducer
		transducer = new Transducer();
		transducer.startTransducer();

		// use default layout
		// dPanel = new DisplayPanel(this);
		// dPanel.setDefaultLayout();
		// dPanel.setTimerListeners();

		// initialize and run
		this.initialize();
		this.initializeBackEnd();
	}

	/**
	 * Initializes all elements of the front end, including the panels, and lays
	 * them out
	 */
	private void initialize()
	{
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		// initialize control panel
		cPanel = new ControlPanel(this, transducer);

		// initialize display panel
		dPanel = new DisplayPanel(this, transducer);

		// add panels in
		// JPanel tempPanel = new JPanel();
		// tempPanel.setPreferredSize(new Dimension(830, 880));
		// this.add(tempPanel);

		this.add(dPanel);
		this.add(cPanel);
	}

	/**
	 * Feel free to use this method to start all the Agent threads at the same time
	 */
	private void initializeBackEnd()
	{
		// ===========================================================================
		// TODO initialize and start Agent threads here
		// ===========================================================================

		

		// *************Instantiate *************

		// Instantiate - Minh
		bin = new BinAgent(transducer, "bin");
		cPanel.setBinAgent();
		
		//Initializing Alex's agents
		AlexConveyorAgent conveyor0=new AlexConveyorAgent("Conveyor0",transducer,0);
		AlexConveyorAgent conveyor1=new AlexConveyorAgent("Conveyor1",transducer,1);
		AlexConveyorAgent conveyor2=new AlexConveyorAgent("Conveyor2",transducer,2);
		AlexConveyorAgent conveyor3=new AlexConveyorAgent("Conveyor3",transducer,3);
		AlexConveyorAgent conveyor4=new AlexConveyorAgent("Conveyor4",transducer,4);
		
		MachineAgent cutterAgent=new MachineAgent("Cutter",transducer,0);
		MachineAgent breakoutAgent=new MachineAgent("Breakout",transducer,1);
		MachineAgent manualBreakoutAgent=new MachineAgent("ManualBreakout",transducer,2);

		//Initializing Sky's PopUps 0 - 2
		SkyPopUpAgent popUp0 = new SkyPopUpAgent(0, "PopUp0", transducer);
		SkyPopUpAgent popUp1 = new SkyPopUpAgent(1, "PopUp1", transducer);
		SkyPopUpAgent popUp2 = new SkyPopUpAgent(2, "PopUp2", transducer);

		//Initializing Sky's Conveyors 5 - 7
		SkyConveyorAgent conveyor5 = new SkyConveyorAgent(5, "Conveyor5", transducer);
		SkyConveyorAgent conveyor6 = new SkyConveyorAgent(6, "Conveyor6", transducer);
		SkyConveyorAgent conveyor7 = new SkyConveyorAgent(7, "Conveyor7", transducer);

		//Initializing Sky's Machines : Drill, Cross_Seamer, Grinder
		SkyMachineAgent drill0 = new SkyMachineAgent(0, "Drill0", transducer);
		SkyMachineAgent drill1 = new SkyMachineAgent(1, "Drill1", transducer);

		SkyMachineAgent cross_seamer0 = new SkyMachineAgent(0, "Cross_Seamer0", transducer);
		SkyMachineAgent cross_seamer1 = new SkyMachineAgent(1, "Cross_Seamer1", transducer);

		SkyMachineAgent grinder0 = new SkyMachineAgent(0, "Grinder0", transducer);
		SkyMachineAgent grinder1 = new SkyMachineAgent(1, "Grinder1", transducer);

		//Initializing Sky's Sensors 10 - 15
		SkySensorAgent sensor10 = new SkySensorAgent(Position.First, 10, "Sensor10", transducer);
		SkySensorAgent sensor11 = new SkySensorAgent(Position.Second, 11, "Sensor11", transducer);
		SkySensorAgent sensor12 = new SkySensorAgent(Position.First, 12, "Sensor12", transducer);
		SkySensorAgent sensor13 = new SkySensorAgent(Position.Second, 13, "Sensor13", transducer);
		SkySensorAgent sensor14 = new SkySensorAgent(Position.First, 14, "Sensor14", transducer);
		SkySensorAgent sensor15 = new SkySensorAgent(Position.Second, 15, "Sensor15", transducer);
		
		//TODO: Josh initializations


		// **************Link the agents *****************

		// Linking - Minh
		bin.setNextComponent(conveyor0);
		
		// Linking - Alex
		conveyor0.setPreAgent(bin);
		conveyor0.setNextAgent(cutterAgent);
		conveyor1.setPreAgent(cutterAgent);
		conveyor1.setNextAgent(conveyor2);
		conveyor2.setPreAgent(conveyor1);
		conveyor2.setNextAgent(breakoutAgent);
		conveyor3.setPreAgent(breakoutAgent);
		conveyor3.setNextAgent(manualBreakoutAgent);
		conveyor4.setPreAgent(manualBreakoutAgent);
		conveyor4.setNextAgent(conveyor5);
		
		cutterAgent.setPreConveyor(conveyor0);
		cutterAgent.setNextConveyor(conveyor1);
		breakoutAgent.setPreConveyor(conveyor2);
		breakoutAgent.setNextConveyor(conveyor3);
		manualBreakoutAgent.setPreConveyor(conveyor3);
		manualBreakoutAgent.setNextConveyor(conveyor4);
		
		// Linking - Sky
		
		// Linking - Josh



		// **************Start Thread *****************
		
		//Minh start threads
		bin.startThread();
		
		//Alex start threads
		conveyor0.startThread();
		conveyor1.startThread();
		conveyor2.startThread();
		conveyor3.startThread();
		conveyor4.startThread();
		cutterAgent.startThread();
		breakoutAgent.startThread();
		manualBreakoutAgent.startThread();
/*
		//Sky start threads
		conveyor5.startThread();
		conveyor6.startThread();
		conveyor7.startThread();

		drill0.startThread();
		drill1.startThread();

		cross_seamer0.startThread();
		cross_seamer1.startThread();

		grinder0.startThread();
		grinder1.startThread();


		popUp0.startThread();
		popUp1.startThread();
		popUp2.startThread();


		sensor10.startThread();
		sensor11.startThread();
		sensor12.startThread();
		sensor13.startThread();
		sensor14.startThread();
		sensor15.startThread();
*/
		//TODO:Josh start threads





		System.out.println("Back end initialization finished.");

	}

	/**
	 * Returns the parent frame of this panel
	 * 
	 * @return the parent frame
	 */
	public FactoryFrame getGuiParent()
	{
		return parent;
	}

	/**
	 * Returns the control panel
	 * 
	 * @return the control panel
	 */
	public ControlPanel getControlPanel()
	{
		return cPanel;
	}

	/**
	 * Returns the display panel
	 * 
	 * @return the display panel
	 */
	public DisplayPanel getDisplayPanel()
	{
		return dPanel;
	}


	public BinAgent getBinAgent(){
		return bin;
	}
}
