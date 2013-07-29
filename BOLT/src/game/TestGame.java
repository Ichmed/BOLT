package game;

public class TestGame extends Game
{
	@Override
	protected void initGame()
	{
		super.initGame();
		this.currentWorld = new World();
	}
}
