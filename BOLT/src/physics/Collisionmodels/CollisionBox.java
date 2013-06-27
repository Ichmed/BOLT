package physics.Collisionmodels;

import java.util.ArrayList;
import org.lwjgl.util.vector.Vector3f;

public class CollisionBox
{
	public Vector3f middle = new Vector3f (0, 0, 0);
	public Vector3f depth = new Vector3f (0, 0, 0);
	public Vector3f width = new Vector3f (0, 0, 0);
	public Vector3f height = new Vector3f (0, 0, 0);
	public float mass = 0;
	
	public CollisionBox () {	}
	
	public CollisionBox (Vector3f depth, Vector3f width, Vector3f height)
	{
		this.depth = depth;
		this.width = width;
		this.height = height;
		Vector3f.add ((Vector3f)depth.scale (0.5f), (Vector3f)width.scale (0.5f), middle);
		Vector3f.add ((Vector3f)middle, (Vector3f)height.scale (0.5f), middle);
	}
	
	public CollisionBox (float mass, Vector3f depth, Vector3f width, Vector3f height)
	{
		this(depth, width, height);
		this.mass = mass;
	}
	
	public CollisionBox(Vector3f... dots)
	{
		ArrayList<Vector3f> straightLines = new ArrayList<Vector3f>();
		for(int a = 0; a < dots.length; a++)
			for(int b = a + 1; a < dots.length; a++)
			{
				
			}
	}
	
	public CollisionBox (float mass, Vector3f... dots)
	{
		this(dots);
		this.mass = mass;
	}
}
