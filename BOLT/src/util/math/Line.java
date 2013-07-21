package util.math;

import org.lwjgl.util.vector.Vector3f;

public class Line
{
	public Vector3f startingPoint = new Vector3f();
	public Vector3f direction = new Vector3f();
	
	public Line() {	}
	
	public Line(Vector3f startingPoint, Vector3f direction)
	{
		this.startingPoint.set(startingPoint.getX(), startingPoint.getY(), startingPoint.getZ());
		this.direction.set(direction.getX(), direction.getY(), direction.getZ());
	}
	
	public Line(float x, float y, float z, Vector3f direction)
	{
		this(new Vector3f(x, y, z), MathHelper.cloneVector(direction));
	}
	
	public Vector3f getStartingPoint()
	{
		return MathHelper.cloneVector(startingPoint);
	}
	
	public Vector3f getDirection()
	{
		return MathHelper.cloneVector(direction);
	}
	
	public void setStartingPoint(Vector3f newStartingPoint)
	{
		startingPoint = MathHelper.cloneVector(newStartingPoint);
	}
	
	public void setSDirection(Vector3f newDirection)
	{
		direction = MathHelper.cloneVector(newDirection);
	}
	
	public Vector3f getPoint(float factor)
	{
		Vector3f temp = new Vector3f();
		Vector3f.add((Vector3f)MathHelper.cloneVector(direction).scale(factor), startingPoint, temp);
		return temp;
	}
}
