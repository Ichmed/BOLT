package util;

import org.lwjgl.util.vector.Vector3f;

public class Line
{
	public Vector3f startingPoint = new Vector3f();
	public Vector3f direction = new Vector3f();
	
	public Line() {	}
	
	public Line(Vector3f startingPoint, Vector3f direction)
	{
		this.startingPoint = startingPoint;
		this.direction = direction;
	}
}
