package entity.probs;

import entity.Entity;

public class StaticProb extends Entity
{
	@Override
	public void render()
	{
		this.model = (String) customValues.get("displayModel");
		super.render();
	}

}
