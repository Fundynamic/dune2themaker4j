package com.fundynamic.d2tm.game.event;

import com.fundynamic.d2tm.game.controls.Mouse;
import com.fundynamic.d2tm.game.rendering.Viewport;
import com.fundynamic.d2tm.game.entities.structures.Structure;
import com.fundynamic.d2tm.game.entities.structures.StructureRepository;
import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.terrain.Terrain;
import com.fundynamic.d2tm.game.terrain.TerrainFactory;
import com.fundynamic.d2tm.graphics.Shroud;
import com.fundynamic.d2tm.math.Vector2D;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import static com.fundynamic.d2tm.game.AssertHelper.assertFloatEquals;
import static com.fundynamic.d2tm.game.map.CellFactory.makeCell;
import static junit.framework.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class ViewportMovementListenerTest {
    public static final float MOVE_SPEED = 2.0F;

    private static final int TILE_WIDTH = 32;
    private static final int TILE_HEIGHT = 32;

    public static final int ANY_COORDINATE_NOT_NEAR_BORDER = 100;
    public static final float INITIAL_VIEWPORT_X = 34F;
    public static final float INITIAL_VIEWPORT_Y = 34F;
    public static final float ONE_FRAME_PER_SECOND_DELTA = 1f;

    public static int HEIGHT_OF_MAP = 40;
    public static int WIDTH_OF_MAP = 46;

    private Viewport viewport;
    private ViewportMovementListener listener;

    private Vector2D screenResolution;
    private Map map;

    @Mock
    private StructureRepository structureRepository;

    private Mouse mouse;

    @Before
    public void setUp() throws SlickException {
        TerrainFactory terrainFactory = Mockito.mock(TerrainFactory.class);
        Shroud shroud = Mockito.mock(Shroud.class);
        Mockito.doReturn(Mockito.mock(Terrain.class)).when(terrainFactory).createEmptyTerrain();
        map = new Map(terrainFactory, shroud, WIDTH_OF_MAP, HEIGHT_OF_MAP);
        screenResolution = new Vector2D(800, 600);
        this.mouse = new Mouse();

        viewport = makeDrawableViewPort(INITIAL_VIEWPORT_X, INITIAL_VIEWPORT_Y, MOVE_SPEED);
        listener = new ViewportMovementListener(viewport, mouse, structureRepository);
    }

    private Viewport makeDrawableViewPort(float viewportX, float viewportY, float moveSpeed) throws SlickException {
        return new Viewport(screenResolution, Vector2D.zero(), Vector2D.create(viewportX, viewportY), map, moveSpeed, TILE_WIDTH, TILE_HEIGHT, mouse) {
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
        listener.mouseMoved(screenResolution.getXAsInt() - 3, ANY_COORDINATE_NOT_NEAR_BORDER, screenResolution.getXAsInt() - 2, ANY_COORDINATE_NOT_NEAR_BORDER);

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
        listener.mouseMoved(ANY_COORDINATE_NOT_NEAR_BORDER, screenResolution.getYAsInt() - 3, ANY_COORDINATE_NOT_NEAR_BORDER, screenResolution.getYAsInt() - 2);

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
        listener.mouseMoved(ANY_COORDINATE_NOT_NEAR_BORDER, ANY_COORDINATE_NOT_NEAR_BORDER, screenResolution.getXAsInt(), screenResolution.getYAsInt());
        updateAndRender(); // and move the viewport
        Vector2D tickOneViewportVector = viewport.getViewingVector();

        // move back to the middle
        listener.mouseMoved(screenResolution.getXAsInt(), screenResolution.getYAsInt(), ANY_COORDINATE_NOT_NEAR_BORDER, ANY_COORDINATE_NOT_NEAR_BORDER);
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
        assertFloatEquals("X position moved over the edge", 32F, viewportVector.getX());
    }

    @Test
    public void stopsMovingUpWhenAtTheUpperEdge() throws SlickException {
        listener.mouseMoved(ANY_COORDINATE_NOT_NEAR_BORDER, 0, ANY_COORDINATE_NOT_NEAR_BORDER, 0); // move up
        updateAndRender();
        updateAndRender();
        updateAndRender();

        Vector2D viewportVector = getLastCalledViewport();
        assertFloatEquals("Y position moved over the edge", 32F, viewportVector.getY());
    }

    @Test
    public void stopsMovingDownWhenAtTheBottomEdge() throws SlickException {
        int viewportX = 0;
        int viewportY = 608;
        float moveSpeed = 16F;

        float maxYViewportPosition = ((HEIGHT_OF_MAP * TILE_HEIGHT) - TILE_HEIGHT)- screenResolution.getY();

        viewport = makeDrawableViewPort(viewportX, viewportY, moveSpeed);
        listener = new ViewportMovementListener(viewport, mouse, structureRepository);

        listener.mouseMoved(ANY_COORDINATE_NOT_NEAR_BORDER, screenResolution.getYAsInt(), ANY_COORDINATE_NOT_NEAR_BORDER, screenResolution.getYAsInt()); // move down
        updateAndRender();
        updateAndRender();
        updateAndRender();

        Vector2D viewportVector = getLastCalledViewport();
        assertFloatEquals("Y position moved over the bottom", maxYViewportPosition, viewportVector.getY());
    }

    @Test
    public void stopsMovingRightWhenAtTheRightEdge() throws SlickException {
        int viewportX = 640;
        int viewportY = 0;
        float moveSpeed = 32F;

        float maxXViewportPosition = ((WIDTH_OF_MAP * TILE_WIDTH) - TILE_WIDTH) - screenResolution.getX();

        viewport = makeDrawableViewPort(viewportX, viewportY, moveSpeed);
        listener = new ViewportMovementListener(viewport, mouse, structureRepository);

        listener.mouseMoved(screenResolution.getXAsInt(), ANY_COORDINATE_NOT_NEAR_BORDER, screenResolution.getXAsInt(), ANY_COORDINATE_NOT_NEAR_BORDER); // move right
        updateAndRender();

        Vector2D viewportVector = getLastCalledViewport();
        assertFloatEquals("X position moved over the right", maxXViewportPosition, viewportVector.getX());
    }

    @Test
    public void leftMouseButtonSelectsStructureWhenHoveredOverCellWithStructure() {
        int NOT_APPLICABLE = -1;
        Cell cell = makeCell();
        Structure structure = new Structure(Vector2D.zero(), Mockito.mock(Image.class), 64, 64);
        cell.setEntity(structure);
        mouse.setHoverCell(cell);

        listener.mouseClicked(Input.MOUSE_LEFT_BUTTON, NOT_APPLICABLE, NOT_APPLICABLE, 1);

        assertSame(structure, mouse.getLastSelectedEntity());
    }

    @Test
    public void rightMouseButtonDeSelectsStructure() {
        int NOT_APPLICABLE = -1;
        Cell cell = makeCell();
        Structure structure = new Structure(Vector2D.zero(), Mockito.mock(Image.class), 64, 64);
        cell.setEntity(structure);
        mouse.setHoverCell(cell);
        mouse.selectEntity();

        listener.mouseClicked(Input.MOUSE_RIGHT_BUTTON, NOT_APPLICABLE, NOT_APPLICABLE, 1);

        assertEquals(null, mouse.getLastSelectedEntity());
    }

    private Vector2D updateAndRenderAndReturnNewViewportVector() throws SlickException {
        updateAndRender();
        return getLastCalledViewport();
    }

    private void updateAndRender() throws SlickException {
        viewport.update(ONE_FRAME_PER_SECOND_DELTA);
        viewport.render(mock(Graphics.class));
    }

    private Vector2D getLastCalledViewport() throws SlickException {
        return viewport.getViewingVector();
    }

}