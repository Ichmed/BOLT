package util;

import org.lwjgl.util.vector.Vector3f;

public class StraightLine
{
	public Vector3f startingPoint = new Vector3f();
	public Vector3f direction = new Vector3f();
	
	public StraightLine() {	}
	
	public StraightLine(Vector3f startingPoint, Vector3f direction)
	{
		this.startingPoint = startingPoint;
		this.direction = direction;
	}
}
