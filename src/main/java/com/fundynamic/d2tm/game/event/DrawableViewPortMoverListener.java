package com.fundynamic.d2tm.game.event;


import com.fundynamic.d2tm.Game;
import com.fundynamic.d2tm.game.drawing.DrawableViewPort;

public class DrawableViewPortMoverListener extends AbstractMouseListener {

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
        if (newx <= 2) drawableViewPort.moveLeft(scrollSpeed);
        if (newx >= Game.SCREEN_WIDTH - 2) drawableViewPort.moveRight(scrollSpeed);
        if (newy <= 2) drawableViewPort.moveUp(scrollSpeed);
        if (newy >= Game.SCREEN_HEIGHT - 2) drawableViewPort.moveDown(scrollSpeed);
    }

    @Override
    public void mouseDragged(int oldx, int oldy, int newx, int newy) {

    }
}
