package com.fundynamic.d2tm.game.entities.projectiles;


import com.fundynamic.d2tm.game.behaviors.Destructible;
import com.fundynamic.d2tm.game.behaviors.Moveable;
import com.fundynamic.d2tm.game.entities.*;
import com.fundynamic.d2tm.game.entities.units.UnitFacings;
import com.fundynamic.d2tm.math.Coordinate;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

public class Projectile extends Entity implements Moveable, Destructible {

    // state
    private Coordinate target;
    private boolean destroyed;

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
    public void render(Graphics graphics, int x, int y) {
        if (graphics == null) throw new IllegalArgumentException("Graphics must be not-null");
        Image sprite = getSprite();
        graphics.drawImage(sprite, x, y);
    }

    public Image getSprite() {
        return spriteSheet.getSprite(getFacing(coordinate, target), 0);
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
        }

        if (target.distance(coordinate) < 0.1F) {
            if (entityData.hasExplosionId()) {
                entityRepository.explodeAt(getCenteredCoordinate(), entityData, player);
            }

            // do damage on cell / range of cells
            EntitiesSet entities = entityRepository.findEntitiesOfTypeAtVector(coordinate, EntityType.UNIT, EntityType.STRUCTURE);
            entities.each(new EntityHandler() {
                @Override
                public void handle(Entity entity) {
                    if (entity.isDestructible()) {
                        Destructible destructibleEntity = (Destructible) entity;
                        destructibleEntity.takeDamage(entityData.damage);
                    }
                }
            });
            destroyed = true;
        }
    }

    @Override
    public void moveTo(Vector2D target) {
        this.target = new Coordinate(target);
    }

    @Override
    public void takeDamage(int hitPoints) {
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
