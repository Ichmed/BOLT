package entity;

import java.io.IOException;
import java.util.HashMap;

import entity.util.EntityLoader;

public class EntityRegistry
{
	public static HashMap<String, EntityBuilder> entries = new HashMap<>();

	/**
	 * Registers an instance of EntityBuilder with it's name as the key
	 * 
	 * @param builder
	 * @return Returns false if the EntityBuilder is already registered
	 */
	public static boolean registerEntityBuilder(EntityBuilder builder)
	{
		if (entries.containsKey(builder.name)) return false;
		entries.put(builder.name, builder);
		return true;
	}
	
	public EntityBuilder getEntityBuilder(String name)
	{
		try
		{
			if (!entries.containsKey(name))
			{
				EntityBuilder e = EntityLoader.loadEntity(name);
				if (e == null) return null;
				registerEntityBuilder(e);
			}
			if (entries.containsKey(name)) return entries.get(name);
		}
		catch (Exception e)
		{
		}
		return null;
	}

	/**
	 * Creates an returns a new instance of a given Entity
	 * @param name The Entity's name
	 * @return A new Entity with all values set to default (position = (0, 0, 0) etc)
	 */
	public static Entity createEntity(String name)
	{ 
		try
		{
			if(!entries.containsKey(name))
			{
				EntityBuilder e = EntityLoader.loadEntity(name);
				if(e == null) return null;
				registerEntityBuilder(e);
			}
			
			if(entries.containsKey(name))
			return entries.get(name).createEntity();
			else return null;
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (InstantiationException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
