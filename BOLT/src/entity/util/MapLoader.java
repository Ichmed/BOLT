package entity.util;

import entity.Entity;
import entity.EntityRegistry;
import game.World;

import java.io.File;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lwjgl.util.vector.Vector3f;

import util.Compressor;

public class MapLoader
{
	public static World loadMap(String file)
	{
		try
		{
			JSONObject content = new JSONObject(Compressor.decompressFile(new File(file)));

			World world = new World();

			JSONArray entities = content.getJSONArray("entity");

			for (int i = 0; i < entities.length(); i++)
			{
				JSONObject o = entities.getJSONObject(i);
				Entity entity = EntityRegistry.createEntity(o.getString("name"));

				JSONArray v = o.getJSONArray("pos");
				entity.position = new Vector3f((float) v.getDouble(0), (float) v.getDouble(1), (float) v.getDouble(2));

				HashMap<String, Object> customValues = new HashMap<>();
				JSONObject c = o.getJSONObject("custom");
				JSONArray cKeys = c.names();
				for (int j = 0; j < c.length(); j++)
				{
					customValues.put(cKeys.getString(i), c.get(cKeys.getString(i)));
				}
				entity.customValues = customValues;

				world.spawnEntity(entity);
			}
			return world;
		}
		catch (JSONException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public static JSONArray serializeVector3f(Vector3f v)
	{
		try
		{
			JSONArray a = new JSONArray();
			a.put(v.x);
			a.put(v.y);
			a.put(v.z);
			return a;
		}
		catch (JSONException e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
