package com.fundynamic.d2tm.game.event;


import com.fundynamic.d2tm.game.controls.Mouse;
import com.fundynamic.d2tm.game.drawing.Viewport;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.math.Vector2D;
import com.fundynamic.d2tm.game.structures.StructuresRepository;
import org.newdawn.slick.Input;

import java.sql.Struct;

public class ViewportMovementListener extends AbstractMouseListener {

    public static final int PIXELS_NEAR_BORDER = 2;

    private final Viewport viewport;
    private final Vector2D viewportDimensions;
    private final Mouse mouse;
    private final StructuresRepository structuresRepository;

    // Viewport dimensions belong to Viewport actually
    public ViewportMovementListener(Viewport viewport, Vector2D viewportDimensions, Mouse mouse, StructuresRepository structuresRepository) {
        this.viewportDimensions = viewportDimensions;
        this.viewport = viewport;
        this.mouse = mouse;
        this.structuresRepository = structuresRepository;
    }

    @Override
    public void mouseWheelMoved(int change) {

    }

    @Override
    public void mouseClicked(int button, int x, int y, int clickCount) {
        // TODO: this is here for now, but we might (probably...) want to put this in a separate listener!
        if (clickCount == 1) {
            if (button == Input.MOUSE_LEFT_BUTTON) {
                mouse.selectStructure();
            }
            // TODO: for now place logic here, but that will probably be moved to another listener?
            if (!mouse.hasAnyStructureSelected()) {
                structuresRepository.placeStructureOnMap(mouse.getHoverCellMapVector(), StructuresRepository.CONSTRUCTION_YARD);
            }
        }
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

        Map map = viewport.getMap();

        mouse.setHoverCell(
                map.getCellByPixelsCoordinates(mapPixelX, mapPixelY),
                map.getVector2DByPixelsCoordinates(mapPixelX, mapPixelY)
        );

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
