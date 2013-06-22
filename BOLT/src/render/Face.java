package render;

import org.lwjgl.util.vector.Vector3f;

public class Face 
{
	public Vector3f vertex = new Vector3f();
	public Vector3f normal = new Vector3f();
	public Vector3f texture = new Vector3f();
	
	public Face(Vector3f vert, Vector3f norm)
	{
		this(vert, norm, new Vector3f(0, 0, 0));
	}
	
	public Face(Vector3f vert, Vector3f norm, Vector3f tex)
	{
		this.vertex = vert;
		this.normal = norm;
		this.texture = tex;
	}
}
