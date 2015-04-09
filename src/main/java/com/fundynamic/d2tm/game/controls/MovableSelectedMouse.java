package com.fundynamic.d2tm.game.controls;


import com.fundynamic.d2tm.game.behaviors.Moveable;
import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.Predicate;
import com.fundynamic.d2tm.game.map.Cell;

import java.util.Set;

public class MovableSelectedMouse extends NormalMouse {

    private EntityRepository entityRepository;

    public MovableSelectedMouse(Mouse mouse) {
        super(mouse);
        mouse.setMouseImage(Mouse.MouseImages.MOVE, 16, 16);
        this.entityRepository = mouse.getEntityRepository();
    }

    @Override
    public void leftClicked() {
        Entity hoveringOverEntity = hoveringOverSelectableEntity();
        if (hoveringOverEntity != null) {
            if (hoveringOverEntity.belongsToPlayer(mouse.getControllingPlayer())) {
                deselectCurrentlySelectedEntity();
                selectEntity(hoveringOverEntity);
            } else {
                Set<Entity> selectedMovableEntities = entityRepository.filter(
                        Predicate.builder().
                                selectedMovableForPlayer(mouse.getControllingPlayer())
                );

                for (Entity entity : selectedMovableEntities) {
                    ((Moveable) entity).moveTo(mouse.getHoverCell().getCoordinatesAsVector2D());
                }
            }
        } else {
            Set<Entity> selectedMovableEntities = entityRepository.filter(
                    Predicate.builder().
                            selectedMovableForPlayer(mouse.getControllingPlayer())
            );

            for (Entity entity : selectedMovableEntities) {
                ((Moveable) entity).moveTo(mouse.getHoverCell().getCoordinatesAsVector2D());
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
        } else {
            mouse.setMouseImage(Mouse.MouseImages.ATTACK, 16, 16);
        }
    }

    @Override
    public void rightClicked() {
        super.rightClicked();
    }

    @Override
    public String toString() {
        return "MovableSelectedMouse";
    }
}
