package com.fundynamic.dune2themaker.domain;

/**
 * An entity can be anything in the game that lives. It could be a bullet, a rocket,
 * a unit, structure, or an explosion effect even.
 * 
 * All entities have hitpoints. Whenever the hitpoints are lower than 1, it is considered
 * dead. 
 * 
 * @author Stefan
 *
 */
public abstract class Entity {

	private int hitpoints;
	private int x, y;
	
	public Entity(int hitpoints) {
		if (hitpoints < 1) {
			throw new IllegalArgumentException("Cannot construct entity that has lower than 1 hitpoints, given hitpoints is [" + hitpoints + "]");
		}
		this.hitpoints = hitpoints;
	}
	
	abstract public void think(); // every entity has its right to think (done in the update phase)
	
	public void setHitPoints(int hitpoints) {
		this.hitpoints = hitpoints;
	}
	
	/**
	 * When hitpoints is lower than 1, this method will return true.
	 * 
	 * @return
	 */
	public boolean isDead() {
		if (hitpoints < 1) {
			return true;
		}
		return false;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	
}
