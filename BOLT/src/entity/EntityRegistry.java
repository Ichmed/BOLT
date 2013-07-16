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
}
