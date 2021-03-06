package game;

public abstract class Game {
	protected static Game currentGame;
	protected World currentWorld;
	private long lastTick;
	private long peak;
	private long ticks = 0;
	private long sum = 0;
	
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
	private boolean isPreInitialized = false;
	
	/**
	 * This method prepares the Game by calling 'preInitGame()' and makes sure the Game is only prepared once.
	 */
	public void prepareGame() {
		if (!this.isPreInitialized) this.preInitGame();
		this.isPreInitialized = true;
	}
	
	/**
	 * This method initializes the Game by calling 'initGame()' and makes sure the Game is only initialized once.
	 */
	public void initializeGame() {
		if (!this.isInitialized) this.initGame();
		this.isInitialized = true;
	}
	
	/**
	 * This method should contain all steps necessary to prepare the Game on startup
	 */
	protected abstract void preInitGame();
	
	/**
	 * This method should contain all steps necessary to initialize the Game on startup
	 */
	protected abstract void initGame();
	
	/**
	 * @return The current Game World
	 */
	public World getCurrentWorld() {
		return currentWorld;
	}
	
	/**
	 * @return The currently running Game
	 */
	public static Game getCurrentGame() {
		return currentGame;
	}
	
	/**
	 * Sets the currently running Game <br>
	 * (Should be called only once)
	 * 
	 * @param currentGame
	 */
	public static void setCurrentGame(Game currentGame) {
		Game.currentGame = currentGame;
	}
	
	public void gameLoop() {
		ticks++;
		lastTick = System.currentTimeMillis();
		this.currentWorld.gameLoop();
		long l = System.currentTimeMillis() - lastTick;
		sum += l;
		peak = Math.max(peak, l);
		// System.out.println("Last Tick: " + l + " Peak: " + peak + "\n Average: " + sum / ticks);
	}
	
	public static void launchGame(String classPath) {
		try {
			Game.currentGame = (Game) Class.forName(classPath).newInstance();
			Game.currentGame.prepareGame();
			Game.currentGame.initializeGame();
		} catch (Exception e) {}
	}
}
