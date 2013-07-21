package game;

import org.lwjgl.util.vector.Vector3f;

public class Camera
{
	/**
	 * The position of the camera
	 */
	private Vector3f position = new Vector3f(0, 0, 0);
	

	/**
	 * The rotation of the camera in degrees
	 */
	private Vector3f rotation = new Vector3f(0, 0, 0);
	
	public Vector3f getPosition()
	{
		return position;
	}

	public void setPosition(Vector3f position)
	{
		this.position = position;
	}

	public Vector3f getRotation()
	{
		return rotation;
	}

	public void setRotation(Vector3f rotation)
	{
		this.rotation = rotation;
	}
}
