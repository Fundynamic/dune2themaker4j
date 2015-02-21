package com.fundynamic.d2tm.game.entities;

import com.fundynamic.d2tm.game.behaviors.Renderable;
import com.fundynamic.d2tm.game.behaviors.Selectable;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.SpriteSheet;

public abstract class Entity implements Renderable {

    // TODO: these might need to be absolute pixel coordinates to make smooth movements possible
    //       instead of cell x,y coordinates which it is right now.
    protected final Vector2D mapCoordinates;
    protected final SpriteSheet spriteSheet;

    public Entity(Vector2D mapCoordinates, SpriteSheet spriteSheet) {
        this.mapCoordinates = mapCoordinates;
        this.spriteSheet = spriteSheet;
    }

    public Vector2D getMapCoordinates() {
        return mapCoordinates;
    }

    public boolean isSelectable() {
        return this instanceof Selectable;
    }
}
