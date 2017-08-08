package com.fundynamic.d2tm.math;

import org.junit.Assert;
import org.junit.Test;

public class Vector2DTest {

    @Test
    public void movesVectorWithMoveVector() {
        Vector2D vec = new Vector2D(0F, 1F);
        Vector2D velocity = new Vector2D(10, 10);
        Vector2D moved = vec.move(velocity);
        Assert.assertEquals(10F, moved.getX(), 0.0001F);
        Assert.assertEquals(11F, moved.getY(), 0.0001F);
    }

    @Test
    public void isZero() {
        Vector2D zero = Vector2D.zero();
        Assert.assertTrue(zero.isZero());
    }

    @Test
    public void angles() {
        Vector2D start = new Vector2D(0F, 0F);
        Vector2D target = new Vector2D(10, 10);
        System.out.println(start.angleTo(target));
    }

    @Test
    public void movesVector() {
        Vector2D vec = new Vector2D(0F, 1F);
        Vector2D moved = vec.move(10F, 10F);
        Assert.assertEquals(10F, moved.getX(), 0.0001F);
        Assert.assertEquals(11F, moved.getY(), 0.0001F);
    }

    @Test
    public void areEqual() {
        Vector2D vec1 = new Vector2D(10F, 10F);
        Vector2D vec2 = new Vector2D(10F, 10F);
        Vector2D vec3 = new Vector2D(11F, 10F);
        Assert.assertEquals(vec1, vec2);
        Assert.assertNotEquals(vec1, vec3);
    }

    @Test
    public void dividesVector() {
        Vector2D vec = new Vector2D(64F, 128F);
        Vector2D divved = vec.div(32F);
        Assert.assertEquals(2F, divved.getX(), 0.0001F);
        Assert.assertEquals(4F, divved.getY(), 0.0001F);
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

    @Test
    public void equals() {
        Assert.assertEquals(Vector2D.create(10, 10), Vector2D.create(10, 10));
    }

}