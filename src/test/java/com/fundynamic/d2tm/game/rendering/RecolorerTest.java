package com.fundynamic.d2tm.game.rendering;

import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.Color;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


public class RecolorerTest {

    private Recolorer recolorer;
    private List<Color> colorsToRecolor;

    @Before
    public void setUp() {
        recolorer = new Recolorer();
        colorsToRecolor = new ArrayList<>();
        colorsToRecolor.add(new Color(214, 0, 0));
        colorsToRecolor.add(new Color(182, 0, 0));
        colorsToRecolor.add(new Color(153, 0, 0));
        colorsToRecolor.add(new Color(125, 0, 0));
        colorsToRecolor.add(new Color(89, 0, 0));
        colorsToRecolor.add(new Color(60, 0, 0));
        colorsToRecolor.add(new Color(32, 0, 0));
    }

    @Test
    public void shouldNotModifyColorWhenItIsAnUnknownColorToRecolor() {
        Color color = new Color(255, 255, 255);

        assertThat(recolorer.isColorToRecolor(color), is(false));

        Color newColor = recolorer.recolorToFactionColor(color, Recolorer.FactionColor.BLUE);

        assertThat(color, is(newColor));
    }

    @Test
    public void testExpectedColorsAreAllColorsToBeRecolored() {
        for (Color color : colorsToRecolor) {
            assertThat(recolorer.isColorToRecolor(color), is(true));
        }
    }

    @Test
    public void shouldRecolorRedToBlue() {
        for (Color color : colorsToRecolor) {
            Color newColor = recolorer.recolorToFactionColor(color, Recolorer.FactionColor.BLUE);
            Color expectedColor = new Color(color.getBlue(), color.getGreen(), color.getRed(), color.getAlpha());
            assertThat(newColor, is(expectedColor));
        }
    }

    @Test
    public void shouldRecolorRedToGreen() {
        for (Color color : colorsToRecolor) {
            Color newColor = recolorer.recolorToFactionColor(color, Recolorer.FactionColor.GREEN);
            Color expectedColor = new Color(color.getGreen(), color.getRed(), color.getBlue(), color.getAlpha());
            assertThat(newColor, is(expectedColor));
        }
    }

    @Test
    public void shouldKeepRedColorsRed() {
        for (Color color : colorsToRecolor) {
            Color newColor = recolorer.recolorToFactionColor(color, Recolorer.FactionColor.RED);
            assertThat(newColor, is(color));
        }
    }

}