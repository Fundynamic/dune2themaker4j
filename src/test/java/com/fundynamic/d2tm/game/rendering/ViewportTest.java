package com.fundynamic.d2tm.game.rendering;

import com.fundynamic.d2tm.game.AbstractD2TMTest;
import com.fundynamic.d2tm.game.controls.Mouse;
import com.fundynamic.d2tm.math.Coordinate;
import org.junit.Test;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class ViewportTest extends AbstractD2TMTest {

    @Test
    public void renderSmokeTest() throws SlickException {
        Mouse mouse = Mouse.create(player, gameContainer, entityRepository, imageRepository);
        Image bufferWithGraphics = mock(Image.class);
        Graphics bufferGraphics = mock(Graphics.class);
        when(bufferWithGraphics.getGraphics()).thenReturn(bufferGraphics);

        makeUnit(player, Coordinate.create(2, 2));
        map.revealAllShroudFor(player);

        Viewport viewport = new Viewport(map, mouse, player, bufferWithGraphics);

        // make sure we draw debug stuff too
        viewport.toggleDrawDebugMode();

        viewport.render(graphics);
    }

}