package entity.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

import entity.Entity;
import entity.EntityBuilder;
import entity.EntityRegistry;

public class EntityLoader
{
	public static ArrayList<FilePath> firstParsingFiles = new ArrayList<>();
	public static ArrayList<FilePath> secondParsingFiles = new ArrayList<>();

	public static EntityBuilder loadEntity(FilePath p, boolean secondParsing) throws IOException
	{
		File OBJFile = new File(p.path + p.file);
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
					secondParsingFiles.add(p);
					if (!secondParsing) System.err.println(p + " has no parent, will be parsed a second time");
					else
					{
						System.err.println(p + " has no parent, will be parsed a second time using default values");
						e = EntityBuilder.defaultEntityBuilder.clone();
					}
					break;
				}
			}
			else if (!parentFound)
			{
				break;
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

	public static void loadEntities(String path, String file) throws IOException
	{
		File OBJFile = new File(path + file);
		BufferedReader reader = new BufferedReader(new FileReader(OBJFile));
		String line;
		while ((line = reader.readLine()) != null)
		{
			firstParsingFiles.add(new FilePath(line.split(" ")[0], line.split(" ")[1]));
		}
		reader.close();

		for (FilePath p : firstParsingFiles)
			try
			{
				loadEntity(p, false);
			}
			catch (IOException e1)
			{
				System.err.println("Could not read " + p);
				e1.printStackTrace();
			}
		for (FilePath p : secondParsingFiles)
			try
			{
				loadEntity(p, false);
			}
			catch (IOException e1)
			{
				System.err.println("Could not read " + p);
				e1.printStackTrace();
			}

	}

	private static class FilePath
	{
		public final String path;
		public final String file;

		public FilePath(String path, String file)
		{
			this.path = path;
			this.file = file;
		}

		@Override
		public String toString()
		{
			return path + file;
		}
	}
}
