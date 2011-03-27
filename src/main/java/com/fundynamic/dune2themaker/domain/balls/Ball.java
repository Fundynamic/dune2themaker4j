package com.fundynamic.dune2themaker.domain.balls;

import com.fundynamic.dune2themaker.Dune2themaker;
import com.fundynamic.dune2themaker.domain.Entity;
import com.fundynamic.dune2themaker.game.entities.behaviors.Bouncable;

public class Ball extends Entity implements Bouncable {

	private int velocityX = 1;
	private int velocityY = 1;
	private int width = 32;
	private int height = 32;
	private int maxVelocityX = 1;
	private int maxVelocityY = 1;
	
	public Ball(int hitpoints) {
		super(hitpoints);
		this.setX(200);
		this.setY(200);
	}

	public int getVelocityX() {
		return velocityX;
	}

	public void setVelocityX(int velocityX) {
		this.velocityX = velocityX;
	}

	public int getVelocityY() {
		return velocityY;
	}

	public void setVelocityY(int velocityY) {
		this.velocityY = velocityY;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getWidth() {
		return width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getHeight() {
		return height;
	}

	@Override
	public void think() {
		// update coordinates
		setX(getX() + getVelocityX());
		setY(getY() + getVelocityY());
		
		adjustVelocityWhenHittingBoundaries();
	}

	public void adjustVelocityWhenHittingBoundaries() {
		// respond to boundaries at screen
		if ((getX() + getWidth()) >= Dune2themaker.SCREEN_WIDTH) {
			setVelocityX(maxVelocityX * -1);
		}
		
		if ((getY() + getHeight()) >= Dune2themaker.SCREEN_HEIGHT) {
			setVelocityY(maxVelocityY * -1);
		}
		
		if (getX() <= 0) {
			setVelocityX(maxVelocityX);
		}

		if (getY() <= 0) {
			setVelocityY(maxVelocityY);
		}
	}

	public void setMaxVelocityY(int maxVelocityY) {
		this.maxVelocityY = maxVelocityY;
	}

	public int getMaxVelocityY() {
		return maxVelocityY;
	}

	public void setMaxVelocityX(int maxVelocityX) {
		this.maxVelocityX = maxVelocityX;
	}

	public int getMaxVelocityX() {
		return maxVelocityX;
	}
}
