import static org.lwjgl.util.glu.GLU.gluPerspective;
import static org.lwjgl.opengl.GL11.*;

import java.io.IOException;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;

import render.Model;
import render.RenderHelper;
import render.util.OBJLoader;

public class Main
{
	public static Model m;

	public static void main(String[] args)
	{
		try
		{
			Display.setDisplayMode(Display.getDesktopDisplayMode());
			Display.setFullscreen(true);
			Display.create();
		}
		catch (LWJGLException e)
		{
			e.printStackTrace();
		}

		try
		{
			m = OBJLoader.loadModel("test/crystal.obj");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		gluPerspective(100, (float) Display.getWidth() / Display.getHeight(), 0.01f, 1000);
		glMatrixMode(GL_MODELVIEW);

		glScaled(0.005d, 0.005d, 0.005d);
		while (!Display.isCloseRequested())
			gameLoop();

		Display.destroy();
	}

	public static void gameLoop()
	{
		glPushMatrix();

		glEnable(GL_BLEND);
		glEnable(GL_DEPTH_TEST); 

		glEnable(GL_TEXTURE_2D);

		glColor4d(1, 1, 1, 0.5d);

		RenderHelper.bindTexture("test/crystal.png");
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		glTranslated(0, 0, -5);
		// glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		m.renderModel();

		Display.update();
		Display.sync(50);

		glPopMatrix();

	}
}
