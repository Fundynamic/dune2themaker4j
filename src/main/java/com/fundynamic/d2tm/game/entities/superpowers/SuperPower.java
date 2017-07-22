package com.fundynamic.d2tm.game.entities.superpowers;

import com.fundynamic.d2tm.game.behaviors.Destructible;
import com.fundynamic.d2tm.game.entities.*;
import com.fundynamic.d2tm.game.entities.projectiles.Projectile;
import com.fundynamic.d2tm.game.map.Trigonometry;
import com.fundynamic.d2tm.game.types.EntityData;
import com.fundynamic.d2tm.math.Coordinate;
import com.fundynamic.d2tm.math.Random;
import org.newdawn.slick.Graphics;

import static com.fundynamic.d2tm.game.entities.superpowers.SuperPower.SuperPowerState.DONE;
import static com.fundynamic.d2tm.game.entities.superpowers.SuperPower.SuperPowerState.EXPLODING;
import static com.fundynamic.d2tm.game.entities.superpowers.SuperPower.SuperPowerState.LAUNCHED;
import static com.fundynamic.d2tm.game.map.Cell.TILE_SIZE;


public class SuperPower extends Entity implements Destructible {

    private Coordinate detonatedAt;

    enum SuperPowerState {
        INITIAL, LAUNCHED, EXPLODING, DONE
    }

    private Coordinate target;
    private boolean destroyed;
    private Coordinate fireStarterCoordinate;
    private SuperPowerState state;
    private float timePassed;
    private float timePassedSinceLastDetonation;

    public SuperPower(Coordinate mapCoordinates, EntityData entityData, Player player, EntityRepository entityRepository) {
        super(mapCoordinates, null, entityData, player, entityRepository);
        target = mapCoordinates;
        state = SuperPowerState.INITIAL;
    }

    // TODO: onCreate!?
    private static float RingOfFireDuration = 1;

    @Override
    public void update(float deltaInSeconds) {
        switch (state) {
            case INITIAL:
                Projectile projectile = entityRepository.placeProjectile(fireStarterCoordinate, entityData.weaponId, player);
                projectile.onEvent(EventType.ENTITY_DESTROYED, this, s -> s.onProjectileDestroyed(projectile));
                projectile.moveTo(target);
                state = LAUNCHED;
                break;
            case EXPLODING:
                timePassed += deltaInSeconds;
                timePassedSinceLastDetonation += deltaInSeconds;

                if (timePassed > RingOfFireDuration) {
                    state = DONE;
                } else if (timePassedSinceLastDetonation > 0.1) {
                    float distance = (float)((Math.log(timePassed + 1) / Math.log(1.25f)));

                    double centerX = detonatedAt.getX();
                    double centerY = detonatedAt.getY();
                    float rangeInPixels = (distance * TILE_SIZE);

                    createRingOfFire(centerX, centerY, rangeInPixels);
                    timePassedSinceLastDetonation = 0;
                }

                break;
            case DONE:
                destroyed = true;
                break;
        }
    }

    public void createRingOfFire(double centerX, double centerY, float rangeInPixels) {
        for (int degrees=0; degrees < 360 / 6; degrees++) {
            // calculate as if we would draw a circle and remember the coordinates
            double circleX = (centerX + (Trigonometry.cos[degrees * 6] * rangeInPixels));
            double circleY = (centerY + (Trigonometry.sin[degrees * 6] * rangeInPixels));

            // TODO: Use some sound manager that makes the sounds, as opposed to the camera and also right volume, etc.
            // GH: https://github.com/Fundynamic/dune2themaker4j/issues/157
            // TODO: Select random sound from 'sound group'
            // GH: https://github.com/Fundynamic/dune2themaker4j/issues/158

            // play sound if it has one in a random manner so it won't blow up your speakers by creating a gazilion
            // explosion particles and thus sounds
            if (entityData.hasExplosionId()) {
                if (Random.getRandomBetween(0, 100) < 4) {
                    EntityData explosion = entityRepository.getEntityData(EntityType.PARTICLE, entityData.explosionId);
                    if (explosion.hasSound()) {
                        explosion.soundData.sound.play();
                    }
                }
                entityRepository.placeExplosionWithCenterAt(Coordinate.create((int) Math.ceil(circleX), (int) Math.ceil(circleY)), player, entityData.explosionId);
            }
        }
    }

    public Void onProjectileDestroyed(Projectile projectile) {
        detonatedAt = projectile.getCenteredCoordinate();
        state = EXPLODING;
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
