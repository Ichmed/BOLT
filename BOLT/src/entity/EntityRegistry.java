package entity;

import java.util.HashMap;

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

	public static Entity createEntity(String name)
	{ 
		try
		{
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
		return null;
	}
}
