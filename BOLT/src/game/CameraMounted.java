package game;

import org.lwjgl.util.vector.Vector3f;

import util.math.MathHelper;

import entity.Entity;
import entity.ICameraMount;

/**
 * This camera has to be mounted to an Entity, meaning it will have the entities rotation and position values. 
 * If the Entity implements ICameraMount it can specify the cameras rotation and position otherwise the entities own values will be used instead.
 * Because of this nature the cameras position and rotation can not be set, though it is possible to change the cameras parent
 * @author Ichmed
 *
 */
public class CameraMounted extends Camera
{
	/**
	 * The Entity the camera is mounted to
	 */
	private Entity parent;

	@Override
	public Vector3f getPosition()
	{
		if (parent instanceof ICameraMount) return MathHelper.cloneVector(((ICameraMount) parent).getCameraPosition());
		return MathHelper.cloneVector(parent.position);
	}
	
	/**
	 * This method will not work because the camera is mounted to an Entity
	 */
	@Override
	public void setPosition(Vector3f position)
	{
	}

	@Override
	public Vector3f getRotation()
	{
		if (parent instanceof ICameraMount) return MathHelper.cloneVector(((ICameraMount) parent).getCameraRotation());
		return MathHelper.cloneVector(parent.rotation);
	}
	
	/**
	 * This method will not work because the camera is mounted to an Entity
	 */
	@Override
	public void setRotation(Vector3f rotation)
	{
	}

	/**
	 * 
	 * @return The Entity the camera is currently mounted to.
	 */
	public Entity getParent()
	{
		return parent;
	}

	/**
	 * Sets the Entity the camera is mounted to.
	 * @param parent The new parent
	 */
	public void setParent(Entity parent)
	{
		this.parent = parent;
	}

}
