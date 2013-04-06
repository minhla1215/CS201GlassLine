package engine.interfaces;

import engine.util.GlassType;

public interface Conveyor {
	public abstract void msgGlassEntering();
	public abstract void msgGlassExiting();
	public abstract void msgPassingGlass(GlassType gt);

	public abstract void msgIAmAvailable();
}
