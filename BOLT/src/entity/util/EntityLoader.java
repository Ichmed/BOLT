package entity.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

import entity.EntityBuilder;
import entity.EntityRegistry;

public class EntityLoader
{
	public static ArrayList<String> firstParsingFiles = new ArrayList<>();
	public static ArrayList<String> secondParsingFiles = new ArrayList<>();

	/**
	 * 
	 * @param path The path of the .entity file
	 * @param secondParsing Is this the second parsing?
	 * @return Returns an instance of EntityBuilder if successful and null if not
	 * @throws IOException
	 */
	private static EntityBuilder loadEntity(String path, boolean secondParsing) throws IOException
	{
		File OBJFile = new File(path);
		BufferedReader reader = new BufferedReader(new FileReader(OBJFile));
		EntityBuilder e = new EntityBuilder();
		String line;
		boolean parentFound = false;
		while ((line = reader.readLine()) != null)
		{
			if (line.startsWith("#"))
			;
			else if (line.startsWith("parent "))
			{
				String parent = line.split(" ")[1];
				if (EntityRegistry.entries.containsKey(parent))
				{
					e = EntityRegistry.entries.get(parent).clone();
					e.parent = parent;
					parentFound = true;
				}
				else
				{
					secondParsingFiles.add(path);
					if (!secondParsing) System.err.println(path + " has no parent, will be parsed a second time");
					else
					{
						System.err.println(path + " has no parent, will be parsed a second time using default values");
						e = EntityBuilder.defaultEntityBuilder.clone();
					}
					break;
				}
			}
			else if (!parentFound)
			{
				reader.close();
				return null;
			}
			else if (line.startsWith("name "))
			{
				e.name = line.split(" ")[1];
			}
			else if (line.startsWith("fullName "))
			{
				e.fullName = line.split(" ")[1];
			}
			else if (line.startsWith("physicsType "))
			{
				e.physicsType = Integer.valueOf(line.split(" ")[1]);
			}
			else if (line.startsWith("collisionType "))
			{
				e.collisionType = Integer.valueOf(line.split(" ")[1]);
			}
			else if (line.startsWith("invisible "))
			{
				e.invisible = Boolean.valueOf(line.split(" ")[1]);
			}
			else if (line.startsWith("gravity "))
			{
				e.gravity = Boolean.valueOf(line.split(" ")[1]);
			}
			else if (line.startsWith("class "))
			{
				e.classPath = line.split(" ")[1];
			}
			else if (line.startsWith("model "))
			{
				e.model = line.split(" ")[1];
			}
			else if (line.startsWith("collisionModel "))
			{
				e.collisionModel = line.split(" ")[1];
			}
			else if (line.startsWith("weight "))
			{
				e.weight = Float.valueOf(line.split(" ")[1]);
			}
			else if (line.startsWith("balancePoint "))
			{
				e.balancePoint = new Vector3f(Float.valueOf(line.split(" ")[1]), Float.valueOf(line.split(" ")[2]), Float.valueOf(line.split(" ")[3]));
			}
			else
			{
				if (line.startsWith("byte "))
				{
					e.customValues.put(line.split(" ")[1], Byte.valueOf(line.split(" ")[2]));
				}
				else if (line.startsWith("float "))
				{
					e.customValues.put(line.split(" ")[1], Float.valueOf(line.split(" ")[2]));
				}
				else if (line.startsWith("integer "))
				{
					e.customValues.put(line.split(" ")[1], Integer.valueOf(line.split(" ")[2]));
				}
				else if (line.startsWith("boolean "))
				{
					e.customValues.put(line.split(" ")[1], Boolean.valueOf(line.split(" ")[2]));
				}
				else if (line.startsWith("string "))
				{
					e.customValues.put(line.split(" ")[1], line.substring(line.indexOf(" ", 8)).trim());
				}
			}
		}
		reader.close();
		return e;
	}
	
	/**
	 * 
	 * @param path The path of the .entlist file to read in
	 * @throws IOException
	 */
	public static void loadEntities(String path) throws IOException
	{
		File OBJFile = new File(path);
		BufferedReader reader = new BufferedReader(new FileReader(OBJFile));
		String line;
		while ((line = reader.readLine()) != null)
		{
			firstParsingFiles.add(line);
		}
		reader.close();

		for (String s : firstParsingFiles)
			try
			{
				EntityBuilder b = loadEntity(s, false);
				if(b != null) EntityRegistry.registerEntityBuilder(b);
			}
			catch (IOException e1)
			{
				System.err.println("Could not read " + s);
				e1.printStackTrace();
			}
		for (String s : secondParsingFiles)
			try
			{
				EntityBuilder b = loadEntity(s, true);
				if(b != null) EntityRegistry.registerEntityBuilder(b);
			}
			catch (IOException e1)
			{
				System.err.println("Could not read " + s);
				e1.printStackTrace();
			}

	}
}
