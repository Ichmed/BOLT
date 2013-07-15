package render.util;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

public class Misc
{
	/**
	 * Puts the float-array into a float buffer, flips the buffer and returns it
	 * @param fs
	 * @return The flipped floatBuffer
	 */
	public static FloatBuffer asFloatBuffer(float[] fs)
	{
		FloatBuffer buffer = BufferUtils.createFloatBuffer(fs.length);
		buffer.put(fs);
		buffer.flip();
		return buffer;
	}
}
