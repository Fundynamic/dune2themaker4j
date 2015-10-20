package com.fundynamic.d2tm.game.entities;

import com.fundynamic.d2tm.game.behaviors.*;
import com.fundynamic.d2tm.game.rendering.RenderQueue;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.SpriteSheet;

public abstract class Entity implements Renderable, Updateable {

    // Final properties of unit
    protected final EntityData entityData;
    protected final SpriteSheet spriteSheet;
    protected final Player player;
    protected final EntityRepository entityRepository;

    protected Vector2D absoluteCoordinates;

    public Entity(Vector2D absoluteCoordinates, SpriteSheet spriteSheet, EntityData entityData, Player player, EntityRepository entityRepository) {
        this.absoluteCoordinates = absoluteCoordinates;
        this.spriteSheet = spriteSheet;
        this.entityData = entityData;
        this.player = player;
        this.entityRepository = entityRepository;
        if (player != null) {
            // temporarily, because 'particle' does not belong to a player
            player.addEntity(this);
        }
    }

    public Vector2D getAbsoluteCoordinates() {
        return absoluteCoordinates;
    }

    public int getX() {
        return absoluteCoordinates.getXAsInt();
    }

    public int getY() {
        return absoluteCoordinates.getYAsInt();
    }

    public int getSight() {
        return entityData.sight;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean belongsToPlayer(Player other) {
        return other.equals(player);
    }

    public boolean isMovable() {
        return this instanceof Moveable;
    }

    public boolean isSelectable() {
        return this instanceof Selectable;
    }

    public boolean isUpdatable() {
        return this instanceof Updateable;
    }

    public boolean isDestructible() {
        return this instanceof Destructible;
    }

    public boolean isDestroyer() {
        return this instanceof Destroyer;
    }

    public abstract EntityType getEntityType();

    public boolean removeFromPlayerSet(Entity entity) {
        if (player != null) {
            return player.removeEntity(entity);
        }
        return false;
    }

    public Vector2D getRandomPositionWithin() {
        int topX = getX() - 16;
        int topY = getY() - 16;
        return Vector2D.random(topX, topX + entityData.width, topY, topY + entityData.height);
    }

    public Vector2D getDimensions() {
        return Vector2D.create(entityData.width, entityData.height);
    }

    @Override
    public void enrichRenderQueue(RenderQueue renderQueue) {
        // by default do nothing
    }

    public Vector2D getCenteredPosition() {
        return absoluteCoordinates.add(getHalfSize());
    }

    public Vector2D getHalfSize() {
        return Vector2D.create(entityData.width / 2, entityData.height / 2);
    }

    /**
     * Return the metadata about this entity.
     * @return
     */
    public EntityData getEntityData() {
        return this.entityData;
    }
}