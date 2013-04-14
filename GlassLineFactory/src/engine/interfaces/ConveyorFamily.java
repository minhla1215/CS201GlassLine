package engine.interfaces;

import engine.util.GlassType;

public interface ConveyorFamily {
	public abstract void msgPassingGlass(GlassType gt);

	public abstract void msgIAmAvailable();
	
	public abstract void msgIAmNotAvailable();
}
