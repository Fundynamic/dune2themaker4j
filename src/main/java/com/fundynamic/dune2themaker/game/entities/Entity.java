package com.fundynamic.dune2themaker.game.entities;

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
	
	public Entity(int hitpoints) {
		if (hitpoints < 1) {
			throw new IllegalArgumentException("Cannot construct entity that has lower than 1 hitpoints, given hitpoints is [" + hitpoints + "]");
		}
		this.hitpoints = hitpoints;
	}
	
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
}
