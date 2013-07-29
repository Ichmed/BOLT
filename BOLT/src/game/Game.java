package game;

public abstract class Game
{
	protected static Game currentGame;
	protected World currentWorld;
	
	/**
	 * The path to an .entList file containing the paths to all entities this game might need
	 */
	public String entListFilePath = "";
	
	/**
	 * The name of the Game
	 */
	public String name = "";
	
	/**
	 * Only used internally to make sure the Game is only initialized once
	 */
	private boolean isInitialized = false;
	
	/**
	 * This method initializes the Game by calling 'initGames()' and makes sure the Game is only initialized once.
	 */
	public void initializeGame()
	{
		if(! this.isInitialized) this.initGame();
	}

	/**
	 * This method should contain all steps necessary to initialize the Game on startup
	 */
	protected void initGame()
	{
	}

	/**
	 * 
	 * @return The current Game World
	 */
	public World getCurrentWorld()
	{
		return currentWorld;
	}

	/**
	 * 
	 * @return The currently running Game
	 */
	public static Game getCurrentGame()
	{
		return currentGame;
	}

	/**
	 * Sets the currently running Game <br>
	 * (Should be called only once)
	 * @param currentGame
	 */
	public static void setCurrentGame(Game currentGame)
	{
		Game.currentGame = currentGame;
	}
	
	public void gameLoop()
	{
		this.currentWorld.gameLoop();
	}
}
