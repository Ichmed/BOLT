package render;

import org.lwjgl.util.vector.Vector3f;

public class Face 
{
	public Vector3f vertex = new Vector3f();
	public Vector3f normal = new Vector3f();
	
	public Face(Vector3f vert, Vector3f norm)
	{
		this.vertex = vert;
		this.normal = norm;
	}
}
