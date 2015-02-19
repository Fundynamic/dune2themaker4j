package com.fundynamic.d2tm.game.map;

import com.fundynamic.d2tm.game.math.Vector2D;
import junit.framework.Assert;
import org.junit.Test;

public class PerimeterTest {

    public static final float MIN_X = 10;
    public static final float MAX_X = 20;
    public static final float MIN_Y = 10;
    public static final float MAX_Y = 20;

    private final Perimeter perimeter;

    public PerimeterTest() {
        perimeter = new Perimeter(MIN_X, MAX_X, MIN_Y, MAX_Y);
    }

    @Test
    public void correctsWhenGoingOverLeftEdge() {
        Vector2D correctedVector = perimeter.makeSureVectorStaysWithin(Vector2D.create(MIN_X - 0.1F, MIN_Y));
        Assert.assertEquals(Vector2D.create(MIN_X, MIN_Y), correctedVector);
    }

    @Test
    public void correctsWhenGoingOverRightEdge() {
        Vector2D correctedVector = perimeter.makeSureVectorStaysWithin(Vector2D.create(MAX_X + 0.1F, MIN_Y));
        Assert.assertEquals(Vector2D.create(MAX_X, MIN_Y), correctedVector);
    }

    @Test
    public void correctsWhenGoingUpperLeftEdge() {
        Vector2D correctedVector = perimeter.makeSureVectorStaysWithin(Vector2D.create(MIN_X, MIN_Y - 0.1F));
        Assert.assertEquals(Vector2D.create(MIN_X, MIN_Y), correctedVector);
    }

    @Test
    public void correctsWhenGoingBottomLeftEdge() {
        Vector2D correctedVector = perimeter.makeSureVectorStaysWithin(Vector2D.create(MIN_X, MAX_Y + 0.1F));
        Assert.assertEquals(Vector2D.create(MIN_X, MAX_Y), correctedVector);
    }

    @Test
    public void doesNothingWhenVectorIsWithinPerimiter() {
        Vector2D correctedVector = perimeter.makeSureVectorStaysWithin(Vector2D.create(MIN_X, MIN_Y));
        Assert.assertEquals(Vector2D.create(MIN_X, MIN_Y), correctedVector);
    }

}