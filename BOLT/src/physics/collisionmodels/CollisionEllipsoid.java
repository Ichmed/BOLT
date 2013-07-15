package physics.collisionmodels;

import org.lwjgl.util.vector.Vector3f;

/**
 * an Ellipsoid shaped Collisionbox 
 * @author Marcel Mundl
 */
public class CollisionEllipsoid {
	
	/**
	 * the mass of the ellipsoid
	 * used to calculate falling of objects
	 */
	public float mass = 0;
	/**
	 * the length of the ellipsoid
	 */
	public float length = 0;
	/**
	 * the width of the ellipsoid
	 */
	public float width = 0;
	/**
	 * the height of the ellipsoid
	 */
	public float height = 0;
	/**
	 * a vector pointing at the middle of the ellipsoid
	 */
	public Vector3f middle = null;

	/**
	 * create a new Collisionbox based on a Vector pointing at the middle, length, width, height and a mass
	 * @param middle a vector to the middle
	 * @param length the first radius
	 * @param width the second radius
	 * @param height the third radius
	 * @param mass the mass used for the calculation of falling
	 */
	public CollisionEllipsoid( Vector3f middle, float length, float width, float height, float mass ) {
		this.middle = middle;
		this.length = length;
		this.width = width;
		this.height = height;
		this.mass = mass;
	}
	
	/**
	 * create a new Collisionbox based on a Vector pointing at the middle, length, width, height and a mass of 0
	 * @param middle a vector to the middle
	 * @param length the first radius
	 * @param width the second radius
	 * @param height the third radius
	 */
	public CollisionEllipsoid( Vector3f middle, float length, float width, float height ) {
		this.middle = middle;
		this.length = length;
		this.width = width;
		this.height = height;
	}
	
	/**
	 * create a new Collisionbox based on the position (with 3 coordinates), length, width and height
	 * @param x the position in x direction
	 * @param y the position in y direction
	 * @param z the position in z direction
	 * @param length the first radius
	 * @param width the second radius
	 * @param height the third radius
	 */
	public CollisionEllipsoid( float x, float y, float z, float length, float width, float height ){
		this.middle = new Vector3f(x,y,z);
		this.length = length;
		this.width = width;
		this.height = height;
	}
	
	/**
	 * create a new Collisionbox based on the position (with 3 coordinates), length, width, height and a mass
	 * @param x the position in x direction
	 * @param y the position in y direction
	 * @param z the position in z direction
	 * @param length the first radius
	 * @param width the second radius
	 * @param height the third radius
	 * @param mass the mass used for the calculation of falling
	 */
	public CollisionEllipsoid( float x, float y, float z, float length, float width, float height, float mass ){
		this.middle = new Vector3f(x,y,z);
		this.length = length;
		this.width = width;
		this.height = height;
		this.mass = mass;
	}

}
