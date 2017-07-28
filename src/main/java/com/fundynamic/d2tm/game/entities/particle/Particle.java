package com.fundynamic.d2tm.game.entities.particle;


import com.fundynamic.d2tm.game.behaviors.Destructible;
import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.EntityType;
import com.fundynamic.d2tm.game.types.EntityData;
import com.fundynamic.d2tm.math.Coordinate;
import com.fundynamic.d2tm.math.Random;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

public class Particle extends Entity implements Destructible {

    private boolean destroyed = false;
    private float sprite = 0;
    private float animationSpeed;
    private float alpha = 1.0f;
    private float scale = 1.0f;

    public Particle(Coordinate coordinate, SpriteSheet spriteSheet, EntityData entityData, EntityRepository entityRepository) {
        super(coordinate, spriteSheet, entityData, null, entityRepository);
        animationSpeed = entityData.animationSpeed;
//        scale += Math.random() * 3;
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.PARTICLE;
    }

    @Override
    public void render(Graphics graphics, int x, int y) {
        if (graphics == null) throw new IllegalArgumentException("Graphics must be not-null");
        Image sprite = getSprite();

        sprite.setImageColor(1, 1, 1, alpha);
        sprite.draw(x, y, scale);
        sprite.setImageColor(1, 1, 1, 1);

        graphics.resetTransform();
        graphics.scale(1f,1f);
    }

    public Image getSprite() {
        return spritesheet.getSprite((int) sprite, 0);
    }

    @Override
    public void update(float deltaInSeconds) {
        sprite += EntityData.getRelativeSpeed(animationSpeed, deltaInSeconds);

        if ("SMOKE".equals(this.entityData.name)) {
            alpha = 1f - (sprite / spritesheet.getHorizontalCount());
        } else {
            alpha = 1.5f - (sprite / spritesheet.getHorizontalCount()); // all other sprites never fade out entirely
        }

        if (sprite >= spritesheet.getHorizontalCount()) {
            destroyed = true;
        }
    }

    @Override
    public void takeDamage(int hitPoints, Entity origin) {
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
