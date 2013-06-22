package render;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class RenderHelper
{
	private static HashMap<String, Texture> textures = new HashMap<String, Texture>();

	public static final char[] characterChart = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '.', ',', ':', ';',
			'-', '+', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '\'', '"', '!', '?', '=', '*', '_', '#', '\'', ' ' };

	public static boolean bindTexture(String s)
	{
		Texture t = textures.get(s);
		if (t != null)
		{
			t.bind();
			return true;
		}
		else
		{
			Texture u = loadTexture(s);
			u.bind();
			textures.put(s, u);
			return true;
		}
	}

	private static Texture loadTexture(String path)
	{
		try
		{
			return TextureLoader.getTexture(".png", new FileInputStream(new File(path)));
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static void renderRect(int posX, int posY, int sizeX, int sizeY)
	{
		renderRect(posX, posY, sizeX, sizeY, 1, 1);
	}

	public static void renderRect(int posX, int posY, int sizeX, int sizeY, float texSizeX, float texSizeY)
	{
		renderRect(posX, posY, sizeX, sizeY, 0, 0, texSizeX, texSizeY);
	}

	public static void renderRect(int posX, int posY, int sizeX, int sizeY, float texPosX, float texPosY, float texSizeX, float texSizeY)
	{
		GL11.glBegin(GL11.GL_QUADS);

		GL11.glTexCoord2f(texPosX, texPosY + texSizeY);
		GL11.glVertex2f(posX, posY);

		GL11.glTexCoord2f(texPosX + texSizeX, texPosY + texSizeY);
		GL11.glVertex2f(posX + sizeX, posY);

		GL11.glTexCoord2f(texPosX + texSizeX, texPosY);
		GL11.glVertex2f(posX + sizeX, posY + sizeY);

		GL11.glTexCoord2f(texPosX, texPosY);
		GL11.glVertex2f(posX, posY + sizeY);

		GL11.glEnd();
	}

	public static void renderString(int posX, int posY, String s)
	{
		renderString(posX, posY, s, 20);
	}

	public static void renderString(int posX, int posY, String s, int size)
	{
		renderString(posX, posY, s, size, -1);
	}

	public static void renderString(int posX, int posY, String s, int size, int frame)
	{
		char[] characters = s.toCharArray();
		for (int i = 0; i < characters.length; i++)
		{
			if (frame >= 0)
			{
				int a = (characters.length == 1 ? 3 : (i == 0 ? 0 : (i == characters.length - 1 ? 2 : 1)));
				bindTexture("textFrames.png");
				renderRect(posX + (size * i), posY, size, size, a * 0.25f, frame * 0.25f, 0.25f, 0.25f);
			}

			char c = characters[i];
			int j = 0;
			for (j = 0; j < characterChart.length; j++)
				if (characterChart[j] == Character.toUpperCase(c)) break;

			bindTexture("characters.png");
			int x = j % 16;
			int y = (j - x) / 16;
			renderRect(posX + (size * i), posY, size, size, 0.0625f * x, 0.0625f * y, 0.0625f, 0.0625f);
		}
	}
}
