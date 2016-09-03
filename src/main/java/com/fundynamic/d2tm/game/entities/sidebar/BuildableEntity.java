package com.fundynamic.d2tm.game.entities.sidebar;


import com.fundynamic.d2tm.game.behaviors.AbsoluteRenderable;
import com.fundynamic.d2tm.game.behaviors.Renderable;
import com.fundynamic.d2tm.game.entities.EntityData;
import com.fundynamic.d2tm.game.entities.Rectangle;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

/**
 * <h1>General</h1>
 * An entity that can be built by a {@link com.fundynamic.d2tm.game.behaviors.EntityBuilder}. It contains little
 * state giving hints if it can be built, its progress and so forth.
 */
public class BuildableEntity {

    /**
     * The thing it is / can be building
     */
    private EntityData entityData;

    public BuildableEntity(EntityData entityData) {
        this.entityData = entityData;
    }

    public boolean hasBuildIcon() {
        return entityData.buildIcon != null;
    }

    public Image getBuildIcon() {
        return entityData.buildIcon;
    }
}
