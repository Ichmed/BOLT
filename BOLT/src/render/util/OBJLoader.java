package render.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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
			if(line.startsWith("v "))
			{
				float x = Float.valueOf(line.split(" ")[1]);
				float y = Float.valueOf(line.split(" ")[2]);
				float z = Float.valueOf(line.split(" ")[3]);
				
				m.vertices.add(new Vector3f(x, y, z));
			}
			else if(line.startsWith("vn "))
			{
				float x = Float.valueOf(line.split(" ")[1]);
				float y = Float.valueOf(line.split(" ")[2]);
				float z = Float.valueOf(line.split(" ")[3]);
				
				m.normals.add(new Vector3f(x, y, z));
			}
			else if(line.startsWith("f "))
			{
				float x = Float.valueOf(line.split(" ")[1].split("/")[0]);
				float y = Float.valueOf(line.split(" ")[2].split("/")[0]);
				float z = Float.valueOf(line.split(" ")[3].split("/")[0]);
				
				Vector3f vertexIndices = new Vector3f(x, y, z);

				x = Float.valueOf(line.split(" ")[1].split("/")[2]);
				y = Float.valueOf(line.split(" ")[2].split("/")[2]);
				z = Float.valueOf(line.split(" ")[3].split("/")[2]);
				
				Vector3f normalIndices = new Vector3f(x, y, z);
				
				m.faces.add(new Face(vertexIndices, normalIndices));
			}
		}
		reader.close();
		return m;
	}
}
