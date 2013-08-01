package entity;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.lwjgl.util.vector.Vector3f;

import util.math.MathHelper;
import editor.Editor;
import event.EntityEvent;

public class EntityBuilder
{
	public static EntityBuilder defaultEntityBuilder;

	public String parent = "";
	public String name = "";
	public String fullName = "";

	public Integer physicsType = 0;
	public Integer collisionType = 2;
	public Boolean invisible = false;
	public Boolean gravity = false;
	public String model = "";
	public String collisionModel = "";
	public Float weight = 0f;
	public Vector3f balancePoint = new Vector3f();

	public List<String> triggers = new ArrayList<>(), functions = new ArrayList<>(), nonInheritedTriggers = new ArrayList<>(), nonInheritedFunctions = new ArrayList<>();

	public String classPath = "";

	public HashMap<String, Object> customValues = new HashMap<>();

	/**
	 * @return returns an exact copy of this EntityBuilder, used for 'parents'
	 */
	@SuppressWarnings("unchecked")
	public EntityBuilder clone()
	{
		EntityBuilder e = new EntityBuilder();

		e.parent = this.parent;
		e.name = this.name;
		e.fullName = this.fullName;

		e.physicsType = this.physicsType;
		e.collisionType = this.collisionType;
		e.invisible = this.invisible;
		e.gravity = this.gravity;
		e.model = this.model;
		e.collisionModel = this.collisionModel;
		e.weight = this.weight;
		e.balancePoint = MathHelper.cloneVector(this.balancePoint);

		e.classPath = this.classPath;

		e.customValues = (HashMap<String, Object>) this.customValues.clone();

		List<String> l = new ArrayList<>();
		for (String s : this.triggers)
			l.add(s);
		e.triggers = l;

		l = new ArrayList<>();
		for (String s : this.functions)
			l.add(s);
		e.functions = l;

		return e;
	}

	public boolean equals(EntityBuilder o)
	{
		System.out.println(toString());
		System.out.println(o.toString());
		return toString().equals(o.toString());
	}

	@Override
	public String toString()
	{
		String s = getClass().getSimpleName() + "[";
		for (Field field : getClass().getFields())
		{
			if (Modifier.isStatic(field.getModifiers())) continue;
			try
			{
				if (field.getName().equals("customValues"))
				{
					s += "customValues=" + Editor.writeValue(JSONObject.wrap((Map<String, Object>) customValues)) + ",";
				}
				else s += field.getName() + "=" + field.get(this) + ",";
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return s.substring(0, s.length() - 2) + "]";
	}

	/**
	 * 
	 * @return creates and returns an instance of Entity specified in this EntityBuilder
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	@SuppressWarnings("unchecked")
	public Entity createEntity() throws ClassNotFoundException, InstantiationException, IllegalAccessException
	{
		Entity e;
		if (classPath != "null")
		{
			Class<? extends Entity> c = (Class<? extends Entity>) Class.forName(classPath);
			e = (Entity) c.newInstance();
		}
		else e = new Entity();
		e.name = this.name;
		e.fullName = this.fullName;
		e.parent = this.parent;
		e.physicsType = this.physicsType;
		e.collisionType = this.collisionType;
		e.invisible = this.invisible;
		e.gravity = this.gravity;
		e.model = this.model;
		e.collisionModel = this.collisionModel;
		e.weight = this.weight;
		e.balancePoint = new Vector3f(this.balancePoint);

		e.customValues = (HashMap<String, Object>) this.customValues.clone();

		for (String s : this.triggers)
			e.events.put(s, new ArrayList<EntityEvent>());

		return e;
	}
}
