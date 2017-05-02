package com.fundynamic.d2tm.game.behaviors;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class HitPointBasedDestructibilityTest {

    public static final int WIDTH_IN_PIXELS = 64;
    private static int MAX_HIT_POINTS = 100;

    private HitPointBasedDestructibility hitPointBasedDestructibility;

    @Before
    public void setUp() {
        hitPointBasedDestructibility = new HitPointBasedDestructibility(MAX_HIT_POINTS, WIDTH_IN_PIXELS);
    }

    @Test
    public void whenMaxHealthThenHealthBarPixelWidthIsWidthInPixels() {
        assertThat(hitPointBasedDestructibility.getHealthBarPixelWidth(), is(WIDTH_IN_PIXELS));
    }

    @Test
    public void whenHalfHealthThenHealthBarPixelWidthIsWidthInPixels() {
        hitPointBasedDestructibility.takeDamage(MAX_HIT_POINTS / 2);
        assertThat(hitPointBasedDestructibility.getHealthBarPixelWidth(), is(WIDTH_IN_PIXELS / 2));
    }

    @Test
    public void whenDamageIsGreaterThanHitPointsThanHitpointsRemainZero() {
        hitPointBasedDestructibility.takeDamage(40);
        Assert.assertEquals((MAX_HIT_POINTS - 40), hitPointBasedDestructibility.getHitPoints());
    }

    @Test
    public void deductsDamageFromHitPoints() {
        hitPointBasedDestructibility.takeDamage((MAX_HIT_POINTS + 10));
        Assert.assertEquals(0, hitPointBasedDestructibility.getHitPoints());
    }

    @Test
    public void isDestroyedWhenWithZeroHitPointsOrLower() {
        hitPointBasedDestructibility.takeDamage(MAX_HIT_POINTS);
        Assert.assertTrue(hitPointBasedDestructibility.hasDied());
    }

    @Test
    public void isDestroyedWithLessThanZeroHitPoints() {
        hitPointBasedDestructibility.takeDamage(MAX_HIT_POINTS + 1);
        Assert.assertTrue(hitPointBasedDestructibility.hasDied());
    }

    @Test
    public void isDestroyedWithZeroHitPoints() {
        hitPointBasedDestructibility.takeDamage(MAX_HIT_POINTS);
        Assert.assertTrue(hitPointBasedDestructibility.hasDied());
    }

    @Test
    public void isNotDestroyedMoreThanOneHitPoint() {
        Assert.assertFalse(hitPointBasedDestructibility.hasDied());
    }

    @Test
    public void isNotDestroyedWithOneHitPoint() {
        hitPointBasedDestructibility.takeDamage(MAX_HIT_POINTS - 1);
        Assert.assertFalse(hitPointBasedDestructibility.hasDied());
    }
}