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

import static org.mockito.Mockito.*;

public class MouseInBattleFieldListenerTest extends AbstractD2TMTest {

    public static final float INITIAL_VIEWPORT_X = 34F;
    public static final float INITIAL_VIEWPORT_Y = 34F;

    public static int HEIGHT_OF_MAP = 40;
    public static int WIDTH_OF_MAP = 46;

    private BattleField battleField;
    private MouseListener listener;

    private Vector2D screenResolution = Vector2D.create(800, 600);

    @Before
    public void setUp() throws SlickException {
        super.setUp();
        map = makeMap(WIDTH_OF_MAP, HEIGHT_OF_MAP);

        battleField = makeDrawableBattleField(INITIAL_VIEWPORT_X, INITIAL_VIEWPORT_Y, battleFieldMoveSpeed);

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