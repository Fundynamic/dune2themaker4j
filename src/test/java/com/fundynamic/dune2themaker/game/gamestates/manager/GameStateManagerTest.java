package com.fundynamic.dune2themaker.game.gamestates.manager;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.fundynamic.dune2themaker.stubs.gamestate.GameStateStub;


public class GameStateManagerTest {

	private GameStateManager gameStateManager;
	
	@Before
	public void setUp(){
		gameStateManager = new GameStateManager();
	}
	
	@Test
	public void mustReturnTrueWhenAllStatesAreFinished() {
		gameStateManager.addGameState(GameStateStub.createFinishedInstance());
		gameStateManager.addGameState(GameStateStub.createFinishedInstance());
		gameStateManager.addGameState(GameStateStub.createFinishedInstance());
		
		boolean finished = gameStateManager.isFinished();
		
		Assert.assertTrue(finished);
	}
	                                                      
}
