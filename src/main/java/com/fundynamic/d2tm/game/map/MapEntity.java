package com.fundynamic.d2tm.game.map;

import com.fundynamic.d2tm.game.behaviors.Renderable;
import com.fundynamic.d2tm.game.behaviors.Selectable;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.SpriteSheet;

/**
 * An entity bound to the map
 */
public abstract class MapEntity implements Renderable {

    protected final Vector2D mapCoordinates;
    protected final SpriteSheet spriteSheet;

    public MapEntity(Vector2D mapCoordinates, SpriteSheet spriteSheet) {
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
