package com.fundynamic.dune2themaker.game;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import com.fundynamic.dune2themaker.game.gamestates.DefaultKeyboardInteractionState;
import com.fundynamic.dune2themaker.game.gamestates.manager.GameStateManager;
import com.fundynamic.dune2themaker.system.control.Keyboard;
import com.fundynamic.dune2themaker.system.control.Mouse;
import com.fundynamic.dune2themaker.system.repositories.ImageRepository;
import com.fundynamic.dune2themaker.util.Validate;

public class D2tmGame implements Game {

	private GameContainer gameContainer;
	
	private ImageRepository imageRepository;
	private GameStateManager gameStateManager;
	private Mouse mouse;
	private Keyboard keyboard;
	private Graphics graphics;

	public D2tmGame() {
	}

	public void init() throws Exception {
		Validate.notNull(gameContainer, "gameContainer may not be null");
		Validate.notNull(imageRepository, "imageRepository may not be null");
		Validate.notNull(gameStateManager, "gameStateManager may not be null");
		Validate.notNull(mouse, "mouse may not be null");
		Validate.notNull(keyboard, "keyboard may not be null");
		
		imageRepository.addItem("mouse_normal", "MS_Normal.png");
		imageRepository.addItem("terrain", "sheet_terrain.png");
		gameContainer.setMouseCursor(imageRepository.getItem("mouse_normal"), 0, 0);

		gameStateManager.init();
	}

	public void update() throws SlickException {
		gameStateManager.update();
	}

	public void render() throws SlickException {
		gameStateManager.render();
	}

	
	public void setGameContainer(GameContainer gameContainer) {
		this.gameContainer = gameContainer;
	}

	public void setImageRepository(ImageRepository imageRepository) {
		this.imageRepository = imageRepository;
	}

	public void setGameStateManager(GameStateManager gameStateManager) {
		this.gameStateManager = gameStateManager;
	}

	public void setMouse(Mouse mouse) {
		this.mouse = mouse;
	}

	public void setKeyboard(Keyboard keyboard) {
		this.keyboard = keyboard;
	}

	public void setGraphics(Graphics graphics) {
		this.graphics = graphics;
	}

}
