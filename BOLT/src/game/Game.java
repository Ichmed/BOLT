package game;

public abstract class Game
{
	public String entListFilePath = "";
	public String name = "";
	
	private boolean isInitialized = false;
	
	public void initializeGame()
	{
		if(! this.isInitialized) this.initGame();
	}

	protected void initGame()
	{
	}
}
