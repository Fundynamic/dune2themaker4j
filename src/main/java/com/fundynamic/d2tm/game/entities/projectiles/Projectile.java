package com.fundynamic.d2tm.game.entities.projectiles;


import com.fundynamic.d2tm.game.behaviors.Destructible;
import com.fundynamic.d2tm.game.behaviors.Moveable;
import com.fundynamic.d2tm.game.entities.*;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

public class Projectile extends Entity implements Moveable, Destructible {

    // state
    private Vector2D target;
    private boolean destroyed;

    // entity properties
    private float speed;
    private int damage;
    private int explosionId;


    public Projectile(Vector2D mapCoordinates, SpriteSheet spriteSheet, Player player,
                      EntityData entityData, EntityRepository entityRepository) {
        super(mapCoordinates, spriteSheet, entityData, player, entityRepository);
        target = mapCoordinates;
        this.speed = entityData.moveSpeed;
        this.damage = entityData.damage;
        this.explosionId = entityData.explosionId;
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.PROJECTILE;
    }


    @Override
    public void render(Graphics graphics, int x, int y) {
        if (graphics == null) throw new IllegalArgumentException("Graphics must be not-null");
        Image sprite = getSprite();
        graphics.drawImage(sprite, x, y);
    }

    public Image getSprite() {
        return spriteSheet.getSprite(getFacing(absoluteCoordinates, target), 0);
    }

    /**
     * Calculates facing index.
     *
     * @return
     */
    public int getFacing(Vector2D from, Vector2D to) {
        int facing = 0;
        if (entityData.hasFacings()) {
            if (from != to) {
                double angle = from.angleTo(to);
                float chop = entityData.getChop();
                angle += (chop / 2);
                facing = (int) (angle / chop) % entityData.getFacings();
            }
        }
        return facing;
    }

    @Override
    public void update(float deltaInSeconds) {
        if (target != absoluteCoordinates) {
            float timeCorrectedSpeed = speed * deltaInSeconds;
            Vector2D direction = target.min(absoluteCoordinates);
            Vector2D normalised = direction.normalise();

            // make sure we don't overshoot
            float distance = absoluteCoordinates.distance(target);
            if (distance < timeCorrectedSpeed) timeCorrectedSpeed = distance;

            absoluteCoordinates = absoluteCoordinates.add(normalised.scale(timeCorrectedSpeed));
        }

        if (target.distance(absoluteCoordinates) < 0.1F) {
            if (explosionId > -1) {
                // spawn explosion
                entityRepository.placeOnMap(absoluteCoordinates, EntityType.PARTICLE, explosionId, player);
            }

            // do damage on cell / range of cells
            EntitiesSet entities = entityRepository.findEntitiesOfTypeAtVector(absoluteCoordinates, EntityType.UNIT, EntityType.STRUCTURE);
            if (entities.hasAny()) {
                Entity entity = entities.getFirst();
                if (entity.isDestructible()) {
                    Destructible destructibleEntity = (Destructible) entity;
                    destructibleEntity.takeDamage(damage);
                }
            }
            destroyed = true;
        }
    }

    @Override
    public void moveTo(Vector2D target) {
        this.target = target;
    }

    @Override
    public void takeDamage(int hitPoints) {

    }

    @Override
    public boolean isDestroyed() {
        return destroyed;
    }
}
