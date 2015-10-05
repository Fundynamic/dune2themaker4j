package com.fundynamic.d2tm.game.entities;

import com.fundynamic.d2tm.game.behaviors.*;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.SpriteSheet;

public abstract class Entity implements Renderable, Updateable {

    // Final properties of unit
    protected EntityData entityData;
    protected final SpriteSheet spriteSheet;
    protected final int sight;
    protected final Player player;
    protected final EntityRepository entityRepository;

    protected Vector2D absoluteMapCoordinates;

    public Entity(Vector2D absoluteMapCoordinates, SpriteSheet spriteSheet, int sight, Player player, EntityRepository entityRepository) {
        this.absoluteMapCoordinates = absoluteMapCoordinates;
        this.spriteSheet = spriteSheet;
        this.sight = sight;
        this.player = player;
        this.entityRepository = entityRepository;
        if (player != null) {
            // temporarily, because 'particle' does not belong to a player
            player.addEntity(this);
        }
    }

    public boolean removeFromMap(Map map) {
        return map.getCellByAbsoluteMapCoordinates(absoluteMapCoordinates).removeEntity();
    }

    public Vector2D getAbsoluteMapCoordinates() {
        return absoluteMapCoordinates;
    }

    public int getX() {
        return absoluteMapCoordinates.getXAsInt();
    }

    public int getY() {
        return absoluteMapCoordinates.getYAsInt();
    }

    public int getSight() {
        return sight;
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

}