package com.fundynamic.dune2themaker.game.gamestates;

import org.newdawn.slick.Graphics;

public interface GameState {

	void init();
	
	void update();
	
	void render(Graphics graphics);
	
	boolean isFinished();
	
}
