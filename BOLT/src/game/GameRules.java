package game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.lwjgl.opengl.Display;

public class GameRules
{
	/**
	 *  Gravity in m/s^2
	 */
	public static float gravity = 9.8f;
	
	/**
	 * Max player speed in m/s
	 */
	public static float playerMaxSpeed = 2.77f;
	
	/**
	 * Developer-mode on/off
	 */
	public static boolean devMode = false;
	
	//load game.rules to static variables
	public static void loadRules()
	{
		File file = new File("game.rules");
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while((line = reader.readLine()) != null)
			{
				if(line.startsWith("gravity"))
					gravity = Float.valueOf(line.split("=")[1]);
				if(line.startsWith("playerMaxSpeed"))
					playerMaxSpeed = Float.valueOf(line.split("=")[1]);
				if(line.startsWith("devMode"))
					devMode = Boolean.valueOf(line.split("=")[1]);
			}
			reader.close();
		}
		catch (IOException e)
		{
			System.err.println("Error reading game rule file!\nGame will shut down now.");
			Display.destroy();
			System.exit(1);
		}
	}
}
