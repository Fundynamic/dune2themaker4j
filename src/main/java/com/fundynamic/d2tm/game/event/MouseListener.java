package com.fundynamic.d2tm.game.event;


import com.fundynamic.d2tm.game.controls.Mouse;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.Input;
import org.newdawn.slick.command.MouseButtonControl;

/**
 * Listens to (Slick) mouse events and passes them to the {@link Mouse} class.
 */
public class MouseListener extends AbstractMouseListener {

    public static final int AMOUNT_OF_BUTTONS = 2; // 0 based index for amount of buttons (3 buttons is 2...)

    private static final int MOUSE_BUTTON_LEFT = 0;
    private static final int MOUSE_BUTTON_RIGHT = 1;

    private final Mouse mouse;
    private Vector2D mouseScreenPosition;
    private Vector2D[] mouseStartDragPositions = new Vector2D[AMOUNT_OF_BUTTONS];

    private int buttonPressedNow = -1;

    public MouseListener(Mouse mouse) {
        this.mouse = mouse;
        this.mouseScreenPosition = Vector2D.zero();
    }

    @Override
    public void mouseWheelMoved(int change) {
        // DO NOTHING
    }

    @Override
    public void mouseClicked(int button, int x, int y, int clickCount) {
        mouseStartDragPositions[button] = null;

        if (clickCount == 1) {
            if (button == Input.MOUSE_LEFT_BUTTON) {
                mouse.leftClicked();
            } else if (button == Input.MOUSE_RIGHT_BUTTON) {
                mouse.rightClicked();
            }
        }
    }

    @Override
    public void mousePressed(int button, int x, int y) {
        buttonPressedNow = button;
        mouseStartDragPositions[button] = Vector2D.create(x, y);
    }

    @Override
    public void mouseReleased(int button, int x, int y) {
        buttonPressedNow = -1;
        if (mouseStartDragPositions[button] != null) {
            Vector2D delta = mouseStartDragPositions[button].min(mouseScreenPosition);
            System.out.println("Delta is " + delta);
            if (Math.abs(delta.getYAsInt()) < 12 && Math.abs(delta.getXAsInt()) < 12) {
                mouseClicked(button, x, y, 1);
                return;
            }
        }

        if (button == Input.MOUSE_LEFT_BUTTON) {
            mouse.leftButtonReleased();
        }
    }

    @Override
    public void mouseMoved(int oldx, int oldy, int newx, int newy) {
        // use this for fixing the SidebarTest
//        System.out.println("" + newx + "," + newy);
        mouse.movedTo(mouseScreenPosition.update(newx,  newy));
    }

    @Override
    public void mouseDragged(int oldx, int oldy, int newx, int newy) {
        if (buttonPressedNow == MOUSE_BUTTON_RIGHT) return; // ignore right button presses for dragging

        mouse.draggedToCoordinates(mouseScreenPosition.update(newx,  newy));
    }

}
