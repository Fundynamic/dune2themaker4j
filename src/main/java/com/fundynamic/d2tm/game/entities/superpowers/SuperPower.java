package com.fundynamic.d2tm.game.entities.superpowers;

import com.fundynamic.d2tm.game.behaviors.Destructible;
import com.fundynamic.d2tm.game.entities.*;
import com.fundynamic.d2tm.game.entities.projectiles.Projectile;
import com.fundynamic.d2tm.math.Coordinate;
import org.newdawn.slick.Graphics;


public class SuperPower extends Entity implements Destructible {

    private Coordinate target;
    private boolean destroyed;
    private Coordinate fireStarterCoordinate;
    private boolean updateCalled;

    public SuperPower(Coordinate mapCoordinates, EntityData entityData, Player player, EntityRepository entityRepository) {
        super(mapCoordinates, null, entityData, player, entityRepository);
        target = mapCoordinates;
    }

    // TODO: onCreate!?

    @Override
    public void update(float deltaInSeconds) {
        if (updateCalled) return;
        Projectile projectile = entityRepository.placeProjectile(fireStarterCoordinate, entityData.weaponId, player);
        projectile.onEvent(EventType.ENTITY_DESTROYED, this, s -> s.onProjectileDestroyed(projectile));
        projectile.moveTo(target);
        updateCalled = true;
    }

    public Void onProjectileDestroyed(Projectile projectile) {
        if (entityRepository.allUnits().hasItems()) {
            for (Entity target : entityRepository.allUnits()) {
                Projectile retaliationProjectile = entityRepository.placeProjectile(projectile.getCenteredCoordinate(), entityData.weaponId, player);
                retaliationProjectile.moveTo(target.getCoordinate());
            }
        }
        destroyed = true;
        return null;
    }

    @Override
    public void render(Graphics graphics, int x, int y) {
        // NA
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.SUPERPOWER;
    }

    @Override
    public void takeDamage(int hitPoints, Entity origin) {
        // NA
    }

    @Override
    public boolean isDestroyed() {
        return destroyed;
    }

    @Override
    public int getHitPoints() {
        return 0; // NA
    }

    @Override
    public String toString() {
        return "SuperPower{" +
                "target=" + target +
                ", destroyed=" + destroyed +
                '}';
    }

    public void setFireStarterCoordinate(Coordinate fireStarterCoordinate) {
        this.fireStarterCoordinate = fireStarterCoordinate;
    }

    public Coordinate getFireStarterCoordinate() {
        return fireStarterCoordinate;
    }

    public void setTarget(Coordinate target) {
        this.target = target;
    }

    public Coordinate getTarget() {
        return target;
    }

}
