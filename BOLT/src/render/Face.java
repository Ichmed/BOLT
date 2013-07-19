package render;

import org.lwjgl.util.vector.Vector3f;

public class Face 
{
	public final Vector3f[] points;
	int pointCount = 0;
	
	public Face(Vector3f... vec)
	{
		points = vec;
		this.pointCount = vec.length;
	}
}
