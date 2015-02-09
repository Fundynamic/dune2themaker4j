package com.fundynamic.d2tm.game.event;


import com.fundynamic.d2tm.game.controls.Mouse;
import com.fundynamic.d2tm.game.drawing.Viewport;
import com.fundynamic.d2tm.game.math.Vector2D;

public class ViewportMovementListener extends AbstractMouseListener {

    public static final int PIXELS_NEAR_BORDER = 2;

    private final Viewport viewport;
    private final Vector2D viewportDimensions;
    private final Mouse mouse;

    // Viewport dimensions belong to Viewport actually
    public ViewportMovementListener(Viewport viewport, Vector2D viewportDimensions, Mouse mouse) {
        this.viewportDimensions = viewportDimensions;
        this.viewport = viewport;
        this.mouse = mouse;
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
        // Viewport dimensions belong to Viewport actually, so we can change the code below to:
        // if (viewport.isNearLeftBorder(newx) etc..
        // or..
        // viewport.updateMousePosition(newx, newy) and move the logic here to viewport to respond to that
        // ie -> Tell don't ask

        if (newx <= PIXELS_NEAR_BORDER) {
            viewport.moveLeft();
        } else if (newx >= viewportDimensions.getX() - PIXELS_NEAR_BORDER) {
            viewport.moveRight();
        } else {
            viewport.stopMovingHorizontally();
        }

        int mapPixelX = newx + viewport.getViewingVector().getX();
        int mapPixelY = newy + viewport.getViewingVector().getY();

        mouse.setHoverCell(viewport.getMap().getCellByPixelsCoordinates(mapPixelX, mapPixelY));

        if (newy <= PIXELS_NEAR_BORDER) {
            viewport.moveUp();
        } else if (newy >= viewportDimensions.getY() - PIXELS_NEAR_BORDER) {
            viewport.moveDown();
        } else {
            viewport.stopMovingVertically();
        }
    }

    @Override
    public void mouseDragged(int oldx, int oldy, int newx, int newy) {

    }
}
