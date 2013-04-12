
package gui.panels.subcontrolpanels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import engine.agent.BinAgent;
import engine.util.Config;
import gui.panels.ControlPanel;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * The ConfigSelectPanel class contains buttons allowing the user to select what
 * type of glass to produce.
 */
@SuppressWarnings("serial")
public class ConfigSelectPanel extends JPanel implements ActionListener
{
	/** The ControlPanel this is linked to */
	private ControlPanel parent;
	
	private List <Config> configList = new ArrayList<Config>();
	
	/**
	 * Creates a new ConfigSelect and links it to the control panel
	 * @param cp
	 *        the ConfigSelect linked to it
	 */
	public ConfigSelectPanel(ControlPanel cp)
	{
		parent = cp;
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
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
		
	}
	
	public List<Config> getConfigList(){
		return configList;
	}

}
