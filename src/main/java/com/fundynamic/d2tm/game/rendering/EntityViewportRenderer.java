package com.fundynamic.d2tm.game.rendering;

import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.EntityType;
import com.fundynamic.d2tm.game.entities.Rectangle;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import java.util.Set;

/**
 * Renders projectiles (for now)
 */
public class EntityViewportRenderer {

    private final EntityRepository entityRepository;
    private final Vector2D viewportDimensions;

    public EntityViewportRenderer(EntityRepository entityRepository, Vector2D viewportDimensions) {
        this.entityRepository = entityRepository;
        this.viewportDimensions = viewportDimensions;
    }

    public void render(Graphics graphics, Vector2D viewingVector, EntityType entityType) throws SlickException {
        // very slow and naive way
        Rectangle rectangle = Rectangle.createWithDimensions(viewingVector.min(Vector2D.create(32, 32)), viewportDimensions.add(Vector2D.create(64, 64)));
        Set<Entity> entities = entityRepository.findEntitiesOfTypeWithinRectangle(rectangle, entityType);

        for (Entity entity : entities) {
            Vector2D mapCoordinates = entity.getAbsoluteMapCoordinates();
            int drawX = mapCoordinates.getXAsInt() - viewingVector.getXAsInt();
            int drawY = mapCoordinates.getYAsInt() - viewingVector.getYAsInt();
            entity.render(graphics, drawX, drawY);
        }
    }
}
