package com.fundynamic.dune2themaker.game;


import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import com.fundynamic.dune2themaker.infrastructure.control.Keyboard;
import com.fundynamic.dune2themaker.infrastructure.control.Mouse;
import com.fundynamic.dune2themaker.infrastructure.math.Vector2D;

import sun.reflect.Reflection;

public class PlayingState {

	private final TerrainFactory terrainFactory;

	private Map map;
	private Graphics graphics;
	private GameContainer gameContainer;

	private Keyboard keyboard;
	private Mouse mouse;

	private Vector2D viewPortDrawingPosition;
	private Vector2D viewPortViewingPosition;
	private Viewport viewport;

	private boolean initialized;


	public PlayingState(GameContainer gameContainer, TerrainFactory terrainFactory) throws SlickException {
		this.terrainFactory = terrainFactory;
		this.graphics = gameContainer.getGraphics();
		this.gameContainer = gameContainer;

		this.keyboard = new Keyboard(gameContainer.getInput());
		this.mouse = new Mouse(gameContainer.getInput());

		// on map load...
		this.map = new Map(terrainFactory, 64, 64);
		this.viewport = new Viewport(600, 600, map);

		viewPortDrawingPosition = new Vector2D(100, 100);
		viewPortViewingPosition = new Vector2D(0, 0);
	}

	public void init() throws SlickException {
		if (!initialized) {
			this.map.init();
			initialized = true;
		}
	}

	public void update() {
		if (keyboard.isKeyUpPressed()) {
			viewPortViewingPosition = viewPortViewingPosition.moveUp();
		}
		if (keyboard.isKeyDownPressed()) {
			viewPortViewingPosition = viewPortViewingPosition.moveDown();
		}
		if (keyboard.isKeyLeftPressed()) {
			viewPortViewingPosition = viewPortViewingPosition.moveLeft();
		}
		if (keyboard.isKeyRightPressed()) {
			viewPortViewingPosition = viewPortViewingPosition.moveRight();
		}

		viewPortDrawingPosition = mouse.getVector2D();

		if (keyboard.isEscPressed()) {
			this.gameContainer.exit();
		}
	}

	public void render() throws SlickException {
		viewport.draw(graphics, viewPortDrawingPosition, viewPortViewingPosition);
	}

}
