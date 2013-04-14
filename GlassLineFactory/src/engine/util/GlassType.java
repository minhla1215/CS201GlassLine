package engine.util;

public class GlassType {

	private boolean[] popUps;
	private boolean[] processed;
	private boolean[] inlineMachineProcessed;
	
	private String glassID;

	
	public GlassType(boolean popUp0, boolean popUp1, boolean popUp2, String ID) {

		//This determines what popup should process this piece of glass
		popUps = new boolean[3];
		glassID = ID;
		popUps[0] = popUp0;
		popUps[1] = popUp1;
		popUps[2] = popUp2;

		//This remembers which popups the glass has already been processed by.
		processed = new boolean[3];
		processed[0] = false;
		processed[1] = false;
		processed[2] = false;
		
		//This remembers which inline machines the glass has already been processed by.
		inlineMachineProcessed = new boolean[7];
		inlineMachineProcessed[0] = false;
		inlineMachineProcessed[1] = false;
		inlineMachineProcessed[2] = false;
		inlineMachineProcessed[3] = false;
		inlineMachineProcessed[4] = false;
		inlineMachineProcessed[5] = false;
		inlineMachineProcessed[6] = false;

	}

	
	
	//This returns a boolean that determines whether a popup needs to process this piece of glass.
	public boolean getConfig(int popUpNumber) {
			return popUps[popUpNumber];
	}

	
	
	//This returns which popups have already processed this piece of glass.
	public boolean getProcessingHistory(int popUpNumber) {
		return processed[popUpNumber];
	}
	
	
	//This returns which inlineMachines have already processed this piece of glass.
	public boolean getInlineMachineProcessingHistory(int inlineMachineNumber) {
		return inlineMachineProcessed[inlineMachineNumber];
	}

	
	
	//This sets Processing History true for a specified popup.
	public void setProcessingHistory(int popUpNumber) {
		processed[popUpNumber] = true;
	}

	
	
	//This sets Processing History true for a specified inline machine.
	public void setInlineMachineProcessingHistory(int inlineMachineNumber) {
		inlineMachineProcessed[inlineMachineNumber] = true;
	}
	
	
	
	//This returns the glass's name.
	public String getGlassID() {
		return glassID;
	}

}
