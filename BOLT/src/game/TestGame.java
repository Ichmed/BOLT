package game;

import entity.util.EntityLoader;
import entity.util.MapLoader;

public class TestGame extends Game
{
	@Override
	protected void initGame()
	{
		super.initGame();
		this.entListFilePath = "test/entities/testList.entlist";
		EntityLoader.findEntities(this.entListFilePath);
		this.currentWorld = new World();
		MapLoader.loadMap(currentWorld, "test/testMap.map");
	}
}
