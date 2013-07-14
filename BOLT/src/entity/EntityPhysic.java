package entity;

import org.lwjgl.util.vector.Vector3f;

public class EntityPhysic extends Entity
{
	public Vector3f velocity = new Vector3f();

	public void accelerate(Vector3f v)
	{
		this.velocity.translate(v.x, v.y, v.z);
	}

	public void onTick()
	{
		this.position.translate(this.velocity.x, this.velocity.y, this.velocity.z);
	}
}
