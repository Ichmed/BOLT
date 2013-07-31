package entity.logic;

import entity.Entity;

public class Counter extends Entity
{
	@Override
	public void onTick()
	{
		if ((int) this.customValues.get("value") >= (int) this.customValues.get("targetValue"))
		{
			this.triggerEvent("onReachTargetValue");
			if((boolean)this.customValues.get("reset"))
				this.customValues.put("value", (int) this.customValues.get("startingValue"));
		}
	}

	@Override
	public void initEntity()
	{
		this.customValues.put("value", (int) this.customValues.get("startingValue"));
	}

	public void increment()
	{
		int i = (int) this.customValues.get("value") + (int) this.customValues.get("incrementValue");
		this.customValues.put("value", i);
		this.triggerEvent("onIncrement");
	}

	public void decrement()
	{
		int i = (int) this.customValues.get("value") - (int) this.customValues.get("decrementValue");
		this.customValues.put("value", i);
		this.triggerEvent("onDecrement");
	}

	public void reset()
	{
		this.customValues.put("value", (int) this.customValues.get("startingValue"));
		this.triggerEvent("onReset");
	}
}
