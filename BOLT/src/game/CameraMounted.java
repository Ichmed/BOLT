package game;

import org.lwjgl.util.vector.Vector3f;

import entity.Entity;

public class CameraMounted extends Camera
{
	private Entity parent;

	@Override
	public Vector3f getPosition()
	{
		return parent.position;
	}

	@Override
	public void setPosition(Vector3f position)
	{
	}

	@Override
	public Vector3f getRotation()
	{
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
