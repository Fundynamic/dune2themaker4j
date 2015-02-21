package com.fundynamic.d2tm.game.event;


import com.fundynamic.d2tm.game.controls.Mouse;
import com.fundynamic.d2tm.game.rendering.Viewport;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.math.Random;
import com.fundynamic.d2tm.game.entities.structures.StructureRepository;
import org.newdawn.slick.Input;

public class ViewportMovementListener extends AbstractMouseListener {

    private final Viewport viewport;
    private final Mouse mouse;
    private final StructureRepository structureRepository;

    public ViewportMovementListener(Viewport viewport, Mouse mouse, StructureRepository structureRepository) {
        this.viewport = viewport;
        this.mouse = mouse;
        this.structureRepository = structureRepository;
    }

    @Override
    public void mouseWheelMoved(int change) {

    }

    @Override
    public void mouseClicked(int button, int x, int y, int clickCount) {

        // TODO: this is here for now, but we want to put this in a separate listener!
        if (clickCount == 1) {
            if (button == Input.MOUSE_LEFT_BUTTON) {
                if (mouse.hoversOverSelectableEntity()) {
                    mouse.deselectStructure(); // deselect any previously selected structure
                    mouse.selectStructure();
                }

                // TODO: same goes for this... which basically is 'place structure here'
                if (!mouse.hasAnyEntitySelected()) {
                    structureRepository.placeStructureOnMap(mouse.getHoverCellMapVector(), Random.getRandomBetween(0, StructureRepository.MAX_TYPES));
                }
            }
            if (button == Input.MOUSE_RIGHT_BUTTON) {
                mouse.deselectStructure();
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
        viewport.tellAboutNewMousePositions(newx, newy);

        int absoluteX = viewport.getAbsoluteX(newx);
        int absoluteY = viewport.getAbsoluteY(newy);

        Map map = viewport.getMap();

        mouse.setHoverCell(map.getCellByAbsolutePixelCoordinates(absoluteX, absoluteY));
    }

    @Override
    public void mouseDragged(int oldx, int oldy, int newx, int newy) {

    }
}
