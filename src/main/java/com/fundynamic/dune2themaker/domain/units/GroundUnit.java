package com.fundynamic.dune2themaker.domain.units;

import com.fundynamic.dune2themaker.domain.Entity;

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
