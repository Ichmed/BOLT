package entity;

import org.lwjgl.util.vector.Vector3f;

public class TrollBall extends Entity
{
	Vector3f velocity;

	private float v;

	public TrollBall()
	{
		super();
	}

	@Override
	public void initEntity()
	{
		super.initEntity();
		v = (float) (double) this.customValues.get("speed");
		this.velocity = new Vector3f((float) Math.random() * v, (float) Math.random() * v, (float) Math.random() * v);
	}

	@Override
	public void onTick()
	{
		super.onTick();
		this.position.translate(this.velocity.x, this.velocity.y, this.velocity.z);

		if (this.position.length() > (double) this.customValues.get("maxRad"))
		{
			this.position = new Vector3f(0, 0, 0);
			this.velocity = new Vector3f((float) Math.random() * v, (float) Math.random() * v, (float) Math.random() * v);
		}
	}

	public void setColor3i(int red, int blue, int green)
	{
		this.customValues.put("red", red);
		this.customValues.put("green", green);
		this.customValues.put("blue", blue);
	}
}
