package com.fundynamic.dune2themaker.game.gamestates;


public abstract class AbstractGameState implements GameState {

	private boolean isFinished = false;
	
	public AbstractGameState() {
		this.isFinished = false;
	}
	
	public void setFinished() {
		this.isFinished = true;
	}
	
	public boolean isFinished() {
		return this.isFinished;
	}

}
