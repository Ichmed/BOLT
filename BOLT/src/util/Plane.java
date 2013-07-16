package util;

import org.lwjgl.util.vector.Vector3f;

public class Plane
{
	Vector3f normal = new Vector3f();
	Vector3f startingPoint = new Vector3f();
	
	public Plane() { }
	
	public Plane(Vector3f normal, Vector3f startingPoint)
	{
		this.normal = normal;
		this.startingPoint = startingPoint;
	}
	
	public Vector3f getPoint(float x, float y)
	{
		return new Vector3f (x, y, (Vector3f.dot(normal, startingPoint) - normal.x * x - normal.y * y) / normal.z);
	}
	
	public void transformToHesseNormalForm()
	{
		normal.normalise ();
		float lastFactor = normal.x * (-startingPoint.x) + normal.y * (-startingPoint.y) + normal.z * (-startingPoint.z);
		if(lastFactor > 0)
			normal.negate ();
	}
	
	/**
	 * calculates the distance between a point and a plane
	 * @param point the point
	 * @param plane the plane
	 * @return the distance
	 */
	public float calculateDistancePoint(Vector3f point)
	{
		this.transformToHesseNormalForm();
		return (this.normal.x * point.x + this.normal.y * point.y + this.normal.z * point.z - this.startingPoint.x * this.normal.x -
				this.startingPoint.y * this.normal.y - this.startingPoint.z * this.normal.z);
	}
	
	/**
	 * intersects a line with a plane
	 * @param line the line
	 * @param plane the plane
	 * @return the Vector3f to the point if parallel method returns the nullVector
	 */
	public Vector3f intersectWithLine(Line line)
	{
		//Checks weather the line is parallel to this
		if(Vector3f.dot(line.direction, this.normal) == 0)
			return new Vector3f(0, 0, 0);
		this.transformToHesseNormalForm();
		//Calculates the factor for the direction-vector of the line
		float factor = (Vector3f.dot(this.normal, this.startingPoint)-Vector3f.dot(line.direction, line.startingPoint))/
						Vector3f.dot(line.direction, this.normal);
		return line.getPoint(factor);
	}
}
