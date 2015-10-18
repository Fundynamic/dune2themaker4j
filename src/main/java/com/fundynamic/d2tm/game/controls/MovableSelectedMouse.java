package com.fundynamic.d2tm.game.controls;


import com.fundynamic.d2tm.game.behaviors.Destroyer;
import com.fundynamic.d2tm.game.behaviors.Moveable;
import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.entities.Predicate;
import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.math.Vector2D;

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
        Entity hoveringOverEntity = hoveringOverSelectableEntity();

        // hovering over an entity and visible for player
        if (hoveringOverEntity != null && mouse.getHoverCell().isVisibleFor(player)) {
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

            for (Entity entity : selectedMovableEntities) {
                Vector2D target = mouse.getHoverCell().getCoordinatesAsVector2D().scale(32F);
                ((Moveable) entity).moveTo(target);
            }
        }
    }

    @Override
    public void mouseMovedToCell(Cell cell) {
        if (cell == null) throw new IllegalArgumentException("argument cell may not be null");
        mouse.setHoverCell(cell);
        Entity entity = hoveringOverSelectableEntity();
        if (entity == null) {
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
