package com.fundynamic.d2tm.game.entities;

import com.fundynamic.d2tm.math.Coordinate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class UnitMoveIntentsTest {

    public static final Coordinate VEC_23_23 = Coordinate.create(23, 23);
    private UnitMoveIntents unitMoveIntents;
    private NullEntity entityOne;
    private NullEntity entityTwo;

    @Before
    public void setUp() {
        unitMoveIntents = new UnitMoveIntents();
        entityOne = new NullEntity();
        entityTwo = new NullEntity();
    }

    @Test
    public void removeAllIntentsByEntity() {
        unitMoveIntents.addIntent(VEC_23_23, entityOne);
        unitMoveIntents.removeAllIntentsBy(entityOne);
        Assert.assertFalse(unitMoveIntents.hasIntent(VEC_23_23.toMapCoordinate()));
    }

    @Test
    public void canClaimVector() {
        unitMoveIntents.addIntent(VEC_23_23, entityOne);
    }

    @Test
    public void canClaimVectorTwiceWithSameEntity() {
        unitMoveIntents.addIntent(VEC_23_23, entityOne);
        unitMoveIntents.addIntent(VEC_23_23, entityOne);
    }

    @Test(expected = IllegalStateException.class)
    public void throwsExceptionWhenClaimingVectorWithDifferentEntities() {
        unitMoveIntents.addIntent(VEC_23_23, entityOne);
        unitMoveIntents.addIntent(VEC_23_23, entityTwo);
    }

    @Test
    public void canAddIntentToVectorWhenAlreadyClaimedWhenAskingForSameEntity() {
        unitMoveIntents.addIntent(VEC_23_23, entityOne);
        Assert.assertTrue(unitMoveIntents.isVectorClaimableBy(VEC_23_23, entityOne));
    }

    @Test
    public void canClaimUnclaimedVector() {
        Assert.assertTrue(unitMoveIntents.isVectorClaimableBy(VEC_23_23, entityOne));
    }

    @Test
    public void canNotAddIntentToVectorWhenAlreadyClaimedWhenAskingForSameEntity() {
        unitMoveIntents.addIntent(VEC_23_23, entityOne);
        Assert.assertFalse(unitMoveIntents.isVectorClaimableBy(VEC_23_23, entityTwo));
    }

    @Test
    public void canReclaimVectorOnceRemoved() {
        unitMoveIntents.addIntent(VEC_23_23, entityOne);
        unitMoveIntents.removeIntent(VEC_23_23);
        unitMoveIntents.addIntent(VEC_23_23, entityTwo);
    }
}