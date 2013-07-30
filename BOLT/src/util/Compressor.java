package util;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class Compressor
{
	/**
	 * Writes the byte value of the given string compressedly to the given file.
	 * 
	 * @param f
	 *            file to write to
	 * @param s
	 *            string to compress & write
	 */
	public static void compressFile(File f, String s)
	{
		compressFile(f, (s + ((s.length() < 18) ? "                 " : "")).getBytes());
	}

	/**
	 * Writes the given bytes compressedly to the given file.
	 * 
	 * @param f
	 *            file to write to
	 * @param input
	 *            bytes to compress & write
	 */
	public static void compressFile(File f, byte[] input)
	{
		byte[] length = ByteBuffer.allocate(4).putInt(input.length).array();

		byte[] buffer = new byte[input.length];
		Deflater deflater = new Deflater(Deflater.BEST_COMPRESSION);
		deflater.setInput(input);
		deflater.finish();
		int len = deflater.deflate(buffer);
		byte[] compr = Arrays.copyOf(buffer, len);
		byte[] output = new byte[compr.length + 4];
		System.arraycopy(length, 0, output, 0, length.length);
		System.arraycopy(compr, 0, output, 4, compr.length);
		setFileContent(f, output);
	}

	/**
	 * Decompresses bytes.
	 * 
	 * @param b
	 *            compressed bytes
	 * @return decompressed bytes
	 */
	public static byte[] decompress(byte[] b)
	{
		try
		{
			int length = ByteBuffer.wrap(Arrays.copyOf(b, 4)).getInt();
			Inflater inflater = new Inflater();
			inflater.setInput(Arrays.copyOfRange(b, 4, b.length));
			byte[] buf = new byte[length];
			inflater.inflate(buf);
			inflater.end();
			return buf;
		}
		catch (Exception e)
		{
			return null;
		}
	}

	/**
	 * Decompresses the content of the given file.<br>
	 * The file won't be modified.
	 * 
	 * @param f
	 *            file to read
	 * @return decompressed file content
	 */
	public static String decompressFile(File f)
	{
		byte[] decompressed = decompress(getFileContentAsByteArray(f));
		String text = new String(decompressed);

		return text;
	}

	/**
	 * Writes bytes to the given file.
	 * 
	 * @param f
	 *            file to write to
	 * @param b
	 *            bytes to write
	 */
	public static void setFileContent(File f, byte[] b)
	{
		try
		{
			f.createNewFile();

			FileOutputStream fos = new FileOutputStream(f);
			fos.write(b);
			fos.close();
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * Reads the content of the given file into bytes.
	 * 
	 * @param f
	 *            file to read
	 * @return content bytes
	 */
	public static byte[] getFileContentAsByteArray(File f)
	{
		try
		{
			byte[] fileData = new byte[(int) f.length()];
			DataInputStream dis = new DataInputStream(new FileInputStream(f));
			dis.readFully(fileData);
			dis.close();
			return fileData;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
