package com.fundynamic.d2tm.game.entities.superpowers;

import com.fundynamic.d2tm.game.behaviors.Destructible;
import com.fundynamic.d2tm.game.entities.*;
import com.fundynamic.d2tm.game.entities.projectiles.Projectile;
import com.fundynamic.d2tm.game.map.Trigonometry;
import com.fundynamic.d2tm.game.types.EntityData;
import com.fundynamic.d2tm.math.Coordinate;
import com.fundynamic.d2tm.math.Random;
import org.newdawn.slick.Graphics;

import java.util.HashSet;
import java.util.Set;

import static com.fundynamic.d2tm.game.entities.superpowers.SuperPower.SuperPowerState.DONE;
import static com.fundynamic.d2tm.game.entities.superpowers.SuperPower.SuperPowerState.EXPLODING;
import static com.fundynamic.d2tm.game.entities.superpowers.SuperPower.SuperPowerState.LAUNCHED;
import static com.fundynamic.d2tm.game.map.Cell.HALF_TILE;
import static com.fundynamic.d2tm.game.map.Cell.TILE_SIZE;


public class SuperPower extends Entity implements Destructible {

    // these are actually Deathhand states (more abstract would be SuperPower states)
    enum SuperPowerState {
        INITIAL, LAUNCHED, EXPLODING, DONE
    }

    private boolean destroyed;

    private Coordinate fireStarterCoordinate;   // where to start launching missile
    private Coordinate target;                  // where to head to
    private Coordinate detonatedAt;             // where the projectile exploded (could theoretically be a different coordinate)

    private SuperPowerState state;

    private float timePassed;
    private float timePassedSinceLastDetonation;

    // in memory coordinates to damage
    private Set<Coordinate> coordinatesToDamage;

    public SuperPower(Coordinate coordinate, EntityData entityData, Player player, EntityRepository entityRepository) {
        super(coordinate, null, entityData, player, entityRepository);
        state = SuperPowerState.INITIAL;
    }

    // TODO: onCreate!?
    public static float RingOfFireTotalTimeDuration = 1f; // 1 second explosion

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

                if (timePassed > RingOfFireTotalTimeDuration) {
                    state = DONE;
                } else {
                    double timeToCreateNewRingOfFireThreshold = 0.1;
                    // time to create a new ring
                    if (timePassedSinceLastDetonation > timeToCreateNewRingOfFireThreshold) {
                        float distance = (float)((Math.log(timePassed + 1) / Math.log(1.25f)));
                        float maxDistance = (float)((Math.log(RingOfFireTotalTimeDuration + 1) / Math.log(1.25f)));

                        double centerX = detonatedAt.getX();
                        double centerY = detonatedAt.getY();
                        float rangeInPixels = (distance * TILE_SIZE);
                        float rangeOfSmokeInPixels = rangeInPixels * 2.5f;
                        float maxDistanceInPixels = maxDistance * 2.5f * TILE_SIZE;

                        createRingOfFire(centerX, centerY, rangeInPixels);
                        createRingOfSmoke(centerX, centerY, rangeOfSmokeInPixels);
                        damageInCircularField(centerX, centerY, maxDistanceInPixels);
                        timePassedSinceLastDetonation = 0;
                    }
                }

                break;
            case DONE:
                destroyed = true;
                break;
        }
    }

    public void damageInCircularField(double centerX, double centerY, float maxDistance) {
        if (coordinatesToDamage == null) {
            this.coordinatesToDamage = determineCoordinatesToDamage(centerX, centerY, maxDistance);
        }

        float damageAtCenter = 65; // the closer to the center the more damage is dealt, the further away, the less damage is received

        Coordinate centerCoordinate = Coordinate.create((float) centerX, (float) centerY);

        EntitiesSet entities = entityRepository.findDestructibleEntities(coordinatesToDamage);

        entities.forEach(entity -> {
            if (!entity.isDestructible()) return;
            for (Coordinate entityCoordinate : entity.getAllCellsAsCoordinates()) {
                float actualDistance = centerCoordinate.distance(entityCoordinate);
                int damageHitpoints = getDamageHitpoints(damageAtCenter, maxDistance, actualDistance);
                ((Destructible) entity).takeDamage(damageHitpoints, null);
            }
        });
    }

    public int getDamageHitpoints(float damageAtCenter, float maxDistance, float actualDistance) {
        float distanceNormalised = Math.max((maxDistance - actualDistance)/maxDistance, 0F);
        return (int)(damageAtCenter * distanceNormalised);
    }

    public Set<Coordinate> determineCoordinatesToDamage(double centerX, double centerY, float maxDistanceInPixels) {
        Set<Coordinate> coordinatesToDamage = new HashSet<>();

        int steps = Math.round(maxDistanceInPixels / HALF_TILE);

        for (int rangeStep = 0; rangeStep < steps; rangeStep++) {
            for (int degrees=0; degrees < 360; degrees += 3) {

                // calculate as if we would draw a circle and remember the coordinates
                float rangeInPixels = (rangeStep * TILE_SIZE);
                double circleX = (centerX + (Trigonometry.cos[degrees] * rangeInPixels));
                double circleY = (centerY + (Trigonometry.sin[degrees] * rangeInPixels));

                // convert back the pixel coordinates back to a cell
                Coordinate coordinate = Coordinate.create(
                        (int) Math.ceil(circleX), (int) Math.ceil(circleY)
                ).toMapCoordinate().toCoordinate(); // snap it, so we can filter out a lot of duplicates

                coordinatesToDamage.add(coordinate);
            }
        }

        return coordinatesToDamage;
    }

    public void createRingOfFire(double centerX, double centerY, float rangeInPixels) {

        for (int degrees=0; degrees < 360; degrees += 6) {
            // calculate as if we would draw a circle and remember the coordinates
            double circleX = (centerX + (Trigonometry.cos[degrees] * rangeInPixels));
            double circleY = (centerY + (Trigonometry.sin[degrees] * rangeInPixels));

            // TODO: Use some sound manager that makes the sounds, as opposed to the camera and also right volume, etc.
            // GH: https://github.com/Fundynamic/dune2themaker4j/issues/157
            // TODO: Select random sound from 'sound group'
            // GH: https://github.com/Fundynamic/dune2themaker4j/issues/158

            // play sound if it has one in a random manner so it won't blow up your speakers by creating a gazillion
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

    public void createRingOfSmoke(double centerX, double centerY, float rangeInPixels) {
        for (int degrees=0; degrees < 360; degrees += 6) {
            // calculate as if we would draw a circle and remember the coordinates
            double circleX = (centerX + (Trigonometry.cos[degrees] * rangeInPixels));
            double circleY = (centerY + (Trigonometry.sin[degrees] * rangeInPixels));
            if (Random.getRandomBetween(0, 100) < 25) {
                entityRepository.placeExplosionWithCenterAt(Coordinate.create((int) Math.ceil(circleX), (int) Math.ceil(circleY)), player, "SMOKE");
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

    public SuperPowerState getState() {
        return state;
    }
}
