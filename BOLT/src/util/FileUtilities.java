package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;

import editor.Editor;

public class FileUtilities {
	public static File getHardDrive(File f) {
		return new File(f.getPath().substring(0, 3));
	}
	
	public static File getJarFile() {
		try {
			return new File(Editor.class.getProtectionDomain().getCodeSource().getLocation().toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String getRelativePath(File from, File to) {
		return from.toPath().relativize(to.toPath()).toFile().toString();
	}
	
	public static String getFileContent(File f) {
		return new String(Compressor.getFileContentAsByteArray(f));
	}
	
	public static void setFileContent(File f, String s) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(f));
			bw.write(s);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
