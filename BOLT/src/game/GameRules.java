package game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.opengl.Display;

public class GameRules
{
	// Gravity in m/s^2
	public static float gravity = 9.8f;
	//MaxSpeed in m/s
	public static float playerMaxSpeed = 2.77f;
	
	//load game.rules to static variables
	public static void loadRules()
	{
		File file = new File("game.rules");
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(file));
			ArrayList<String> rulesIdentifier = new ArrayList<String>();
			ArrayList<Float> rulesValues = new ArrayList<Float>();
			String line;
			while((line = reader.readLine()) != null)
			{
				String identifier = line.split("=")[0];
				float value = Float.parseFloat(line.split("=")[1]);
				rulesIdentifier.add(identifier);
				rulesValues.add(value);
			}
			reader.close();
			for(int i = 0; i < rulesIdentifier.size(); i++)
			{
				if (rulesIdentifier.get(i) == "gravity")
					gravity = rulesValues.get(i);
				else if (rulesIdentifier.get(i) == "playerMaxSpeed")
					playerMaxSpeed = rulesValues.get(i);
			}
		}
		catch (IOException e)
		{
			System.err.println("Error reading game rule file!\nGame will shut down now.");
			Display.destroy();
			System.exit(1);
		}
	}
}
