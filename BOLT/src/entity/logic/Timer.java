package entity.logic;

import entity.Entity;

public class Timer extends Entity {
	private boolean isActive = false;
	private long startingTime = 0;
	
	@Override
	public void initEntity() {
		if ((boolean) this.customValues.get("startActive")) activate();
	}
	
	@Override
	public void onTick() {
		if (this.isActive && System.currentTimeMillis() - this.startingTime < (int) this.customValues.get("durration")) {
			this.triggerEvent("onTimerFinished");
			if ((boolean) this.customValues.get("reset")) this.startingTime = System.currentTimeMillis();
			else this.isActive = false;
		}
	}
	
	public void activate() {
		this.startingTime = System.currentTimeMillis();
		this.isActive = true;
		this.triggerEvent("onActivation");
	}
	
	public void reset(boolean deactivate) {
		this.startingTime = System.currentTimeMillis();
		if (deactivate) this.isActive = false;
		this.triggerEvent("onReset");
	}
	
	public void setDuration(int duration) {
		this.customValues.put("duration", duration);
	}
}
