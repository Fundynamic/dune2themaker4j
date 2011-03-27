package com.fundynamic.dune2themaker.stubs.gamestate;

import org.newdawn.slick.Graphics;

import com.fundynamic.dune2themaker.game.gamestates.GameState;

public class GameStateStub implements GameState {

	private boolean finished;
	
	public static GameStateStub createFinishedInstance() {
		return new GameStateStub(true);
	}

	public static GameStateStub createNotFinishedInstance() {
		return new GameStateStub(false);
	}

	private GameStateStub(boolean finished) {
		this.finished = finished;
	}
	
	public void init() {
	}

	public void update() {
	}

	public void render(Graphics graphics) {
	}

	public boolean isFinished() {
		return finished;
	}
}
