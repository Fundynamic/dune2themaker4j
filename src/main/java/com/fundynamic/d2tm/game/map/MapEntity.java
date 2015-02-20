package com.fundynamic.d2tm.game.map;

import com.fundynamic.d2tm.game.math.Vector2D;
import org.newdawn.slick.SpriteSheet;

/**
 * An entity bound to the map
 */
public class MapEntity {

    protected final Vector2D mapCoordinates;
    protected final SpriteSheet spriteSheet;

    public MapEntity(Vector2D mapCoordinates, SpriteSheet spriteSheet) {
        this.mapCoordinates = mapCoordinates;
        this.spriteSheet = spriteSheet;
    }

    public Vector2D getMapCoordinates() {
        return mapCoordinates;
    }

}
