package com.fundynamic.dune2themaker.game;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import com.fundynamic.dune2themaker.game.gamestates.DefaultKeyboardInteractionState;
import com.fundynamic.dune2themaker.game.gamestates.DummyGameState;
import com.fundynamic.dune2themaker.game.gamestates.DummyTwoGameState;
import com.fundynamic.dune2themaker.game.gamestates.manager.GameStateManager;
import com.fundynamic.dune2themaker.system.control.Keyboard;
import com.fundynamic.dune2themaker.system.control.Mouse;
import com.fundynamic.dune2themaker.system.repositories.ImageRepository;

public class Game {

	private final GameContainer gameContainer;
	
	private final ImageRepository imageRepository;
	private final GameStateManager gameStateManager;
	private final Mouse mouse;
	private final Keyboard keyboard;

	public Game(GameContainer gameContainer) {
		super();
		this.gameContainer = gameContainer;
		this.imageRepository = new ImageRepository();
		this.mouse = new Mouse(gameContainer.getInput());
		this.keyboard = new Keyboard(gameContainer.getInput());
		this.gameStateManager = new GameStateManager();
	}
	
	
	public void init() throws Exception {
		imageRepository.addItem("mouse_normal", "MS_Normal.png");
		this.gameContainer.setMouseCursor(imageRepository.getItem("mouse_normal"), 0, 0);

		DefaultKeyboardInteractionState defaultKeyboardInteractionState = new DefaultKeyboardInteractionState(this);
		defaultKeyboardInteractionState.setGameContainer(gameContainer);
		defaultKeyboardInteractionState.setKeyboard(keyboard);
		
		gameStateManager.addGameState(defaultKeyboardInteractionState);
		gameStateManager.addGameState(new DummyGameState(this));
		gameStateManager.addGameState(new DummyTwoGameState(this));
		gameStateManager.init();
	}

	public void render(Graphics graphics) {
		gameStateManager.render(graphics);
	}

	public void update() {
		gameStateManager.update();
	}

	public Mouse getMouse() {
		return mouse;
	}

	public Keyboard getKeyboard() {
		return keyboard;
	}

	public ImageRepository getImageRepository() {
		return imageRepository;
	}
	
	public GameContainer getGameContainer() {
		return this.gameContainer;
	}
}
