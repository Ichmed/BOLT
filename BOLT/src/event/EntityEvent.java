package event;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import entity.Entity;
import game.Game;

public class EntityEvent
{
	public final Entity owner;
	public String target;
	public String targetFunction;
	public boolean onlyOnce;
	public JSONArray parameters;
	private List<String> flags = new ArrayList<>();
	
	public EntityEvent(Entity owner, JSONObject o)
	{
		this.owner = owner;
		try
		{
			this.target = o.getString("target");
			this.targetFunction = o.getString("function");
			this.parameters = o.getJSONArray("params");
			
			JSONArray f = o.getJSONArray("flags");
			for(int i = 0; i < f.length(); i++)
			{
				this.flags.add(f.getString(i));
			}
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
	}
	
	public boolean isFlagSet(String name)
	{
		return this.flags.contains(name);
	}
	
	public boolean trigger()
	{
		Entity eTarget = Game.getCurrentGame().getCurrentWorld().getEntity(target);
		Method[] methods = eTarget.getClass().getMethods();
		
		Object[] o = new Object[parameters.length()];
		
		try
		{
			for(int i = 0; i < parameters.length(); i++)
			{
				Object obj = parameters.get(i);
				if(obj instanceof String && ((String)obj).startsWith("@"))
					o[i] = owner.customValues.get(((String)obj).replace("@", ""));
				else o[i] = obj;				
			}
			
			for(Method m : methods)
				if(m.getName().equals(targetFunction))
					m.invoke(eTarget, o);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
}
