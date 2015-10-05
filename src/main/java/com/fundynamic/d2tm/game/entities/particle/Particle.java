package com.fundynamic.d2tm.game.entities.particle;


import com.fundynamic.d2tm.game.behaviors.Destructible;
import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.EntityData;
import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.EntityType;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

public class Particle extends Entity implements Destructible {

    boolean destroyed = false;
    private float sprite = 0;
    private float animationSpeed;

    public Particle(Vector2D mapCoordinates, SpriteSheet spriteSheet, EntityData entityData, EntityRepository entityRepository) {
        super(mapCoordinates, spriteSheet, 0, null, entityRepository);
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

    }

    @Override
    public boolean isDestroyed() {
        return destroyed;
    }
}
