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

import com.fundynamic.dune2themaker.game.pieces.Piece;
import com.fundynamic.dune2themaker.game.pieces.RedPiece;
import com.fundynamic.dune2themaker.game.pieces.YellowPiece;
import com.fundynamic.dune2themaker.system.repositories.ImageContainer;

/**
 * Simple stack game
 * 
 * @author S. Hendriks
 *
 */
public class Dune2themaker extends BasicGame {

	public final static int SCREEN_WIDTH	= 800;
	public final static int SCREEN_HEIGHT 	= 600;

	List<Piece> pieces;
	
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
		// add images
		ImageContainer.getInstance().addImage("MS_Normal.png", false);
		ImageContainer.getInstance().addImage("background.bmp", false);
		ImageContainer.getInstance().addImage("piece_yellow.bmp", false);
		ImageContainer.getInstance().addImage("piece_red.bmp", false);

		// initialize grid
		TiledBoard grid = TiledBoard.getInstance();
		grid.setWidth(8);
		
		// add pieces
		pieces = new ArrayList<Piece>();
		pieces.add(new YellowPiece(0, 0));
		pieces.add(new RedPiece(1, 0));
		pieces.add(new YellowPiece(2, 0));
		pieces.add(new YellowPiece(3, 0));
	}

	@Override
	public void update(GameContainer gameContainer, int arg1) throws SlickException {
		Input input = gameContainer.getInput();
		
		// quit when ESC is pressed.
		if (input.isKeyPressed(Input.KEY_ESCAPE)) {
			gameContainer.exit();
		}
		
	}

	public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {

		Input input = gameContainer.getInput();
	
		Image image = ImageContainer.getInstance().getImage("background.bmp");
		graphics.drawImage(image, 0, 0);
		
		
		TiledBoard grid = TiledBoard.getInstance();
		grid.draw(graphics);
		
		for (Piece piece : pieces) {
			grid.draw(piece, graphics);
		}
		
	}

}
