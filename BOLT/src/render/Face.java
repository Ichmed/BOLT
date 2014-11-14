package render;

import org.lwjgl.util.vector.Vector3f;

public class Face {
	public final Vector3f[] points;
	
	public Face(Vector3f... vec) {
		points = vec;
	}
}
