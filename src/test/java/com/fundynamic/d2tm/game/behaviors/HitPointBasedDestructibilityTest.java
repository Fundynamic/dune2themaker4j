package com.fundynamic.d2tm.game.behaviors;

import org.junit.Assert;
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
        Assert.assertTrue(hitPointBasedDestructibility.hasDied());
    }

    @Test
    public void isDestroyedWithLessThanZeroHitPoints() {
        hitPointBasedDestructibility.takeDamage(HIT_POINTS + 1);
        Assert.assertTrue(hitPointBasedDestructibility.hasDied());
    }

    @Test
    public void isNotDestroyedMoreThanOneHitPoint() {
        Assert.assertFalse(hitPointBasedDestructibility.hasDied());
    }

    @Test
    public void isNotDestroyedWithOneHitPoint() {
        hitPointBasedDestructibility.takeDamage(HIT_POINTS - 1);
        Assert.assertFalse(hitPointBasedDestructibility.hasDied());
    }
}