package util;

import org.json.JSONArray;
import org.json.JSONException;

public class JSONUtilities
{
	public static boolean containsValue(JSONArray array, Object value)
	{
		for (int i = 0; i < array.length(); i++)
		{
			try
			{
				if (array.get(i).equals(value)) return true;
			}
			catch (JSONException e)
			{
				e.printStackTrace();
			}
		}

		return false;
	}
}
