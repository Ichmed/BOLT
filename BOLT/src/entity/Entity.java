package entity;

import game.World;

import java.util.HashMap;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

public class Entity
{
	public String parent;
	public String name;
	public String fullName;
	
	public Integer physicsType;
	public Integer collisionType;
	public Boolean invisible;
	public Boolean gravity;
	public String model;
	public String collisionModel;
	public Float weight;
	public Vector3f balancePoint;
	
	public HashMap<String, Object> customValues;
	
	public World worldObj;
	
	public Vector3f position;
	
	public void onTick()
	{
	}
	
	public void setPosition(float x, float y, float z)
	{
		this.position = new Vector3f(x, y, z);
	}
	
	public void applySaveData(HashMap<String, Object> map)
	{
		for(String s : map.keySet())
			this.customValues.put(s, map.get(s));
	}
}
