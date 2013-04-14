
package gui.panels;

import engine.josh.agent.JoshBackSensorAgent;
import engine.josh.agent.JoshConveyorAgent;
import engine.josh.agent.JoshFrontSensorAgent;
import engine.josh.agent.JoshInlineMachineAgent;
import gui.drivers.FactoryFrame;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import transducer.TChannel;
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
	//These are all of Josh's agents
	public JoshFrontSensorAgent sensor17, sensor19, sensor21, sensor23, sensor25, sensor27, sensor29;
	public JoshConveyorAgent conveyor8, conveyor9, conveyor10, conveyor11, conveyor12, conveyor13, conveyor14;
	public JoshBackSensorAgent sensor16, sensor18, sensor20, sensor22, sensor24, sensor26, sensor28;
	public JoshInlineMachineAgent washer, painter, uvLamp, oven, corner3, corner4;
	
	
	
	
	/** The frame connected to the FactoryPanel */
	private FactoryFrame parent;

	/** The control system for the factory, displayed on right */
	private ControlPanel cPanel;

	/** The graphical representation for the factory, displayed on left */
	private DisplayPanel dPanel;

	/** Allows the control panel to communicate with the back end and give commands */
	private Transducer transducer;

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
	//HEY!!! THIS IS WHERE YOU INSTANTIATE ALL OF YOUR BACKEND AGENTS.
	//NEVER FORGET ABOUT THIS PLACE.
	//I'M GONNA ADD MORE TEXT TO MAKE THIS SPOT REALLY OBVIOUS.
	//MEARGH!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1!!!!!!!
	private void initializeBackEnd()
	{
		//Initializing Josh's Part of Factory//////////////////////////////////////
		
		sensor16 = new JoshBackSensorAgent("sensor16", 16, transducer);
		sensor17 = new JoshFrontSensorAgent("sensor17", 17, transducer);
		sensor18 = new JoshBackSensorAgent("sensor18", 18, transducer);
		sensor19 = new JoshFrontSensorAgent("sensor19", 19, transducer);
		sensor20 = new JoshBackSensorAgent("sensor20", 20, transducer);
		sensor21 = new JoshFrontSensorAgent("sensor21", 21, transducer);
		sensor22 = new JoshBackSensorAgent("sensor22", 22, transducer);
		sensor23 = new JoshFrontSensorAgent("sensor23", 23, transducer);
		sensor24 = new JoshBackSensorAgent("sensor24", 24, transducer);
		sensor25 = new JoshFrontSensorAgent("sensor25", 25, transducer);
		sensor26 = new JoshBackSensorAgent("sensor26", 26, transducer);
		sensor27 = new JoshFrontSensorAgent("sensor27", 27, transducer);
		sensor28 = new JoshBackSensorAgent("sensor28", 28, transducer);
		sensor29 = new JoshFrontSensorAgent("sensor29", 29, transducer);
		
		conveyor8 = new JoshConveyorAgent("conveyor8", 8, transducer);
		conveyor9 = new JoshConveyorAgent("conveyor9", 9, transducer);
		conveyor10 = new JoshConveyorAgent("conveyor10", 10, transducer);
		conveyor11 = new JoshConveyorAgent("conveyor11", 11, transducer);
		conveyor12 = new JoshConveyorAgent("conveyor12", 12, transducer);
		conveyor13 = new JoshConveyorAgent("conveyor13", 13, transducer);
		conveyor14 = new JoshConveyorAgent("conveyor14", 14, transducer);
		
		washer = new JoshInlineMachineAgent(false, TChannel.WASHER, "washer", 3, transducer);
		painter = new JoshInlineMachineAgent(false, TChannel.PAINTER, "painter", 4, transducer);
		uvLamp= new JoshInlineMachineAgent(false, TChannel.UV_LAMP, "uvLamp", 5, transducer);
		oven = new JoshInlineMachineAgent(false, TChannel.OVEN, "oven", 6, transducer);
		corner3 = new JoshInlineMachineAgent(true, TChannel.CORNER, "corner3", 100, transducer);
		corner4 = new JoshInlineMachineAgent(true, TChannel.CORNER, "corner4", 100, transducer);
		
		//Josh's agents setting up connections
		sensor16.set_inlineMachine(null);
		sensor16.set_conveyor(conveyor8);
		conveyor8.set_backSensor(sensor16);
		conveyor8.set_frontSensor(sensor17);
		sensor17.set_conveyor(conveyor8);
		sensor17.set_inlineMachine(washer);
		washer.set_frontSensor(sensor17);
		washer.set_backSensor(sensor18);
		sensor18.set_inlineMachine(washer);
		sensor18.set_conveyor(conveyor9);
		conveyor9.set_backSensor(sensor18);
		conveyor9.set_frontSensor(sensor19);
		sensor19.set_conveyor(conveyor9);
		sensor19.set_inlineMachine(corner3);
		corner3.set_frontSensor(sensor19);
		corner3.set_backSensor(sensor20);
		sensor20.set_inlineMachine(corner3);
		sensor20.set_conveyor(conveyor10);
		conveyor10.set_backSensor(sensor20);
		conveyor10.set_frontSensor(sensor21);
		sensor21.set_conveyor(conveyor10);
		sensor21.set_inlineMachine(painter);
		painter.set_frontSensor(sensor21);
		painter.set_backSensor(sensor22);
		sensor22.set_inlineMachine(painter);
		sensor22.set_conveyor(conveyor11);
		conveyor11.set_backSensor(sensor22);
		conveyor11.set_frontSensor(sensor23);
		sensor23.set_conveyor(conveyor11);
		sensor23.set_inlineMachine(uvLamp);
		uvLamp.set_frontSensor(sensor23);
		uvLamp.set_backSensor(sensor24);
		sensor24.set_inlineMachine(uvLamp);
		sensor24.set_conveyor(conveyor12);
		conveyor12.set_backSensor(sensor24);
		conveyor12.set_frontSensor(sensor25);
		sensor25.set_conveyor(conveyor12);
		sensor25.set_inlineMachine(corner4);
		corner4.set_frontSensor(sensor25);
		corner4.set_backSensor(sensor26);
		sensor26.set_inlineMachine(corner4);
		sensor26.set_conveyor(conveyor13);
		conveyor13.set_backSensor(sensor26);
		conveyor13.set_frontSensor(sensor27);
		sensor27.set_conveyor(conveyor13);
		sensor27.set_inlineMachine(oven);
		oven.set_frontSensor(sensor27);
		oven.set_backSensor(sensor28);
		sensor28.set_inlineMachine(oven);
		sensor28.set_conveyor(conveyor14);
		conveyor14.set_backSensor(sensor28);
		conveyor14.set_frontSensor(sensor29);
		sensor29.set_conveyor(conveyor14);
		sensor29.set_inlineMachine(null);
		

		sensor16.startThread();
		sensor17.startThread();
		sensor18.startThread();
		sensor19.startThread();
		sensor20.startThread();
		sensor21.startThread();
		sensor22.startThread();
		sensor23.startThread();
		sensor24.startThread();
		sensor25.startThread();
		sensor26.startThread();
		sensor27.startThread();
		sensor28.startThread();
		sensor29.startThread();
		
		conveyor8.startThread();
		conveyor9.startThread();
		conveyor10.startThread();
		conveyor11.startThread();
		conveyor12.startThread();
		conveyor13.startThread();
		conveyor14.startThread();
		
		washer.startThread();
		painter.startThread();
		uvLamp.startThread();
		oven.startThread();
		corner3.startThread();
		corner4.startThread();
		
		
		
		
		
		
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
}
