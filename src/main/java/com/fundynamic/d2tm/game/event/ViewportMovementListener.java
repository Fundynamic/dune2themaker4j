package com.fundynamic.d2tm.game.event;


import com.fundynamic.d2tm.game.controls.Mouse;
import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.entities.structures.Structure;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.rendering.Viewport;
import com.fundynamic.d2tm.math.Random;
import org.newdawn.slick.Input;

public class ViewportMovementListener extends AbstractMouseListener {

    private final Viewport viewport;
    private final Mouse mouse;
    private final EntityRepository entityRepository;
    private final Player player;
    private EntityRepository.EntityType entityType;

    public ViewportMovementListener(Viewport viewport, Mouse mouse, EntityRepository entityRepository, Player player) {
        this.viewport = viewport;
        this.mouse = mouse;
        this.entityRepository = entityRepository;
        this.entityType = EntityRepository.EntityType.STRUCTURE;
        this.player = player;
    }

    @Override
    public void mouseWheelMoved(int change) {

    }

    @Override
    public void mouseClicked(int button, int x, int y, int clickCount) {

        // TODO: this is here for now, but we want to put this in a separate listener!
        if (clickCount == 1) {
            if (button == Input.MOUSE_LEFT_BUTTON) {
                if (mouse.isMovingCursor()) {
                    // move unit...
                } else {
                    if (mouse.hoversOverSelectableEntity()) {
                        mouse.deselectEntity();
                        mouse.selectEntity();

                        // add some fun so we place units/structures depending on what we selected
                        if (mouse.getLastSelectedEntity() instanceof Structure) {
                            entityType = EntityRepository.EntityType.STRUCTURE;
                        } else {
                            entityType = EntityRepository.EntityType.UNIT;
                        }
                    }

                    // TODO: same goes for this... which basically is 'place structure here'
                    if (!mouse.hasAnyEntitySelected()) {
                        entityRepository.placeOnMap(mouse.getHoverCellMapVector(), entityType, Random.getRandomBetween(0, 2), player);
                    }
                }
            }

            if (button == Input.MOUSE_RIGHT_BUTTON) {
                mouse.deselectEntity();
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
