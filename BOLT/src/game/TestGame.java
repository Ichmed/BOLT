package game;

import render.util.RenderHelper;
import render.util.ShaderLoader;
import entity.util.EntityIO;
import entity.util.MapIO;

public class TestGame extends Game {
	
	@Override
	protected void preInitGame() {
		this.entListFilePath = "test/entities/testList.entlist";
		RenderHelper.initGLSettings();
	}
	
	@Override
	protected void initGame() {
		EntityIO.findEntities(this.entListFilePath);
		this.currentWorld = new World();
		MapIO.loadMap(currentWorld, "test/testMap.map");
		ShaderLoader.useProgram("test/", "shader");
	}
}
