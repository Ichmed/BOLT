package render.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import render.Face;
import render.Model;

public class OBJLoader
{
	public static Model loadModel(String s) throws IOException
	{
		File file = new File(s);
		BufferedReader reader = new BufferedReader(new FileReader(file));
		Model m = new Model();
		String line;

		while ((line = reader.readLine()) != null)
		{
			if (line.startsWith("v "))
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

				m.faces.add(new Face(vertexIndices, normalIndices, textureIndices));
			}
		}
		reader.close();
		return m;
	}
}
