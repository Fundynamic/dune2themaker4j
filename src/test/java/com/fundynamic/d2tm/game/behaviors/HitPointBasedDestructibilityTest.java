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
        hitPointBasedDestructibility.reduce(MAX_HIT_POINTS / 2);
        assertThat(hitPointBasedDestructibility.getHealthBarPixelWidth(), is(WIDTH_IN_PIXELS / 2));
    }

    @Test
    public void whenDamageIsGreaterThanHitPointsThanHitpointsRemainZero() {
        hitPointBasedDestructibility.reduce(40);
        Assert.assertEquals((MAX_HIT_POINTS - 40), hitPointBasedDestructibility.getCurrent());
    }

    @Test
    public void deductsDamageFromHitPoints() {
        hitPointBasedDestructibility.reduce((MAX_HIT_POINTS + 10));
        Assert.assertEquals(0, hitPointBasedDestructibility.getCurrent());
    }

    @Test
    public void isDestroyedWhenWithZeroHitPointsOrLower() {
        hitPointBasedDestructibility.reduce(MAX_HIT_POINTS);
        Assert.assertTrue(hitPointBasedDestructibility.isZero());
    }

    @Test
    public void isDestroyedWithLessThanZeroHitPoints() {
        hitPointBasedDestructibility.reduce(MAX_HIT_POINTS + 1);
        Assert.assertTrue(hitPointBasedDestructibility.isZero());
    }

    @Test
    public void isDestroyedWithZeroHitPoints() {
        hitPointBasedDestructibility.reduce(MAX_HIT_POINTS);
        Assert.assertTrue(hitPointBasedDestructibility.isZero());
    }

    @Test
    public void isNotDestroyedMoreThanOneHitPoint() {
        Assert.assertFalse(hitPointBasedDestructibility.isZero());
    }

    @Test
    public void isNotDestroyedWithOneHitPoint() {
        hitPointBasedDestructibility.reduce(MAX_HIT_POINTS - 1);
        Assert.assertFalse(hitPointBasedDestructibility.isZero());
    }

    @Test
    public void isMaxed() {
        Assert.assertTrue(hitPointBasedDestructibility.isMaxed());
        hitPointBasedDestructibility.reduce(1);
        Assert.assertFalse(hitPointBasedDestructibility.isMaxed());
    }

    @Test
    public void isZero() {
        hitPointBasedDestructibility.reduce(((float)MAX_HIT_POINTS-0.001f));
        Assert.assertFalse(hitPointBasedDestructibility.isZero());
        hitPointBasedDestructibility.reduce(0.001f);
        Assert.assertTrue(hitPointBasedDestructibility.isZero());
    }
}