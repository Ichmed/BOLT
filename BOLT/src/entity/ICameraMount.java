package entity;

import org.lwjgl.util.vector.Vector3f;

public interface ICameraMount {
	public Vector3f getCameraPosition();
	
	public Vector3f getCameraRotation();
}
