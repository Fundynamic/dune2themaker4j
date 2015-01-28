package com.fundynamic.d2tm.game.math;

import org.junit.Assert;
import org.junit.Test;

public class Vector2DTest {

    @Test
    public void movesVector() {
        Vector2D vec = new Vector2D(0F, 1F);
        Vector2D moved = vec.move(10F, 10F);
        Assert.assertEquals(10F, moved.getX(), 0.0001F);
        Assert.assertEquals(11F, moved.getY(), 0.0001F);
    }

    @Test
    public void toInt() {
        Vector2D vec = new Vector2D(1.1F, 2.6F);
        Assert.assertEquals(1, vec.getXAsInt());
        Assert.assertEquals(2, vec.getYAsInt());
    }

    @Test
    public void returnsDifference() {
        Vector2D vecOrg = new Vector2D(10, 10);
        Vector2D vecNew = new Vector2D(5, 12);
        Vector2D vecDiff = vecOrg.diff(vecNew);
        Assert.assertEquals(5,  vecDiff.getXAsInt());
        Assert.assertEquals(-2,  vecDiff.getYAsInt());
    }

}