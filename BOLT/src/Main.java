import static org.lwjgl.util.glu.GLU.gluPerspective;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;

public class Main 
{
	public static void main(String[] args)
	{
		try
		{
			Display.setDisplayMode(Display.getDesktopDisplayMode());
			Display.create();
		}
		catch (LWJGLException e)
		{
			e.printStackTrace();
		}
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		gluPerspective(140, (float)Display.getWidth() / Display.getHeight(), 0.001f, 100);
		glMatrixMode(GL_MODELVIEW);
		
		while(!Display.isCloseRequested()) gameLoop();
		
		Display.destroy();
	}
	
	public static void gameLoop()
	{
		Display.update();
		Display.sync(50);
	}
}
