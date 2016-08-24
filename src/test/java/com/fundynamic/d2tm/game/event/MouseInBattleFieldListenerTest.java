package com.fundynamic.d2tm.game.event;

import com.fundynamic.d2tm.game.AbstractD2TMTest;
import com.fundynamic.d2tm.game.controls.Mouse;
import com.fundynamic.d2tm.math.Vector2D;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import static org.mockito.Mockito.*;

public class MouseInBattleFieldListenerTest extends AbstractD2TMTest {

    private MouseListener listener;

    @Before
    public void setUp() throws SlickException {
        super.setUp();

        listener = new MouseListener(mouse);
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

}