package com.fundynamic.dune2themaker.game.entities.units;

import com.fundynamic.dune2themaker.game.entities.Entity;

/**
 * Airborn units move pixel by pixel. "hovering" above cells
 * 
 * @author Stefan
 *
 */
public abstract class AirUnit extends Entity {

	public AirUnit(int hitpoints) {
		super(hitpoints);
	}

}
