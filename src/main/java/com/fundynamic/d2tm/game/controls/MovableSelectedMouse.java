package com.fundynamic.d2tm.game.controls;


import com.fundynamic.d2tm.game.behaviors.Destroyer;
import com.fundynamic.d2tm.game.behaviors.Moveable;
import com.fundynamic.d2tm.game.behaviors.Selectable;
import com.fundynamic.d2tm.game.entities.*;
import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.math.Coordinate;

import java.util.Set;

/**
 *
 * The MovableSelectedMouse behavior orders units to attack or move to the cell the mouse hovers on.
 *
 */
public class MovableSelectedMouse extends NormalMouse {

    private final EntityRepository entityRepository;
    private final Player player;

    public MovableSelectedMouse(Mouse mouse, EntityRepository entityRepository) {
        super(mouse);
        mouse.setMouseImage(Mouse.MouseImages.MOVE, 16, 16);
        this.entityRepository = entityRepository;
        player = mouse.getControllingPlayer();
    }

    @Override
    public void leftClicked() {
        Entity hoveringOverEntity = mouse.hoveringOverSelectableEntity();

        // hovering over an entity and visible for player
        if (!NullEntity.is(hoveringOverEntity) && mouse.getHoverCell().isVisibleFor(player)) {
            // select entity when entity belongs to player
            if (hoveringOverEntity.belongsToPlayer(player)) {
                deselectCurrentlySelectedEntity();
                selectEntity(hoveringOverEntity);
            } else {
                // does not belong to player; that can only mean 'attack'!
                if (hoveringOverEntity.isDestructible()) {
                    Set<Entity> selectedDestroyersEntities = entityRepository.filter(
                            Predicate.builder().
                                    selectedDestroyersForPlayer(player)
                    );

                    for (Entity entity : selectedDestroyersEntities) {
                        ((Destroyer) entity).attack(hoveringOverEntity);
                    }
                }
            }
        } else {
            Set<Entity> selectedMovableEntities = entityRepository.filter(
                    Predicate.builder().
                            selectedMovableForPlayer(player)
            );

            Coordinate target = mouse.getHoverCell().getMapCoordinate().toCoordinate();
            for (Entity entity : selectedMovableEntities) {
                ((Moveable) entity).moveTo(target);
            }
        }
    }

    @Override
    public void mouseMovedToCell(Cell cell) {
        if (cell == null) throw new IllegalArgumentException("argument cell may not be null");
        Entity previousHoveringEntity = mouse.hoveringOverSelectableEntity();
        mouse.setHoverCell(cell);
        Entity entity = mouse.hoveringOverSelectableEntity();

        if (entity != previousHoveringEntity) {
            // shifted focus from one entity to another (or to nothing)
            if (previousHoveringEntity.isSelectable()) {
                ((Selectable) previousHoveringEntity).lostFocus();
            }
        }

        if (entity.isSelectable()) {
            ((Selectable) entity).getsFocus();
        }

        // no entity on the cell we're hovering over, so we can move
        if (NullEntity.is(entity)) {
            mouse.setMouseImage(Mouse.MouseImages.MOVE, 16, 16);
            return;
        }

        if (entity.belongsToPlayer(mouse.getControllingPlayer())) {
            mouse.setMouseImage(Mouse.MouseImages.HOVER_OVER_SELECTABLE_ENTITY, 16, 16);
        } else if(cell.isVisibleFor(player)) {
            mouse.setMouseImage(Mouse.MouseImages.ATTACK, 16, 16);
        }
    }

    @Override
    public String toString() {
        return "MovableSelectedMouse";
    }
}
