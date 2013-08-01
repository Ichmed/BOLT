package physics.collisionObjects;

import org.lwjgl.util.vector.Vector3f;

import util.math.MathHelper;

/**
 * /** a Sphere shaped CollisionBox
 * 
 * @author Felix Schmidt
 */
public class CollisionSphere extends CollisionObject
{
	/**
	 * the radius of the Sphere
	 */
	public float radius = 0;

	/**
	 * creates a CollisionSphere with all values set to 0
	 */
	public CollisionSphere()
	{
	}

	/**
	 * creates a CollisionSphere with the given radius and the middle but with
	 * mass set to 0
	 * 
	 * @param middle
	 *            the middle of the Sphere
	 * @param radius
	 *            the radius of the Sphere
	 */
	public CollisionSphere(Vector3f middle, float radius)
	{
		this.middle = new Vector3f(middle);
		this.radius = radius;
	}

	/**
	 * creates a CollisionSphere with the middle given through its coordinates
	 * and a radius but with mass set to 0
	 * 
	 * @param x
	 *            the x coordinate of the middle of the Sphere
	 * @param y
	 *            the x coordinate of the middle of the Sphere
	 * @param z
	 *            the x coordinate of the middle of the Sphere
	 * @param radius
	 *            the radius of the Sphere
	 */
	public CollisionSphere(float x, float y, float z, float radius)
	{
		middle.x = x;
		middle.y = y;
		middle.z = z;
		this.radius = radius;
	}

	/**
	 * creates a CollisionSphere with the given radius the middle and a mass
	 * 
	 * @param middle
	 *            the middle of the Sphere
	 * @param radius
	 *            the radius of the Sphere
	 * @param mass
	 *            the mass of the surrounded object
	 */
	public CollisionSphere(Vector3f middle, float radius, float mass)
	{
		this.middle = new Vector3f(middle);
		this.radius = radius;
		this.mass = mass;
	}

	/**
	 * creates a CollisionSphere with the middle given through its coordinates,
	 * a radius and a mass
	 * 
	 * @param x
	 *            the x coordinate of the middle of the Sphere
	 * @param y
	 *            the x coordinate of the middle of the Sphere
	 * @param z
	 *            the x coordinate of the middle of the Sphere
	 * @param radius
	 *            the radius of the Sphere
	 * @param mass
	 *            the mass of the surrounded object
	 */
	public CollisionSphere(float x, float y, float z, float radius, float mass)
	{
		middle.x = x;
		middle.y = y;
		middle.z = z;
		this.radius = radius;
		this.mass = mass;
	}

	/**
	 * creates a CollisionSphere out of the object given as points and mass set
	 * to 0
	 * 
	 * @param points
	 *            all the points of the object
	 */
	public CollisionSphere(Vector3f... points)
	{
		CollisionSphere temp = create(points);
		radius = temp.radius;
		middle = temp.middle;
	}

	/**
	 * creates a CollisionSphere out of the object given as points and a mass
	 * 
	 * @param mass
	 *            the mass of the surrounded object
	 * @param points
	 *            all the points of the object
	 */
	public CollisionSphere(float mass, Vector3f... points)
	{
		this(points);
		this.mass = mass;
	}

	public Vector3f getMiddle()
	{
		return MathHelper.cloneVector(middle);
	}

	public void setMiddle(Vector3f middle)
	{
		this.middle = MathHelper.cloneVector(middle);
	}

	/**
	 * creates a CollisionSphere out of the object given as points and mass set
	 * to 0
	 * 
	 * @param points
	 *            all the points of the object
	 * @return the best CollisionSphere of the object
	 */
	public static CollisionSphere create(Vector3f... points)
	{
		CollisionSphere temp = new CollisionSphere();
		Vector3f middle = new Vector3f();
		for (Vector3f point : points)
			middle.setX(middle.getX() + point.getX());
		middle.setX(middle.getX() / points.length);
		for (Vector3f point : points)
			middle.setX(middle.getY() + point.getY());
		middle.setY(middle.getY() / points.length);
		for (Vector3f point : points)
			middle.setX(middle.getZ() + point.getZ());
		middle.setZ(middle.getZ() / points.length);
		float biggestRadius = 0;
		for (Vector3f point : points)
		{
			Vector3f radius = new Vector3f();
			Vector3f.sub(temp.middle, point, radius);
			if (radius.length() > biggestRadius) biggestRadius = radius.length();
		}
		temp.radius = biggestRadius;
		return temp;
	}

	/**
	 * creates a CollisionSphere out of the object given as points and a mass
	 * 
	 * @param mass
	 *            the mass of the surrounded object
	 * @param points
	 *            all the points of the object
	 * @return the best CollisionSphere of the object with a mass
	 */
	public static CollisionSphere create(float mass, Vector3f... points)
	{
		CollisionSphere temp = create(points);
		temp.mass = mass;
		return temp;
	}

	/**
	 * calculates the distance to a point
	 * 
	 * @param point
	 *            the point to calculate the distance
	 * @return the distance to the Sphere surface, negative if the point is
	 *         within the Sphere
	 */
	public float calculateDistanceToPoint(Vector3f point)
	{
		Vector3f temp = new Vector3f();
		Vector3f.sub(point, middle, temp);
		return (float) Math.sqrt(Vector3f.dot(temp, temp)) - radius;
	}

	@Override
	public String toString()
	{
		return "radius: " + radius + "\nmiddle: " + middle.toString() + "\nmass: " + mass;
	}
}
