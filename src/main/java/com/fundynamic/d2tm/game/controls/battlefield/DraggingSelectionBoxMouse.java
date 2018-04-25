package com.fundynamic.d2tm.game.controls.battlefield;


import com.fundynamic.d2tm.game.behaviors.Renderable;
import com.fundynamic.d2tm.game.behaviors.Selectable;
import com.fundynamic.d2tm.game.entities.EntitiesSet;
import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.Predicate;
import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.game.rendering.gui.battlefield.BattleField;
import com.fundynamic.d2tm.math.Coordinate;
import com.fundynamic.d2tm.math.Rectangle;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import java.util.Set;

public class DraggingSelectionBoxMouse extends AbstractBattleFieldMouseBehavior implements Renderable {

    private final Vector2D startingCoordinates;

    private Vector2D dragCoordinates;
    private final EntityRepository entityRepository;

    public DraggingSelectionBoxMouse(BattleField battleField, EntityRepository entityRepository, Cell hoverCell, Vector2D startingCoordinates) {
        super(battleField, hoverCell);
        this.startingCoordinates = startingCoordinates;
        this.dragCoordinates = startingCoordinates;
        this.entityRepository = entityRepository;
    }

    @Override
    public void leftClicked() {
        NormalMouse normalMouse = new NormalMouse(battleField, getHoverCell());
        setMouseBehavior(normalMouse);
        normalMouse.leftClicked();
    }

    @Override
    public void rightClicked() {
        NormalMouse normalMouse = new NormalMouse(battleField, getHoverCell());
        setMouseBehavior(normalMouse);
        normalMouse.rightClicked();
    }

    @Override
    public void mouseMovedToCell(Cell cell) {
        setHoverCell(cell);
    }

    @Override
    public void draggedToCoordinates(Vector2D viewportCoordinates) {
        if (viewportCoordinates != null) {
            this.dragCoordinates = viewportCoordinates;
        }
    }

    @Override
    public void leftButtonReleased() {
        deselectEverything();

        Set<Entity> entities = getEntitiesWithinDraggedRectangle(entityRepository);

        // empty list, set mouse behavior back to normal
        if (entities.isEmpty()) {
            battleField.setMouseBehavior(new NormalMouse(battleField, getHoverCell()));
            return;
        }

        // entities found within the rectangle, mark them as selected
        for (Entity entity : entities) {
            ((Selectable) entity).select();
        }

        // ... and set mouse behavior
        battleField.setMouseBehavior(new MovableSelectedMouse(battleField, getHoverCell()));

        // and tell the battleField which units got selected
        battleField.entitiesSelected(EntitiesSet.fromSet(entities));
    }

    private Set<Entity> getEntitiesWithinDraggedRectangle(EntityRepository entityRepository) {
        Coordinate absDragVec = battleField.translateViewportCoordinateToAbsoluteMapCoordinate(dragCoordinates);
        Coordinate absStartingVec = battleField.translateViewportCoordinateToAbsoluteMapCoordinate(startingCoordinates);
        final Rectangle rectangle = Rectangle.create(absDragVec, absStartingVec);

        return entityRepository.findMovableWithinRectangleForPlayer(mouse.getControllingPlayer(), rectangle);
    }

    private void deselectEverything() {
        Set<Entity> entitiesToDeselect = entityRepository.filter(
                Predicate.builder().
                        selectedMovableForPlayer(mouse.getControllingPlayer())
        );
        for (Entity entity : entitiesToDeselect) {
            ((Selectable) entity).deselect();
        }
    }

    @Override
    public String toString() {
        return "DraggingSelectionBoxMouse{" +
                "startingCoordinates=" + startingCoordinates +
                ", dragCoordinates=" + dragCoordinates +
                '}';
    }

    @Override
    public void render(Graphics graphics) {
        graphics.setColor(Color.white);
        float original = graphics.getLineWidth();
        graphics.setLineWidth(1);

//        System.out.println("starting coords = " + startingCoordinates + " drag = " + dragCoordinates);
        // we need to do all this stuff because there is only one way to draw a rect (with width and height)
        // and you cannot pass it a negative number for width and height (it behaves weird)...
        int dragX = dragCoordinates.getXAsInt();
        int dragY = dragCoordinates.getYAsInt();
        int startingX = startingCoordinates.getXAsInt();
        int startingY = startingCoordinates.getYAsInt();

        int startX = Math.min(startingX, dragX);
        int startY = Math.min(startingY, dragY);

        int width = Math.abs(startingX - dragX);
        int height = Math.abs(startingY - dragY);

        graphics.drawRect(startX, startY, width, height);
        graphics.setLineWidth(original);
    }
}
