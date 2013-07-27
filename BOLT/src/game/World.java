package game;

import java.util.HashMap;
import entity.Entity;

public class World
{
	private Map currentMap;
	/**
	 * A HashMap containing every Entity in this World. Access them using 'getEntity(int key)'.
	 */
	private HashMap<Integer, Entity> entityMap = new HashMap<>();

	public void spawnEntity(Entity entity)
	{
		this.entityMap.put(entity.key, entity);
		entity.worldObj = this;
	}
	
	/**	
	 * @param key the Entitity's unique identifier
	 * @return The Entity with this identifier
	 */
	public Entity getEntity(int key)
	{
		return this.entityMap.get(key);
	}

	public Map getCurrentMap()
	{
		return currentMap;
	}
}
