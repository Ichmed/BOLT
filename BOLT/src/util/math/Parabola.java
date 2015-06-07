package util.math;

import org.lwjgl.util.vector.Vector3f;

/**
 * a parabola in 3d to calculate collisions
 * 
 * @author marcel
 */
public class Parabola {
	/**
	 * the starting point of the graph
	 */
	public Vector3f startingPoint = null;
	/**
	 * the basic direction
	 */
	public Vector3f dir = null;
	/**
	 * the influence on the basic direction
	 */
	public Vector3f inf = null;
	
	/**
	 * creates a parabola based on 2 vectors
	 * 
	 * @param a a vector pointing at the starting point
	 * @param b the vector multiplied with u
	 * @param c the vector multiplied with u²
	 */
	public Parabola(Vector3f a, Vector3f b, Vector3f c) {
		this.startingPoint = MathHelper.cloneVector(a);
		this.dir = MathHelper.cloneVector(b);
		this.inf = MathHelper.cloneVector(c);
	}
	
	public Vector3f getStartingPoint() {
		return MathHelper.cloneVector(startingPoint);
	}
	
	public Vector3f getDirection() {
		return MathHelper.cloneVector(dir);
	}
	
	public Vector3f getInfluence() {
		return MathHelper.cloneVector(inf);
	}
	
	public void setStartingPoint(Vector3f startingPoint) {
		this.startingPoint = MathHelper.cloneVector(startingPoint);
	}
	
	public void setDirection(Vector3f dir) {
		this.dir = MathHelper.cloneVector(dir);
	}
	
	public void setInfluence(Vector3f inf) {
		this.inf = MathHelper.cloneVector(inf);
	}
	
	/**
	 * gives you a point from the graph by using the parameter
	 * 
	 * @param u the parameter
	 * @return
	 */
	public Vector3f getPoint(float u) {
		Vector3f result = new Vector3f();
		result.setX(startingPoint.getX() + u * dir.getX() + u * u * inf.getX());
		result.setY(startingPoint.getY() + u * dir.getY() + u * u * inf.getY());
		result.setZ(startingPoint.getZ() + u * dir.getZ() + u * u * inf.getZ());
		return result;
	}
	
	@Override
	public String toString() {
		return "direction: " + dir.toString() + "\ninfluence: " + inf.toString() + "\nstartingPoint: " + startingPoint.toString();
	}
}
