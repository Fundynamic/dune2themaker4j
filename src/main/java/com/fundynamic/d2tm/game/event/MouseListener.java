package com.fundynamic.d2tm.game.event;


import com.fundynamic.d2tm.game.controls.Mouse;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.Input;

/**
 * Listens to mouse events and passes them to our mouse class.
 */
public class MouseListener extends AbstractMouseListener {

    private final Mouse mouse;

    public MouseListener(Mouse mouse) {
        this.mouse = mouse;
    }

    @Override
    public void mouseWheelMoved(int change) {
        // DO NOTHING
    }

    @Override
    public void mouseClicked(int button, int x, int y, int clickCount) {
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
        // no logic yet
    }

    @Override
    public void mouseReleased(int button, int x, int y) {
        if (button == Input.MOUSE_LEFT_BUTTON) {
            mouse.leftButtonReleased();
        }
    }

    @Override
    public void mouseMoved(int oldx, int oldy, int newx, int newy) {
        mouse.movedTo(Vector2D.create(newx, newy));
    }

    @Override
    public void mouseDragged(int oldx, int oldy, int newx, int newy) {
        mouse.draggedToCoordinates(Vector2D.create(newx, newy));
    }

}
