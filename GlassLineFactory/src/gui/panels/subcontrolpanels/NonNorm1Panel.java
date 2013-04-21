package gui.panels.subcontrolpanels;

import engine.alex.agent.AlexConveyorAgent;
import engine.alex.agent.AlexInlineMachineAgent;
import engine.interfaces.ConveyorFamily;
import engine.josh.agent.JoshConveyorAgent;
import gui.panels.ControlPanel;
import gui.panels.FactoryPanel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class NonNorm1Panel extends JPanel implements ActionListener{

	ControlPanel parent;
	FactoryPanel fp;
	
	// Declaration of Swing
	JComboBox conveyorDropList, popupDropList, offlineDropList, inlineDropList;
	JButton conveyorJamButton, conveyorUnJamButton;
	JButton inlineBreakButton, inlineUnBreakButton;
	JButton popupBreakButton, popupUnBreakButton;
	JButton offlineBreakButton, offlineUnBreakButton, offlineOnButton, offlineOffButton, offlineRemoveGlassButton, offlineProcessingTimeButton;
	JTextField offlineProcessingTimeField;
	JLabel offlineProcessingTimeLabel;
	JLabel conveyorLabel, popupLabel, inlineLabel, offlineLabel;
	
	// Declaration of Swing Box container
	JPanel conveyorContainer, popupContainer, inlineContainer, offlineContainer, offlineProcessingTimeContainer;
	
	boolean started = false;
	public NonNorm1Panel(ControlPanel cp)
	{
		parent = cp;

		this.setBackground(Color.gray);
		this.setForeground(Color.black);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		// initialization of labels
		conveyorLabel = new JLabel("CONVEYORS");
		conveyorLabel.setOpaque(true);
		conveyorLabel.setBackground(Color.red);
		conveyorLabel.setForeground(Color.blue);
		conveyorLabel.setPreferredSize(new Dimension(400,15));
		conveyorLabel.setMaximumSize(new Dimension(400,15));
		conveyorLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		inlineLabel = new JLabel("INLINE MACHINSE");
		inlineLabel.setForeground(Color.blue);
		//inlineLabel.setHorizontalAlignment(SwingConstants.LEFT);
		popupLabel = new JLabel("POP UPS");
		popupLabel.setForeground(Color.blue);
		offlineLabel = new JLabel("OFFLINE MACHINES");
		offlineLabel.setForeground(Color.blue);
		offlineProcessingTimeLabel = new JLabel("Processing Time");
		
		// initialization of drop downs
		conveyorDropList = new JComboBox();
		popupDropList = new JComboBox();
		offlineDropList = new JComboBox();
		inlineDropList = new JComboBox();
		
		// setting size for drop list
		conveyorDropList.setPreferredSize(new Dimension(200,25));
		conveyorDropList.setMaximumSize(new Dimension(300,25));
		popupDropList.setPreferredSize(new Dimension(200,25));
		popupDropList.setMaximumSize(new Dimension(300,25));
		inlineDropList.setPreferredSize(new Dimension(200,25));
		inlineDropList.setMaximumSize(new Dimension(300,25));
		offlineDropList.setPreferredSize(new Dimension(200,25));
		offlineDropList.setMaximumSize(new Dimension(200,25));
		
	
		// Adding the drop down for the Combo Box
		for(int i = 1; i <= 15; i++){
			conveyorDropList.addItem("Conveyor " + i);
		}
		for(int i = 1; i <= 3; i++){
			popupDropList.addItem("Popup " + i);
		}
		
		// Adding specific drop down for offline
		offlineDropList.addItem("Driller 1");
		offlineDropList.addItem("Driller 2");
		offlineDropList.addItem("Cross-seamer 1");
		offlineDropList.addItem("Cross-seamer 2");
		offlineDropList.addItem("Grinder 1");
		offlineDropList.addItem("Grinder 2");
		
		// Adding specific drop down for inline
		inlineDropList.addItem("Cutter");
		inlineDropList.addItem("Breakout");
		inlineDropList.addItem("Manual Breakout");
		inlineDropList.addItem("Washer");
		inlineDropList.addItem("Painter");
		inlineDropList.addItem("UV Lamp");
		inlineDropList.addItem("Oven");
		
		// Initialization of Buttons
		conveyorJamButton = new JButton("Jam");
		conveyorUnJamButton = new JButton("Unjam");
		inlineBreakButton = new JButton("Break");
		inlineUnBreakButton = new JButton("Fix");
		popupBreakButton = new JButton("Break");
		popupUnBreakButton = new JButton("Fix");
		offlineBreakButton = new JButton("Break");
		offlineUnBreakButton = new JButton("Fix");
		offlineOnButton = new JButton("On");
		offlineOffButton = new JButton("Off");
		offlineRemoveGlassButton = new JButton("Remove Glass");
		offlineProcessingTimeButton = new JButton("Set");
		
		// offline Text Field Container
		offlineProcessingTimeField = new JTextField(4);
		offlineProcessingTimeField.setPreferredSize(new Dimension(40,25));
		offlineProcessingTimeField.setMaximumSize(new Dimension(40,25));
		
		// Set initial states of buttons
		conveyorJamButton.setEnabled(true);
		//conveyorUnJamButton.setEnabled(false);
		inlineBreakButton.setEnabled(true);
		inlineUnBreakButton.setEnabled(false);
		popupBreakButton.setEnabled(true);
		popupUnBreakButton.setEnabled(false);
		
		// adding action listeners to buttons
		conveyorJamButton.addActionListener(this);
		conveyorUnJamButton.addActionListener(this);
		inlineBreakButton.addActionListener(this);
		inlineUnBreakButton.addActionListener(this);
		popupBreakButton.addActionListener(this);
		popupUnBreakButton.addActionListener(this);
		offlineBreakButton.addActionListener(this);
		offlineUnBreakButton.addActionListener(this);
		offlineOnButton.addActionListener(this);
		offlineOffButton.addActionListener(this);
		offlineProcessingTimeButton.addActionListener(this);
		offlineRemoveGlassButton.addActionListener(this);
		
		
		// Initialization of containers
		conveyorContainer = new JPanel();
		inlineContainer = new JPanel();
		popupContainer = new JPanel();
		offlineContainer = new JPanel();
		offlineProcessingTimeContainer = new JPanel();
		conveyorContainer.setLayout(new BoxLayout(conveyorContainer,BoxLayout.X_AXIS));
		inlineContainer.setLayout(new BoxLayout(inlineContainer,BoxLayout.X_AXIS));
		offlineContainer.setLayout(new BoxLayout(offlineContainer,BoxLayout.X_AXIS));
		popupContainer.setLayout(new BoxLayout(popupContainer,BoxLayout.X_AXIS));
		offlineProcessingTimeContainer.setLayout(new BoxLayout(offlineProcessingTimeContainer,BoxLayout.X_AXIS));
		
		// Adding elements to Conveyor Container
		conveyorContainer.add(conveyorDropList);
		conveyorContainer.add(conveyorJamButton);
		conveyorContainer.add(conveyorUnJamButton);
		
		// Adding elements to inline container
		inlineContainer.add(inlineDropList);
		inlineContainer.add(inlineBreakButton);
		inlineContainer.add(inlineUnBreakButton);
		
		// Adding elements to popup container
		popupContainer.add(popupDropList);
		popupContainer.add(popupBreakButton);
		popupContainer.add(popupUnBreakButton);
		
		// Adding elements to offline container
		offlineContainer.add(offlineDropList);
		offlineContainer.add(offlineBreakButton);
		offlineContainer.add(offlineUnBreakButton);
		offlineContainer.add(offlineOnButton);
		offlineContainer.add(offlineOffButton);
		
		// Adding elements to offline Processing Time container
		offlineProcessingTimeContainer.add(offlineProcessingTimeLabel);
		offlineProcessingTimeContainer.add(offlineProcessingTimeField);
		offlineProcessingTimeContainer.add(offlineProcessingTimeButton);
		offlineProcessingTimeContainer.add(offlineRemoveGlassButton);
		
		// Adding containers to GUI
		//this.setAlignmentX(Component.LEFT_ALIGNMENT);
		this.add(conveyorLabel);
		this.add(conveyorContainer);
		this.add(inlineLabel);
		this.add(inlineContainer);
		this.add(popupLabel);
		this.add(popupContainer);
		this.add(offlineLabel);
		this.add(offlineContainer);
		this.add(offlineProcessingTimeContainer);
	}
	
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		int index = 0;
		if(!started){
			fp = parent.getGuiParent();
			started = true;
		}
		if(ae.getSource() == conveyorJamButton){

			index = conveyorDropList.getSelectedIndex();

			//conveyorJamButton.setEnabled(false);
			//conveyorUnJamButton.setEnabled(true);

			// Call message
			if(fp.getConveyorList()[index] instanceof AlexConveyorAgent){
				((AlexConveyorAgent) fp.getConveyorList()[index]).msgConveyorJammed();}
			//			else if(fp.getConveyorList()[index] instanceof JoshConveyorAgent){
			//				((JoshConveyorAgent) fp.getConveyorList()[index]).msgConveyorJammed();}
			//			else if(fp.getConveyorList()[index] instanceof SkyConveyorAgent){
			//				((SkyConveyorAgent) fp.getConveyorList()[index]).msgConveyorJammed();}



		}
		else if(ae.getSource() == conveyorUnJamButton){
			index = conveyorDropList.getSelectedIndex();
			//conveyorJamButton.setEnabled(true);
			//conveyorUnJamButton.setEnabled(false);
			
			// Call message
			if(fp.getConveyorList()[index] instanceof AlexConveyorAgent){
				((AlexConveyorAgent) fp.getConveyorList()[index]).msgConveyorUnjammed();}
			//fp.getConveyorList()[index].msgConveyorUnjammed();
		}
		else if(ae.getSource() == inlineBreakButton){
			index = inlineDropList.getSelectedIndex();
			//inlineBreakButton.setEnabled(false);
			//inlineUnBreakButton.setEnabled(true);
			
			// Call message
			if(fp.getConveyorList()[index] instanceof AlexInlineMachineAgent){
				((AlexInlineMachineAgent) fp.getInlineList()[index]).msgInlineMachineBreak();}
			//fp.getInlineList()[index].msgInlineMachineBreak();
		}
		else if(ae.getSource() == inlineUnBreakButton){
			index = inlineDropList.getSelectedIndex();
			//inlineBreakButton.setEnabled(true);
			//inlineUnBreakButton.setEnabled(false);
			
			// Call message
			if(fp.getConveyorList()[index] instanceof AlexInlineMachineAgent){
				((AlexInlineMachineAgent) fp.getInlineList()[index]).msgInlineMachineUnbreak();}
			//fp.getInlineList()[index].msgInlineMachineUnbreak();
		}
		else if(ae.getSource() == popupBreakButton){
			index = popupDropList.getSelectedIndex();
			popupBreakButton.setEnabled(false);
			popupUnBreakButton.setEnabled(true);
			
			// Call message
			//fp.getPopupList()[index].msgPopupBreak();
		}
		else if(ae.getSource() == popupUnBreakButton){
			index = popupDropList.getSelectedIndex();
			popupBreakButton.setEnabled(true);
			popupUnBreakButton.setEnabled(false);
			
			// Call message
			//fp.getPopupList()[index].msgPopupUnbreak();
		}
		else if(ae.getSource() == offlineBreakButton){
			index = offlineDropList.getSelectedIndex();
			offlineBreakButton.setEnabled(false);
			offlineUnBreakButton.setEnabled(true);
			
			// Call message
			//fp.getOfflineList()[index].msgOfflineMachineBreak();
		}
		else if(ae.getSource() == offlineUnBreakButton){
			index = offlineDropList.getSelectedIndex();
			offlineBreakButton.setEnabled(true);
			offlineUnBreakButton.setEnabled(false);
			
			// Call message
			//fp.getOfflineList()[index].msgOfflineMachineUnbreak();
		}
		else if(ae.getSource() == offlineOnButton){
			index = offlineDropList.getSelectedIndex();
			offlineOnButton.setEnabled(false);
			offlineOffButton.setEnabled(true);
			
			// Call message
			//fp.getOfflineList()[index].msgOfflineMachineBreak();
		}
		else if(ae.getSource() == offlineOffButton){
			index = offlineDropList.getSelectedIndex();
			offlineOnButton.setEnabled(true);
			offlineOffButton.setEnabled(false);
			
			// Call message
			//fp.getOfflineList()[index].msgOfflineMachineBreak();
		}
		else if(ae.getSource() == offlineProcessingTimeButton){
			index = offlineDropList.getSelectedIndex();
			String ss = offlineProcessingTimeField.getText();
			Integer pTime = 0;
			try{
				pTime = Integer.parseInt(ss);
			}
			catch(Exception e){
				System.out.println("Enter an integer for Processing Time.");
				return;
			}
			
			// Call message
			//fp.getOfflineList()[index].msgChangeProcessingTime((int) pTime);
		}
		else if(ae.getSource() == offlineRemoveGlassButton){
			index = offlineDropList.getSelectedIndex();
			System.out.println(index);
			// Call message
			//fp.getOfflineList()[index].msgRemoveGlass();
		}
		
	}

	public ControlPanel getGuiParent()
	{
		return parent;
	}
	
}
