package com.fundynamic.d2tm.game.event;


import com.fundynamic.d2tm.Game;
import com.fundynamic.d2tm.game.drawing.DrawableViewPort;

public class DrawableViewPortMoverListener extends AbstractMouseListener {

    public static final int PIXELS_NEAR_BORDER = 2;
    private final DrawableViewPort drawableViewPort;
    private float scrollSpeed;

    public DrawableViewPortMoverListener(DrawableViewPort drawableViewPort, float scrollSpeed) {
        this.drawableViewPort = drawableViewPort;
        this.scrollSpeed = scrollSpeed;
    }

    public DrawableViewPortMoverListener(DrawableViewPort drawableViewPort) {
        this(drawableViewPort, 1F);
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
            drawableViewPort.moveLeft(scrollSpeed);
        } else if (newx >= Game.SCREEN_WIDTH - PIXELS_NEAR_BORDER) {
            drawableViewPort.moveRight(scrollSpeed);
        } else {
            drawableViewPort.moveRight(0F);
        }

        if (newy <= PIXELS_NEAR_BORDER) {
            drawableViewPort.moveUp(scrollSpeed);
        } else if (newy >= Game.SCREEN_HEIGHT - PIXELS_NEAR_BORDER) {
            drawableViewPort.moveDown(scrollSpeed);
        } else {
            drawableViewPort.moveDown(0F);
        }
    }

    @Override
    public void mouseDragged(int oldx, int oldy, int newx, int newy) {

    }
}
