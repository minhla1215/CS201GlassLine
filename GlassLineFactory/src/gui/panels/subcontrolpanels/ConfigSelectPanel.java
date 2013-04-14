// Minh and Alex


package gui.panels.subcontrolpanels;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import engine.agent.BinAgent;
import engine.util.Config;
import engine.util.GlassType;
import gui.panels.ControlPanel;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import transducer.TChannel;
import transducer.TEvent;

/**
 * The ConfigSelectPanel class contains buttons allowing the user to select what
 * type of glass to produce.
 */
@SuppressWarnings("serial")
public class ConfigSelectPanel extends JPanel implements ActionListener
{
	/** The ControlPanel this is linked to */
	private ControlPanel parent;
	
	//private List <Config> configList = new ArrayList<Config>();
	//alex change into glasstype
	private List <GlassType> configList;
	private List <GlassType> toBeCreatedConfigList;
	
	//ALEX: SET UP GUI FOR PRODUCTION PANEL
	JButton produceButton;
	//JList list;
	BinAgent bin;
	//String[] name;
	
	JPanel configPanel;
	JScrollPane configScrollPane;
	
	List <JPanel> configPanelList;
	List <JTextField> configTextList;
	List <JButton> configDeleteList;
	
	
	int feedSpeed = 500;
	
	/**
	 * Creates a new ConfigSelect and links it to the control panel
	 * @param cp
	 *        the ConfigSelect linked to it
	 */
	public ConfigSelectPanel(ControlPanel cp)
	{
		parent = cp;
		bin = null;
		//this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		//Alex GUI for production panel
		configList = Collections.synchronizedList(new ArrayList<GlassType>());
		toBeCreatedConfigList = new ArrayList<GlassType>();
		configPanelList = new ArrayList<JPanel>();
		configTextList = new ArrayList<JTextField>();
		configDeleteList = new ArrayList<JButton>();
//		name=new String[10];
//		list=new JList(name);
//		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//		list.setVisibleRowCount(10);
//		list.addListSelectionListener(this);
		
		configPanel = new JPanel();
		configPanel.setLayout(new BoxLayout(configPanel, BoxLayout.Y_AXIS));
		
		// Creation of the title display
		JPanel cPanel = new JPanel();
		JLabel nLabel = new JLabel("Name of Config");
		JLabel numLabel = new JLabel("Amount to produce");
		JLabel delLabel = new JLabel("Delete Config");
		cPanel.setLayout(new GridLayout(1,3));
		cPanel.add(nLabel);
		cPanel.add(numLabel);
		cPanel.add(delLabel);
		configPanel.add(cPanel);
		
		configScrollPane = new JScrollPane(configPanel);
		configScrollPane.createVerticalScrollBar();
		//configScrollPane.getViewport().add(configScrollPane);  
		
		
		
		this.setLayout(new BorderLayout());
		produceButton = new JButton("Produce");
		produceButton.addActionListener(this);
		
		
		//this.add(list,BorderLayout.WEST);
		this.add(configPanel,BorderLayout.NORTH);
		this.add(produceButton,BorderLayout.SOUTH);
	}

	/**
	 * Returns the parent panel
	 * @return the parent panel
	 */
	public ControlPanel getGuiParent()
	{
		return parent;
	}
	
	public JPanel getConfigPanel(){
		return configPanel;
	}
	
	
	public void addConfigToPanel(GlassType gt){
		configList.add(gt);
		JTextField text = new JTextField(5);
		text.setText("0");
		JLabel name = new JLabel(gt.getGlassID());
		JButton delete = new JButton("Delete");
		configTextList.add(text);
		configDeleteList.add(delete);
		JPanel pane = new JPanel();
		pane.setLayout(new GridLayout(1,3));
		pane.add(name);
		pane.add(text);
		pane.add(delete);
		delete.addActionListener(this);
		configPanelList.add(pane);
		configPanel.add(pane);
		//configPanel.revalidate();
		
	}
	
	
	

	public void actionPerformed(ActionEvent ae)
	{
		/*
		// alex: produce the chosen glasstype
		if(ae.getSource() == produceButton){
			if(bin!=null){
				System.out.println("product created");
				//ArrayList <Config> testConfig = new ArrayList<Config>();
				//testConfig.add(new Config(true,true,true, "Dragon"));
				//bin.hereIsConfig(testConfig);
				while(!configList.isEmpty()){
<<<<<<< HEAD
					bin.hereIsConfig(configList.remove(0));
=======
					new Timer().schedule(new TimerTask(){
					    public void run(){//this routine is like a message reception    
					    	bin.hereIsConfig(configList.remove(0));
					    }
					}, feedSpeed);
				
>>>>>>> branch 'master' of https://github.com/usc-csci201-spring2013/cs201GlassLine_Team08.git
				}
			}
		}
		*/
		if(ae.getSource() == produceButton){
			if(bin != null){
				for(int i = 0; i < configTextList.size();i++){
					String s = configTextList.get(i).getText();
					int size = 0;
					try{
						size = Integer.parseInt(s);
					}
					catch(Exception e){
						System.out.println("Enter intergers for values");
						return;
					}
					for(int j = 0; j < size; j++){
						toBeCreatedConfigList.add(configList.get(i));
					}
				}
			}
			//while(!toBeCreatedConfigList.isEmpty()){
			while(toBeCreatedConfigList.size() > 0){
				bin.hereIsConfig(toBeCreatedConfigList.remove(0));
			
			}
		}
		
		for(int i = 0; i < configDeleteList.size();i++){
			if(ae.getSource() == configDeleteList.get(i)){
				System.out.println(i);
				configList.remove(i);
				configDeleteList.remove(i);
				configTextList.remove(i);
				configPanel.remove(configPanelList.get(i));
				configPanelList.remove(i);
				revalidate();
				return;
			}
		}
	}
	
	public List<GlassType> getConfigList(){
		return configList;
	}
	
	public void setBinAgent(BinAgent b){
		this.bin = b;
	}

//	public JList getList() {
//		return list;
//	}


}
