package com.fundynamic.dune2themaker.game.gamestates;

import com.fundynamic.dune2themaker.game.Game;

public abstract class AbstractGameState implements GameState {

	private boolean isFinished = false;
	protected final Game game;
	
	public AbstractGameState(Game game) {
		if (game == null) throw new IllegalArgumentException("game may not be null");
		this.game = game;
		this.isFinished = false;
	}
	
	public void setFinished() {
		this.isFinished = true;
	}
	
	public boolean isFinished() {
		return this.isFinished;
	}

}
