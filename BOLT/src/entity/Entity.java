package entity;

import java.util.HashMap;

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
}
