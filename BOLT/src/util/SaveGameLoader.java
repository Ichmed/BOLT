package util;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

public class SaveGameLoader
{
	public static void loadSaveGame(String file)
	{
		try
		{
			JSONObject content = new JSONObject(Compressor.decompressFile(new File(file)));
			// TODO: do something with this loaded data!
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
	}

	public static void saveSaveGame(String file)
	{
		Compressor.compressFile(new File(file), "PUT_CONTENT_OF_SAVEGAME_HERE");
		// TODO: do something usefull!
	}
}
