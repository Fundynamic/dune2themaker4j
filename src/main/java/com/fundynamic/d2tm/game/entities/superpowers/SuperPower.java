package com.fundynamic.d2tm.game.entities.superpowers;

import com.fundynamic.d2tm.game.behaviors.Destructible;
import com.fundynamic.d2tm.game.entities.*;
import com.fundynamic.d2tm.game.entities.projectiles.Projectile;
import com.fundynamic.d2tm.math.Coordinate;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SpriteSheet;


public class SuperPower extends Entity implements Destructible {

    private Coordinate target;
    private boolean destroyed;
    private Coordinate fireStarterCoordinate;

    public SuperPower(Coordinate mapCoordinates, EntityData entityData, Player player, EntityRepository entityRepository) {
        super(mapCoordinates, null, entityData, player, entityRepository);
        target = mapCoordinates;
    }

    @Override
    public void update(float deltaInSeconds) {
        Projectile projectile = entityRepository.placeProjectile(fireStarterCoordinate, entityData.weaponId, player);
        projectile.moveTo(target);
        destroyed = true;
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
