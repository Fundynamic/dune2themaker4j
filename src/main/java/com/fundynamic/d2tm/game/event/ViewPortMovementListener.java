package com.fundynamic.d2tm.game.event;


import com.fundynamic.d2tm.game.drawing.ViewPort;
import com.fundynamic.d2tm.game.math.Vector2D;

public class ViewPortMovementListener extends AbstractMouseListener {

    public static final int PIXELS_NEAR_BORDER = 2;

    private final ViewPort viewPort;
    private final Vector2D<Integer> screenResolution;

    public ViewPortMovementListener(ViewPort viewPort, Vector2D<Integer> screenResolution) {
        this.screenResolution = screenResolution;
        this.viewPort = viewPort;
    }

    @Override
    public void mouseWheelMoved(int change) {

    }

    @Override
    public void mouseClicked(int button, int x, int y, int clickCount) {

    }

    @Override
    public void mousePressed(int button, int x, int y) {

    }

    @Override
    public void mouseReleased(int button, int x, int y) {

    }

    @Override
    public void mouseMoved(int oldx, int oldy, int newx, int newy) {
        if (newx <= PIXELS_NEAR_BORDER) {
            viewPort.moveLeft();
        } else if (newx >= screenResolution.getX() - PIXELS_NEAR_BORDER) {
            viewPort.moveRight();
        } else {
            viewPort.stopMovingHorizontally();
        }

        if (newy <= PIXELS_NEAR_BORDER) {
            viewPort.moveUp();
        } else if (newy >= screenResolution.getY() - PIXELS_NEAR_BORDER) {
            viewPort.moveDown();
        } else {
            viewPort.stopMovingVertically();
        }
    }

    @Override
    public void mouseDragged(int oldx, int oldy, int newx, int newy) {

    }
}
