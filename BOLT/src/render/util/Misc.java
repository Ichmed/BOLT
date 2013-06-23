package render.util;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

public class Misc
{
	public static FloatBuffer asFloatBuffer(float[] fs)
	{
		FloatBuffer buffer = BufferUtils.createFloatBuffer(fs.length);
		buffer.put(fs);
		buffer.flip();
		return buffer;
	}
}
