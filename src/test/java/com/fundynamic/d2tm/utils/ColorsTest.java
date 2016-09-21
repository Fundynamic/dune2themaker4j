package com.fundynamic.d2tm.utils;

import org.junit.Assert;
import org.junit.Test;
import org.newdawn.slick.Color;


public class ColorsTest {

    @Test
    public void createsColorWithoutAlpha() {
        Color c = new Color(234, 123, 23);
        Assert.assertEquals(c, Colors.create(234, 123, 23));
    }

    @Test
    public void createsColorWithAlpha() {
        Color c = new Color(234, 123, 23, 56);
        Assert.assertEquals(c, Colors.create(234, 123, 23, 56));
    }

    @Test
    public void cachesCreatedColorWithoutAlpha() {
        Color color1 = Colors.create(234, 23, 142);
        Color color2 = Colors.create(234, 23, 142);
        Assert.assertTrue(color1 == color2);
    }

    @Test
    public void cachesCreatedColorWithAlpha() {
        Color color1 = Colors.create(234, 23, 142, 32);
        Color color2 = Colors.create(234, 23, 142, 32);
        Assert.assertTrue(color1 == color2);
    }

    @Test
    public void createsNewColorWhenNotCachedOnDifferentAlpha() {
        Color color1 = Colors.create(234, 23, 142, 32);
        Color color2 = Colors.create(234, 23, 142, 33); // different alpha
        Assert.assertTrue(color1 != color2);
    }

    @Test
    public void createsNewColorWhenNotCachedOnDifferentRed() {
        Color color1 = Colors.create(234, 23, 142, 32);
        Color color2 = Colors.create(233, 23, 142, 32); // different red
        Assert.assertTrue(color1 != color2);
    }

    @Test
    public void createsNewColorWhenNotCachedOnDifferentGreen() {
        Color color1 = Colors.create(234, 23, 142, 32);
        Color color2 = Colors.create(234, 21, 142, 32); // different green
        Assert.assertTrue(color1 != color2);
    }

    @Test
    public void createsNewColorWhenNotCachedOnDifferentBlue() {
        Color color1 = Colors.create(234, 23, 142, 32);
        Color color2 = Colors.create(234, 23, 141, 32); // different blue
        Assert.assertTrue(color1 != color2);
    }

}