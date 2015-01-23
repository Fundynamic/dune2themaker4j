package com.fundynamic.d2tm.game.event;

import com.fundynamic.d2tm.game.Viewport;
import com.fundynamic.d2tm.game.drawing.DrawableViewPort;
import com.fundynamic.d2tm.game.math.Vector2D;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import static com.fundynamic.d2tm.Game.SCREEN_HEIGHT;
import static com.fundynamic.d2tm.Game.SCREEN_WIDTH;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class DrawableViewPortMoverListenerTest {

    public static final float MOVE_SPEED = 1.0F;
    public static final float SCROLL_SPEED = 2.0F;

    public static final int ANY_COORDINATE_NOT_NEAR_BORDER = 100;
    public static final float INITIAL_VIEWPORT_X = 4F;
    public static final float INITIAL_VIEWPORT_Y = 4F;

    @Mock
    private Viewport viewport;

    private DrawableViewPort drawableViewPort;
    private DrawableViewPortMoverListener listener;

    private int renderAndUpdatedCalled;

    @Before
    public void setUp() {
        drawableViewPort = new DrawableViewPort(viewport, Vector2D.zero(), new Vector2D<>(INITIAL_VIEWPORT_X, INITIAL_VIEWPORT_Y), mock(Graphics.class), MOVE_SPEED);
        listener = new DrawableViewPortMoverListener(drawableViewPort, SCROLL_SPEED);
        renderAndUpdatedCalled = 0;
    }

    @Test
    public void mouseHittingLeftBorderOfScreenMovesViewportToLeft() throws SlickException {
        // moved to 2 pixels close to the left of the screen
        listener.mouseMoved(3, ANY_COORDINATE_NOT_NEAR_BORDER, 2, ANY_COORDINATE_NOT_NEAR_BORDER);

        Vector2D<Float> viewportVector = updateAndRenderAndReturnNewViewportVector();

        Assert.assertEquals((INITIAL_VIEWPORT_X - (MOVE_SPEED * SCROLL_SPEED)), viewportVector.getX(), 0.0001F);
        Assert.assertEquals(INITIAL_VIEWPORT_Y, viewportVector.getY(), 0.0001F);
    }

    @Test
    public void mouseHittingRightBorderOfScreenMovesViewportToRight() throws SlickException {
        // moved to 2 pixels close to the right of the screen
        listener.mouseMoved(SCREEN_WIDTH - 3, ANY_COORDINATE_NOT_NEAR_BORDER, SCREEN_WIDTH - 2, ANY_COORDINATE_NOT_NEAR_BORDER);

        Vector2D<Float> viewportVector = updateAndRenderAndReturnNewViewportVector();

        Assert.assertEquals((INITIAL_VIEWPORT_X + (MOVE_SPEED * SCROLL_SPEED)), viewportVector.getX(), 0.0001F);
        Assert.assertEquals(INITIAL_VIEWPORT_Y, viewportVector.getY(), 0.0001F);
    }

    @Test
    public void mouseHittingTopOfScreenMovesViewportUp() throws SlickException {
        // moved to 2 pixels close to the top of the screen
        listener.mouseMoved(ANY_COORDINATE_NOT_NEAR_BORDER, 3, ANY_COORDINATE_NOT_NEAR_BORDER, 2);

        Vector2D<Float> viewportVector = updateAndRenderAndReturnNewViewportVector();

        Assert.assertEquals(INITIAL_VIEWPORT_X, viewportVector.getX(), 0.0001F);
        Assert.assertEquals((INITIAL_VIEWPORT_Y - (MOVE_SPEED * SCROLL_SPEED)), viewportVector.getY(), 0.0001F);
    }

    @Test
    public void mouseHittingBottomOfScreenMovesViewportDown() throws SlickException {
        // moved to 2 pixels close to the bottom of the screen
        listener.mouseMoved(ANY_COORDINATE_NOT_NEAR_BORDER, SCREEN_HEIGHT - 3, ANY_COORDINATE_NOT_NEAR_BORDER, SCREEN_HEIGHT - 2);

        Vector2D<Float> viewportVector = updateAndRenderAndReturnNewViewportVector();

        Assert.assertEquals(INITIAL_VIEWPORT_X, viewportVector.getX(), 0.0001F);
        Assert.assertEquals((INITIAL_VIEWPORT_Y + (MOVE_SPEED * SCROLL_SPEED)), viewportVector.getY(), 0.0001F);
    }

    @Test
    public void mouseFromTopLeftToNoBorderStopsMoving() throws SlickException {
        // this updates viewport coordinates one move up and to the left
        listener.mouseMoved(ANY_COORDINATE_NOT_NEAR_BORDER, ANY_COORDINATE_NOT_NEAR_BORDER, 0, 0);
        renderAndUpdate(); // and move the viewport
        Vector2D<Float> tickOneViewportVector = drawableViewPort.getViewingVector();

        // move back to the middle
        listener.mouseMoved(0, 0, ANY_COORDINATE_NOT_NEAR_BORDER, ANY_COORDINATE_NOT_NEAR_BORDER);
        renderAndUpdate(); // and stop moving

        // Check that the viewport stopped moving
        Vector2D<Float> viewportVector = getLastCalledViewport();
        Assert.assertEquals("Y position moved while expected to have stopped", tickOneViewportVector.getY(), viewportVector.getY(), 0.0001F);
        Assert.assertEquals("X position moved while expected to have stopped", tickOneViewportVector.getX(), viewportVector.getX(), 0.0001F);
    }

    @Test
    public void mouseFromBottomRightToNoBorderStopsMoving() throws SlickException {
        // this updates viewport coordinates one move up and to the left
        listener.mouseMoved(ANY_COORDINATE_NOT_NEAR_BORDER, ANY_COORDINATE_NOT_NEAR_BORDER, SCREEN_WIDTH, SCREEN_HEIGHT);
        renderAndUpdate(); // and move the viewport
        Vector2D<Float> tickOneViewportVector = drawableViewPort.getViewingVector();

        // move back to the middle
        listener.mouseMoved(SCREEN_WIDTH, SCREEN_HEIGHT, ANY_COORDINATE_NOT_NEAR_BORDER, ANY_COORDINATE_NOT_NEAR_BORDER);
        renderAndUpdate(); // and stop moving

        // Check that the viewport stopped moving
        Vector2D<Float> viewportVector = getLastCalledViewport();
        Assert.assertEquals("Y position moved while expected to have stopped", tickOneViewportVector.getY(), viewportVector.getY(), 0.0001F);
        Assert.assertEquals("X position moved while expected to have stopped", tickOneViewportVector.getX(), viewportVector.getX(), 0.0001F);
    }

    // TODO:
    // Leaving border (left/right/top/bottom) will stop moving viewport
    private Vector2D<Float> updateAndRenderAndReturnNewViewportVector() throws SlickException {
        renderAndUpdate();
        return getLastCalledViewport();
    }

    private void renderAndUpdate() throws SlickException {
        drawableViewPort.update();
        drawableViewPort.render();
        renderAndUpdatedCalled++;
    }

    private Vector2D<Float> getLastCalledViewport() throws SlickException {
        Mockito.verify(viewport, times(renderAndUpdatedCalled)).draw(Mockito.<Graphics>anyObject(), Mockito.<Vector2D<Integer>>anyObject(), Mockito.<Vector2D<Float>>any());
        return drawableViewPort.getViewingVector();
    }

}