package com.fundynamic.dune2themaker.dune.terrain;

public class Concrete implements Terrain, ConstructionGround, Destructable {

	public int getRowOnSpriteSheet() {
		return 6;
	}

	public int getHealth() {
		return 0;
	}

	public void damage(int hitPoints) {

	}

}
