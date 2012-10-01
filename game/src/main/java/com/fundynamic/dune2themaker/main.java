package com.fundynamic.dune2themaker;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Bootstrap;
import com.fundynamic.dune2themaker.game.PlayingState;
import com.fundynamic.dune2themaker.game.TerrainFactory;

/**
 * Dune 2 The Maker lives in Java
 *
 * @author S. Hendriks
 */
public class main extends BasicGame {

	public final static int SCREEN_WIDTH = 800;
	public final static int SCREEN_HEIGHT = 600;

	private PlayingState playingState;

	public main(String title) {
		super(title);
	}

	public static void main(String[] args) {
		Bootstrap.runAsApplication(new main("Dune II - The Maker"), SCREEN_WIDTH, SCREEN_HEIGHT, false);
	}

	@Override
	public void init(GameContainer gameContainer) throws SlickException {
		try {
            Theme theme = new Theme(new Image("sheet_terrain.png"));
			TerrainFactory terrainFactory = new DuneTerrainFactory(theme);
			playingState = new PlayingState(gameContainer, terrainFactory);
			// calling init here does not work with images
		} catch (Exception e) {
			throw new SlickException("Exception occurred while initializing game.", e);
		}
	}

	@Override
	public void update(GameContainer gameContainer, int arg1) throws SlickException {
		// calling init here does not work with images
		playingState.update();
	}

	public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {
		playingState.init();
		playingState.render();
	}

}
