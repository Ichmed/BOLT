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
	
	public void transformToHesseNormalForm()
	{
		normal.normalise ();
		float lastFactor = normal.x * (-startingPoint.x) + normal.y * (-startingPoint.y) + normal.z * (-startingPoint.z);
		if(lastFactor > 0)
			normal.negate ();
	}
}
