package render;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Model
{
	public List<Vector3f> vertices = new ArrayList<Vector3f>();
	public List<Vector3f> normals = new ArrayList<Vector3f>();
	public List<Vector2f> tetxures = new ArrayList<Vector2f>();
	
	public boolean hasNormals = false;
	public boolean hasTextures = false;

	public List<Face> faces = new ArrayList<Face>();

	public Model()
	{
	}

	public void renderModel()
	{
		glBegin(GL_TRIANGLES);
		for (Face face : this.faces)
		{
			if(this.hasNormals)
			{
				Vector3f n1 = this.normals.get((int) face.normal.x - 1);
				glNormal3f(n1.x, n1.y, n1.z);
			}
			if(this.hasTextures)
			{
				Vector2f t1 = this.tetxures.get((int)face.texture.x - 1);
				glTexCoord2f(t1.x, t1.y);
			}
			Vector3f v1 = this.vertices.get((int) face.vertex.x - 1);
			glVertex3f(v1.x, v1.y, v1.z);


			if(this.hasNormals)
			{
				Vector3f n2 = this.normals.get((int) face.normal.y - 1);
				glNormal3f(n2.x, n2.y, n2.z);
			}
			if(this.hasTextures)
			{
				Vector2f t2 = this.tetxures.get((int)face.texture.y - 1);
				glTexCoord2f(t2.x, t2.y);
			}
			Vector3f v2 = this.vertices.get((int) face.vertex.y - 1);
			glVertex3f(v2.x, v2.y, v2.z);

			if(this.hasNormals)
			{
				Vector3f n3 = this.normals.get((int) face.normal.z - 1);
				glNormal3f(n3.x, n3.y, n3.z);
			}
			if(this.hasTextures)
			{
				Vector2f t3 = this.tetxures.get((int)face.texture.z - 1);
				glTexCoord2f(t3.x, t3.y);
			}
			Vector3f v3 = this.vertices.get((int) face.vertex.z - 1);
			glVertex3f(v3.x, v3.y, v3.z);
		}
		glEnd();
	}
}
