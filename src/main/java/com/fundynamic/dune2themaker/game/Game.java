package com.fundynamic.dune2themaker.game;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import com.fundynamic.dune2themaker.game.gamestates.DefaultKeyboardInteractionState;
import com.fundynamic.dune2themaker.game.gamestates.DummyGameState;
import com.fundynamic.dune2themaker.game.gamestates.manager.GameStateManager;
import com.fundynamic.dune2themaker.system.control.Keyboard;
import com.fundynamic.dune2themaker.system.control.Mouse;
import com.fundynamic.dune2themaker.system.repositories.ImageRepository;

/**
 * Root game class
 * 
 * @author Stefan
 *
 */
public class Game {

	private final GameContainer gameContainer; // the Slick Game Container it wraps
	
	// this game has ...
	private final ImageRepository imageRepository;
	private final Mouse mouse;
	private final Keyboard keyboard;
	private final GameStateManager gameStateManager;

	public Game(GameContainer gameContainer) {
		super();
		this.gameContainer = gameContainer;
		this.imageRepository = new ImageRepository();
		this.mouse = new Mouse(gameContainer.getInput());
		this.keyboard = new Keyboard(gameContainer.getInput());
		this.gameStateManager = new GameStateManager();
	}
	
	
	public void init() throws Exception {
		getImageRepository().addItem("mouse_normal", "MS_Normal.png");
		this.gameContainer.setMouseCursor(getImageRepository().getItem("mouse_normal"), 0, 0);

		gameStateManager.addGameState(new DefaultKeyboardInteractionState(this));
		gameStateManager.addGameState(new DummyGameState(this));
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
