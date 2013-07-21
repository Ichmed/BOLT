package game;

import org.lwjgl.util.vector.Vector3f;

import entity.Entity;
import entity.ICameraMount;

public class CameraMounted extends Camera
{
	private Entity parent;

	@Override
	public Vector3f getPosition()
	{
		if (parent instanceof ICameraMount) return ((ICameraMount) parent).getCameraPosition();
		return parent.position;
	}

	@Override
	public void setPosition(Vector3f position)
	{
	}

	@Override
	public Vector3f getRotation()
	{
		if (parent instanceof ICameraMount) return ((ICameraMount) parent).getCameraRotation();
		return parent.rotation;
	}

	@Override
	public void setRotation(Vector3f rotation)
	{
	}

	public Entity getParent()
	{
		return parent;
	}

	public void setParent(Entity parent)
	{
		this.parent = parent;
	}

}
