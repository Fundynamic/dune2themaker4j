package com.fundynamic.d2tm.game.event;

import com.fundynamic.d2tm.game.drawing.Viewport;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.math.Vector2D;
import com.fundynamic.d2tm.game.terrain.Terrain;
import com.fundynamic.d2tm.game.terrain.TerrainFactory;
import com.fundynamic.d2tm.graphics.Shroud;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import static com.fundynamic.d2tm.game.AssertHelper.assertFloatEquals;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class ViewportMovementListenerTest {
    public static final float MOVE_SPEED = 2.0F;

    private static final int TILE_WIDTH = 32;
    private static final int TILE_HEIGHT = 32;

    public static final int ANY_COORDINATE_NOT_NEAR_BORDER = 100;
    public static final float INITIAL_VIEWPORT_X = 4F;
    public static final float INITIAL_VIEWPORT_Y = 4F;

    public static int HEIGHT_OF_MAP = 20;
    public static int WIDTH_OF_MAP = 26;

    private Viewport viewport;
    private ViewportMovementListener listener;

    private Vector2D screenResolution;
    private Map map;

    @Before
    public void setUp() throws SlickException {
        TerrainFactory terrainFactory = Mockito.mock(TerrainFactory.class);
        Shroud shroud = Mockito.mock(Shroud.class);
        Mockito.doReturn(Mockito.mock(Terrain.class)).when(terrainFactory).createEmptyTerrain();
        map = new Map(terrainFactory, shroud, WIDTH_OF_MAP, HEIGHT_OF_MAP, TILE_WIDTH, TILE_HEIGHT);
        screenResolution = new Vector2D(800, 600);

        viewport = makeDrawableViewPort(INITIAL_VIEWPORT_X, INITIAL_VIEWPORT_Y, MOVE_SPEED);
        listener = new ViewportMovementListener(viewport, screenResolution);
    }

    private Viewport makeDrawableViewPort(float viewportX, float viewportY, float moveSpeed) throws SlickException {
        return new Viewport(screenResolution, Vector2D.zero(), new Vector2D(viewportX, viewportY), mock(Graphics.class), map, moveSpeed, TILE_WIDTH, TILE_HEIGHT) {
            // ugly seam in the code, but I'd rather do this than create a Spy
            @Override
            protected Image constructImage(Vector2D screenResolution) throws SlickException {
                return Mockito.mock(Image.class);
            }
        };
    }

    @Test
    public void mouseHittingLeftBorderOfScreenMovesViewportToLeft() throws SlickException {
        // moved to 2 pixels close to the left of the screen
        listener.mouseMoved(3, ANY_COORDINATE_NOT_NEAR_BORDER, 2, ANY_COORDINATE_NOT_NEAR_BORDER);

        Vector2D viewportVector = updateAndRenderAndReturnNewViewportVector();

        assertFloatEquals(INITIAL_VIEWPORT_X - MOVE_SPEED, viewportVector.getX());
        assertFloatEquals(INITIAL_VIEWPORT_Y, viewportVector.getY());
    }

    @Test
    public void mouseHittingRightBorderOfScreenMovesViewportToRight() throws SlickException {
        // moved to 2 pixels close to the right of the screen
        listener.mouseMoved(screenResolution.getX() - 3, ANY_COORDINATE_NOT_NEAR_BORDER, screenResolution.getX() - 2, ANY_COORDINATE_NOT_NEAR_BORDER);

        Vector2D viewportVector = updateAndRenderAndReturnNewViewportVector();

        assertFloatEquals(INITIAL_VIEWPORT_X + MOVE_SPEED, viewportVector.getX());
        assertFloatEquals(INITIAL_VIEWPORT_Y, viewportVector.getY());
    }

    @Test
    public void mouseHittingTopOfScreenMovesViewportUp() throws SlickException {
        // moved to 2 pixels close to the top of the screen
        listener.mouseMoved(ANY_COORDINATE_NOT_NEAR_BORDER, 3, ANY_COORDINATE_NOT_NEAR_BORDER, 2);

        Vector2D viewportVector = updateAndRenderAndReturnNewViewportVector();

        assertFloatEquals(INITIAL_VIEWPORT_X, viewportVector.getX());
        assertFloatEquals(INITIAL_VIEWPORT_Y - MOVE_SPEED, viewportVector.getY());
    }

    @Test
    public void mouseHittingBottomOfScreenMovesViewportDown() throws SlickException {
        // moved to 2 pixels close to the bottom of the screen
        listener.mouseMoved(ANY_COORDINATE_NOT_NEAR_BORDER, screenResolution.getY() - 3, ANY_COORDINATE_NOT_NEAR_BORDER, screenResolution.getY() - 2);

        Vector2D viewportVector = updateAndRenderAndReturnNewViewportVector();

        assertFloatEquals(INITIAL_VIEWPORT_X, viewportVector.getX());
        assertFloatEquals(INITIAL_VIEWPORT_Y + MOVE_SPEED, viewportVector.getY());
    }

    @Test
    public void mouseFromTopLeftToNoBorderStopsMoving() throws SlickException {
        // this updates viewport coordinates one move up and to the left
        listener.mouseMoved(ANY_COORDINATE_NOT_NEAR_BORDER, ANY_COORDINATE_NOT_NEAR_BORDER, 0, 0);
        updateAndRender(); // and move the viewport
        Vector2D tickOneViewportVector = viewport.getViewingVector();

        // move back to the middle
        listener.mouseMoved(0, 0, ANY_COORDINATE_NOT_NEAR_BORDER, ANY_COORDINATE_NOT_NEAR_BORDER);
        updateAndRender(); // and stop moving

        // Check that the viewport stopped moving
        Vector2D viewportVector = getLastCalledViewport();
        assertFloatEquals("Y position moved while expected to have stopped", tickOneViewportVector.getY(), viewportVector.getY());
        assertFloatEquals("X position moved while expected to have stopped", tickOneViewportVector.getX(), viewportVector.getX());
    }

    @Test
    public void mouseFromBottomRightToNoBorderStopsMoving() throws SlickException {
        // this updates viewport coordinates one move up and to the left
        listener.mouseMoved(ANY_COORDINATE_NOT_NEAR_BORDER, ANY_COORDINATE_NOT_NEAR_BORDER, screenResolution.getX(), screenResolution.getY());
        updateAndRender(); // and move the viewport
        Vector2D tickOneViewportVector = viewport.getViewingVector();

        // move back to the middle
        listener.mouseMoved(screenResolution.getX(), screenResolution.getY(), ANY_COORDINATE_NOT_NEAR_BORDER, ANY_COORDINATE_NOT_NEAR_BORDER);
        updateAndRender(); // and stop moving

        // Check that the viewport stopped moving
        Vector2D viewportVector = getLastCalledViewport();
        assertFloatEquals("Y position moved while expected to have stopped", tickOneViewportVector.getY(), viewportVector.getY());
        assertFloatEquals("X position moved while expected to have stopped", tickOneViewportVector.getX(), viewportVector.getX());
    }

    @Test
    public void stopsMovingLeftWhenAtTheLeftEdge() throws SlickException {
        listener.mouseMoved(0, ANY_COORDINATE_NOT_NEAR_BORDER, 0, ANY_COORDINATE_NOT_NEAR_BORDER); // move left
        updateAndRender();
        updateAndRender();
        updateAndRender();

        Vector2D viewportVector = getLastCalledViewport();
        assertFloatEquals("X position moved over the edge", 0F, viewportVector.getX());
    }

    @Test
    public void stopsMovingUpWhenAtTheUpperEdge() throws SlickException {
        listener.mouseMoved(ANY_COORDINATE_NOT_NEAR_BORDER, 0, ANY_COORDINATE_NOT_NEAR_BORDER, 0); // move up
        updateAndRender();
        updateAndRender();
        updateAndRender();

        Vector2D viewportVector = getLastCalledViewport();
        assertFloatEquals("Y position moved over the edge", 0F, viewportVector.getY());
    }

    @Test
    public void stopsMovingDownWhenAtTheBottomEdge() throws SlickException {
        int viewportX = 0;
        int viewportY = 0;
        float moveSpeed = 16F;

        float maxYViewportPosition = (HEIGHT_OF_MAP * TILE_HEIGHT) - screenResolution.getY();

        viewport = makeDrawableViewPort(viewportX, viewportY, moveSpeed);
        listener = new ViewportMovementListener(viewport, screenResolution);

        listener.mouseMoved(ANY_COORDINATE_NOT_NEAR_BORDER, screenResolution.getY(), ANY_COORDINATE_NOT_NEAR_BORDER, screenResolution.getY()); // move down
        updateAndRender();
        updateAndRender();
        updateAndRender();

        Vector2D viewportVector = getLastCalledViewport();
        assertFloatEquals("Y position moved over the bottom", maxYViewportPosition, viewportVector.getY());
    }

    @Test
    public void stopsMovingRightWhenAtTheRightEdge() throws SlickException {
        int viewportX = 0;
        int viewportY = 0;
        float moveSpeed = 32F;

        float maxXViewportPosition = (WIDTH_OF_MAP * TILE_WIDTH) - screenResolution.getX();

        viewport = makeDrawableViewPort(viewportX, viewportY, moveSpeed);
        listener = new ViewportMovementListener(viewport, screenResolution);

        listener.mouseMoved(screenResolution.getX(), ANY_COORDINATE_NOT_NEAR_BORDER, screenResolution.getX(), ANY_COORDINATE_NOT_NEAR_BORDER); // move right
        updateAndRender();

        Vector2D viewportVector = getLastCalledViewport();
        assertFloatEquals("X position moved over the right", maxXViewportPosition, viewportVector.getX());
    }

    private Vector2D updateAndRenderAndReturnNewViewportVector() throws SlickException {
        updateAndRender();
        return getLastCalledViewport();
    }

    private void updateAndRender() throws SlickException {
        viewport.update();
        viewport.render();
    }

    private Vector2D getLastCalledViewport() throws SlickException {
        return viewport.getViewingVector();
    }

}