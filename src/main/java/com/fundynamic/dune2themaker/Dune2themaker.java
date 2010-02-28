package com.fundynamic.dune2themaker;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import com.fundynamic.dune2themaker.game.Game;
import com.fundynamic.dune2themaker.game.pieces.Piece;
import com.fundynamic.dune2themaker.game.pieces.RedPiece;
import com.fundynamic.dune2themaker.game.pieces.YellowPiece;
import com.fundynamic.dune2themaker.system.control.Mouse;
import com.fundynamic.dune2themaker.system.drawers.ImageDrawer;
import com.fundynamic.dune2themaker.system.repositories.ImageContainer;
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

	/**
	 * @param args
	 */
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

	/**
	 * Load files here, and everything else to set up.
	 */
	@Override
	public void init(GameContainer gameContainer) throws SlickException {
		try {
			game = new Game(gameContainer);
			game.init();
		} catch (Exception e) {
			throw new SlickException("Exception occured while initializing game.", e);
		}
	}

	@Override
	public void update(GameContainer gameContainer, int arg1) throws SlickException {
		this.game.update();
	}

	public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {
		this.game.render(graphics);
	}

}
