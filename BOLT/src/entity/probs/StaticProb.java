package entity.probs;

import java.io.File;

import entity.Entity;

public class StaticProb extends Entity
{
	@Override
	public void render()
	{
		System.out.println(customValues.get("displayModel"));
		this.model = ((File) customValues.get("displayModel")).getPath();
		super.render();
	}
}
