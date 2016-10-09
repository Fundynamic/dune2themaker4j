package com.fundynamic.d2tm.game.entities.entitybuilders;


import com.fundynamic.d2tm.game.entities.EntityData;
import com.fundynamic.d2tm.game.entities.Player;

/**
 * <h1>General</h1>
 * An entity that can be built by a {@link com.fundynamic.d2tm.game.behaviors.EntityBuilder}.
 * <h2>Coming to life</h2>
 * <p>
 *     This buildable entity spawns its Entity equivalent on the Battlefield
 * </p>
 */
public class SpawningBuildableEntity extends AbstractBuildableEntity {

    public SpawningBuildableEntity(EntityData entityData, Player player) {
        super(entityData, player);
    }

}
