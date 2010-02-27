package com.fundynamic.dune2themaker.game.pieces;

import org.newdawn.slick.Image;

import com.fundynamic.dune2themaker.system.repositories.ImageContainer;

public class YellowPiece extends Piece {

	public YellowPiece(int x, int y) {
		super(x, y);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getScore() {
		return 0;
	}

	@Override
	public Image getImage() {		
		return ImageContainer.getInstance().getImage("piece_yellow.bmp");
	}


	
}
