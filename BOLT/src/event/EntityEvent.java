package event;

import entity.Entity;
import game.Game;

public class EntityEvent
{
	public final Entity owner;
	public final Entity target;
	public final boolean onlyOnce;
	
	public EntityEvent(Entity owner, String targetName, String script, boolean onlyOnce)
	{
		this.owner = owner;
		this.target = Game.getCurrentGame().getCurrentWorld().getEntity(targetName);
		this.onlyOnce = onlyOnce;
	}
	
	public boolean activate()
	{		
		return true;
	}
}
