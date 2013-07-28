package entity;

import game.World;

import static org.lwjgl.opengl.GL11.*;

import java.util.HashMap;
import org.lwjgl.util.vector.Vector3f;

import render.RenderHelper;

import util.math.MathHelper;

public class Entity
{
	/**
	 * The Entity's parent, all values not specified in the .ent file of this
	 * Entity will be taken from the parent's .ent file
	 */
	public String parent;

	/**
	 * The Entity's name without spaces
	 */
	public String name;

	/**
	 * The Entity's display-name, may contain spaces
	 */
	public String fullName;

	/**
	 * how the entity reacts to physical force applied to it <br>
	 * Types: <br>
	 * 1 physical <br>
	 * 2 static <br>
	 */
	public Integer physicsType;

	/**
	 * Types: <br>
	 * 1 solid (all objects will collide with this object) <br>
	 * 2 gameSolid (only object marked as gamePlay objects will collide with the
	 * object (players, projectiles, etc.) but not particles etc.) <br>
	 * 3 not Solid (everything will pass through this entity) <br>
	 */
	public Integer collisionType;

	/**
	 * If true the Entity won't be rendered in-game
	 */
	public Boolean invisible;

	/**
	 * Should the entity be affected by gravity
	 */
	public Boolean gravity;

	/**
	 * The display model for this entity
	 */
	public String model;

	/**
	 * The complex collision model (might be something else than the display
	 * model)
	 */
	public String collisionModel;

	/**
	 * The Entity's weight in Kg
	 */
	public Float weight;

	/**
	 * The Entity's balance-point
	 */
	public Vector3f balancePoint;

	/**
	 * types: <br>
	 * byte <br>
	 * float <br>
	 * integer <br>
	 * boolean <br>
	 * string <br>
	 */
	public HashMap<String, Object> customValues;

	/**
	 * A reference to the World this Entity is in
	 */
	public World worldObj;

	/**
	 * The Entity's position
	 */
	public Vector3f position;

	/**
	 * The Entity's rotation
	 */
	public Vector3f rotation;

	/**
	 * The Entity's unique key used to identify it
	 */
	public int key;

	/**
	 * This method is called every game-tick to make the Entity perform whatever
	 * it does
	 */
	public void onTick()
	{
		if (!this.invisible)
		{
			render();
		}
	}

	public void render()
	{
		glPushMatrix();
		
		glTranslated(this.position.x, this.position.y, this.position.z);
		glRotated(this.rotation.x, 1, 0, 0);
		glRotated(this.rotation.y, 0, 1, 0);
		glRotated(this.rotation.z, 0, 0, 1);
		RenderHelper.renderModel(this.model);
		
		glPopMatrix();
	}

	public void setPosition(float x, float y, float z)
	{
		this.position = new Vector3f(x, y, z);
	}

	public void setPosition(Vector3f v)
	{
		this.position = MathHelper.cloneVector(v);
	}

	public void setRotation(float x, float y, float z)
	{
		this.rotation = new Vector3f(x, y, z);
	}

	public void setRotation(Vector3f v)
	{
		this.rotation = MathHelper.cloneVector(v);
	}

	public Vector3f getPosition()
	{
		return MathHelper.cloneVector(this.position);
	}

	public Vector3f getRotation()
	{
		return MathHelper.cloneVector(this.rotation);
	}

	/**
	 * This method takes a HashMap<String, Object> and tranfers it's content to
	 * the Entity's 'customValues'
	 * 
	 * @param map
	 *            A HashMap containing the custom values
	 */
	public void applySaveData(HashMap<String, Object> map)
	{
		for (String s : map.keySet())
			this.customValues.put(s, map.get(s));
	}
}
