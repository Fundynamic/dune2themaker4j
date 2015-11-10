package com.fundynamic.d2tm.game.entities;


import com.fundynamic.d2tm.math.Coordinate;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SpriteSheet;

public class NullEntity extends Entity {

    public static NullEntity INSTANCE = new NullEntity();

    public NullEntity() {
        super(null, null, null, null, null);
    }

    public NullEntity(Coordinate coordinate, SpriteSheet spriteSheet, EntityData entityData, Player player, EntityRepository entityRepository) {
        super(coordinate, spriteSheet, entityData, player, entityRepository);
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.NONE;
    }

    @Override
    public void render(Graphics graphics, int x, int y) {

    }

    @Override
    public void update(float deltaInSeconds) {

    }
}
