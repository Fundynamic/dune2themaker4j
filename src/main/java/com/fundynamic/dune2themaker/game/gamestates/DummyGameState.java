package com.fundynamic.dune2themaker.game.gamestates;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import com.fundynamic.dune2themaker.Dune2themaker;
import com.fundynamic.dune2themaker.game.Game;
import com.fundynamic.dune2themaker.game.entities.Ball;
import com.fundynamic.dune2themaker.system.drawers.ImageDrawer;
import com.fundynamic.dune2themaker.util.Random;

public class DummyGameState extends AbstractGameState {

	private List<Ball> balls;
	
	public DummyGameState(Game game) {
		super(game);
	}

	public void init() {
		try {
			Image image = new Image("piece_red.bmp");
			game.getImageRepository().addItem("ball", image);
			balls = new ArrayList<Ball>();
			
			int amount = 50;
			for (int i = 0; i < amount; i++) {
				Ball ball = new Ball(10);
				ball.setWidth(image.getWidth());
				ball.setHeight(image.getHeight());
				ball.setX(Random.getRandomInt(0, Dune2themaker.SCREEN_WIDTH-32));
				ball.setY(Random.getRandomInt(0, Dune2themaker.SCREEN_HEIGHT-32));
				ball.setVelocityX(Random.getFiftyFifty(-1, 1));
				ball.setVelocityY(Random.getFiftyFifty(-1, 1));
				balls.add(ball);
			}
			
		} catch (SlickException se) {
			throw new RuntimeException(se);
		}
		
	}

	public void update() {
		for (Ball ball : balls) {
			ball.think();
		}
	}

	public void render(Graphics graphics) {
		ImageDrawer drawer = new ImageDrawer(graphics, game.getImageRepository());
		for (Ball ball : balls) {
			drawer.drawImage("ball", ball.getX(), ball.getY());			
		}
		
	}

}
