import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_COLOR_MATERIAL;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_DIFFUSE;
import static org.lwjgl.opengl.GL11.GL_FRONT;
import static org.lwjgl.opengl.GL11.GL_LIGHT0;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_LIGHT_MODEL_AMBIENT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_POSITION;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_SHININESS;
import static org.lwjgl.opengl.GL11.GL_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glColorMaterial;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLight;
import static org.lwjgl.opengl.GL11.glLightModel;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMaterialf;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotated;
import static org.lwjgl.opengl.GL11.glShadeModel;
import static org.lwjgl.opengl.GL11.glTranslated;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.util.glu.GLU.gluPerspective;
import game.Camera;

import java.io.IOException;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import render.Model;
import render.util.Misc;
import render.util.OBJLoader;
import render.util.ShaderLoader;
import util.MathHelper;

public class Main
{
	public static Model m;

	static int i = 0;
	static Camera camera = new Camera();

	public static void main(String[] args)
	{
		//TODO: Catch arguments for Console
		
		try
		{
			Display.setDisplayMode(new DisplayMode(640, 480));
//			Display.setDisplayModeAndFullscreen(Display.getDesktopDisplayMode());
			Display.create();
		}
		catch (LWJGLException e)
		{
			e.printStackTrace();
		}
		

		try
		{
			m = OBJLoader.loadModel("test/", "crystal.obj");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

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
		
		glLightModel(GL_LIGHT_MODEL_AMBIENT, Misc.asFloatBuffer(new float[]{0.1f, 0.1f, 0.1f, 1}));	

		ShaderLoader.useProgram("test/", "shader");
		
		Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);
		Mouse.setGrabbed(true);

		while (!Display.isCloseRequested())
			gameLoop();

		Display.destroy();
	}

	public static void gameLoop()
	{
		i++;		
		
		camera.rotation.x -= (Mouse.getX() - (Display.getWidth() / 2f)) / (float)Display.getWidth();
		camera.rotation.y -= (Mouse.getY() - (Display.getHeight() / 2f)) / (float)Display.getHeight();
		
		if((Mouse.getY() - (Display.getHeight() / 2f)) / (float)Display.getHeight() != 0)
		
//		MathHelper.normalize(camera.rotation);
//		System.out.println(camera.rotation.x);
		System.out.println(camera.rotation.y);
		System.out.println(camera.rotation.z);
		
		
		Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);
		
		if(Keyboard.isKeyDown(Keyboard.KEY_W))
		{
			camera.position.x += camera.rotation.y % 2;
			camera.position.y += camera.rotation.x % 2;
		}
		
		//TODO: Camera Control and rotation
		
		glPushMatrix();
		glTranslated(-5, -5, -5);
		glTranslatef(-camera.position.x, -camera.position.y, -camera.position.z);
		glRotated(Math.toDegrees(Math.asin(camera.rotation.z / MathHelper.pyth(camera.rotation.z, camera.rotation.y))), 1f, 0f, 0f);
		glRotated(Math.toDegrees(Math.asin(camera.rotation.x / MathHelper.pyth(camera.rotation.x, camera.rotation.z))), 1f, 0f, 0f);
		glRotated(Math.toDegrees(Math.asin(camera.rotation.y / MathHelper.pyth(camera.rotation.y, camera.rotation.x))), 1f, 0f, 0f);
		
//		
//		glRotated(Math.toDegrees(Math.atan2(camera.rotation.y, camera.rotation.z)), 1f, 0f, 0f);
//		glRotated(Math.toDegrees(Math.atan2(camera.rotation.z, camera.rotation.x)), 0f, 1f, 0f);
//		glRotated(Math.toDegrees(Math.atan2(camera.rotation.y, camera.rotation.x)), 0f, 0f, 1f);
		glTranslated(5, 5, 5);
//
//		System.out.println(Math.atan2(camera.rotation.y, camera.rotation.z));
		System.out.println(Math.toDegrees(Math.atan2(camera.rotation.y, camera.rotation.z)));

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
}