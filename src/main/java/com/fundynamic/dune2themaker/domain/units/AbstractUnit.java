package com.fundynamic.dune2themaker.domain.units;

import com.fundynamic.dune2themaker.domain.Entity;

/**
 * A unit in this game has an X and Y coordinate
 * Units take orders, belong to a player.
 * 
 * @author Stefan
 *
 */
public abstract class AbstractUnit extends Entity {

	public AbstractUnit(int hitpoints) {
		super(hitpoints);
	}

	abstract void move();
}
