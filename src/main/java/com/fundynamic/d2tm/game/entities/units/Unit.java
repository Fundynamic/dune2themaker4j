package com.fundynamic.d2tm.game.entities.units;

import com.fundynamic.d2tm.game.behaviors.*;
import com.fundynamic.d2tm.game.entities.*;
import com.fundynamic.d2tm.game.entities.predicates.BelongsToPlayer;
import com.fundynamic.d2tm.game.entities.predicates.NotPredicate;
import com.fundynamic.d2tm.game.entities.predicates.PredicateBuilder;
import com.fundynamic.d2tm.game.entities.projectiles.Projectile;
import com.fundynamic.d2tm.game.entities.units.states.*;
import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.rendering.gui.battlefield.RenderQueue;
import com.fundynamic.d2tm.math.Coordinate;
import com.fundynamic.d2tm.math.Random;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.Graphics;

import static com.fundynamic.d2tm.game.map.Cell.TILE_SIZE;

/**
 * Should become observable with RxJava!
 */
public class Unit extends Entity implements Selectable, Moveable, Destructible, Destroyer, Focusable {

    public static final int GUARD_TIMER_INTERVAL = 5;

    // state
    private UnitState state;

    // Behaviors
    private FadingSelection fadingSelection;

    // use contexts!?
    protected final HitPointBasedDestructibility hitPointBasedDestructibility;

    private RenderQueueEnrichableWithFacingLogic bodyFacing;
    private RenderQueueEnrichableWithFacingLogic cannonFacing;

    private Vector2D target;                // the (end) goal to attack/move
    private Vector2D nextTargetToMoveTo;    // the next step/target to move to (ie, this is a step towards the 'real' target)

    // Dependencies
    private final Map map;

    // Drawing 'movement' from cell to cell
    private Vector2D offset;

    private Entity entityToAttack;
    private float attackTimer; // needed for attackRate

    // give units a bit more intelligence
    private float guardTimer = 0;

    public Unit(Map map, Coordinate coordinate, RenderQueueEnrichableWithFacingLogic unitSpriteSheet, RenderQueueEnrichableWithFacingLogic barrelSpriteSheet, FadingSelection fadingSelection, HitPointBasedDestructibility hitPointBasedDestructibility, Player player, EntityData entityData, EntityRepository entityRepository) {
        super(coordinate, unitSpriteSheet, entityData, player, entityRepository);
        this.map = map;
        this.bodyFacing = unitSpriteSheet;
        this.cannonFacing = barrelSpriteSheet;

        this.fadingSelection = fadingSelection;
        this.hitPointBasedDestructibility = hitPointBasedDestructibility;
        this.target = coordinate;
        this.nextTargetToMoveTo = coordinate;
        this.offset = Vector2D.zero();
        this.guardTimer = Random.getRandomBetween(0, GUARD_TIMER_INTERVAL);
        this.state = new IdleState(this, entityRepository, map);
        if (entityData.moveSpeed < 0.0001f) {
            throw new IllegalArgumentException("The speed of this unit is so slow, you must be joking right? - given moveSpeed is " + entityData.moveSpeed);
        }
    }

    @Override
    public void render(Graphics graphics, int x, int y) {
        if (graphics == null) throw new IllegalArgumentException("Graphics must be not-null");

        // Todo: get rid of offset, because those are used for cell-by-cell movements. This could
        // perhaps be optimized? (without any offsets?)
        int drawY = y + offset.getYAsInt();
        int drawX = x + offset.getXAsInt();

        bodyFacing.render(graphics, drawX, drawY);
        cannonFacing.render(graphics, drawX, drawY);
    }

    @Override
    public void update(float deltaInSeconds) {
        if (hitPointBasedDestructibility.hasDied()) {
            die();
        }

        Cell unitCell = map.getCellFor(this);

        // we can harvest
        if (isHarvester() && unitCell.isHarvestable()) {
            harvesting();
        }

        state.update(deltaInSeconds);

        if (this.isDestroyed()) {
            return;
        }

        if (shouldAttack()) {
            chaseOrAttack(deltaInSeconds);
        }

        cannonFacing.update(deltaInSeconds);
        bodyFacing.update(deltaInSeconds);

        if (!shouldMove() && !shouldAttack()) {
            guardTimer += deltaInSeconds;
            if (guardTimer > GUARD_TIMER_INTERVAL) {
                guardTimer = 0F;

                // scan environment within range for enemies
                EntitiesSet entities = entityRepository.findEntitiesOfTypeAtVectorWithinDistance(getCenteredCoordinate(), entityData.sight * 32, EntityType.UNIT, EntityType.STRUCTURE);

                EntitiesSet enemyEntities = entities.filter(new NotPredicate(BelongsToPlayer.instance(player)));

                if (enemyEntities.isEmpty()) {
                    if (this.getPlayer().isCPU()) {
                        //TODO: all enemy units seem to attack always!?
                        float distance = 131072; // 64X64X32
                        Entity enemyToAttack = null;
                        for (Entity entity : entities) {
                            if (!(entity instanceof Unit)) continue;
                            Unit unit = (Unit) entity;
                            if (!unit.hasEnemyToAttack()) continue;

                            float distanceToEnemyOfFriend = unit.getEntityToAttack().distance(this);
                            if (distanceToEnemyOfFriend < distance) {
                                distance = distanceToEnemyOfFriend;
                                enemyToAttack = unit.getEntityToAttack();
                            }
                        }

                        if (enemyToAttack != null) {
                            attack(enemyToAttack);
                        } else {
                            EntitiesSet allUnits = entityRepository.allUnits();
                            enemyEntities = allUnits.filter(new NotPredicate(BelongsToPlayer.instance((player))));

                            distance = 131072; // 64X64X32
                            enemyToAttack = null;
                            for (Entity entity : enemyEntities) {
                                if (!(entity instanceof Unit)) continue;
                                Unit unit = (Unit) entity;

                                float distanceToEnemyUnit = unit.distance(this);
                                if (distanceToEnemyUnit < distance) {
                                    distance = distanceToEnemyUnit;
                                    enemyToAttack = unit;
                                }
                            }

                            if (enemyToAttack != null) {
                                attack(enemyToAttack);
                            }
                        }
                    } // HACK HACK: CPU thing here

                } else {
                    attack(enemyEntities.getFirst());
                }
            }
        }

        fadingSelection.update(deltaInSeconds);
    }

    public boolean shouldMove() {
        return !this.target.equals(coordinate);
    }

    public boolean shouldAttack() {
        // we consider attacking when we have stopped moving, and when we have some entity to attack
        return !shouldMove() && hasEnemyToAttack();
    }

    public boolean hasEnemyToAttack() {
        return entityToAttack != null;
    }

    public void chaseOrAttack(float deltaInSeconds) {
        float attackRange = entityData.attackRange;
        // ok, we don't need to move, so lets see if we are in range
        if (distance(entityToAttack) <= attackRange) {
            // in range!!
            bodyFacing.desireToFaceTo(UnitFacings.getFacingInt(coordinate, entityToAttack.getCoordinate()));
            cannonFacing.desireToFaceTo(UnitFacings.getFacingInt(coordinate, entityToAttack.getCoordinate()));

            if (((Destructible) entityToAttack).isDestroyed()) {
                // target is destroyed, so stop attacking...
                entityToAttack = null;
            } else {
                // target is not yet destroyed
                boolean readyToFire = false;

                if (cannonFacing.isRequiredToFaceEnemyBeforeShooting()) {
                    readyToFire = cannonFacing.isFacingDesiredFacing();
                } else if (bodyFacing.isRequiredToFaceEnemyBeforeShooting()) {
                    readyToFire = bodyFacing.isFacingDesiredFacing();
                }

                if (readyToFire) { // unit is facing target, commence attacking

                    // weird check here, but we should have a weapon before we can fire...
                    if (entityData.hasWeaponId()) {
                        attackTimer += entityData.getRelativeAttackRate(deltaInSeconds);

                        // fire projectiles! - we use this while loop so that in case if insane high number of attack
                        // rates we can keep up with slow FPS
                        while(attackTimer > 1.0F) {
                            Projectile projectile = entityRepository.placeProjectile(coordinate.add(getHalfSize()), entityData.weaponId, this);
                            projectile.moveTo(entityToAttack.getRandomPositionWithin());
                            attackTimer -= 1.0F;
                        }
                    }
                } else {
                    // not facing yet, turn towards it
                    bodyFacing.update(deltaInSeconds);
                }
            }
        } else {
            if (((Destructible) entityToAttack).isDestroyed()) {
                entityToAttack = null;
            } else {
//                target = decideWhatCellToMoveToNextOrStopMovingWhenNotPossible(entityToAttack.getCoordinate());
                moveTo(entityToAttack.getCoordinate());
            }
        }
    }

    public boolean canMoveToCell(Coordinate intendedMapCoordinatesToMoveTo) {
        EntitiesSet entities = entityRepository.findAliveEntitiesOfTypeAtVector(intendedMapCoordinatesToMoveTo.addHalfTile(), EntityType.UNIT, EntityType.STRUCTURE);
        entities = entities.exclude(this); // do not count ourselves as blocking
        Cell cell = map.getCellByAbsoluteMapCoordinates(new Coordinate(intendedMapCoordinatesToMoveTo));

        return entities.isEmpty() &&
                cell.isPassable(this) &&
                UnitMoveIntents.instance.isVectorClaimableBy(intendedMapCoordinatesToMoveTo, this);
    }

    public Coordinate getNextIntendedCellToMoveToTarget() {
        int nextXCoordinate = coordinate.getXAsInt();
        int nextYCoordinate = coordinate.getYAsInt();
        if (target.getXAsInt() < coordinate.getXAsInt()) nextXCoordinate -= TILE_SIZE;
        if (target.getXAsInt() > coordinate.getXAsInt()) nextXCoordinate += TILE_SIZE;
        if (target.getYAsInt() < coordinate.getYAsInt()) nextYCoordinate -= TILE_SIZE;
        if (target.getYAsInt() > coordinate.getYAsInt()) nextYCoordinate += TILE_SIZE;

        return Coordinate.create(nextXCoordinate, nextYCoordinate);
    }

    public boolean hasNoNextCellToMoveTo() {
        return nextTargetToMoveTo == coordinate;
    }

    @Override
    public String toString() {
        return "Unit [" +
                "sight=" + getSight() +
                "entityType=" + getEntityType() +
                "image=" + getEntityData().image +
                ", player=" + super.player +
                ", hitPoints=" + hitPointBasedDestructibility +
                ", coordinate=" + super.coordinate +
                "]\n";
    }

    /**
     * Unit is marked as selected. To get all selected units for player, use the com.fundynamic.d2tm.game.entities.EntityRepository#filter(Predicate) and
     * along with a {@link PredicateBuilder#isSelected()}
     */
    @Override
    public void select() {
        fadingSelection.select();
    }

    @Override
    public void deselect() {
        fadingSelection.deselect();
    }

    @Override
    public boolean isSelected() {
        return fadingSelection.isSelected();
    }

    @Override
    public void getsFocus() {
        fadingSelection.getsFocus();
    }

    @Override
    public void lostFocus() {
        fadingSelection.lostFocus();
    }

    public void idle() {
        this.setState(new IdleState(this, entityRepository, map));
    }

    @Override
    public void moveTo(Vector2D absoluteMapCoordinates) {
        this.target = absoluteMapCoordinates;
        // TODO: Is this correct?
//        this.entityToAttack = null; // forget about attacking
        this.cannonFacing.desireToFaceTo(UnitFacings.getFacingInt(this.coordinate, absoluteMapCoordinates));
        this.setState(new GoalResolverState(this, entityRepository, map));
    }

    @Override
    public void takeDamage(int hitPoints, Entity origin) {
        if (player.isCPU()) {
            if (origin == null) {
                Vector2D target = coordinate;
                // keep thinking of a random position to move to
                while (target.equals(coordinate)) {
                    target = getRandomVectorToMoveTo();
                }
                moveTo(target);
            } else {
                if (entityToAttack == null) {
                    attack(origin);
                }
            }
        }
        // TODO: set state to dying when applicable!?
        hitPointBasedDestructibility.takeDamage(hitPoints);
    }

    public Vector2D getRandomVectorToMoveTo() {
        // we're hit and we don't have an idea where it came from
        int correctX = Random.getRandomBetween(-1, 2) * TILE_SIZE;
        int correctY = Random.getRandomBetween(-1, 2) * TILE_SIZE;
        return coordinate.add(Vector2D.create(correctX, correctY));
    }

    @Override
    public boolean isDestroyed() {
        return state instanceof DeadState;
    }

    @Override
    public int getHitPoints() {
        return hitPointBasedDestructibility.getHitPoints();
    }

    @Override
    public void attack(Entity entity) {
        if (!entity.isDestructible()) {
            System.out.println("I (" + this.toString() + ") attack an entity that is not destructible -> " + entity);
            return;
        }

        entityToAttack = entity;
        if (offset.equals(Vector2D.zero())) {
            target = coordinate;
        } else {
            target = nextTargetToMoveTo;
        }
    }

    public Vector2D getOffset() {
        return offset;
    }

    public Vector2D getNextTargetToMoveTo() {
        return nextTargetToMoveTo;
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.UNIT;
    }

    public void setOffset(Vector2D offset) {
        this.offset = offset;
    }

    public void setFadingSelection(FadingSelection fadingSelection) {
        this.fadingSelection = fadingSelection;
    }

    @Override
    public void enrichRenderQueue(RenderQueue renderQueue) {
        if (isSelected()) {
            renderQueue.putEntityGui(this.hitPointBasedDestructibility, getCoordinateWithOffset());
            renderQueue.putEntityGui(this.fadingSelection, getCoordinateWithOffset());
        } else {
            if (fadingSelection.hasFocus()) {
                renderQueue.putEntityGui(this.hitPointBasedDestructibility, getCoordinateWithOffset());
            }
        }
    }

    public Coordinate getCoordinateWithOffset() {
        return this.getCoordinate().add(this.offset);
    }

    @Override
    public Coordinate getCenteredCoordinate() {
        return super.getCenteredCoordinate().add(offset);
    }

    public boolean hasFocus() {
        return fadingSelection.hasFocus();
    }

    public void setFacing(int facing) {
        this.bodyFacing.faceTowards(facing);
        this.cannonFacing.faceTowards(facing);
    }

    public Entity getEntityToAttack() {
        return entityToAttack;
    }

    @Override
    public Vector2D getDimensions() {
        return Vector2D.create(TILE_SIZE, TILE_SIZE);
    }

    public void harvest(Coordinate target) {
        moveTo(target);
    }

    private void setState(UnitState state) {
        if (this.state.getClass().equals(state.getClass())) {
            // do not set, same type
        } else {
            System.out.println("Unit [" + entityData.name + "] sets state from [" + this.state + "] to [" + state + "]");
            this.state = state;
        }
    }

    public void die() {
        setState(new DyingState(this, entityRepository, map));
    }

    public void dead() {
        setState(new DeadState(this, entityRepository, map));
    }

    public void seekSpice() {
        if (isAnimating()) {
            stopAndResetAnimating();
        }
        setState(new SeekSpiceState(this, entityRepository, map));
    }

    public void harvesting() {
        setState(new HarvestingState(this, entityRepository, map));
    }

    public boolean isAnimating() {
        return bodyFacing.isAnimating();
    }

    public void startAnimating() {
        bodyFacing.startAnimating();
    }

    public void stopAndResetAnimating() {
        bodyFacing.stopAndResetAnimating();
    }

    public void moveToCell(Coordinate nextIntendedCoordinatesToMoveTo) {
        nextTargetToMoveTo = nextIntendedCoordinatesToMoveTo;
        bodyFacing.desireToFaceTo(UnitFacings.getFacingInt(coordinate, nextTargetToMoveTo));

        UnitMoveIntents.instance.addIntent(nextTargetToMoveTo, this);

        setState(new MoveToCellState(this, entityRepository, map));
    }

    public Vector2D getTarget() {
        return target;
    }

    public void arrivedAtCell(Coordinate coordinateToMoveTo) {
        this.coordinate = coordinateToMoveTo;
        this.nextTargetToMoveTo = coordinateToMoveTo;

        UnitMoveIntents.instance.removeIntent(coordinateToMoveTo);

        // TODO: replace with some event "unit moved to coordinate" which is picked up
        // elsewhere (Listener?)
        map.revealShroudFor(this.coordinate.toMapCoordinate(), getSight(), player);

        setState(new GoalResolverState(this, entityRepository, map));
    }

    public RenderQueueEnrichableWithFacingLogic getBodyFacing() {
        return bodyFacing;
    }
}
