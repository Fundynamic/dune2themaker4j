package com.fundynamic.d2tm.game.entities.projectiles;


import com.fundynamic.d2tm.game.behaviors.Destructible;
import com.fundynamic.d2tm.game.behaviors.Moveable;
import com.fundynamic.d2tm.game.entities.*;
import com.fundynamic.d2tm.game.entities.units.UnitFacings;
import com.fundynamic.d2tm.game.types.EntityData;
import com.fundynamic.d2tm.math.Coordinate;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

public class Projectile extends Entity implements Moveable, Destructible {

    // state
    private Coordinate target;
    private boolean destroyed;
    private float height = 0F; // supposed 'height' of projectile
    private float distanceCalculatedALaunch;

    public Projectile(Coordinate mapCoordinates, SpriteSheet spriteSheet, Player player,
                      EntityData entityData, EntityRepository entityRepository) {
        super(mapCoordinates, spriteSheet, entityData, player, entityRepository);
        target = mapCoordinates;
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.PROJECTILE;
    }

    @Override
    public void die() {
        detonate();
    }

    @Override
    public void render(Graphics graphics, int x, int y) {
        if (graphics == null) throw new IllegalArgumentException("Graphics must be not-null");
        Image sprite = getSprite();

        if (height > 0) {
            drawShadowImage(graphics, x, y, sprite);
        }

        graphics.drawImage(sprite, x, (y - height));
    }

    public void drawShadowImage(Graphics graphics, int x, int y, Image sprite) {
        sprite.setImageColor(0, 0, 0, 0.5f); // set color of image to black, transparent
        int shadowX = x + Math.round(height / 8);
        graphics.drawImage(sprite, shadowX, y);
        sprite.setImageColor(1, 1, 1, 1); // restore drawing to opaque
    }

    public Image getSprite() {
        return spritesheet.getSprite(getFacing(coordinate, target), 0);
    }

    public int getFacing(Vector2D from, Vector2D to) {
        int facing = 0;
        if (entityData.hasFacings() && !from.equals(to)) {
            facing = UnitFacings.calculateFacingSpriteIndex(from, to, entityData.getFacings(), entityData.getChop());
        }
        return facing;
    }

    @Override
    public void update(float deltaInSeconds) {
        if (target != coordinate) {
            float timeCorrectedSpeed = entityData.getRelativeMoveSpeed(deltaInSeconds);
            Vector2D direction = target.min(coordinate);
            Vector2D normalised = direction.normalise();

            // make sure we don't overshoot
            float distance = coordinate.distance(target);
            if (distance < timeCorrectedSpeed) timeCorrectedSpeed = distance;

            Vector2D delta = normalised.scale(timeCorrectedSpeed);
            coordinate = coordinate.add(delta);

            if (canAscendAndDescend()) {
                if (shouldAscend()) {
                    ascend();
                } else if (shouldDescend()) {
                    descend();
                }
            }
        }

        if (getCurrentDistanceToTarget() < 0.1F) {
            detonate();
        }
    }

    public void detonate() {
        if (entityData.hasExplosionId()) {
            entityRepository.explodeAt(getCenteredCoordinate(), entityData, player);
        }

        // do damage on cell / range of cells
        EntitiesSet entities = entityRepository.findAliveEntitiesOfTypeAtVector(coordinate, EntityType.UNIT, EntityType.STRUCTURE);
        entities.each(new EntityHandler() {
            @Override
            public void handle(Entity entity) {
                if (entity.isDestructible()) {
                    Destructible destructibleEntity = (Destructible) entity;
                    destructibleEntity.takeDamage(entityData.damage, origin);
                }
            }
        });
        destroyed = true;
    }

    public void descend() {
        if (height > 0) {
            float maxDescensionAtFlightPercentage = 1f - entityData.startToDescendPercentage;

            float descendProgress = 1.0f - getFlightProgress();
            float progress = descendProgress * (1 / maxDescensionAtFlightPercentage);

            height = Math.min(entityData.maxAscensionHeight, progress * entityData.maxAscensionHeight);
            if (height < 0) height = 0;
        }
    }

    public boolean shouldDescend() {
        return getFlightProgress() > entityData.startToDescendPercentage;
    }

    public void ascend() {
        if (height < entityData.maxAscensionHeight) {
            height = (getFlightProgress() * entityData.maxAscensionHeight) * (1 / entityData.maxAscensionAtFlightPercentage);
            if (height >= entityData.maxAscensionHeight) height = entityData.maxAscensionHeight;
        }
    }

    public boolean shouldAscend() {
        return getFlightProgress() < entityData.maxAscensionAtFlightPercentage;
    }

    public boolean canAscendAndDescend() {
        return entityData.maxAscensionHeight > 0;
    }

    public float getFlightProgress() {
        return 1.0f - (getCurrentDistanceToTarget() / distanceCalculatedALaunch);
    }

    public float getCurrentDistanceToTarget() {
        return target.distance(coordinate);
    }

    @Override
    public void moveTo(Coordinate moveToTarget) {
        Coordinate newTarget = new Coordinate(moveToTarget);
        distanceCalculatedALaunch = target.distance(newTarget);
        if (isHarvester()) {
            HarvesterDeliveryIntents.instance.removeAllIntentsBy(this);
        }
        this.target = newTarget;
    }

    @Override
    public void takeDamage(int hitPoints, Entity origin) {
        // a projectile cannot take damage (yet?, we could somehow make it possible that projectiles can get intercepted?)
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
        return "Projectile{" +
                "target=" + target +
                ", destroyed=" + destroyed +
                '}';
    }

    public Coordinate getTarget() {
        return target;
    }
}
