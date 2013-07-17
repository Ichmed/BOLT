package util.math;

import org.lwjgl.util.vector.Vector3f;

/**
 * a parabola in 3d to calculate collisions
 * @author marcel
 */
public class Parabola {
	/**
	 * the starting point of the graph
	 */
	Vector3f startpoint = null;
	/**
	 * the basic direction
	 */
	Vector3f dir = null;
	/**
	 * the influence on the basic direction
	 */
	Vector3f inf = null;
	
	/**
	 * creates a parabola based on 2 vectors
	 * @param a a vector pointing at the starting point
	 * @param b the vector multiplied with u
	 * @param c the vector multiplied with u²
	 */
	public Parabola(Vector3f a, Vector3f b, Vector3f c) {
		 this.startpoint = a;
		 this.dir = b;
		 this.inf = c;
	}

	/**
	 * gives you a point from the graph by using the parameter
	 * @param u the parameter
	 * @return
	 */
	public Vector3f getPoint(float u){
		Vector3f result = new Vector3f();
		result.setX(startpoint.getX() + u * dir.getX() + u * u * inf.getX());
		result.setY(startpoint.getY() + u * dir.getY() + u * u * inf.getY());
		result.setZ(startpoint.getZ() + u * dir.getZ() + u * u * inf.getZ());
		return result;
	}
}
