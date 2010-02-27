package com.fundynamic.dune2themaker.game.entities;

import static org.junit.Assert.fail;
import junit.framework.Assert;

import org.junit.Test;

public class EntityTest {

	@Test
	public void testIsNotDeadWhenHitPointsIsAboveZero() {
		Entity entity = new Entity(1) {};
		Assert.assertFalse(entity.isDead());		
	}

	@Test(expected = IllegalArgumentException.class)
	public void cannotConstructEntityWithHitPointsLessThanOne() {
		Entity entity = new Entity(0) {};
	}
	
	@Test
	public void mustBeDeadWhenHitPointsIsLowerThanOne() {
		Entity entity = new Entity(3) {};
		entity.setHitPoints(0);
		Assert.assertTrue(entity.isDead());
	}

}
