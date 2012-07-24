package com.fundynamic.dune2themaker.dune;


import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import com.fundynamic.dune2themaker.infrastructure.control.Keyboard;
import com.fundynamic.dune2themaker.infrastructure.control.Mouse;
import com.fundynamic.dune2themaker.infrastructure.math.Vector2D;

public class PlayingState {

	private Map map;
	private Graphics graphics;
	private GameContainer gameContainer;

	private Keyboard keyboard;
	private Mouse mouse;

	private Vector2D viewPortDrawingPosition;
	private Vector2D viewPortViewingPosition;
	private Viewport viewport;

	private boolean isInitialized;

	public PlayingState(GameContainer gameContainer) throws SlickException {
		this.graphics = gameContainer.getGraphics();
		this.gameContainer = gameContainer;

		this.keyboard = new Keyboard(gameContainer.getInput());
		this.mouse = new Mouse(gameContainer.getInput());

		// on map load...
		Theme theme = new Theme(new Image("sheet_terrain.png"));
		this.map = new Map(64, 64, theme);
		this.viewport = new Viewport(300, 300, map);

		viewPortDrawingPosition = new Vector2D(100, 100);
		viewPortViewingPosition = new Vector2D(0, 0);
	}

	public void init() throws SlickException {
		if (!isInitialized) {
			this.map.init();
			isInitialized = true;
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
