package com.fundynamic.dune2themaker.game.gamestates;


public interface GameState {

	void init();
	
	void update();
	
	void render();
	
	boolean isFinished();
	
}
