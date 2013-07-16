package game;

import org.lwjgl.util.vector.Vector3f;

public class Camera
{
	/**
	 * The position of the camera
	 */
	public Vector3f position = new Vector3f(0, 0, 0);
	
	/**
	 * The rotation of the camera in degrees
	 */
	public Vector3f rotation = new Vector3f(0, 0, 0);
}
