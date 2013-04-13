
package gui.panels.subcontrolpanels;

import engine.agent.BinAgent;
import engine.util.Config;
import engine.util.GlassType;
import gui.panels.ControlPanel;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

/**
 * The GlassSelectPanel class contains buttons allowing the user to select what
 * type of glass to produce.
 */
@SuppressWarnings("serial")
public class GlassSelectPanel extends JPanel implements ActionListener
{
	/** The ControlPanel this is linked to */
	private ControlPanel parent;
	
	// JLabels for the popups
	//JLabel configLabel;
	JPanel namePanel, popup1Panel, popup2Panel, popup3Panel; 
	JLabel nameLabel, popup1Label, popup2Label, popup3Label;
	JCheckBox popup1CheckBox, popup2CheckBox, popup3CheckBox;
	JTextField nameTextField;
	JButton produceButton;
	JButton createButton;
	//BinAgent bin;
	
	//Alex: Add panels and checkbox for all the inline machine
	JPanel cutterPanel,breakoutPanel,manualBreakoutPanel,washerPanel,painterPanel,uvLampPanel,ovenPanel;
	JLabel cutterLabel,breakoutLabel,manualBreakoutLabel,washerLabel,painterLabel,uvLampLabel,ovenLabel;
	JCheckBox cutterCheckBox,breakoutCheckBox,manualBreakoutCheckBox,washerCheckBox,painterCheckBox,uvLampCheckBox,ovenCheckBox;
	JPanel glassTypeCreatePanel=new JPanel();
	JPanel choicePanel=new JPanel();
	JPanel row1Panel=new JPanel();
	JPanel row2Panel=new JPanel();
	JPanel row3Panel=new JPanel();
	
	ConfigSelectPanel configSelectPanel;

	/**
	 * Creates a new GlassSelect and links it to the control panel
	 * @param cp
	 *        the ControlPanel linked to it
	 */
	public GlassSelectPanel(ControlPanel cp)
	{
		//this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		//this.setLayout(new GridLayout(7,1));
		this.setLayout(new BorderLayout());
		//bin = null;
		parent = cp;
		configSelectPanel = cp.getConfigSelectPanel();
		
		//configLabel = new JLabel("Create Different Type of the glass:",JLabel.CENTER);
		
		namePanel = new JPanel();
		namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.X_AXIS));
		nameLabel = new JLabel("Name:   ");
		nameTextField = new JTextField(20);
		//nameTextField.setSize(20,20);
		namePanel.add(nameLabel);
		namePanel.add(nameTextField);
		
		popup1Panel = new JPanel();
		popup1Panel.setLayout(new BoxLayout(popup1Panel, BoxLayout.X_AXIS));
		popup1Label = new JLabel("Pop-up 1");
		popup1CheckBox = new JCheckBox();
		popup1CheckBox.addActionListener(this);
		popup1Panel.add(popup1Label);
		popup1Panel.add(popup1CheckBox);
		
		popup2Panel = new JPanel();
		popup2Panel.setLayout(new BoxLayout(popup2Panel, BoxLayout.X_AXIS));
		popup2Label = new JLabel("Pop-up 2");
		popup2CheckBox = new JCheckBox();
		popup2CheckBox.addActionListener(this);
		popup2Panel.add(popup2Label);
		popup2Panel.add(popup2CheckBox);
		
		popup3Panel = new JPanel();
		popup3Panel.setLayout(new BoxLayout(popup3Panel, BoxLayout.X_AXIS));
		popup3Label = new JLabel("Pop-up 3");
		popup3CheckBox = new JCheckBox();
		popup3CheckBox.addActionListener(this);
		popup3Panel.add(popup3Label);
		popup3Panel.add(popup3CheckBox);
		
		createButton = new JButton("Create the Glass");
		createButton.addActionListener(this);
		
		//testing
		//produceButton = new JButton("Produce");
		
		//this.add(configLabel);
		//this.add(namePanel);
		//this.add(popup1Panel);
		//this.add(popup2Panel);
		//this.add(popup3Panel);
		//this.add(createButton);
		
		//testing
		//this.add(produceButton);
		//produceButton.addActionListener(this);
		
		//Alex: set up all the inline machine checkbox
				cutterPanel = new JPanel();
				cutterPanel.setLayout(new BoxLayout(cutterPanel, BoxLayout.X_AXIS));
				cutterLabel = new JLabel("Cutter");
				cutterCheckBox = new JCheckBox();
				cutterCheckBox.addActionListener(this);
				cutterCheckBox.setSelected(true);
				cutterPanel.add(cutterLabel);
				cutterPanel.add(cutterCheckBox);
				
				breakoutPanel = new JPanel();
				breakoutPanel.setLayout(new BoxLayout(breakoutPanel, BoxLayout.X_AXIS));
				breakoutLabel = new JLabel("Breakout");
				breakoutCheckBox = new JCheckBox();
				breakoutCheckBox.addActionListener(this);
				breakoutCheckBox.setSelected(true);
				breakoutPanel.add(breakoutLabel);
				breakoutPanel.add(breakoutCheckBox);
				
				manualBreakoutPanel = new JPanel();
				manualBreakoutPanel.setLayout(new BoxLayout(manualBreakoutPanel, BoxLayout.X_AXIS));
				manualBreakoutLabel = new JLabel("ManualBreakout");
				manualBreakoutCheckBox = new JCheckBox();
				manualBreakoutCheckBox.addActionListener(this);
				manualBreakoutCheckBox.setSelected(true);
				manualBreakoutPanel.add(manualBreakoutLabel);
				manualBreakoutPanel.add(manualBreakoutCheckBox);
				
				washerPanel = new JPanel();
				washerPanel.setLayout(new BoxLayout(washerPanel, BoxLayout.X_AXIS));
				washerLabel = new JLabel("Washer");
				washerCheckBox = new JCheckBox();
				washerCheckBox.addActionListener(this);
				washerCheckBox.setSelected(true);
				washerPanel.add(washerLabel);
				washerPanel.add(washerCheckBox);
				
				painterPanel = new JPanel();
				painterPanel.setLayout(new BoxLayout(painterPanel, BoxLayout.X_AXIS));
				painterLabel = new JLabel("Painter");
				painterCheckBox = new JCheckBox();
				painterCheckBox.addActionListener(this);
				painterCheckBox.setSelected(true);
				painterPanel.add(painterLabel);
				painterPanel.add(painterCheckBox);
				
				uvLampPanel = new JPanel();
				uvLampPanel.setLayout(new BoxLayout(uvLampPanel, BoxLayout.X_AXIS));
				uvLampLabel = new JLabel("UvLamp");
				uvLampCheckBox = new JCheckBox();
				uvLampCheckBox.addActionListener(this);
				uvLampCheckBox.setSelected(true);
				uvLampPanel.add(uvLampLabel);
				uvLampPanel.add(uvLampCheckBox);
				
				ovenPanel = new JPanel();
				ovenPanel.setLayout(new BoxLayout(ovenPanel, BoxLayout.X_AXIS));
				ovenLabel = new JLabel("Oven");
				ovenCheckBox = new JCheckBox();
				ovenCheckBox.addActionListener(this);
				ovenCheckBox.setSelected(true);
				ovenPanel.add(ovenLabel);
				ovenPanel.add(ovenCheckBox);
		
		
		//Alex setup GUI
		row1Panel.setLayout(new GridLayout(1,3));
		row2Panel.setLayout(new GridLayout(1,3));
		row3Panel.setLayout(new GridLayout(1,4));
		row1Panel.setBorder(new TitledBorder(""));
		row2Panel.setBorder(new TitledBorder(""));
		row3Panel.setBorder(new TitledBorder(""));
		namePanel.setBorder(new TitledBorder(""));
		row1Panel.add(popup1Panel);
		row1Panel.add(popup2Panel);
		row1Panel.add(popup3Panel);
		row2Panel.add(cutterPanel);
		row2Panel.add(breakoutPanel);
		row2Panel.add(manualBreakoutPanel);
		row3Panel.add(washerPanel);
		row3Panel.add(painterPanel);
		row3Panel.add(uvLampPanel);
		row3Panel.add(ovenPanel);

		
		choicePanel.setLayout(new GridLayout(3,1));
		choicePanel.add(row1Panel);
		choicePanel.add(row2Panel);
		choicePanel.add(row3Panel);
		
		
		
		glassTypeCreatePanel.setLayout(new BorderLayout());
		glassTypeCreatePanel.add(namePanel,BorderLayout.NORTH);
		glassTypeCreatePanel.add(choicePanel,BorderLayout.CENTER);
		glassTypeCreatePanel.setBorder(new TitledBorder("Create Different Type of the glasses"));
		
		
		//this.add(configLabel,BorderLayout.NORTH);
		this.add(glassTypeCreatePanel,BorderLayout.CENTER);
		this.add(createButton,BorderLayout.SOUTH);
		
		
	}

	/**
	 * Returns the parent panel
	 * @return the parent panel
	 */
	public ControlPanel getGuiParent()
	{
		return parent;
	}

	public void actionPerformed(ActionEvent ae)
	{
//		if(ae.getSource() == produceButton){
//			if(bin!=null){
//				System.out.println("product created");
//				ArrayList <Config> testConfig = new ArrayList<Config>();
//				testConfig.add(new Config(true,true,true, "Dragon"));
//				bin.hereIsConfig(testConfig);
//			}
//		}
		if(ae.getSource() == createButton){	
			// create a new config in the Config Select Panel's list
			
//			configSelectPanel.getConfigList().add(
//			new Config(popup1CheckBox.isSelected(),popup2CheckBox.isSelected(),
//					popup3CheckBox.isSelected(), nameTextField.getText()));
			
			
			//Alex add new glass type to the list
			GlassType tempGlass=new GlassType(popup1CheckBox.isSelected(),popup2CheckBox.isSelected(),
							popup3CheckBox.isSelected(), nameTextField.getText());
			tempGlass.getinlineMachineProcessingNeeded()[0]=cutterCheckBox.isSelected();
			tempGlass.getinlineMachineProcessingNeeded()[1]=breakoutCheckBox.isSelected();
			tempGlass.getinlineMachineProcessingNeeded()[2]=manualBreakoutCheckBox.isSelected();
			tempGlass.getinlineMachineProcessingNeeded()[3]=washerCheckBox.isSelected();
			tempGlass.getinlineMachineProcessingNeeded()[4]=painterCheckBox.isSelected();
			tempGlass.getinlineMachineProcessingNeeded()[5]=uvLampCheckBox.isSelected();
			tempGlass.getinlineMachineProcessingNeeded()[6]=ovenCheckBox.isSelected();
			configSelectPanel.getConfigList().add(tempGlass);
			
			

			popup1CheckBox.setSelected(false);
			popup2CheckBox.setSelected(false);
			popup3CheckBox.setSelected(false);
			nameTextField.setText("");
			
			//reset all the check box
			cutterCheckBox.setSelected(true);
			breakoutCheckBox.setSelected(true);
			manualBreakoutCheckBox.setSelected(true);
			washerCheckBox.setSelected(true);
			painterCheckBox.setSelected(true);
			uvLampCheckBox.setSelected(true);
			ovenCheckBox.setSelected(true);
			
				
		}
		
	}

//	public void setBinAgent(BinAgent b){
//		this.bin = b;
//	}
}
