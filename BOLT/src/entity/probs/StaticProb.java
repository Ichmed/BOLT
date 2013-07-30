package entity.probs;

import static org.lwjgl.opengl.GL11.glRotated;
import static org.lwjgl.opengl.GL11.glTranslated;

import org.lwjgl.opengl.GL11;

import render.RenderHelper;

import entity.Entity;

public class StaticProb extends Entity
{
	@Override
	public void render()
	{
		this.model = (String) customValues.get("displayModel");
		super.render();
	}

	protected void doRendering()
	{
		glTranslated(this.position.x, this.position.y, this.position.z);
		glRotated(this.rotation.x, 1, 0, 0);
		glRotated(this.rotation.y, 0, 1, 0);
		glRotated(this.rotation.z, 0, 0, 1);
		double d = (double) customValues.get("size");
		GL11.glScaled(d, d, d);
		RenderHelper.renderModel(this.model);
	}
}
