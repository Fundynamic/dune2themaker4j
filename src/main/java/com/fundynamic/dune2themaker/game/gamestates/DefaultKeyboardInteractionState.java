package com.fundynamic.dune2themaker.game.gamestates;

import org.newdawn.slick.Graphics;

import com.fundynamic.dune2themaker.game.Game;

public class DefaultKeyboardInteractionState extends AbstractGameState {

	public DefaultKeyboardInteractionState(Game game) {
		super(game);
	}

	public void init() {
	}

	public void update() {
		if (game.getKeyboard().isEscPressed()) {
			game.getGameContainer().exit();
		}
	}

	public void render(Graphics graphics) {
		// TODO Auto-generated method stub
		
	}

}
