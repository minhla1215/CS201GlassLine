
package gui.panels.subcontrolpanels;

import engine.agent.BinAgent;
import engine.util.Config;
import gui.panels.ControlPanel;

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
	JLabel configLabel;
	JPanel namePanel, popup1Panel, popup2Panel, popup3Panel; 
	JLabel nameLabel, popup1Label, popup2Label, popup3Label;
	JCheckBox popup1CheckBox, popup2CheckBox, popup3CheckBox;
	JTextField nameTextField;
	JButton produceButton;
	JButton createButton;
	BinAgent bin;
	
	ConfigSelectPanel configSelectPanel;

	/**
	 * Creates a new GlassSelect and links it to the control panel
	 * @param cp
	 *        the ControlPanel linked to it
	 */
	public GlassSelectPanel(ControlPanel cp)
	{
		//this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setLayout(new GridLayout(7,1));
		bin = null;
		parent = cp;
		configSelectPanel = cp.getConfigSelectPanel();
		
		configLabel = new JLabel("Select the pop-up's for the glass:");
		
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
		
		createButton = new JButton("Create Config");
		createButton.addActionListener(this);
		
		//testing
		produceButton = new JButton("Produce");
		
		this.add(configLabel);
		this.add(namePanel);
		this.add(popup1Panel);
		this.add(popup2Panel);
		this.add(popup3Panel);
		this.add(createButton);
		
		//testing
		this.add(produceButton);
		produceButton.addActionListener(this);
		
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
		if(ae.getSource() == produceButton){
			if(bin!=null){
				System.out.println("product created");
				ArrayList <Config> testConfig = new ArrayList<Config>();
				testConfig.add(new Config(true,true,true, "Dragon"));
				bin.hereIsConfig(testConfig);
			}
		}
		if(ae.getSource() == createButton){	
			// create a new config in the Config Select Panel's list
			configSelectPanel.getConfigList().add(
					new Config(popup1CheckBox.isSelected(),popup2CheckBox.isSelected(),
							popup3CheckBox.isSelected(), nameTextField.getText()));
			popup1CheckBox.setSelected(false);
			popup2CheckBox.setSelected(false);
			popup3CheckBox.setSelected(false);
			nameTextField.setText("");
				
		}
		
	}

	public void setBinAgent(BinAgent b){
		this.bin = b;
	}
}
