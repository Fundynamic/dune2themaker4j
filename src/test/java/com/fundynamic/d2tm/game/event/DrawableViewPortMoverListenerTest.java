package com.fundynamic.d2tm.game.event;

import com.fundynamic.d2tm.Game;
import com.fundynamic.d2tm.game.Viewport;
import com.fundynamic.d2tm.game.drawing.DrawableViewPort;
import com.fundynamic.d2tm.game.math.Vector2D;
import junit.framework.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import static com.fundynamic.d2tm.Game.SCREEN_WIDTH;
import static org.mockito.Mockito.mock;

public class DrawableViewPortMoverListenerTest {

    public static final float MOVE_SPEED = 1.0F;
    public static final float SCROLL_SPEED = 2.0F;

    @Test
    public void mouseHittingLeftBorderOfScreenMovesViewportToLeft() throws SlickException {
        Viewport viewport = mock(Viewport.class);
        DrawableViewPort drawableViewPort = new DrawableViewPort(viewport, Vector2D.zero(), Vector2D.zero(), mock(Graphics.class), MOVE_SPEED);

        DrawableViewPortMoverListener listener = new DrawableViewPortMoverListener(drawableViewPort, SCROLL_SPEED);

        listener.mouseMoved(3, 100, 2, 100); // moved 2 pixels close to the left of the screen

        drawableViewPort.update();
        drawableViewPort.render();

        ArgumentCaptor<Vector2D> captor = ArgumentCaptor.forClass(Vector2D.class);
        Mockito.verify(viewport).draw(Mockito.<Graphics>anyObject(), Mockito.<Vector2D<Integer>>anyObject(), captor.capture());

        Vector2D<Float> viewportVector = captor.getValue();
        Assert.assertEquals(-MOVE_SPEED * SCROLL_SPEED, viewportVector.getX(), 0.0001F);
    }

    @Test
    public void mouseHittingRightBorderOfScreenMovesViewportToRight() throws SlickException {
        Viewport viewport = mock(Viewport.class);
        DrawableViewPort drawableViewPort = new DrawableViewPort(viewport, Vector2D.zero(), Vector2D.zero(), mock(Graphics.class), MOVE_SPEED);

        DrawableViewPortMoverListener listener = new DrawableViewPortMoverListener(drawableViewPort, SCROLL_SPEED);

        listener.mouseMoved(SCREEN_WIDTH - 3, 100, SCREEN_WIDTH - 2, 100); // moved 2 pixels close to the right of the screen

        drawableViewPort.update();
        drawableViewPort.render();

        ArgumentCaptor<Vector2D> captor = ArgumentCaptor.forClass(Vector2D.class);
        Mockito.verify(viewport).draw(Mockito.<Graphics>anyObject(), Mockito.<Vector2D<Integer>>anyObject(), captor.capture());

        Vector2D<Float> viewportVector = captor.getValue();
        Assert.assertEquals(MOVE_SPEED * SCROLL_SPEED, viewportVector.getX(), 0.0001F);
    }

}