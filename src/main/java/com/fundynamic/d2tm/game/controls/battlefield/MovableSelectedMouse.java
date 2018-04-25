package com.fundynamic.d2tm.game.controls.battlefield;


import com.fundynamic.d2tm.game.behaviors.Destroyer;
import com.fundynamic.d2tm.game.behaviors.Harvester;
import com.fundynamic.d2tm.game.behaviors.Moveable;
import com.fundynamic.d2tm.game.behaviors.Selectable;
import com.fundynamic.d2tm.game.entities.EntitiesSet;
import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.NullEntity;
import com.fundynamic.d2tm.game.entities.Predicate;
import com.fundynamic.d2tm.game.entities.units.Unit;
import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.game.rendering.gui.battlefield.BattleField;
import com.fundynamic.d2tm.math.Coordinate;
import com.fundynamic.d2tm.math.MapCoordinate;

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

    private EntitiesSet entitiesSetOfAllMovable;

    public MovableSelectedMouse(BattleField battleField, Cell hoverCell) {
        super(battleField, hoverCell);
        mouse.setMouseImageMove();
    }

    public EntitiesSet getAllSelectedMovableEntitiesForPlayer() {
        return entityRepository.filter(
                Predicate.builder().
                        selectedMovableForPlayer(player)
        );
    }

    @Override
    public void leftClicked() {
        Entity hoveringOverEntity = hoveringOverSelectableEntity();

        // hovering over an entity and visible for player
        Cell cell = getHoverCell();
        if (cell == null) return;

        if (!NullEntity.is(hoveringOverEntity) && cell.isVisibleFor(player)) {
            // select entity when entity belongs to player
            if (hoveringOverEntity.belongsToPlayer(player)) {

                if (hoveringOverEntity.isRefinery()) {
                    EntitiesSet harvestersSelected = entitiesSetOfAllMovable.filter(Predicate.isHarvester());
                    if (harvestersSelected.hasAny()) {
                        harvestersSelected.forEach(entity -> ((Harvester)entity).returnToRefinery(hoveringOverEntity));
                        return;
                    }
                }

                selectEntity(hoveringOverEntity);
            } else {
                attackDestructibleIfApplicable(hoveringOverEntity);
            }

        } else {
            MapCoordinate mapCoordinate = cell.getMapCoordinate();
            Coordinate target = mapCoordinate.toCoordinate();

            EntitiesSet harvestersSelected = entitiesSetOfAllMovable.filter(Predicate.isHarvester());
            if (harvestersSelected.hasAny() && entitiesSetOfAllMovable.sameSizeAs(harvestersSelected)) {
                if (cell.isHarvestable()) {
                    for (Entity entity : entitiesSetOfAllMovable) {
                        ((Unit) entity).harvestAt(target);
                    }
                } else {
                    for (Entity entity : entitiesSetOfAllMovable) {
                        ((Moveable) entity).moveTo(target);
                    }
                }
            } else {
                for (Entity entity : entitiesSetOfAllMovable) {
                    ((Moveable) entity).moveTo(target);
                }
            }
        }
    }

    public void attackDestructibleIfApplicable(Entity hoveringOverEntity) {
        // does not belong to player; that can only mean 'attack'!
        if (hoveringOverEntity.isDestructible()) {

            EntitiesSet entitiesCapableOfDestroyingThings = entitiesSetOfAllMovable.filter(
                    Predicate.builder().
                            selectedDestroyersForPlayer(player)
            );

            entitiesCapableOfDestroyingThings.each(entity -> ((Destroyer) entity).attack(hoveringOverEntity));
        }
    }

    @Override
    public void mouseMovedToCell(Cell cell) {
        if (cell == null) throw new IllegalArgumentException("argument cell may not be null");
        entitiesSetOfAllMovable = getAllSelectedMovableEntitiesForPlayer();

        // get the hovering entity without updated cell
        Entity previousHoveringEntity = hoveringOverSelectableEntity();

        // update cell that mouse hovers over
        setHoverCell(cell);

        if(!cell.isVisibleFor(player)) {
            mouse.setMouseImageMove();
            return;
        }

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
            mouse.setMouseImageMove();

            EntitiesSet harvestersSelected = entitiesSetOfAllMovable.filter(Predicate.isHarvester());
            if (harvestersSelected.hasAny() && harvestersSelected.sameSizeAs(entitiesSetOfAllMovable)) {
                if (cell.isHarvestable()) {
                    mouse.setMouseImageAttack();
                }
            }

            return;
        }

        // tell the current entity we hover over that it gained (mouse) focus
        if (entity.isSelectable()) {
            ((Selectable) entity).getsFocus();
        }

        // if entity belongs to the player who controls the mouse...
        if (entity.belongsToPlayer(mouse.getControllingPlayer())) {

            if (entity.isRefinery()) {
                EntitiesSet harvestersSelected = entitiesSetOfAllMovable.filter(Predicate.isHarvester());
                if (harvestersSelected.hasAny()) {
                    mouse.setMouseImageEnter();
                    return;
                }
            }

            // show the 'selectable icon' for mouse icon
            mouse.setMouseImageSelectable();
        } else {
            // or show an attack icon
            mouse.setMouseImageAttack();
        }

    }

    @Override
    public String toString() {
        return "MovableSelectedMouse";
    }
}
