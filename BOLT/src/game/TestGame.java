package game;

import entity.util.MapLoader;

public class TestGame extends Game
{
	@Override
	protected void initGame()
	{
		super.initGame();
		this.entListFilePath = "test/entities/testList.entlist";
		this.currentWorld = new World();
		MapLoader.loadMap(currentWorld, "test/testMap.map");
	}
}
