package com.fundynamic.d2tm.game.controls;


import com.fundynamic.d2tm.game.behaviors.Selectable;
import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.Predicate;
import com.fundynamic.d2tm.game.entities.Rectangle;
import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.game.rendering.Viewport;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import java.util.Set;

public class DraggingSelectionBoxMouse extends AbstractMouseBehavior {

    private final Vector2D startingCoordinates;
    private final Viewport viewport;

    private Vector2D dragCoordinates;
    private final EntityRepository entityRepository;

    public DraggingSelectionBoxMouse(Mouse mouse, Vector2D startingCoordinates) {
        super(mouse);
        this.viewport = mouse.getBattlefield();
        this.startingCoordinates = startingCoordinates;
        this.dragCoordinates = startingCoordinates;
        this.entityRepository = mouse.getEntityRepository();
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

        for (Entity entity : entities) {
            ((Selectable) entity).select();
        }

        if (entities.size() > 0) {
            mouse.setMouseBehavior(new MovableSelectedMouse(mouse, mouse.getEntityRepository()));
            return;
        }
        mouse.setMouseBehavior(new NormalMouse(mouse));
    }

    private Set<Entity> getEntitiesWithinDraggedRectangle(EntityRepository entityRepository) {
        Vector2D absDragVec = viewport.translateViewportCoordinateToAbsoluteMapCoordinate(dragCoordinates);
        Vector2D absStartingVec = viewport.translateViewportCoordinateToAbsoluteMapCoordinate(startingCoordinates);
        final Rectangle rectangle = Rectangle.create(absDragVec, absStartingVec);

        return entityRepository.findMovableWithinRectangleForPlayer(mouse.getControllingPlayer(), rectangle);
    }

    private void deselectEverything() {
        EntityRepository entityRepository = mouse.getEntityRepository();
        Set<Entity> entitiesToDeselect = entityRepository.filter(
                Predicate.builder().
                        selectedMovableForPlayer(mouse.getControllingPlayer())
        );
        for (Entity entity : entitiesToDeselect) {
            ((Selectable) entity).deselect();
        }
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.white);

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

        g.drawRect(startX, startY, width, height);
    }

    @Override
    public String toString() {
        return "DraggingSelectionBoxMouse{" +
                "startingCoordinates=" + startingCoordinates +
                ", dragCoordinates=" + dragCoordinates +
                '}';
    }
}
