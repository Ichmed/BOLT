package render;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import util.math.MathHelper;

public class Model
{
	public List<Vector3f> vertices = new ArrayList<Vector3f>();
	public List<Vector3f> normals = new ArrayList<Vector3f>();
	public List<Vector2f> tetxures = new ArrayList<Vector2f>();

	public HashMap<String, Material> materials = new HashMap<>();

	public boolean hasNormals = false;
	public boolean hasTextures = false;
	public boolean usesMaterials = false;

	/**
	 * A list of all faces that make up this model, divided into smaller groups for use with materials
	 */
	public List<List<Face>> faces = new ArrayList<List<Face>>();
	/**
	 * A list of all materials used by this model
	 */
	public List<String> faceMaterials = new ArrayList<>();

	public Model()
	{
	}

	/**
	 * renders the Model with the textures, color, etc. specified in the .obj and the .mtl file
	 */
	public void renderModel()
	{
		if (!this.usesMaterials) glDisable(GL_TEXTURE_2D);
		for (int i = 0; i < faces.size(); i++)
		{
			if (this.usesMaterials)
			{
				Material m = materials.get(faceMaterials.get(i));
				if (m.hasTexture) RenderHelper.bindTexture(m.texturePath);
				else glDisable(GL_TEXTURE_2D);
				
//				glColor4f(m.difuseColor.x, m.difuseColor.y, m.difuseColor.z, m.difuseColor.w);

				glMaterial(GL_FRONT, GL_AMBIENT, MathHelper.asFloatBuffer(new float[]{m.ambientColor.x, m.ambientColor.y, m.ambientColor.z, m.ambientColor.w}));
				glMaterial(GL_FRONT, GL_DIFFUSE, MathHelper.asFloatBuffer(new float[]{m.diffuseColor.x, m.diffuseColor.y, m.diffuseColor.z, m.diffuseColor.w}));
				glMaterial(GL_FRONT, GL_SPECULAR, MathHelper.asFloatBuffer(new float[]{m.specularColor.x, m.specularColor.y, m.specularColor.z, m.specularColor.w}));
				
				glMaterialf(GL_FRONT, GL_SHININESS, m.shininess);							
			}
			glBegin(GL_FAN);
			for (Face face : this.faces.get(i))
			{
				for(int i = 1; i < face.pointCount + 1; i++)
				{
					if (this.hasNormals)
					{
						Vector3f n1 = this.normals.get((int) face.points[i].z);
						glNormal3f(n1.x, n1.y, n1.z);
					}
					if (this.hasTextures)
					{
						Vector2f t1 = this.tetxures.get((int) face.points[i].y);
						glTexCoord2f(t1.x, 1 - t1.y);
					}
					Vector3f v1 = this.vertices.get((int)  face.points[i].x);
					glVertex3f(v1.x, v1.y, v1.z);
				}
				
			}
			glEnd();
		}
		glEnable(GL_TEXTURE_2D);
	}
	
	/**
	 * 
	 * @return Returns a list of all faces used by this model without dividing them into material-groups
	 */
	public List<Face> getFaceList()
	{
		List<Face> l = new ArrayList<>();
		for(List<Face> p : this.faces)
		{
			for(Face f : p)
				l.add(f);
		}
		return l;		
	}
	
	public Vector3f[] getVerteciesAsArray()
	{
		Vector3f[] v = new Vector3f[this.vertices.size()];
		for(int i = 0; i < this.vertices.size(); i++)
		{
			Vector3f w = this.vertices.get(i);
			v[i] = new Vector3f(w.x, w.y, w.z);
		}
		return v;
	}
}
