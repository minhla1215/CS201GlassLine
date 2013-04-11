package engine.util;

public class Config {
	String configID;
	private boolean[] popUps;

	public Config(boolean popUp0, boolean popUp1, boolean popUp2, String ID) {
		//This determines what popup should process this piece of config
		popUps = new boolean[3];
		configID = ID;
		popUps[0] = popUp0;
		popUps[1] = popUp1;
		popUps[2] = popUp2;
	}

	public boolean getConfig(int popUpNumber) {
		return popUps[popUpNumber];
	}

	public String getConfigID() {
		return configID;
	}
}
