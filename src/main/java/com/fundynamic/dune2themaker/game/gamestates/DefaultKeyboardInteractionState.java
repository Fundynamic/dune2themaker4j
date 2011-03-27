package com.fundynamic.dune2themaker.game.gamestates;

import org.newdawn.slick.GameContainer;

import com.fundynamic.dune2themaker.system.control.Keyboard;

public class DefaultKeyboardInteractionState extends AbstractGameState {

	private Keyboard keyboard;
	private GameContainer gameContainer;
	
	public DefaultKeyboardInteractionState() {
	}

	public void init() {
	}

	public void update() {
		if (keyboard.isEscPressed()) {
			gameContainer.exit();
		}
	}

	public void render() {
		
	}

	public void setKeyboard(Keyboard keyboard) {
		this.keyboard = keyboard;
	}

	public void setGameContainer(GameContainer gameContainer) {
		this.gameContainer = gameContainer;
	}

}
