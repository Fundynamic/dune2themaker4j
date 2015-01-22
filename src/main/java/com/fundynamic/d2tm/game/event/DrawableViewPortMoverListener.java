package com.fundynamic.d2tm.game.event;


import com.fundynamic.d2tm.game.drawing.DrawableViewPort;

public class DrawableViewPortMoverListener extends AbstractMouseListener {

    private final DrawableViewPort drawableViewPort;
    private float velocityIncreaser;

    public DrawableViewPortMoverListener(DrawableViewPort drawableViewPort, float velocityIncreaser) {
        this.drawableViewPort = drawableViewPort;
        this.velocityIncreaser = velocityIncreaser;
    }

    public DrawableViewPortMoverListener(DrawableViewPort drawableViewPort) {
        this(drawableViewPort, 0.5F);
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
        if (newx <= 2) {
            drawableViewPort.moveLeft(velocityIncreaser);
        }
    }

    @Override
    public void mouseDragged(int oldx, int oldy, int newx, int newy) {

    }
}
