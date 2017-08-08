package com.fundynamic.d2tm.game.entities;


import com.fundynamic.d2tm.math.Coordinate;
import org.newdawn.slick.Graphics;

/**
 * A null entity to prevent 'null checks'. But rather have an entity doing nothing. See also the Null Object Pattern.
 */
public class NullEntity extends Entity {

    public static NullEntity INSTANCE = new NullEntity();

    public NullEntity() {
        super(Coordinate.create(0,0), null, null, null, null);
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.NONE;
    }

    @Override
    public void die() {

    }

    @Override
    public void render(Graphics graphics, int x, int y) {

    }

    @Override
    public void update(float deltaInSeconds) {

    }

    public static boolean is(Entity entity) {
        return NullEntity.INSTANCE.equals(entity) || entity instanceof NullEntity;
    }
}
