package engine.util;

public class GlassType {
	private boolean[] popUps;
	private boolean[] processed;
	private String glassID;

	public GlassType(boolean popUp0, boolean popUp1, boolean popUp2, String ID) {
		popUps = new boolean[3];
		processed = new boolean[3];
		glassID = ID;
		popUps[0] = popUp0;
		popUps[1] = popUp1;
		popUps[2] = popUp2;

		processed[0] = false;
		processed[1] = false;
		processed[2] = false;
	}

	public boolean getConfig(int popUpNumber) {

		return popUps[popUpNumber];

	}

	public boolean getProcessingHistory(int popUpNumber) {

		return processed[popUpNumber];

	}

	public void setProcessingHistory(int popUpNumber) {

		processed[popUpNumber] = true;

	}

	public String getGlassID() {
		return glassID;
	}
}
