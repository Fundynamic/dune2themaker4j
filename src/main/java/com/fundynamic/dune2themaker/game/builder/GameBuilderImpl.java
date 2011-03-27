package com.fundynamic.dune2themaker.game.builder;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import com.fundynamic.dune2themaker.game.D2tmGame;
import com.fundynamic.dune2themaker.game.Game;
import com.fundynamic.dune2themaker.game.gamestates.manager.GameStateManager;
import com.fundynamic.dune2themaker.system.control.Keyboard;
import com.fundynamic.dune2themaker.system.control.Mouse;
import com.fundynamic.dune2themaker.system.repositories.ImageRepository;

public class GameBuilderImpl implements GameBuilder {

	private final GameContainer gameContainer;
	private final ImageRepository imageRepository;
	private final GameStateManager gameStateManager;
	private final Mouse mouse;
	private final Keyboard keyboard;
	private final Graphics graphics; 

	public GameBuilderImpl(GameContainer gameContainer) {
		super();
		this.gameContainer = gameContainer;
		this.imageRepository = new ImageRepository();
		this.gameStateManager = new GameStateManager();
		this.mouse = new Mouse(gameContainer.getInput());
		this.keyboard = new Keyboard(gameContainer.getInput());
		this.graphics = gameContainer.getGraphics();
	}

	public Game buildGame() {
		D2tmGame game = new D2tmGame();
		game.setGameContainer(gameContainer);
		game.setGameStateManager(gameStateManager);
		game.setGraphics(graphics);
		game.setImageRepository(imageRepository);
		game.setKeyboard(keyboard);
		game.setMouse(mouse);
		return game;
	}

}
