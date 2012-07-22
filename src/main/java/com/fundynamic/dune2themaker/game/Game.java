package com.fundynamic.dune2themaker.game;


import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import com.fundynamic.dune2themaker.infrastructure.control.Keyboard;
import com.fundynamic.dune2themaker.infrastructure.math.Vector2D;

public class Game {

	private Map map;
	private Graphics graphics;
	private GameContainer gameContainer;

	private Keyboard keyboard;
	private Vector2D viewPortDrawingPosition;

	public Game(GameContainer gameContainer) throws SlickException {
		this.map = new Map();
		this.graphics = gameContainer.getGraphics();
		this.gameContainer = gameContainer;
		this.keyboard = new Keyboard(gameContainer.getInput());
		viewPortDrawingPosition = new Vector2D(100, 100);
	}

	public void update() {
		if (keyboard.isKeyUpPressed()) {
			viewPortDrawingPosition = viewPortDrawingPosition.moveUp();
		}

		if (keyboard.isEscPressed()) {
			this.gameContainer.exit();
		}
	}

	public void render() {
		map.draw(this.graphics, viewPortDrawingPosition);
	}

}
