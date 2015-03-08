package com.fundynamic.d2tm.game.entities;

import com.fundynamic.d2tm.game.behaviors.Renderable;
import com.fundynamic.d2tm.game.behaviors.Selectable;
import com.fundynamic.d2tm.game.behaviors.Updateable;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.SpriteSheet;

public abstract class Entity implements Renderable, Updateable {

    // TODO: these might need to be absolute pixel coordinates to make smooth movements possible
    //       instead of cell x,y coordinates which it is right now.
    protected final Vector2D mapCoordinates;
    protected final SpriteSheet spriteSheet;
    protected final int sight;
    protected final Player player;

    public Entity(Vector2D mapCoordinates, SpriteSheet spriteSheet, int sight, Player player) {
        this.mapCoordinates = mapCoordinates;
        this.spriteSheet = spriteSheet;
        this.sight = sight;
        this.player = player;
    }

    public Vector2D getMapCoordinates() {
        return mapCoordinates;
    }

    public boolean isSelectable() {
        return this instanceof Selectable;
    }

    public int getSight() {
        return sight;
    }
}