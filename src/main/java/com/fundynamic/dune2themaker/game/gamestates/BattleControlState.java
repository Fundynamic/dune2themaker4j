package com.fundynamic.dune2themaker.game.gamestates;

import org.newdawn.slick.Graphics;

import com.fundynamic.dune2themaker.util.Validate;


public class BattleControlState implements GameState {
	
	private Graphics graphics;
	
	public BattleControlState(Graphics graphics) {
		Validate.notNull(graphics);
		this.graphics = graphics;
	}
	
	public void init() {
	}

	public void render() {
		
	}

	public void update() {
		
	}

	public boolean isFinished() {
		return false;
	}


}
