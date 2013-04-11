package engine.interfaces;

import engine.util.GlassType;


public interface JoshBackSensor {

	public abstract void msgPassingGlass(GlassType gt);
	
	public abstract void msgIAmAvailable();
}
