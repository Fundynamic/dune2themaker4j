package com.fundynamic.dune2themaker.game.gamestates.manager;

import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.Graphics;

import com.fundynamic.dune2themaker.game.gamestates.AbstractGameState;
import com.fundynamic.dune2themaker.game.gamestates.GameState;

public class GameStateManager implements GameState {

	private final List<GameState> states;
	
	public GameStateManager() {
		this.states = new LinkedList<GameState>();
	}
	
	public void init() {
		for (GameState gameState : states) {
			gameState.init();
		}
	}

	public void update() {
		for (GameState gameState : states) {
			if (gameState.isFinished()) continue;
			gameState.update();
		}
	}
	
	public void removeGameState(AbstractGameState gameState) {
		int indexToRemove = findIndexOfReference(gameState);
		if (indexToRemove > -1) {
			states.remove(indexToRemove);
		} else {
			throw new IllegalStateException("GameState [" + gameState + "] cannot be removed, because it does not exist in list [" + this.states + "]");
		}
	}
	
	public void addGameState(GameState gameState) {
		states.add(gameState);
	}
	
	private int findIndexOfReference(GameState ref) {
		int index = 0;
		for (index = 0; index < states.size(); index++) {
			GameState gameState = states.get(index);
			if (gameState == ref) return index;
		}
		return -1;
	}

	public void render(Graphics graphics) {
		for (GameState gameState : states) {
			if (gameState.isFinished()) continue;
			gameState.render(graphics);
		}
	}

	public boolean isFinished() {
		for (GameState gameState : states) {
			if (!gameState.isFinished()) {
				return false;
			}
		}
		return true;
	}
}
