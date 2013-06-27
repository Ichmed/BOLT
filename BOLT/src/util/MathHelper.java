package util;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public abstract class MathHelper
{
	public static Vector2f normalize(Vector2f v)
	{
		v.scale(1 / v.length());
		return v;
	}
	
	public static Vector3f normalize(Vector3f v)
	{
		v.scale(1 / v.length());
		return v;
	}
	
	public static Vector4f normalize(Vector4f v)
	{
		v.scale(1 / v.length());
		return v;
	}
	
	/*TODO: Intersection between a plane and a straight line
	public static Vector3f calculateIntersection()
	{
		
	}*/
}
