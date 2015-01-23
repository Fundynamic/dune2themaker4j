package com.fundynamic.d2tm.game.event;


import com.fundynamic.d2tm.Game;
import com.fundynamic.d2tm.game.drawing.DrawableViewPort;

public class DrawableViewPortMoverListener extends AbstractMouseListener {

    public static final int PIXELS_NEAR_BORDER = 2;
    private final DrawableViewPort drawableViewPort;

    public DrawableViewPortMoverListener(DrawableViewPort drawableViewPort) {
        this.drawableViewPort = drawableViewPort;
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
            drawableViewPort.moveLeft();
        } else if (newx >= Game.SCREEN_WIDTH - PIXELS_NEAR_BORDER) {
            drawableViewPort.moveRight();
        } else {
            drawableViewPort.stopMovingHorizontally();
        }

        if (newy <= PIXELS_NEAR_BORDER) {
            drawableViewPort.moveUp();
        } else if (newy >= Game.SCREEN_HEIGHT - PIXELS_NEAR_BORDER) {
            drawableViewPort.moveDown();
        } else {
            drawableViewPort.stopMovingVertically();
        }
    }

    @Override
    public void mouseDragged(int oldx, int oldy, int newx, int newy) {

    }
}
