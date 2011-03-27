package com.fundynamic.dune2themaker.game.gamestates;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import com.fundynamic.dune2themaker.system.repositories.ImageRepository;
import com.fundynamic.dune2themaker.util.Validate;


public class BattleControlState implements GameState {
	
	private Graphics graphics;
	private ImageRepository imageRepository;
	
	public BattleControlState(Graphics graphics) {
		Validate.notNull(graphics);
		this.graphics = graphics;
	}
	
	public void init() {
	}

	public void render() {
		Image image = imageRepository.getItem("terrain");
		//graphics.drawImage(image, 0, 0, 0, 0, 200, 200);
		int drawX = 16;
		int drawY = 100;
		int drawX2 = drawX + 32;
		int drawY2 = drawY + 32;
//		graphics.drawImage(image, drawX, drawY, drawX2, drawY2, 32, 32, 0, 0);
		
		// draws a piece, the latest 2x2 coordinates are mapped to the image itself
		graphics.drawImage(image, drawX, drawY, drawX2, drawY2, 32, 32, 64, 64);

	}

	public void update() {
		
	}

	public boolean isFinished() {
		return false;
	}

	public void setImageRepository(ImageRepository imageRepository) {
		this.imageRepository = imageRepository;
	}

}
