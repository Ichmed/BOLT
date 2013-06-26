package util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class ErrorHandler
{
	public static void logError(String name, String log)
	{
		System.err.println(log);
		try
		{
			File logFile = new File("errorLogs/" + name + ".log");
			System.out.println(name);
			logFile.createNewFile();
			FileWriter writer = new FileWriter(logFile);
			writer.append(log);
			writer.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}		
	}
}