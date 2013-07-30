package util;

import java.io.File;
import java.net.URISyntaxException;

import editor.Editor;

public class FileUtilities
{
	public static File getHardDrive(File f)
	{
		return new File(f.getPath().substring(0, 3));
	}

	public static File getJarFile()
	{
		try
		{
			return new File(Editor.class.getProtectionDomain().getCodeSource().getLocation().toURI());
		}
		catch (URISyntaxException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public static String getRelativePath(File from, File to)
	{
		return from.toPath().relativize(to.toPath()).toFile().toString();
	}
}
