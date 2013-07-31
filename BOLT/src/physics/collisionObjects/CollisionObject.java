package physics.collisionObjects;

import org.lwjgl.util.vector.Vector3f;

public abstract class CollisionObject
{
	/**
	 * a vector pointing at the middle of the ellipsoid
	 */
	public Vector3f middle = new Vector3f(0, 0, 0);
	/**
	 * the mass of the surrounded object
	 */
	public float mass = 0;
}
