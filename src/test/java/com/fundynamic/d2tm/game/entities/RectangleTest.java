package com.fundynamic.d2tm.game.entities;

import com.fundynamic.d2tm.math.Rectangle;
import com.fundynamic.d2tm.math.Vector2D;
import org.junit.Assert;
import org.junit.Test;

public class RectangleTest {

    @Test
    public void createsRectangleFromTopLeftToBottomRight() {
        Rectangle rec = Rectangle.create(Vector2D.create(0, 0), Vector2D.create(10, 15));
        Assert.assertEquals(10, rec.getWidth());
        Assert.assertEquals(15, rec.getHeight());
    }

    @Test
    public void createsRectangleFromBottomRightToTopLeft() {
        Rectangle rec = Rectangle.create(Vector2D.create(10, 15), Vector2D.create(0, 0));
        Assert.assertEquals(10, rec.getWidth());
        Assert.assertEquals(15, rec.getHeight());
    }

    @Test
    public void isWithinReturnsFalseWhenOutside() {
        Rectangle rec = Rectangle.create(Vector2D.create(0, 0), Vector2D.create(10, 15));
        Assert.assertFalse(rec.isVectorWithin(Vector2D.create(-1, -1)));
        Assert.assertFalse(rec.isVectorWithin(Vector2D.create(-1, 14)));
        Assert.assertFalse(rec.isVectorWithin(Vector2D.create(10, 15)));
        Assert.assertFalse(rec.isVectorWithin(Vector2D.create(11, 15)));
    }

    @Test
    public void isWithinReturnsTrueWithTopLeftToBottomRight() {
        Rectangle rec = Rectangle.create(Vector2D.create(0, 0), Vector2D.create(10, 15));
        Assert.assertTrue(rec.isVectorWithin(Vector2D.create(0, 0)));
    }

    @Test
    public void isWithinReturnsTrueWithBottomRightToTopLeft() {
        Rectangle rec = Rectangle.create(Vector2D.create(10, 15), Vector2D.create(0, 0));
        Assert.assertTrue(rec.isVectorWithin(Vector2D.create(0, 0)));
    }

}