package com.fundynamic.dune2themaker;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import com.fundynamic.dune2themaker.game.Game;
import com.fundynamic.dune2themaker.game.builder.GameBuilder;
import com.fundynamic.dune2themaker.game.builder.GameBuilderImpl;
import com.fundynamic.dune2themaker.game.gamestates.manager.GameStateManager;
import com.fundynamic.dune2themaker.system.control.Keyboard;
import com.fundynamic.dune2themaker.system.control.Mouse;
import com.fundynamic.dune2themaker.system.repositories.ImageRepository;

/**
 * 
 * Dune 2 The Maker lives in Java
 * 
 * @author S. Hendriks
 *
 */
public class Dune2themaker extends BasicGame {

	public final static int SCREEN_WIDTH	= 800;
	public final static int SCREEN_HEIGHT 	= 600;

	private Game game;
	
	public Dune2themaker(String title) {
		super(title);
	}

	public static void main(String[] args) {
		try { 
		    AppGameContainer container = 
		    			new AppGameContainer(new Dune2themaker("Dune II - The Maker")); 
		    container.setDisplayMode(SCREEN_WIDTH, SCREEN_HEIGHT, false);
		    container.setVSync(true);
		    container.start();
		} catch (SlickException e) { 
		    e.printStackTrace(); 
		}
	}

	@Override
	public void init(GameContainer gameContainer) throws SlickException {
		try {
			GameBuilder gameBuilder = new GameBuilderImpl(gameContainer);
			game = gameBuilder.buildGame();
			game.init();
		} catch (Exception e) {
			throw new SlickException("Exception occured while initializing game.", e);
		}
	}

	@Override
	public void update(GameContainer gameContainer, int arg1) throws SlickException {
		game.update();
	}

	public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {
		game.render();
	}

}
