package engine.interfaces;

import engine.util.GlassType;

public interface SkyMachine {

	public abstract void msgPassingGlass(GlassType gt);

	public abstract void msgIAmAvailable();

	public abstract void msgIAmReady();
	
	public abstract String getName();

}