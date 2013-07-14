package entity;

import java.util.HashMap;

public class EntityRegistry
{
	public static HashMap<String, EntityBuilder> entries = new HashMap<>();
	
	public static boolean registerEntityBuilder(EntityBuilder e)
	{
		if(entries.containsKey(e.name)) return false;
		entries.put(e.name, e);
		return true;
	}
}
