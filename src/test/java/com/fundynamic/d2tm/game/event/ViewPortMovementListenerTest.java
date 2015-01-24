package com.fundynamic.d2tm.game.event;

import com.fundynamic.d2tm.game.drawing.ViewPort;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.math.Vector2D;
import com.fundynamic.d2tm.graphics.Tile;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class ViewPortMovementListenerTest {
    public static final float MOVE_SPEED = 2.0F;

    public static final int ANY_COORDINATE_NOT_NEAR_BORDER = 100;
    public static final float INITIAL_VIEWPORT_X = 4F;
    public static final float INITIAL_VIEWPORT_Y = 4F;

    public static int HEIGHT_OF_MAP = 20;
    public static int WIDTH_OF_MAP = 26;

    private ViewPort viewPort;
    private ViewPortMovementListener listener;

    private Vector2D<Integer> screenResolution;
    private Map map;

    @Before
    public void setUp() throws SlickException {
        map = new Map(null, WIDTH_OF_MAP, HEIGHT_OF_MAP);
        screenResolution = new Vector2D<>(800, 600);

        viewPort = makeDrawableViewPort(INITIAL_VIEWPORT_X, INITIAL_VIEWPORT_Y, MOVE_SPEED);
        listener = new ViewPortMovementListener(viewPort, screenResolution);
    }

    private ViewPort makeDrawableViewPort(float viewportX, float viewportY, float moveSpeed) throws SlickException {
        return new ViewPort(screenResolution, Vector2D.zero(), new Vector2D<>(viewportX, viewportY), mock(Graphics.class), map, moveSpeed) {
            // ugly seam in the code, but I'd rather do this than create a Spy
            @Override
            protected Image constructImage(Vector2D<Integer> screenResolution) throws SlickException {
                return Mockito.mock(Image.class);
            }
        };
    }

    @Test
    public void mouseHittingLeftBorderOfScreenMovesViewportToLeft() throws SlickException {
        // moved to 2 pixels close to the left of the screen
        listener.mouseMoved(3, ANY_COORDINATE_NOT_NEAR_BORDER, 2, ANY_COORDINATE_NOT_NEAR_BORDER);

        Vector2D<Float> viewportVector = updateAndRenderAndReturnNewViewportVector();

        Assert.assertEquals(INITIAL_VIEWPORT_X - MOVE_SPEED, viewportVector.getX(), 0.0001F);
        Assert.assertEquals(INITIAL_VIEWPORT_Y, viewportVector.getY(), 0.0001F);
    }

    @Test
    public void mouseHittingRightBorderOfScreenMovesViewportToRight() throws SlickException {
        // moved to 2 pixels close to the right of the screen
        listener.mouseMoved(screenResolution.getX() - 3, ANY_COORDINATE_NOT_NEAR_BORDER, screenResolution.getX() - 2, ANY_COORDINATE_NOT_NEAR_BORDER);

        Vector2D<Float> viewportVector = updateAndRenderAndReturnNewViewportVector();

        Assert.assertEquals(INITIAL_VIEWPORT_X + MOVE_SPEED, viewportVector.getX(), 0.0001F);
        Assert.assertEquals(INITIAL_VIEWPORT_Y, viewportVector.getY(), 0.0001F);
    }

    @Test
    public void mouseHittingTopOfScreenMovesViewportUp() throws SlickException {
        // moved to 2 pixels close to the top of the screen
        listener.mouseMoved(ANY_COORDINATE_NOT_NEAR_BORDER, 3, ANY_COORDINATE_NOT_NEAR_BORDER, 2);

        Vector2D<Float> viewportVector = updateAndRenderAndReturnNewViewportVector();

        Assert.assertEquals(INITIAL_VIEWPORT_X, viewportVector.getX(), 0.0001F);
        Assert.assertEquals(INITIAL_VIEWPORT_Y - MOVE_SPEED, viewportVector.getY(), 0.0001F);
    }

    @Test
    public void mouseHittingBottomOfScreenMovesViewportDown() throws SlickException {
        // moved to 2 pixels close to the bottom of the screen
        listener.mouseMoved(ANY_COORDINATE_NOT_NEAR_BORDER, screenResolution.getY() - 3, ANY_COORDINATE_NOT_NEAR_BORDER, screenResolution.getY() - 2);

        Vector2D<Float> viewportVector = updateAndRenderAndReturnNewViewportVector();

        Assert.assertEquals(INITIAL_VIEWPORT_X, viewportVector.getX(), 0.0001F);
        Assert.assertEquals(INITIAL_VIEWPORT_Y + MOVE_SPEED, viewportVector.getY(), 0.0001F);
    }

    @Test
    public void mouseFromTopLeftToNoBorderStopsMoving() throws SlickException {
        // this updates viewport coordinates one move up and to the left
        listener.mouseMoved(ANY_COORDINATE_NOT_NEAR_BORDER, ANY_COORDINATE_NOT_NEAR_BORDER, 0, 0);
        updateAndRender(); // and move the viewport
        Vector2D<Float> tickOneViewportVector = viewPort.getViewingVector();

        // move back to the middle
        listener.mouseMoved(0, 0, ANY_COORDINATE_NOT_NEAR_BORDER, ANY_COORDINATE_NOT_NEAR_BORDER);
        updateAndRender(); // and stop moving

        // Check that the viewport stopped moving
        Vector2D<Float> viewportVector = getLastCalledViewport();
        Assert.assertEquals("Y position moved while expected to have stopped", tickOneViewportVector.getY(), viewportVector.getY(), 0.0001F);
        Assert.assertEquals("X position moved while expected to have stopped", tickOneViewportVector.getX(), viewportVector.getX(), 0.0001F);
    }

    @Test
    public void mouseFromBottomRightToNoBorderStopsMoving() throws SlickException {
        // this updates viewport coordinates one move up and to the left
        listener.mouseMoved(ANY_COORDINATE_NOT_NEAR_BORDER, ANY_COORDINATE_NOT_NEAR_BORDER, screenResolution.getX(), screenResolution.getY());
        updateAndRender(); // and move the viewport
        Vector2D<Float> tickOneViewportVector = viewPort.getViewingVector();

        // move back to the middle
        listener.mouseMoved(screenResolution.getX(), screenResolution.getY(), ANY_COORDINATE_NOT_NEAR_BORDER, ANY_COORDINATE_NOT_NEAR_BORDER);
        updateAndRender(); // and stop moving

        // Check that the viewport stopped moving
        Vector2D<Float> viewportVector = getLastCalledViewport();
        Assert.assertEquals("Y position moved while expected to have stopped", tickOneViewportVector.getY(), viewportVector.getY(), 0.0001F);
        Assert.assertEquals("X position moved while expected to have stopped", tickOneViewportVector.getX(), viewportVector.getX(), 0.0001F);
    }

    @Test
    public void stopsMovingLeftWhenAtTheLeftEdge() throws SlickException {
        listener.mouseMoved(0, ANY_COORDINATE_NOT_NEAR_BORDER, 0, ANY_COORDINATE_NOT_NEAR_BORDER); // move left
        updateAndRender();
        updateAndRender();
        updateAndRender();

        Vector2D<Float> viewportVector = getLastCalledViewport();
        Assert.assertEquals("X position moved over the edge", 0F, viewportVector.getX(), 0.0001F);
    }

    @Test
    public void stopsMovingUpWhenAtTheUpperEdge() throws SlickException {
        listener.mouseMoved(ANY_COORDINATE_NOT_NEAR_BORDER, 0, ANY_COORDINATE_NOT_NEAR_BORDER, 0); // move up
        updateAndRender();
        updateAndRender();
        updateAndRender();

        Vector2D<Float> viewportVector = getLastCalledViewport();
        Assert.assertEquals("Y position moved over the edge", 0F, viewportVector.getY(), 0.0001F);
    }

    @Test
    public void stopsMovingDownWhenAtTheBottomEdge() throws SlickException {
        int viewportX = 0;
        int viewportY = 0;
        float moveSpeed = 16F;

        float maxYViewportPosition = (HEIGHT_OF_MAP * Tile.HEIGHT) - screenResolution.getY();

        viewPort = makeDrawableViewPort(viewportX, viewportY, moveSpeed);
        listener = new ViewPortMovementListener(viewPort, screenResolution);

        listener.mouseMoved(ANY_COORDINATE_NOT_NEAR_BORDER, screenResolution.getY(), ANY_COORDINATE_NOT_NEAR_BORDER, screenResolution.getY()); // move down
        updateAndRender();
        updateAndRender();
        updateAndRender();

        Vector2D<Float> viewportVector = getLastCalledViewport();
        Assert.assertEquals("Y position moved over the bottom", maxYViewportPosition, viewportVector.getY(), 0.0001F);
    }

    @Test
    public void stopsMovingRightWhenAtTheRightEdge() throws SlickException {
        int viewportX = 0;
        int viewportY = 0;
        float moveSpeed = 32F;

        float maxXViewportPosition = (WIDTH_OF_MAP * Tile.WIDTH) - screenResolution.getX();

        viewPort = makeDrawableViewPort(viewportX, viewportY, moveSpeed);
        listener = new ViewPortMovementListener(viewPort, screenResolution);

        listener.mouseMoved(screenResolution.getX(), ANY_COORDINATE_NOT_NEAR_BORDER, screenResolution.getX(), ANY_COORDINATE_NOT_NEAR_BORDER); // move right
        updateAndRender();

        Vector2D<Float> viewportVector = getLastCalledViewport();
        Assert.assertEquals("X position moved over the right", maxXViewportPosition, viewportVector.getX(), 0.0001F);
    }

    private Vector2D<Float> updateAndRenderAndReturnNewViewportVector() throws SlickException {
        updateAndRender();
        return getLastCalledViewport();
    }

    private void updateAndRender() throws SlickException {
        viewPort.update();
        viewPort.render();
    }

    private Vector2D<Float> getLastCalledViewport() throws SlickException {
        return viewPort.getViewingVector();
    }

}