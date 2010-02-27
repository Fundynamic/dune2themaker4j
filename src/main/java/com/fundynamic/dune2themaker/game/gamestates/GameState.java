package com.fundynamic.dune2themaker.game.gamestates;

import org.newdawn.slick.Graphics;

public interface GameState {

	// initialize game state
	public void init();
	
	// run update method when game is in update state
	public void update();
	
	// run this render method when game is in render state
	public void render(Graphics graphics);
	
}
