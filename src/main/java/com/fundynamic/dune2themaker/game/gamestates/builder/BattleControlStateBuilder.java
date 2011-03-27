package com.fundynamic.dune2themaker.game.gamestates.builder;

import org.newdawn.slick.Graphics;

import com.fundynamic.dune2themaker.game.gamestates.BattleControlState;
import com.fundynamic.dune2themaker.system.repositories.ImageRepository;

public class BattleControlStateBuilder {

	private Graphics graphics;
	private ImageRepository imageRepository;

	public BattleControlStateBuilder(Graphics graphics, ImageRepository imageRepository) {
		this.graphics = graphics;
		this.imageRepository = imageRepository;
	}
	
	public BattleControlState buildBattleControlState() {
		BattleControlState state = new BattleControlState(graphics);
		state.setImageRepository(imageRepository);
		return state;
	}

}
