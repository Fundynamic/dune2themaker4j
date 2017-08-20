package com.fundynamic.d2tm.game.controls.battlefield;


import com.fundynamic.d2tm.game.behaviors.Selectable;
import com.fundynamic.d2tm.game.controls.Mouse;
import com.fundynamic.d2tm.game.entities.EntitiesSet;
import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.Predicate;
import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.game.rendering.gui.battlefield.BattleField;
import com.fundynamic.d2tm.math.Vector2D;

import java.util.Set;

/**
 *
 */
public class NormalMouse extends AbstractBattleFieldMouseBehavior {

    public NormalMouse(BattleField battleField) {
        super(battleField);
        mouse.setMouseImage(Mouse.MouseImages.NORMAL, 0, 0);
    }

    @Override
    public void leftClicked() {
        Entity entity = hoveringOverSelectableEntity();
        if (entity == null) return;

        selectEntity(entity);

        if (entity.belongsToPlayer(player) && entity.isMovable()) {
            setMouseBehavior(new MovableSelectedMouse(battleField));
        }
    }

    protected void selectEntity(Entity entity) {
        if (!entity.isSelectable()) return;
        if (entity.isWithinOtherEntity()) return;
        deselectCurrentlySelectedEntities();
        ((Selectable) entity).select();
        battleField.entitiesSelected(EntitiesSet.fromSingle(entity));
    }

    @Override
    public void rightClicked() {
        deselectCurrentlySelectedEntities();
        setMouseBehavior(new NormalMouse(battleField));
    }

    protected void deselectCurrentlySelectedEntities() {
        Set<Entity> entities = entityRepository.filter(
                Predicate.builder().
                        forPlayer(player).
                        isSelected().
                        build());

        for (Entity entity : entities) {
            ((Selectable) entity).deselect();
        }

        // Tell battlefield these entities got deselected
        battleField.entitiesDeselected(EntitiesSet.fromSet(entities));

        mouse.setMouseImage(Mouse.MouseImages.NORMAL, 0, 0);
    }

    @Override
    public void mouseMovedToCell(Cell cell) {
        if (cell == null) throw new IllegalArgumentException("argument cell may not be null");
        Entity previousHoveringEntity = hoveringOverSelectableEntity();
        setHoverCell(cell);

        Entity entity = hoveringOverSelectableEntity();
        if (!entity.equals(previousHoveringEntity)) {
            // shifted focus from one entity to another (or to nothing)
            if (previousHoveringEntity.isSelectable()) {
                ((Selectable) previousHoveringEntity).lostFocus();
            }
        }

        if (entity.isSelectable()) {
            mouse.setMouseImage(Mouse.MouseImages.HOVER_OVER_SELECTABLE_ENTITY, 16, 16);
            ((Selectable) entity).getsFocus();
        } else {
            mouse.setMouseImage(Mouse.MouseImages.NORMAL, 0, 0);
        }
    }

    @Override
    public void draggedToCoordinates(Vector2D viewportCoordinates) {
        if (viewportCoordinates != null) {
            setMouseBehavior(new DraggingSelectionBoxMouse(battleField, entityRepository, viewportCoordinates));
        }
    }

    @Override
    public String toString() {
        return "NormalMouse";
    }
}
