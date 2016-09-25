package com.fundynamic.d2tm.game.entities.sidebar;


import com.fundynamic.d2tm.game.behaviors.Updateable;
import com.fundynamic.d2tm.game.entities.EntityData;
import org.newdawn.slick.Image;

/**
 * <h1>General</h1>
 * An entity that can be built by a {@link com.fundynamic.d2tm.game.behaviors.EntityBuilder}.
 * <h2>Coming to life</h2>
 * <p>
 *     This buildable entity spawns its Entity equivalent on the Battlefield
 * </p>
 */
public class SpawningBuildableEntity extends AbstractBuildableEntity {

    public SpawningBuildableEntity(EntityData entityData) {
        super(entityData);
    }

}
