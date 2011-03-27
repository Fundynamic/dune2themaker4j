package com.fundynamic.dune2themaker.game.entities.units;

import com.fundynamic.dune2themaker.game.entities.Entity;

/**
 * Ground units move square by square
 * 
 * @author Stefan
 *
 */
public abstract class GroundUnit extends Entity {

	public GroundUnit(int hitpoints) {
		super(hitpoints);
	}

}
