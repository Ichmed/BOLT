package game;

import org.lwjgl.util.vector.Vector3f;

/**
 * The standard camera, freely controllable via setRotation(Vector3f rotation) and setPosition(Vector3f position)
 * @author Ichmed
 *
 */
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

	/**
	 * @return The position of the camera, the returned Vector3f is a new object and has no reference to the original vector
	 */
	public Vector3f getPosition()
	{
		return new Vector3f(position.x, position.y, position.z);
	}

	/**
	 * Sets the position
	 * @param position
	 */
	public void setPosition(Vector3f position)
	{
		this.position.x = position.x;
		this.position.y = position.y;
		this.position.z = position.z;
	}

	/**
	 * 
	 * @return The rotation of the camera, the returned Vector3f is a new object and has no reference to the original vector
	 */
	public Vector3f getRotation()
	{
		return new Vector3f(rotation.x, rotation.y, rotation.z);
	}

	/**
	 * Sets the rotation of the camera
	 * @param rotation
	 */
	public void setRotation(Vector3f rotation)
	{
		this.rotation.x = rotation.x;
		this.rotation.y = rotation.y;
		this.rotation.z = rotation.z;
	}
}
