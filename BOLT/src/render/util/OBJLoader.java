package render.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import render.Face;
import render.Material;
import render.Model;

public class OBJLoader
{
	public static Model loadModel(String path, String file) throws IOException
	{
		File OBJFile = new File(path + file);
		BufferedReader reader = new BufferedReader(new FileReader(OBJFile));
		Model m = new Model();
		String line;
		
		ArrayList<Face> faces = new ArrayList<Face>();

		while ((line = reader.readLine()) != null)
		{
			if(line.startsWith("mtllib"))
			{
				loadMaterialsFile(path, line.split(" ")[1], m);
			}
			else if (line.startsWith("v "))
			{
				float x = Float.valueOf(line.split(" ")[1]);
				float y = Float.valueOf(line.split(" ")[2]);
				float z = Float.valueOf(line.split(" ")[3]);

				m.vertices.add(new Vector3f(x, y, z));
			}
			else if (line.startsWith("vn "))
			{
				float x = Float.valueOf(line.split(" ")[1]);
				float y = Float.valueOf(line.split(" ")[2]);
				float z = Float.valueOf(line.split(" ")[3]);

				m.normals.add(new Vector3f(x, y, z));
			}
			else if (line.startsWith("vt "))
			{
				float x = Float.valueOf(line.split(" ")[1]);
				float y = Float.valueOf(line.split(" ")[2]);

				m.tetxures.add(new Vector2f(x, y));
			}
			else if (line.startsWith("f "))
			{
				String s1 = line.split(" ")[1], s2 = line.split(" ")[2], s3 = line.split(" ")[3];

				float x = Float.valueOf(s1.split("/")[0]);
				float y = Float.valueOf(s2.split("/")[0]);
				float z = Float.valueOf(s3.split("/")[0]);

				Vector3f vertexIndices = new Vector3f(x, y, z);
				Vector3f normalIndices = new Vector3f(0, 0, 0);
				Vector3f textureIndices = new Vector3f(0, 0, 0);

				try
				{

					x = Float.valueOf(s1.split("/")[1]);
					y = Float.valueOf(s2.split("/")[1]);
					z = Float.valueOf(s3.split("/")[1]);

					textureIndices = new Vector3f(x, y, z);
					m.hasTextures = true;
				}
				catch (Exception e)
				{
				}

				try
				{
					x = Float.valueOf(s1.split("/")[2]);
					y = Float.valueOf(s2.split("/")[2]);
					z = Float.valueOf(s3.split("/")[2]);

					normalIndices = new Vector3f(x, y, z);
					m.hasNormals = true;
				}
				catch (Exception e)
				{
				}

				faces.add(new Face(vertexIndices, normalIndices, textureIndices));
			}
			else if (line.startsWith("usemtl"))
			{
				m.faces.add(faces);
				m.faceMaterials.add(line.split(" ")[1]);
			}
		}
		reader.close();
		return m;
	}

	private static void loadMaterialsFile(String path, String file, Model model) throws IOException
	{
		model.usesMaterials = true;
		
		File materialsFile = new File(path + file);
		BufferedReader reader = new BufferedReader(new FileReader(materialsFile));
		
		Material m = null;		
		String line;
		
		while ((line = reader.readLine()) != null)
		{
			if(line.startsWith("newmtl "))
			{
				m = new Material();
				model.materials.put(line.split(" ")[1], m);
				model.faceMaterials.add(line.split(" ")[1]);
			}
			else if(line.startsWith("Kd "))
			{
				float red = Float.valueOf(line.split(" ")[1]);
				float green = Float.valueOf(line.split(" ")[2]);
				float blue = Float.valueOf(line.split(" ")[3]);
				float alpha = 1;
				if(line.split(" ").length > 4) alpha = Float.valueOf(line.split(" ")[4]);
				m.colorVec = new Vector4f(red, green, blue, alpha);
			}
			else if(line.startsWith("d ") || line.startsWith("Tr "))
			{
				m.colorVec.setW(Float.valueOf(line.split(" ")[1]));
			}
			else if(line.startsWith("map_Kd "))
			{
				m.hasTexture = true;
				m.texturePath = path + line.split(" ")[1];
			}
		}
		reader.close();
	}
}
