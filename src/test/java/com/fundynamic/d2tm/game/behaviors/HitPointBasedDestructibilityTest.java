package com.fundynamic.d2tm.game.behaviors;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

public class HitPointBasedDestructibilityTest {

    private static int HIT_POINTS = 100;
    private HitPointBasedDestructibility hitPointBasedDestructibility;

    @Before
    public void setUp() {
        hitPointBasedDestructibility = new HitPointBasedDestructibility(HIT_POINTS);
    }

    @Test
    public void deductsDamageFromHitPoints() {
        hitPointBasedDestructibility.takeDamage(40);
        Assert.assertEquals((HIT_POINTS - 40), hitPointBasedDestructibility.getHitPoints());
    }

    @Test
    public void isDestroyedWhenWithZeroHitPointsOrLower() {
        hitPointBasedDestructibility.takeDamage(HIT_POINTS);
        Assert.assertTrue(hitPointBasedDestructibility.isDestroyed());
    }

    @Test
    public void isDestroyedWithLessThanZeroHitPoints() {
        hitPointBasedDestructibility.takeDamage(HIT_POINTS + 1);
        Assert.assertTrue(hitPointBasedDestructibility.isDestroyed());
    }

    @Test
    public void isNotDestroyedMoreThanOneHitPoint() {
        Assert.assertFalse(hitPointBasedDestructibility.isDestroyed());
    }

    @Test
    public void isNotDestroyedWithOneHitPoint() {
        hitPointBasedDestructibility.takeDamage(HIT_POINTS - 1);
        Assert.assertFalse(hitPointBasedDestructibility.isDestroyed());
    }
}