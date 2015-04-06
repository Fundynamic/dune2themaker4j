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

import java.util.List;

public class DraggingSelectionBoxMouse extends AbstractMouseBehavior {

    private final Vector2D startingCoordinates;
    private Vector2D dragCoordinates;

    public DraggingSelectionBoxMouse(Mouse mouse, Vector2D startingCoordinates) {
        super(mouse);
        this.startingCoordinates = startingCoordinates;
        this.dragCoordinates = startingCoordinates;
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
    public void draggedToCoordinates(Vector2D coordinates) {
        this.dragCoordinates = coordinates;
    }

    @Override
    public void leftButtonReleased() {
        final Viewport viewport = mouse.getViewport();
        Vector2D absDragVec = viewport.translateScreenToAbsoluteMapPixels(dragCoordinates);
        Vector2D absStartingVec = viewport.translateScreenToAbsoluteMapPixels(startingCoordinates);
        final Rectangle rectangle = Rectangle.create(absDragVec, absStartingVec);

        System.out.println("Rectangle dragged " + rectangle);

        EntityRepository entityRepository = mouse.getEntityRepository();
        List<Entity> entities = entityRepository.find(new Predicate<Entity>() {
            @Override
            public boolean test(Entity entity) {
                if (!entity.isSelectable()) return false;
                if (!entity.isMovable()) return false;
                if (!entity.getPlayer().equals(mouse.getControllingPlayer())) return false;

                // deselect always
                ((Selectable) entity).deselect();

                Vector2D mapCoordinates = entity.getAbsoluteMapPixelCoordinates();
                return rectangle.isWithin(mapCoordinates);
            }
        });

        for (Entity entity : entities) {
            ((Selectable) entity).select();
        }

        if (entities.size() > 0) {
            mouse.setMouseBehavior(new MovableSelectedMouse(mouse));
            return;
        }
        mouse.setMouseBehavior(new NormalMouse(mouse));
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.white);

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
}
