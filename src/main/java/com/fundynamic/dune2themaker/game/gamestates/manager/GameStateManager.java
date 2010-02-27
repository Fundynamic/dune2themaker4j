package com.fundynamic.dune2themaker.game.gamestates.manager;

import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.Graphics;

import com.fundynamic.dune2themaker.game.gamestates.AbstractGameState;
import com.fundynamic.dune2themaker.game.gamestates.GameState;

/**
 * Composite pattern. The gameStateManager is a gamestate itself.
 * It is able to run other gamestates. GameStates added to the list
 * of the gamestate manager will enable them to be ran.
 * 
 * @author Stefan
 *
 */
public class GameStateManager implements GameState {

	private final List<AbstractGameState> states;
	
	public GameStateManager() {
		this.states = new LinkedList<AbstractGameState>();
	}
	
	public void init() {
		for (AbstractGameState gameState : states) {
			gameState.init();
		}
	}

	public void update() {
		for (AbstractGameState gameState : states) {
			if (gameState.isFinished()) continue;
			gameState.update();
		}
	}
	
	// remove given game state 
	public void removeGameState(AbstractGameState gameState) {
		int indexToRemove = findIndexOfReference(gameState);
		if (indexToRemove > -1) {
			states.remove(indexToRemove);
		} else {
			throw new IllegalStateException("GameState [" + gameState + "] cannot be removed, because it does not exist in list [" + this.states + "]");
		}
	}
	
	public void addGameState(AbstractGameState gameState) {
		states.add(gameState);
	}
	
	private int findIndexOfReference(AbstractGameState ref) {
		int index = 0;
		for (index = 0; index < states.size(); index++) {
			AbstractGameState gameState = states.get(index);
			if (gameState == ref) return index;
		}
		return -1;
	}

	public void render(Graphics graphics) {
		for (AbstractGameState gameState : states) {
			if (gameState.isFinished()) continue;
			gameState.render(graphics);
		}
	}
}
