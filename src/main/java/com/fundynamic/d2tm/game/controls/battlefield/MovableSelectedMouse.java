package com.fundynamic.d2tm.game.controls.battlefield;


import com.fundynamic.d2tm.game.behaviors.Destroyer;
import com.fundynamic.d2tm.game.behaviors.Moveable;
import com.fundynamic.d2tm.game.behaviors.Selectable;
import com.fundynamic.d2tm.game.controls.Mouse;
import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.NullEntity;
import com.fundynamic.d2tm.game.entities.Predicate;
import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.game.rendering.gui.battlefield.BattleField;
import com.fundynamic.d2tm.math.Coordinate;

import java.util.Set;

/**
 *
 * <h2>When activated?</h2>
 * <p>
 * Whenever entities are being selected that are movable or able to attack this behavior gets
 * activated.
 * </p>
 * <h2>What does this do?</h2>
 * <h3>Left-click</h3>
 * <p>
 * Orders upon the selected units to attack or move to the cell the mouse hovers on.
 * </p>
 * <h3>Right-click</h3>
 * <p>
 * Deselects all selected entities and goes back to NormalMouse mode.
 * </p>
 *
 */
public class MovableSelectedMouse extends NormalMouse {

    public MovableSelectedMouse(BattleField battleField) {
        super(battleField);
        mouse.setMouseImage(Mouse.MouseImages.MOVE, 16, 16);
    }

    @Override
    public void leftClicked() {
        Entity hoveringOverEntity = hoveringOverSelectableEntity();

        // hovering over an entity and visible for player
        if (!NullEntity.is(hoveringOverEntity) && getHoverCell().isVisibleFor(player)) {
            // select entity when entity belongs to player
            if (hoveringOverEntity.belongsToPlayer(player)) {
                selectEntity(hoveringOverEntity);
            } else {
                attackDestructibleIfApplicable(hoveringOverEntity);

            }
        } else {
            Set<Entity> selectedMovableEntities = entityRepository.filter(
                    Predicate.builder().
                            selectedMovableForPlayer(player)
            );

            Coordinate target = getHoverCell().getMapCoordinate().toCoordinate();
            for (Entity entity : selectedMovableEntities) {
                ((Moveable) entity).moveTo(target);
            }
        }
    }

    public void attackDestructibleIfApplicable(Entity hoveringOverEntity) {
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

    @Override
    public void mouseMovedToCell(Cell cell) {
        if (cell == null) throw new IllegalArgumentException("argument cell may not be null");

        // get the hovering entity without updated cell
        Entity previousHoveringEntity = hoveringOverSelectableEntity();

        // update cell that mouse hovers over
        setHoverCell(cell);

        // get again the entity the mouse is hovering over
        Entity entity = hoveringOverSelectableEntity();

        // when they are not the same
        if (!entity.equals(previousHoveringEntity)) {

            // shifted focus from one entity to another (or to nothing)
            // so tell the previous entity we lost focus
            if (previousHoveringEntity.isSelectable()) {
                ((Selectable) previousHoveringEntity).lostFocus();
            }

        }

        // no entity on the cell we're hovering over, so we can move
        if (NullEntity.is(entity)) {
            mouse.setMouseImage(Mouse.MouseImages.MOVE, 16, 16);
            return;
        }

        // tell the current entity we hover over that it gained (mouse) focus
        if (entity.isSelectable()) {
            ((Selectable) entity).getsFocus();
        }

        // if entity belongs to the player who controls the mouse...
        if (entity.belongsToPlayer(mouse.getControllingPlayer())) {
            // show the 'selectable icon' for mouse icon
            mouse.setMouseImage(Mouse.MouseImages.HOVER_OVER_SELECTABLE_ENTITY, 16, 16);
        } else if(cell.isVisibleFor(player)) {
            // or show an attack icon
            mouse.setMouseImage(Mouse.MouseImages.ATTACK, 16, 16);
        }
    }

    @Override
    public String toString() {
        return "MovableSelectedMouse";
    }
}
