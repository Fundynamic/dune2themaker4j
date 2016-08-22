package com.fundynamic.d2tm.game.controls;


import com.fundynamic.d2tm.game.behaviors.Renderable;
import com.fundynamic.d2tm.game.behaviors.Selectable;
import com.fundynamic.d2tm.game.controls.battlefield.AbstractBattleFieldMouseBehavior;
import com.fundynamic.d2tm.game.controls.battlefield.MovableSelectedMouse;
import com.fundynamic.d2tm.game.controls.battlefield.NormalMouse;
import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.Predicate;
import com.fundynamic.d2tm.game.entities.Rectangle;
import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.game.rendering.gui.battlefield.BattleField;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import java.util.Set;

public class DraggingSelectionBoxMouse extends AbstractBattleFieldMouseBehavior implements Renderable {

    private final Vector2D startingCoordinates;

    private Vector2D dragCoordinates;
    private final EntityRepository entityRepository;

    public DraggingSelectionBoxMouse(BattleField battleField, EntityRepository entityRepository, Vector2D startingCoordinates) {
        super(battleField);
        this.startingCoordinates = startingCoordinates;
        this.dragCoordinates = startingCoordinates;
        this.entityRepository = entityRepository;
    }

    @Override
    public void leftClicked() {
        // DO NOTHING
    }

    @Override
    public void rightClicked() {
        // DO NOTHING
    }

    @Override
    public void mouseMovedToCell(Cell cell) {
        // DO NOTHING
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
            battleField.setMouseBehavior(new NormalMouse(battleField));
            return;
        }

        // entities found within the rectangle, mark them as selected
        for (Entity entity : entities) {
            ((Selectable) entity).select();
        }

        // ... and set mouse behavior
        battleField.setMouseBehavior(new MovableSelectedMouse(battleField));
    }

    private Set<Entity> getEntitiesWithinDraggedRectangle(EntityRepository entityRepository) {
        Vector2D absDragVec = battleField.translateViewportCoordinateToAbsoluteMapCoordinate(dragCoordinates);
        Vector2D absStartingVec = battleField.translateViewportCoordinateToAbsoluteMapCoordinate(startingCoordinates);
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
    }
}
