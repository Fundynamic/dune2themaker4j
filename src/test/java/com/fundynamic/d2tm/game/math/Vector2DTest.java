package com.fundynamic.d2tm.game.math;

import org.junit.Assert;
import org.junit.Test;

public class Vector2DTest {

    @Test
    public void movesVector() {
        Vector2D<Float> vec = new Vector2D<>(0F, 1F);
        Vector2D<Float> moved = vec.move(10F, 10F);
        Assert.assertEquals(10F, moved.getX(), 0.0001F);
        Assert.assertEquals(11F, moved.getY(), 0.0001F);
    }

    @Test
    public void toInt() {
        Vector2D<Float> vec = new Vector2D<>(1.1F, 2.6F);
        Vector2D<Integer> intVec = vec.toInt();
        Assert.assertEquals(1, (int) intVec.getX());
        Assert.assertEquals(2, (int) intVec.getY());
    }

}