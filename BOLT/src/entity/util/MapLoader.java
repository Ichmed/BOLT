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
	public static void loadMap(World world, String file)
	{
		try
		{
			JSONObject content = new JSONObject(Compressor.decompressFile(new File(file)));
			
			JSONArray entities = content.getJSONArray("entities");

			for (int i = 0; i < entities.length(); i++)
			{
				JSONObject o = entities.getJSONObject(i);
				Entity entity = EntityRegistry.createEntity(o.getString("name"));
				
				JSONArray pos = o.getJSONArray("pos");
				JSONArray rot = o.getJSONArray("rot");
				entity.setPosition((float) pos.getDouble(0), (float) pos.getDouble(1), (float) pos.getDouble(2));
				entity.setRotation((float) rot.getDouble(0), (float) rot.getDouble(1), (float) rot.getDouble(2));

				
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
		}
		catch (JSONException e)
		{
			e.printStackTrace();
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
