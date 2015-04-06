package com.fundynamic.d2tm.game.entities;

import com.fundynamic.d2tm.math.Vector2D;
import junit.framework.Assert;
import org.junit.Test;

public class RectangleTest {

    @Test
    public void createsRectangleFromTopLeftToBottomRight() {
        Rectangle rec = Rectangle.create(Vector2D.create(0, 0), Vector2D.create(10, 15));
        Assert.assertEquals(10, rec.getWidthAsInt());
        Assert.assertEquals(15, rec.getHeightAsInt());
    }

    @Test
    public void createsRectangleFromBottomRightToTopLeft() {
        Rectangle rec = Rectangle.create(Vector2D.create(10, 15), Vector2D.create(0, 0));
        Assert.assertEquals(10, rec.getWidthAsInt());
        Assert.assertEquals(15, rec.getHeightAsInt());
    }

    @Test
    public void isWithinReturnsFalseWhenOutside() {
        Rectangle rec = Rectangle.create(Vector2D.create(0, 0), Vector2D.create(10, 15));
        Assert.assertFalse(rec.isWithin(Vector2D.create(-1, -1)));
        Assert.assertFalse(rec.isWithin(Vector2D.create(-1, 14)));
        Assert.assertFalse(rec.isWithin(Vector2D.create(10, 15)));
        Assert.assertFalse(rec.isWithin(Vector2D.create(11, 15)));
    }

    @Test
    public void isWithinReturnsTrueWithTopLeftToBottomRight() {
        Rectangle rec = Rectangle.create(Vector2D.create(0, 0), Vector2D.create(10, 15));
        Assert.assertTrue(rec.isWithin(Vector2D.create(0, 0)));
    }

    @Test
    public void isWithinReturnsTrueWithBottomRightToTopLeft() {
        Rectangle rec = Rectangle.create(Vector2D.create(10, 15), Vector2D.create(0, 0));
        Assert.assertTrue(rec.isWithin(Vector2D.create(0, 0)));
    }

}