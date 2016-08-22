package com.fundynamic.d2tm.game.rendering;

import com.fundynamic.d2tm.game.AbstractD2TMTest;
import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.math.Coordinate;
import com.fundynamic.d2tm.math.Vector2D;
import org.junit.Assert;
import org.junit.Test;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class BattleFieldTest extends AbstractD2TMTest {

    @Test
    public void renderSmokeTest() throws SlickException {
        Image bufferWithGraphics = mock(Image.class);
        Graphics bufferGraphics = mock(Graphics.class);
        when(bufferWithGraphics.getGraphics()).thenReturn(bufferGraphics);

        makeUnit(player, Coordinate.create(2, 2), "QUAD");
        map.revealAllShroudFor(player);

        battleField.render(graphics);
    }

    @Test
    public void translatesScreenCoordinatesIntoAbsoluteMapCoordinates() {
        Vector2D mouseCoordinates = Vector2D.create(87, 73);
        battleField.movedTo(mouseCoordinates);

        Cell hoverCell = battleField.getHoverCell();
        // Y = (73 - 42) / 32 = 0.96 == 1
        // X = (87 - 0) / 32 = 2,71 = 3
        Assert.assertEquals(3, hoverCell.getX());
        Assert.assertEquals(1, hoverCell.getY());
    }

}