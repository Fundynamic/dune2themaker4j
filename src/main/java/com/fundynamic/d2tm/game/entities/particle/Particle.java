package com.fundynamic.d2tm.game.entities.particle;


import com.fundynamic.d2tm.game.behaviors.Destructible;
import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.EntityData;
import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.EntityType;
import com.fundynamic.d2tm.math.Coordinate;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

public class Particle extends Entity implements Destructible {

    private boolean destroyed = false;
    private float sprite = 0;
    private float animationSpeed;

    public Particle(Coordinate coordinate, SpriteSheet spriteSheet, EntityData entityData, EntityRepository entityRepository) {
        super(coordinate, spriteSheet, entityData, null, entityRepository);
        animationSpeed = entityData.animationSpeed;
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.PARTICLE;
    }

    @Override
    public void render(Graphics graphics, int x, int y) {
        if (graphics == null) throw new IllegalArgumentException("Graphics must be not-null");
        Image sprite = getSprite();
        graphics.drawImage(sprite, x, y);
    }

    public Image getSprite() {
        return spriteSheet.getSprite((int) sprite, 0);
    }

    @Override
    public void update(float deltaInSeconds) {
        sprite += animationSpeed * deltaInSeconds;

        if (sprite >= spriteSheet.getHorizontalCount()) {
            destroyed = true;
        }
    }

    @Override
    public void takeDamage(int hitPoints) {
        // particles can't take damage...
    }

    @Override
    public boolean isDestroyed() {
        return destroyed;
    }

    @Override
    public int getHitPoints() {
        return 0;
    }

    @Override
    public String toString() {
        return "Particle{" +
                "destroyed=" + destroyed +
                ", sprite=" + sprite +
                ", animationSpeed=" + animationSpeed +
                '}';
    }
}
