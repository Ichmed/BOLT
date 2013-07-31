package entity.logic;

import entity.Entity;

public class Comperator extends Entity
{
	@Override
	public void onTick()
	{
		int a = (int)this.customValues.get("valueA");
		int b = (int)this.customValues.get("valueB");
		
		String s = (String)this.customValues.get("operator");
		if(s.equals("==") && a == b) reachTrigger();
		else if(s.equals("<=") && a <= b) reachTrigger();
		else if(s.equals(">=") && a <= b) reachTrigger();
		else if(s.equals("<") && a <= b) reachTrigger();
		else if(s.equals(">") && a <= b) reachTrigger();
	}
	
	public void reachTrigger()
	{ 
		this.triggerEvent("onReachtargetValue");
		if((boolean)this.customValues.get("reset"))
		{
			this.customValues.put("valueA", this.customValues.get("startingValueA"));
			this.customValues.put("valueB", this.customValues.get("startingValueB"));
		}
	}
	
	public void setValueA(int i)
	{
		this.customValues.put("valueA", i);		
	}
	
	public void setValueB(int i)
	{
		this.customValues.put("valueB", i);		
	}
	
	public void setOperator(String s)
	{
		if(s.equals("==") || s.equals(">=") || s.equals("<=") || s.equals("<") || s.equals(">"))
			this.customValues.put("operator", s);
	}
}
