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

    @Test
    public void scaleContainCenterSameAspectRatioAndSameDimensionsAndOnOrigin() {
        Rectangle rec = new Rectangle(0, 0, 100, 100);
        Rectangle result = rec.scaleContainCenter(new Vector2D(100, 100));
        Assert.assertEquals(new Rectangle(0, 0, 100, 100), result);
    }

    @Test
    public void scaleContainCenterSameAspectRatioAndSameDimensionsAndNotOnOrigin() {
        Rectangle rec = new Rectangle(50, 50, 150, 150);
        Rectangle result = rec.scaleContainCenter(new Vector2D(100, 100));
        Assert.assertEquals(new Rectangle(50, 50, 150, 150), result);
    }

    @Test
    public void scaleContainCenterSameAspectRatioAndDifferentDimensions() {
        Rectangle rec = new Rectangle(0, 0, 50, 50);
        Rectangle result = rec.scaleContainCenter(new Vector2D(100, 100));
        Assert.assertEquals(new Rectangle(0, 0, 50, 50), result);
    }

    @Test
    public void scaleContainCenterSameHeightAndOnOrigin() {
        Rectangle rec = new Rectangle(0, 0, 100, 100);
        Rectangle result = rec.scaleContainCenter(new Vector2D(50, 100));
        Assert.assertEquals(new Rectangle(25, 0, 75, 100), result);
    }

    @Test
    public void scaleContainCenterSameHeightAndNotOnOrigin() {
        Rectangle rec = new Rectangle(50, 50, 150, 150);
        Rectangle result = rec.scaleContainCenter(new Vector2D(50, 100));
        Assert.assertEquals(new Rectangle(75, 50, 125, 150), result);
    }

    @Test
    public void scaleContainCenterSameWidthAndOnOrigin() {
        Rectangle rec = new Rectangle(0, 0, 100, 100);
        Rectangle result = rec.scaleContainCenter(new Vector2D(100, 50));
        Assert.assertEquals(new Rectangle(0, 25, 100, 75), result);
    }

    @Test
    public void scaleContainCenterSameWidthAndNotOnOrigin() {
        Rectangle rec = new Rectangle(50, 50, 150, 150);
        Rectangle result = rec.scaleContainCenter(new Vector2D(100, 50));
        Assert.assertEquals(new Rectangle(50, 75, 150, 125), result);
    }
}