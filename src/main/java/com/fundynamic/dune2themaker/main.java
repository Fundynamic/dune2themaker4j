package com.fundynamic.dune2themaker;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import com.fundynamic.dune2themaker.dune.PlayingState;

/**
 * 
 * Dune 2 The Maker lives in Java
 * 
 * @author S. Hendriks
 *
 */
public class main extends BasicGame {

	public final static int SCREEN_WIDTH	= 800;
	public final static int SCREEN_HEIGHT 	= 600;

	private PlayingState playingState;
	
	public main(String title) {
		super(title);
	}

	public static void main(String[] args) {
		try { 
		    AppGameContainer container = 
		    			new AppGameContainer(new main("Dune II - The Maker"));
		    container.setDisplayMode(SCREEN_WIDTH, SCREEN_HEIGHT, false);
		    container.setVSync(false);
		    container.start();
		} catch (SlickException e) { 
		    e.printStackTrace(); 
		}
	}

	@Override
	public void init(GameContainer gameContainer) throws SlickException {
		try {
			playingState = new PlayingState(gameContainer);
		} catch (Exception e) {
			throw new SlickException("Exception occured while initializing game.", e);
		}
	}

	@Override
	public void update(GameContainer gameContainer, int arg1) throws SlickException {
		playingState.update();
	}

	public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {
		playingState.render();
	}

}
