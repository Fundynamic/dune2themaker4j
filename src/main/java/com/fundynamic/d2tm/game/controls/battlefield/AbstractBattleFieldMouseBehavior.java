package com.fundynamic.d2tm.game.controls.battlefield;


import com.fundynamic.d2tm.game.behaviors.Renderable;
import com.fundynamic.d2tm.game.controls.AbstractMouseBehavior;
import com.fundynamic.d2tm.game.controls.MouseBehavior;
import com.fundynamic.d2tm.game.entities.*;
import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.rendering.gui.battlefield.BattleField;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.Graphics;

/**
 * This abstract mouse behavior also has a relation with the battlefield. Ie, they
 * are not associated with any other 'gui' element but the battlefield
 */
public abstract class AbstractBattleFieldMouseBehavior extends AbstractMouseBehavior<AbstractBattleFieldMouseBehavior> implements MouseBehavior, CellBasedMouseBehavior, Renderable {

    protected final BattleField battleField;
    protected final EntityRepository entityRepository;
    protected final Player player;
    protected final Map map;

    protected Vector2D mouseCoordinates;

    private Cell hoverCell;

    public AbstractBattleFieldMouseBehavior(BattleField battleField) {
        // dependencies from battlefield
        super(battleField.getMouse());
        this.battleField = battleField;
        this.entityRepository = battleField.getEntityRepository();
        this.map = battleField.getMap();

        // dependencies from dependencies from battlefield :/
        this.player = mouse.getControllingPlayer();
        this.mouseCoordinates = Vector2D.zero();
    }

    public abstract void leftClicked();

    public abstract void rightClicked();

    @Override
    public void render(Graphics graphics) {
        /***
         * Usually we do not need to render additional things
         */
    }

    /**
     * React to movements to screen coordinates
     * @param coordinates
     */
    public void movedTo(Vector2D coordinates) {
        /**
         * This method does not need to do anything because {@link BattleField#movedTo(Vector2D)}
         * already calls {@link #mouseMovedToCell(Cell)} of this behavior.
         *
         * For convenience we do store the coordinates in case we need it (for rendering)
         */
        mouseCoordinates = coordinates;
    }

    /**
     * Returns the cell that the mouse is 'hovering over'. This method may return NULL!
     * @return
     */
    public Cell getHoverCell() {
        return hoverCell;
    }

    public void setHoverCell(Cell hoverCell) {
        this.hoverCell = hoverCell;
    }

    /**
     * MOVE to BattleFieldMouseBehavior
     * @param entity
     * @return
     */
    public boolean hoveringOverVisibleEntity(Entity entity) {
        return entity.isVisibleFor(player, map);
    }

    ///BATTLEFIELD HAS MOUSE STATE?
    ///IT MAKES NO SENSE TO PUT IT IN MOUSE ITSELF BECAUSE THAT IS TOO ABSTRACT
    public Entity hoveringOverSelectableEntity() {
        if (hoverCell == null) return NullEntity.INSTANCE;

        EntitiesSet entities = entityRepository.filter(
                Predicate.builder().
                    vectorWithin(hoverCell.getCoordinate()).
                    isNotWithinAnotherEntity().
                    isSelectable()
        );

        Entity entity = entities.getFirst();
        if (entity == null) return NullEntity.INSTANCE;
        if (!entity.isSelectable()) return NullEntity.INSTANCE;
        if (!hoveringOverVisibleEntity(entity)) return NullEntity.INSTANCE;
        return entity;
    }

    @Override
    public void setMouseBehavior(AbstractBattleFieldMouseBehavior mouseBehavior) {
        battleField.setMouseBehavior(mouseBehavior);
    }

}
