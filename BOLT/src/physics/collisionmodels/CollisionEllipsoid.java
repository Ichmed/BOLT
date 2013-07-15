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
	 * first radius
	 */
	public float rad1 = 0;
	/**
	 * second radius
	 */
	public float rad2 = 0;
	/**
	 * third radius
	 */
	public float rad3 = 0;
	/**
	 * a vector pointing at the middle of the ellipsoid
	 */
	public Vector3f middle = null;

	/**
	 * create a new Collisionbox based on a Vector pointing at the middle and 3 radii and a mass
	 * @param middle a vector to the middle
	 * @param rad1 the first radius
	 * @param rad2 the second radius
	 * @param rad3 the third radius
	 * @param mass the mass used for the calculation of falling
	 */
	public CollisionEllipsoid( Vector3f middle, float rad1, float rad2, float rad3, float mass ) {
		this.middle = middle;
		this.rad1 = rad1;
		this.rad2 = rad2;
		this.rad3 = rad3;
		this.mass = mass;
	}
	
	/**
	 * create a new Collisionbox based on a Vector pointing at the middle and 3 radii and a mass of 0
	 * @param middle a vector to the middle
	 * @param rad1 the first radius
	 * @param rad2 the second radius
	 * @param rad3 the third radius
	 */
	public CollisionEllipsoid( Vector3f middle, float rad1, float rad2, float rad3 ) {
		this.middle = middle;
		this.rad1 = rad1;
		this.rad2 = rad2;
		this.rad3 = rad3;
	}
	
	/**
	 * create a new Collisionbox based on the position (with 3 Koordinates) and 3 radius
	 * @param x the position in x direction
	 * @param y the position in y ditection
	 * @param z the position in z ditection
	 * @param rad1 the first radius
	 * @param rad2 the second radius
	 * @param rad3 the third radius
	 */
	public CollisionEllipsoid( float x, float y, float z, float rad1, float rad2, float rad3 ){
		this.middle = new Vector3f(x,y,z);
		this.rad1 = rad1;
		this.rad2 = rad2;
		this.rad3 = rad3;
	}
	
	/**
	 * create a new Collisionbox based on the position (with 3 Koordinates) and 3 radius and a mass
	 * @param x the position in x direction
	 * @param y the position in y ditection
	 * @param z the position in z ditection
	 * @param rad1 the first radius
	 * @param rad2 the second radius
	 * @param rad3 the third radius
	 * @param mass the mass used for the calculation of falling
	 */
	public CollisionEllipsoid( float x, float y, float z, float rad1, float rad2, float rad3, float mass ){
		this.middle = new Vector3f(x,y,z);
		this.rad1 = rad1;
		this.rad2 = rad2;
		this.rad3 = rad3;
		this.mass = mass;
	}

}
