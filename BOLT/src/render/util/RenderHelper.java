package render.util;

import static org.lwjgl.opengl.ARBFramebufferObject.GL_DEPTH_ATTACHMENT;
import static org.lwjgl.opengl.ARBFramebufferObject.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.ARBFramebufferObject.GL_FRAMEBUFFER_COMPLETE;
import static org.lwjgl.opengl.ARBFramebufferObject.GL_MAX_RENDERBUFFER_SIZE;
import static org.lwjgl.opengl.ARBFramebufferObject.GL_RENDERBUFFER;
import static org.lwjgl.opengl.ARBFramebufferObject.glBindFramebuffer;
import static org.lwjgl.opengl.ARBFramebufferObject.glBindRenderbuffer;
import static org.lwjgl.opengl.ARBFramebufferObject.glCheckFramebufferStatus;
import static org.lwjgl.opengl.ARBFramebufferObject.glFramebufferRenderbuffer;
import static org.lwjgl.opengl.ARBFramebufferObject.glGenFramebuffers;
import static org.lwjgl.opengl.ARBFramebufferObject.glGenRenderbuffers;
import static org.lwjgl.opengl.ARBFramebufferObject.glRenderbufferStorage;
import static org.lwjgl.opengl.ARBShadowAmbient.GL_TEXTURE_COMPARE_FAIL_VALUE_ARB;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL14.GL_COMPARE_R_TO_TEXTURE;
import static org.lwjgl.opengl.GL14.GL_DEPTH_COMPONENT32;
import static org.lwjgl.opengl.GL14.GL_DEPTH_TEXTURE_MODE;
import static org.lwjgl.opengl.GL14.GL_TEXTURE_COMPARE_MODE;
import static org.lwjgl.util.glu.GLU.gluErrorString;
import static org.lwjgl.util.glu.GLU.gluPerspective;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.HashMap;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import render.Model;
import util.math.MathHelper;

public class RenderHelper {
	private static HashMap<String, Texture> textures = new HashMap<String, Texture>();
	public static HashMap<String, Model> models = new HashMap<>();
	
	private static int shadowMapWidth;
	private static int shadowMapHeight;
	private static int frameBuffer;
	private static int renderBuffer;
	
	private static final FloatBuffer textureBuffer = BufferUtils.createFloatBuffer(16);
	private static final Matrix4f depthModelViewProjection = new Matrix4f();
	
	@Deprecated
	public static final char[] characterChart = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '.', ',', ':', ';', '-', '+', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '\'', '"', '!', '?', '=', '*', '_', '#', '\'', ' ' };
	
	public static boolean bindTexture(String path) {
		Texture t = textures.get(path);
		if (t != null) {
			t.bind();
			return true;
		} else {
			t = loadTexture(path);
			t.bind();
			textures.put(path, t);
			return true;
		}
	}
	
	/**
	 * Returns a model. If it doesn't exist the method will try to load it
	 * 
	 * @param path The path to the model file
	 * @return The model / null if the model could not be loaded
	 */
	public static Model getModel(String path) {
		Model m = models.get(path);
		if (m == null) {
			models.put(path, ModelLoader.loadModel(path));
			m = models.get(path);
		}
		return m;
	}
	
	/**
	 * Renders a model at the GL-coordinate-origin. If it doesn't exist the method will try to load the model
	 * 
	 * @param path The path to the model file
	 */
	public static void renderModel(String path) {
		getModel(path).renderModel();
	}
	
	private static Texture loadTexture(String path) {
		try {
			return TextureLoader.getTexture(".png", new FileInputStream(new File(path)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void renderRect(int posX, int posY, int sizeX, int sizeY) {
		renderRect(posX, posY, sizeX, sizeY, 1, 1);
	}
	
	public static void renderRect(int posX, int posY, int sizeX, int sizeY, float texSizeX, float texSizeY) {
		renderRect(posX, posY, sizeX, sizeY, 0, 0, texSizeX, texSizeY);
	}
	
	public static void renderRect(int posX, int posY, int sizeX, int sizeY, float texPosX, float texPosY, float texSizeX, float texSizeY) {
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
	
	@Deprecated
	public static void renderString(int posX, int posY, String s) {
		renderString(posX, posY, s, 20);
	}
	
	@Deprecated
	public static void renderString(int posX, int posY, String s, int size) {
		renderString(posX, posY, s, size, -1);
	}
	
	@Deprecated
	public static void renderString(int posX, int posY, String s, int size, int frame) {
		char[] characters = s.toCharArray();
		for (int i = 0; i < characters.length; i++) {
			if (frame >= 0) {
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
	
	public static void initGLSettings() {
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		gluPerspective(100, (float) Display.getWidth() / Display.getHeight(), 0.01f, 1000);
		glMatrixMode(GL_MODELVIEW);
		
		glShadeModel(GL_SMOOTH);
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_CULL_FACE);
		
		glEnable(GL_POLYGON_OFFSET_FILL);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		
		glTexParameteri(GL_TEXTURE_2D, GL_DEPTH_TEXTURE_MODE, GL_INTENSITY);
		
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_COMPARE_FAIL_VALUE_ARB, 0.5f);
		
		glTexGeni(GL_S, GL_TEXTURE_GEN_MODE, GL_EYE_LINEAR);
		glTexGeni(GL_T, GL_TEXTURE_GEN_MODE, GL_EYE_LINEAR);
		glTexGeni(GL_R, GL_TEXTURE_GEN_MODE, GL_EYE_LINEAR);
		glTexGeni(GL_Q, GL_TEXTURE_GEN_MODE, GL_EYE_LINEAR);
		
		glPolygonOffset(1.0f, 0f);
		
		glEnable(GL_LIGHTING);
		glEnable(GL_LIGHT0);
		glLightModel(GL_LIGHT_MODEL_AMBIENT, MathHelper.asFloatBuffer(new float[] { 0.1f, 0.1f, 0.1f, 1f }));
		glLight(GL_LIGHT0, GL_DIFFUSE, MathHelper.asFloatBuffer(new float[] { 1.5f, 1.5f, 1.5f, 1 }));
		glEnable(GL_COLOR_MATERIAL);
		glColorMaterial(GL_FRONT, GL_DIFFUSE);
		glMaterialf(GL_FRONT, GL_SHININESS, 1000f);
		
		glLightModel(GL_LIGHT_MODEL_AMBIENT, MathHelper.asFloatBuffer(new float[] { 0.1f, 0.1f, 0.1f, 1 }));
	}
	
	public static void setUpFrameBufferObject() {
		final int MAX_RENDERBUFFER_SIZE = glGetInteger(GL_MAX_RENDERBUFFER_SIZE);
		final int MAX_TEXTURE_SIZE = glGetInteger(GL_MAX_TEXTURE_SIZE);
		if (MAX_TEXTURE_SIZE > 1024) {
			if (MAX_RENDERBUFFER_SIZE < MAX_TEXTURE_SIZE) {
				shadowMapWidth = shadowMapHeight = MAX_RENDERBUFFER_SIZE;
			} else {
				shadowMapWidth = shadowMapHeight = 1024;
			}
		} else {
			shadowMapWidth = shadowMapHeight = MAX_TEXTURE_SIZE;
		}
		frameBuffer = glGenFramebuffers();
		glBindFramebuffer(GL_FRAMEBUFFER, frameBuffer);
		renderBuffer = glGenRenderbuffers();
		glBindRenderbuffer(GL_RENDERBUFFER, renderBuffer);
		
		glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT32, shadowMapWidth, shadowMapHeight);
		
		glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, renderBuffer);
		glDrawBuffer(GL_NONE);
		glReadBuffer(GL_NONE);
		
		int FBOStatus = glCheckFramebufferStatus(GL_FRAMEBUFFER);
		if (FBOStatus != GL_FRAMEBUFFER_COMPLETE) {
			System.err.println("Framebuffer error: " + gluErrorString(glGetError()));
		}
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
	}
	
	private static void generateTextureCoordinates() {
		glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_COMPARE_MODE, GL_COMPARE_R_TO_TEXTURE);
		
		glEnable(GL_TEXTURE_GEN_S);
		
		glEnable(GL_TEXTURE_GEN_T);
		
		glEnable(GL_TEXTURE_GEN_R);
		
		glEnable(GL_TEXTURE_GEN_Q);
		textureBuffer.clear();
		textureBuffer.put(0, depthModelViewProjection.m00);
		textureBuffer.put(1, depthModelViewProjection.m01);
		textureBuffer.put(2, depthModelViewProjection.m02);
		textureBuffer.put(3, depthModelViewProjection.m03);
		
		glTexGen(GL_S, GL_EYE_PLANE, textureBuffer);
		
		textureBuffer.put(0, depthModelViewProjection.m10);
		textureBuffer.put(1, depthModelViewProjection.m11);
		textureBuffer.put(2, depthModelViewProjection.m12);
		textureBuffer.put(3, depthModelViewProjection.m13);
		
		glTexGen(GL_T, GL_EYE_PLANE, textureBuffer);
		
		textureBuffer.put(0, depthModelViewProjection.m20);
		textureBuffer.put(1, depthModelViewProjection.m21);
		textureBuffer.put(2, depthModelViewProjection.m22);
		textureBuffer.put(3, depthModelViewProjection.m23);
		
		glTexGen(GL_R, GL_EYE_PLANE, textureBuffer);
		
		textureBuffer.put(0, depthModelViewProjection.m30);
		textureBuffer.put(1, depthModelViewProjection.m31);
		textureBuffer.put(2, depthModelViewProjection.m32);
		textureBuffer.put(3, depthModelViewProjection.m33);
		
		glTexGen(GL_Q, GL_EYE_PLANE, textureBuffer);
	}
	
	public static void renderBox(float x, float y, float z) {
		glDisable(GL_CULL_FACE);
		glBegin(GL_QUADS);
		{
			glVertex3f(0, 0, 0);
			glVertex3f(x, 0, 0);
			glVertex3f(x, 0, z);
			glVertex3f(0, 0, z);
		}
		glEnd();
		
		glBegin(GL_QUADS);
		{
			glVertex3f(0, 0, 0);
			glVertex3f(x, 0, 0);
			glVertex3f(x, y, 0);
			glVertex3f(0, y, 0);
		}
		glEnd();
		
		glBegin(GL_QUADS);
		{
			glVertex3f(0, 0, 0);
			glVertex3f(0, y, 0);
			glVertex3f(0, y, z);
			glVertex3f(0, 0, z);
		}
		glEnd();
		
		glBegin(GL_QUADS);
		{
			glVertex3f(x, 0, 0);
			glVertex3f(x, y, 0);
			glVertex3f(x, y, z);
			glVertex3f(x, 0, z);
		}
		glEnd();
		
		glBegin(GL_QUADS);
		{
			glVertex3f(0, y, 0);
			glVertex3f(x, y, 0);
			glVertex3f(x, y, z);
			glVertex3f(0, y, z);
		}
		glEnd();
		
		glBegin(GL_QUADS);
		{
			glVertex3f(0, 0, z);
			glVertex3f(x, 0, z);
			glVertex3f(x, y, z);
			glVertex3f(0, y, z);
		}
		glEnd();
	}
}
