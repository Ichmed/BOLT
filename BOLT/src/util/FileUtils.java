package util;

import java.io.File;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class FileUtils
{
	static public String relativePath(File from, File to)
	{
		return relativePath(from, to, File.separatorChar);
	}

	static public String relativePath(File from, File to, char separatorChar)
	{
		String fromPath = from.getAbsolutePath();
		String toPath = to.getAbsolutePath();
		boolean isDirectory = from.isDirectory();
		return relativePath(fromPath, toPath, isDirectory, separatorChar);
	}

	public static String relativePath(String fromPath, String toPath, boolean fromIsDirectory, char separatorChar)
	{
		ArrayList<String> fromElements = splitPath(fromPath);
		ArrayList<String> toElements = splitPath(toPath);
		while (!fromElements.isEmpty() && !toElements.isEmpty())
		{
			if (!(fromElements.get(0).equals(toElements.get(0))))
			{
				break;
			}
			fromElements.remove(0);
			toElements.remove(0);
		}

		StringBuffer result = new StringBuffer();
		for (int i = 0; i < fromElements.size() - (fromIsDirectory ? 0 : 1); i++)
		{
			result.append("..");
			result.append(separatorChar);
		}
		for (String s : toElements)
		{
			result.append(s);
			result.append(separatorChar);
		}
		return result.substring(0, result.length() - 1);
	}

	private static ArrayList<String> splitPath(String path)
	{
		ArrayList<String> pathElements = new ArrayList<String>();
		for (StringTokenizer st = new StringTokenizer(path, File.separator); st.hasMoreTokens();)
		{
			String token = st.nextToken();
			if (token.equals("."))
			{
				// do nothing
			}
			else if (token.equals(".."))
			{
				if (!pathElements.isEmpty())
				{
					pathElements.remove(pathElements.size() - 1);
				}
			}
			else
			{
				pathElements.add(token);
			}
		}
		return pathElements;
	}

}
