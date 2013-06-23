package render;

import org.lwjgl.util.vector.Vector4f;

public class Material
{
	public Vector4f difuseColor = new Vector4f(1, 1, 1, 1);
	public Vector4f ambientColor = new Vector4f(1, 1, 1, 1);
	public float transperency = 1;
	public String texturePath = "";
	public boolean hasTexture = false;
}
