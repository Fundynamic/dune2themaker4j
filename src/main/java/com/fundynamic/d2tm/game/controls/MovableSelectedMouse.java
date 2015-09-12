package com.fundynamic.d2tm.game.controls;


import com.fundynamic.d2tm.game.behaviors.Destroyer;
import com.fundynamic.d2tm.game.behaviors.Moveable;
import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.entities.Predicate;
import com.fundynamic.d2tm.game.map.Cell;

import java.util.Set;

public class MovableSelectedMouse extends NormalMouse {

    private final EntityRepository entityRepository;
    private final Player player;

    public MovableSelectedMouse(Mouse mouse) {
        super(mouse);
        mouse.setMouseImage(Mouse.MouseImages.MOVE, 16, 16);
        this.entityRepository = mouse.getEntityRepository();
        player = mouse.getControllingPlayer();
    }

    @Override
    public void leftClicked() {
        Entity hoveringOverEntity = hoveringOverSelectableEntity();

        if (hoveringOverEntity != null && mouse.getHoverCell().isVisibleFor(player)) {
            if (hoveringOverEntity.belongsToPlayer(player)) {
                deselectCurrentlySelectedEntity();
                selectEntity(hoveringOverEntity);
            } else {
                if (hoveringOverEntity.isDestructible()) {
                    Set<Entity> selectedAttackableEntities = entityRepository.filter(
                            Predicate.builder().
                                    selectedAttackableForPlayer(player)
                    );

                    for (Entity entity : selectedAttackableEntities) {
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
        } else if(cell.isVisibleFor(player)) {
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
