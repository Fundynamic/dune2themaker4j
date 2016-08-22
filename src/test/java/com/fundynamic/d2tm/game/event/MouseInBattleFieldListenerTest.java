package com.fundynamic.d2tm.game.event;

import com.fundynamic.d2tm.game.AbstractD2TMTest;
import com.fundynamic.d2tm.game.controls.Mouse;
import com.fundynamic.d2tm.game.rendering.gui.battlefield.BattleField;
import com.fundynamic.d2tm.math.Vector2D;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import static com.fundynamic.d2tm.game.AssertHelper.assertFloatEquals;
import static org.mockito.Mockito.*;

public class MouseInBattleFieldListenerTest extends AbstractD2TMTest {
    public static final float MOVE_SPEED = 2.0F;

    public static final int ANY_COORDINATE_NOT_NEAR_BORDER = 100;
    public static final float INITIAL_VIEWPORT_X = 34F;
    public static final float INITIAL_VIEWPORT_Y = 34F;
    public static final float ONE_FRAME_PER_SECOND_DELTA = 1f;

    public static int HEIGHT_OF_MAP = 40;
    public static int WIDTH_OF_MAP = 46;

    private BattleField battleField;
    private MouseListener listener;

    private Vector2D screenResolution = Vector2D.create(800, 600);

    @Before
    public void setUp() throws SlickException {
        super.setUp();
        map = makeMap(WIDTH_OF_MAP, HEIGHT_OF_MAP);

        battleField = makeDrawableBattleField(INITIAL_VIEWPORT_X, INITIAL_VIEWPORT_Y, MOVE_SPEED);

        listener = new MouseListener(mouse);
    }

    private BattleField makeDrawableBattleField(float viewportX, float viewportY, float moveSpeed) throws SlickException {
        return new BattleField(
                screenResolution,
                Vector2D.zero(),
                Vector2D.create(viewportX, viewportY),
                map,
                mouse,
                moveSpeed,
                TILE_SIZE,
                player,
                mock(Image.class),
                entityRepository
        );
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
        Vector2D tickOneViewportVector = battleField.getViewingVector();

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
        Vector2D tickOneViewportVector = battleField.getViewingVector();

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

        float maxYViewportPosition = ((HEIGHT_OF_MAP * TILE_SIZE) - TILE_SIZE)- screenResolution.getY();

        battleField = makeDrawableBattleField(viewportX, viewportY, moveSpeed);
        listener = new MouseListener(mouse);

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

        float maxXViewportPosition = ((WIDTH_OF_MAP * TILE_SIZE) - TILE_SIZE) - screenResolution.getX();

        battleField = makeDrawableBattleField(viewportX, viewportY, moveSpeed);
        listener = new MouseListener(mouse);

        listener.mouseMoved(screenResolution.getXAsInt(), ANY_COORDINATE_NOT_NEAR_BORDER, screenResolution.getXAsInt(), ANY_COORDINATE_NOT_NEAR_BORDER); // move right
        updateAndRender();

        Vector2D viewportVector = getLastCalledViewport();
        assertFloatEquals("X position moved over the right", maxXViewportPosition, viewportVector.getX());
    }

    @Test
    public void whenLeftMouseButtonClickedOnceExecuteLogicInMouseClass() {
        Mouse mouse = mock(Mouse.class);
        listener = new MouseListener(mouse);

        int clickCount = 1;

        listener.mouseClicked(Input.MOUSE_LEFT_BUTTON, 0, 0, clickCount);

        verify(mouse).leftClicked();
    }

    @Test
    public void whenRightMouseButtonClickedOnceExecuteLogicInMouseClass() {
        Mouse mouse = mock(Mouse.class);
        listener = new MouseListener(mouse);

        int clickCount = 1;

        listener.mouseClicked(Input.MOUSE_RIGHT_BUTTON, 0, 0, clickCount);

        verify(mouse).rightClicked();
    }

    @Test
    public void whenLeftMouseButtonIsReleasedPropagateToMouseClass() {
        Mouse mouse = mock(Mouse.class);
        listener = new MouseListener(mouse);

        listener.mouseReleased(Input.MOUSE_LEFT_BUTTON, 0, 0);

        verify(mouse).leftButtonReleased();
    }

    @Test
    public void propagateDragging() {
        Mouse mouse = mock(Mouse.class);
        listener = new MouseListener(mouse);

        int oldX = 0;
        int oldY = 0;
        int newX = 1;
        int newY = 1;

        listener.mouseDragged(oldX, oldY, newX, newY);

        verify(mouse).draggedToCoordinates(Vector2D.create(newX, newY));
    }

    @Test
    public void whenRightMouseButtonIsReleasedNothingHappens() {
        Mouse mouse = mock(Mouse.class);
        listener = new MouseListener(mouse);

        listener.mouseReleased(Input.MOUSE_RIGHT_BUTTON, 0, 0);

        verifyZeroInteractions(mouse);
    }

    @Test
    public void whenLeftMouseButtonClickedTwiceNothingShouldHappen() {
        int clickCount = 2;
        listener.mouseClicked(Input.MOUSE_LEFT_BUTTON, 0, 0, clickCount);
    }

    @Test
    public void whenRightMouseButtonClickedTwiceNothingShouldHappen() {
        int clickCount = 2;
        listener.mouseClicked(Input.MOUSE_RIGHT_BUTTON, 0, 0, clickCount);
    }

    private Vector2D updateAndRenderAndReturnNewViewportVector() throws SlickException {
        updateAndRender();
        return getLastCalledViewport();
    }

    private void updateAndRender() throws SlickException {
        battleField.update(ONE_FRAME_PER_SECOND_DELTA);
        battleField.render(graphics);
    }

    private Vector2D getLastCalledViewport() throws SlickException {
        return battleField.getViewingVector();
    }

}