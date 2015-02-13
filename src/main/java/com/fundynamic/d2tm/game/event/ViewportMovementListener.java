package com.fundynamic.d2tm.game.event;


import com.fundynamic.d2tm.game.controls.Mouse;
import com.fundynamic.d2tm.game.drawing.Viewport;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.math.Random;
import com.fundynamic.d2tm.game.structures.StructuresRepository;
import org.newdawn.slick.Input;

public class ViewportMovementListener extends AbstractMouseListener {

    private final Viewport viewport;
    private final Mouse mouse;
    private final StructuresRepository structuresRepository;

    public ViewportMovementListener(Viewport viewport, Mouse mouse, StructuresRepository structuresRepository) {
        this.viewport = viewport;
        this.mouse = mouse;
        this.structuresRepository = structuresRepository;
    }

    @Override
    public void mouseWheelMoved(int change) {

    }

    @Override
    public void mouseClicked(int button, int x, int y, int clickCount) {

        // TODO: this is here for now, but we want to put this in a separate listener!
        if (clickCount == 1) {
            if (button == Input.MOUSE_LEFT_BUTTON) {
                mouse.selectStructure();
            }
            // TODO: same goes for this... which basically is 'place structure here'
            if (!mouse.hasAnyStructureSelected()) {
                structuresRepository.placeStructureOnMap(mouse.getHoverCellMapVector(), Random.getRandomBetween(0, StructuresRepository.MAX_TYPES));
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
