package com.fundynamic.d2tm.game.entities;

import com.fundynamic.d2tm.game.behaviors.*;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.SpriteSheet;

public abstract class Entity implements Renderable, Updateable {

    private static final float TILE_SIZE = 32.0f;

    // Final properties of unit
    protected final SpriteSheet spriteSheet;
    protected final int sight;
    protected final Player player;

    // TODO: these might need to be absolute pixel coordinates to make smooth movements possible
    //       instead of cell x,y coordinates which it is right now.
    protected Vector2D mapCoordinates;

    public Entity(Vector2D mapCoordinates, SpriteSheet spriteSheet, int sight, Player player) {
        this.mapCoordinates = mapCoordinates;
        this.spriteSheet = spriteSheet;
        this.sight = sight;
        this.player = player;
        player.addEntity(this);
    }

    public Vector2D getMapCoordinates() {
        return mapCoordinates;
    }

    public Vector2D getAbsoluteMapPixelCoordinates() {
        float absX = mapCoordinates.getX() * TILE_SIZE;
        float absY = mapCoordinates.getY() * TILE_SIZE;
        return Vector2D.create(absX, absY);
    }

    public int getSight() {
        return sight;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean belongsToPlayer(Player other) {
        return player.equals(other);
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

}