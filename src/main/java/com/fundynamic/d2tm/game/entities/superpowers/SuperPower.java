package com.fundynamic.d2tm.game.entities.superpowers;

import com.fundynamic.d2tm.game.entities.*;
import com.fundynamic.d2tm.math.Coordinate;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SpriteSheet;


public class SuperPower extends Entity {

    public SuperPower(Coordinate coordinate, SpriteSheet spritesheet, EntityData entityData, Player player, EntityRepository entityRepository) {
        super(coordinate, spritesheet, entityData, player, entityRepository);
    }

    @Override
    public void update(float deltaInSeconds) {

    }

    @Override
    public void render(Graphics graphics, int x, int y) {
        // NA
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.SUPERPOWER;
    }

}
