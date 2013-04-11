
package gui.panels.subcontrolpanels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import engine.agent.BinAgent;
import engine.util.Config;
import gui.panels.ControlPanel;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * The GlassSelectPanel class contains buttons allowing the user to select what
 * type of glass to produce.
 */
@SuppressWarnings("serial")
public class GlassSelectPanel extends JPanel implements ActionListener
{
	/** The ControlPanel this is linked to */
	private ControlPanel parent;
	JButton produceButton;
	BinAgent bin;

	/**
	 * Creates a new GlassSelect and links it to the control panel
	 * @param cp
	 *        the ControlPanel linked to it
	 */
	public GlassSelectPanel(ControlPanel cp)
	{
		bin = null;
		parent = cp;
		produceButton = new JButton("Produce");
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
			
			ArrayList <Config> testConfig = new ArrayList<Config>();
			testConfig.add(new Config(true,true,true, "Dragon"));
			bin.hereIsConfig(testConfig);
		}
	}
	
	public void setBinAgent(BinAgent b){
		this.bin = b;
	}
}
