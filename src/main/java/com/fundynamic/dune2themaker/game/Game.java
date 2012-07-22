package com.fundynamic.dune2themaker.game;


import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import com.fundynamic.dune2themaker.infrastructure.control.Keyboard;
import com.fundynamic.dune2themaker.infrastructure.control.Mouse;
import com.fundynamic.dune2themaker.infrastructure.math.Vector2D;

public class Game {

	private Map map;
	private Graphics graphics;
	private GameContainer gameContainer;

	private Keyboard keyboard;
	private Mouse mouse;

	private Vector2D viewPortDrawingPosition;
	private Vector2D viewPortViewingPosition;
	private Viewport viewport;

	public Game(GameContainer gameContainer) throws SlickException {
		this.map = new Map();
		this.graphics = gameContainer.getGraphics();
		this.gameContainer = gameContainer;
		this.keyboard = new Keyboard(gameContainer.getInput());
		this.viewport = new Viewport(100, 100, map);
		this.mouse = new Mouse(gameContainer.getInput());

		viewPortDrawingPosition = new Vector2D(100, 100);
		viewPortViewingPosition = new Vector2D(50, 50);
	}

	public void update() {
		if (keyboard.isKeyUpPressed()) {
			viewPortViewingPosition = viewPortViewingPosition.moveUp();
		}

		viewPortDrawingPosition = mouse.getVector2D();

		if (keyboard.isEscPressed()) {
			this.gameContainer.exit();
		}
	}

	public void render() throws SlickException {
		viewport.draw(graphics, viewPortDrawingPosition, viewPortViewingPosition);
//		map.draw(this.graphics, viewPortDrawingPosition);
	}

}
