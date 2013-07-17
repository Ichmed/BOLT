import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluPerspective;
import game.Camera;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.vector.Vector3f;

import render.Model;
import render.util.Misc;
import render.util.OBJLoader;
import render.util.ShaderLoader;
import util.math.MathHelper;

public class Main
{
	public static Model m;
	
	
	static int i = 0;
	static Camera camera = new Camera();

	public static int cameraSpeed;
	public static boolean fullscreen;
	public static int resX = 0;
	public static int resY = 0;

	public static void main(String[] args)
	{
		// TODO: Catch arguments for Console

		try
		{
			m = OBJLoader.loadModel("test/", "crystal.obj");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		loadOptions();
		System.out.printf("fullscreen: %b\nresolution: %dx%d\ncameraspeed: %d\n", fullscreen, resX, resY, cameraSpeed);
		/*try {
			Display.create();
		} catch (LWJGLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		try{
			if(fullscreen){
				enterFullscreen();
			}else{
				leaveFullscreen();
			}
		} catch(LWJGLException e){
			e.printStackTrace();
		}
		System.out.printf("hallo\n");
		initGLSettings();

		Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);
		Mouse.setGrabbed(true);

		while (!Display.isCloseRequested())
			gameLoop();

		Display.destroy();
	}

	public static void enterFullscreen() throws LWJGLException{
		Display.destroy();
		Display.setFullscreen(true);
		Display.setDisplayModeAndFullscreen(Display.getDesktopDisplayMode());
	//	Display.setDisplayModeAndFullscreen(new DisplayMode(resX, resY, 32, 60));
		Display.create();
		fullscreen = true;
		System.out.printf("laeuft\n");
		return;
	}

	public static void leaveFullscreen() throws LWJGLException{
		Display.destroy();
		Display.setFullscreen(false);
		Display.setDisplayMode(new DisplayMode(resX, resY));
		Display.create();
		fullscreen = false;
		System.out.printf("laeuft\n");
	}

	public static void toggleFullscreen(){
		try{
			if(Display.isFullscreen()){
				leaveFullscreen();
			} else {
				enterFullscreen();
			}
		} catch(LWJGLException e){
			e.printStackTrace();
		}
	}
	
	public static void gameLoop()
	{
		i++;

		camera.rotation.y += ((Mouse.getX() - (Display.getWidth() / 2)) / (float) Display.getWidth()) * cameraSpeed;
		camera.rotation.x -= ((Mouse.getY() - (Display.getHeight() / 2)) / (float) Display.getHeight()) * cameraSpeed;

		camera.rotation.x = MathHelper.clamp(camera.rotation.x, -90, 90);

		Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);

		Vector3f rot = new Vector3f((float) Math.cos(camera.rotation.x), (float) Math.cos(camera.rotation.y), (float) Math.cos(camera.rotation.z));

		// TODO camera movement
		if (Keyboard.isKeyDown(Keyboard.KEY_F11))
		{
			toggleFullscreen();
			initGLSettings();
			Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);
		}

		glPushMatrix();

		glRotated(camera.rotation.x, 1f, 0f, 0f);
		glRotated(camera.rotation.y, 0f, 1f, 0f);
		glRotated(camera.rotation.z, 0f, 0f, 1f);
		glTranslatef(-camera.position.x, -camera.position.y, -camera.position.z);

		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		glLight(GL_LIGHT0, GL_POSITION,
				Misc.asFloatBuffer(new float[] { 5 * (float) Math.sin((double) i / 100d), 5 * (float) Math.sin((double) i / 100d), 5 * (float) Math.cos((double) i / 100d), 1 }));

		glEnable(GL_TEXTURE_2D);
		glTranslated(0, 0, -9);
		// glRotated(i, 0, 1, 0);
		m.renderModel();
		glTranslated(0, 0, 4);
		glTranslated(0, (Math.sin(i / 100d) * 2) - 1, 0);

		// glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		m.renderModel();
		glRotated(i, 0, 1, 0);
		glTranslated(0, 0, 2);
		// glTranslated(0, (Math.sin(i / 100d) * 2) - 1, 0);
		m.renderModel();

		Display.update();
		Display.sync(50);

		glPopMatrix();
	}
	
	public static void initGLSettings()
	{
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		gluPerspective(100, (float) Display.getWidth() / Display.getHeight(), 0.01f, 1000);
		glMatrixMode(GL_MODELVIEW);

		glShadeModel(GL_SMOOTH);
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_CULL_FACE);

		glEnable(GL_LIGHTING);
		glEnable(GL_LIGHT0);
		glLightModel(GL_LIGHT_MODEL_AMBIENT, Misc.asFloatBuffer(new float[] { 0.1f, 0.1f, 0.1f, 1f }));
		glLight(GL_LIGHT0, GL_DIFFUSE, Misc.asFloatBuffer(new float[] { 1.5f, 1.5f, 1.5f, 1 }));
		glEnable(GL_COLOR_MATERIAL);
		glColorMaterial(GL_FRONT, GL_DIFFUSE);
		glMaterialf(GL_FRONT, GL_SHININESS, 10f);

		glLightModel(GL_LIGHT_MODEL_AMBIENT, Misc.asFloatBuffer(new float[] { 0.1f, 0.1f, 0.1f, 1 }));
		
		ShaderLoader.useProgram("test/", "shader");
	}

	public static void loadOptions()
	{
		File OBJFile = new File("nonsync/options.txt");
		String line;
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(OBJFile));
			while ((line = reader.readLine()) != null)
			{
				if (line.startsWith("cameraSpeed")) cameraSpeed = Integer.valueOf(line.split("=")[1]);
				if (line.startsWith("fullscreen")) fullscreen = Boolean.valueOf(line.split("=")[1]);
				if (line.startsWith("resX")) resX = Integer.valueOf(line.split("=")[1]);
				if (line.startsWith("resY")) resY = Integer.valueOf(line.split("=")[1]);
			}
			reader.close();
		}
		catch (FileNotFoundException e1)
		{
			e1.printStackTrace();
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}