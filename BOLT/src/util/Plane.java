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
}
