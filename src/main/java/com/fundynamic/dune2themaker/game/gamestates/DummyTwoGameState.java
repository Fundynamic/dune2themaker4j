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

public class DummyTwoGameState extends AbstractGameState {

	private static final String BALL_YELLOW = "ball_yellow";
	private List<Ball> balls;
	
	public DummyTwoGameState(Game game) {
		super(game);
	}

	public void init() {
		try {
			Image image = new Image("piece_yellow.bmp");
			game.getImageRepository().addItem(BALL_YELLOW, image);
			balls = new ArrayList<Ball>();
			
			int amount = 25;
			for (int i = 0; i < amount; i++) {
				Ball ball = new Ball(10);
				ball.setMaxVelocityX(Random.getRandomInt(1, 10));
				ball.setMaxVelocityY(Random.getRandomInt(1, 10));
				ball.setWidth(image.getWidth());
				ball.setHeight(image.getHeight());
				ball.setX(Random.getRandomInt(0, Dune2themaker.SCREEN_WIDTH-32));
				ball.setY(Random.getRandomInt(0, Dune2themaker.SCREEN_HEIGHT-32));
				ball.setVelocityX(Random.getFiftyFifty(ball.getMaxVelocityX(), ball.getMaxVelocityX() * -1));
				ball.setVelocityY(Random.getFiftyFifty(ball.getMaxVelocityY(), ball.getMaxVelocityY() * -1));
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
			drawer.drawImage(BALL_YELLOW, ball.getX(), ball.getY());			
		}
		
	}

}
