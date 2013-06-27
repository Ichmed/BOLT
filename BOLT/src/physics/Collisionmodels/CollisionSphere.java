package physics.Collisionmodels;

import org.lwjgl.util.vector.Vector3f;

public class CollisionSphere extends org.lwjgl.util.glu.Sphere
{
	public float radius = 0;
	public Vector3f middle = new Vector3f (0, 0, 0);
	public float mass = 0;
	
	public CollisionSphere (Vector3f middle, float radius)
	{
		this.middle = new Vector3f(middle);
		this.radius = radius;
	}
	public CollisionSphere (float x, float y, float z, float radius)
	{
		middle.x = x;
		middle.y = y;
		middle.z = z;
		this.radius = radius;
	}
	public CollisionSphere (Vector3f middle, float radius, float mass)
	{
		this.middle = new Vector3f(middle);
		this.radius = radius;
		this.mass = mass;
	}
	public CollisionSphere (float x, float y, float z, float radius, float mass)
	{
		middle.x = x;
		middle.y = y;
		middle.z = z;
		this.radius = radius;
		this. mass = mass;
	}
}
