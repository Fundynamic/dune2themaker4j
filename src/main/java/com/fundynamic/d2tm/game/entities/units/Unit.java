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
import com.fundynamic.d2tm.game.types.EntityData;
import com.fundynamic.d2tm.math.Coordinate;
import com.fundynamic.d2tm.math.MapCoordinate;
import com.fundynamic.d2tm.math.Random;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import java.util.Set;

import static com.fundynamic.d2tm.game.map.Cell.TILE_SIZE;

/**
 * Should become observable with RxJava!
 */
public class Unit extends Entity implements Selectable, Moveable, Destructible, Destroyer, Focusable {

    public static final int GUARD_TIMER_INTERVAL = 5;

    // state
    private UnitState state;
//    private float harvested;

    // Behaviors
    private FadingSelection fadingSelection;

    // use contexts!?
    protected final HitPointBasedDestructibility hitPointBasedDestructibility;
    protected final HitPointBasedDestructibility harvested;

    private RenderQueueEnrichableWithFacingLogic bodyFacing;
    private RenderQueueEnrichableWithFacingLogic cannonFacing;

    private Coordinate target;                // the (end) goal to attack/move
    private Coordinate nextTargetToMoveTo;    // the next step/target to move to (ie, this is a step towards the 'real' target)
    private Coordinate lastSeenSpiceAt;       // the coordinate we have seen spice for the last time when harvesting

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
        this.lastSeenSpiceAt = coordinate;
        this.nextTargetToMoveTo = coordinate;
        this.offset = Vector2D.zero();
        this.guardTimer = Random.getRandomBetween(0, GUARD_TIMER_INTERVAL);
        this.state = new IdleState(this, entityRepository, map);

        if (entityData.isHarvester) {
            int maxHarvestingCapacity = 700;
            this.harvested = new HarvestedCentered(maxHarvestingCapacity, entityData.getWidth(), entityData.getHeight());
            this.harvested.toZero();
        } else {
            this.harvested = null;
        }

        if (entityData.moveSpeed < 0.0001f) {
            throw new IllegalArgumentException("The speed of this unit is so slow, you must be joking right? - given moveSpeed is " + entityData.moveSpeed);
        }
    }

    @Override
    public void render(Graphics graphics, int x, int y) {
        if (graphics == null) throw new IllegalArgumentException("Graphics must be not-null");
        // has entered other entity, thus do not render anything so it appears 'within' another entity
        // the 'other' entity is responsible for drawing as if the unit has entered.
        if (hasEntered != null) {
            return;
        }

        // Todo: get rid of offset, because those are used for cell-by-cell movements. This could
        // perhaps be optimized? (without any offsets?)
        int drawY = y + offset.getYAsInt();
        int drawX = x + offset.getXAsInt();

        bodyFacing.render(graphics, drawX, drawY);
        cannonFacing.render(graphics, drawX, drawY);
    }

    @Override
    public void update(float deltaInSeconds) {
        if (!isDying() &&
            hitPointBasedDestructibility.isZero()) {
            die();
        }

        state.update(deltaInSeconds);

        if (this.isDestroyed() || isDying()) {
            return;
        }

        if (shouldAttack()) {
            chaseOrAttack(deltaInSeconds);
        }

        if (!cannonFacing.isFacingDesiredFacing()) {
            cannonFacing.update(deltaInSeconds);
        }

        if (!shouldMove() && !shouldAttack()) {
            guardTimer += deltaInSeconds;
            if (guardTimer > GUARD_TIMER_INTERVAL) {
                guardTimer = 0F;

                // scan environment within range for enemies
                EntitiesSet entities = entityRepository.findEntitiesOfTypeAtVectorWithinDistance(getCenteredCoordinate(), entityData.sight * TILE_SIZE, EntityType.UNIT, EntityType.STRUCTURE);

                EntitiesSet enemyEntities = entities.filter(new NotPredicate(BelongsToPlayer.instance(player)));

                if (enemyEntities.isEmpty()) {
                    if (this.getPlayer().isCPU()) {
                        //TODO: enemy units scan entire map... (shouldnt do that ;-))
                        float distance = map.getHeight() * map.getWidth() * TILE_SIZE;
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
                        while (attackTimer > 1.0F) {
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
                moveTo(entityToAttack.getCoordinate());
            }
        }
    }

    /**
     * TODO-HARVESTER: check with EntityRepository {@link EntityRepository.PassableResult} {@link EntityRepository#isPassable(Entity, MapCoordinate)}
     * Possible duplicate!?
     */
    public boolean isCellPassableForMe(Coordinate intendedMapCoordinatesToMoveTo) {
        Cell cell = map.getCellByAbsoluteMapCoordinates(intendedMapCoordinatesToMoveTo);

        if (!cell.isPassable(this)) return false; // no need to further check if terrain does not allow movement

        EntitiesSet entities = entityRepository.findAliveEntitiesOfTypeAtVector(intendedMapCoordinatesToMoveTo.addHalfTile(), EntityType.UNIT, EntityType.STRUCTURE);
        entities = entities.exclude(this); // do not count ourselves as blocking

        if (!UnitMoveIntents.instance.isVectorClaimableBy(intendedMapCoordinatesToMoveTo, this))
            return false;

        if (isHarvester() && isDoneHarvesting()) {
            if (entities.hasOne()) {
                Entity blockingEntity = entities.getFirst();
                if (blockingEntity.isRefinery() &&
                    HarvesterDeliveryIntents.instance.hasDeliveryIntentAt(blockingEntity, this)) {
                    // the thing that blocks us is the refinery where we wanted to go
                    // then allow it.
                    return true;
                }
            }
        }

        // not boarding an entity, so then we only accept movable when it is empty
        return entities.isEmpty();
    }

    public Coordinate getNextIntendedCellToMoveToTarget() {
        Set<MapCoordinate> allSurroundingCellsAsCoordinates = getAllSurroundingCellsAsMapCoordinates();

        float distanceToTarget = distanceTo(target);

        Coordinate bestCoordinate = findClosestCoordinateTowards(allSurroundingCellsAsCoordinates, target, distanceToTarget);

        if (bestCoordinate == null) {
            // almost there
            // TODO: even better fix would be to get a list of top 5 closest, if all 5 closest are
            // occupied then stop? (5 because that is 'half surrounded')
            // TODO: even better than previous todo, implement path finding...
            if (distanceToTarget <= TILE_SIZE) {
                target = coordinate;
                nextTargetToMoveTo = coordinate;
                bestCoordinate = coordinate;
            } else {
                // fall back if we have to take a 'detour'
                bestCoordinate = findClosestCoordinateTowards(allSurroundingCellsAsCoordinates, target, distanceToTarget + TILE_SIZE);
            }
        }

        if (bestCoordinate != null) {
            return bestCoordinate;
        }
        System.out.println("getNextIntendedCellToMoveToTarget, fallback to own coordinate");
        return coordinate;
    }

    public Coordinate findClosestCoordinateTowards(Set<MapCoordinate> allSurroundingCellsAsCoordinates, Coordinate target, float maxDistance) {
        Coordinate bestCoordinate = null;
        float shortestDistance = maxDistance;
        for (MapCoordinate mapCoordinate : allSurroundingCellsAsCoordinates) {
            Coordinate coordinate = mapCoordinate.toCoordinate();
            boolean isCellPassableForMe = isCellPassableForMe(coordinate);
            if (!isCellPassableForMe) continue;

            float distance = coordinate.distance(target);
            if (distance < shortestDistance) {
                shortestDistance = distance;
                bestCoordinate = mapCoordinate.toCoordinate();
            }
        }
        return bestCoordinate;
    }

    public boolean hasNoNextCellToMoveTo() {
        return nextTargetToMoveTo == coordinate;
    }

    @Override
    public String toString() {
        return "Unit [" +
                "name=" + getEntityData().name +
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
        System.out.println("SELECTING UNIT START " + this.toStringShort() + " ========================= ");
        System.out.println("Current state " + this.state);
        System.out.println("Full to string: " + this);
        System.out.println("SELECTING UNIT END " + this.toStringShort() + " ========================= ");

        fadingSelection.select();
    }

    @Override
    public void enterOtherEntity(Entity whichEntityWillBeEntered) {
        super.enterOtherEntity(whichEntityWillBeEntered);
        deselect();
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
        stopAndResetAnimating();

        if (isHarvester()) {
            this.setState(new IdleHarvesterState(this, entityRepository, map));
        } else {
            this.setState(new IdleState(this, entityRepository, map));
        }
    }

    @Override
    public void moveTo(Coordinate absoluteMapCoordinates) {
        this.target = absoluteMapCoordinates;
        // TODO: Is this correct?
//        this.entityToAttack = null; // forget about attacking
        HarvesterDeliveryIntents.instance.removeAllIntentsBy(this);

        cannonFacing.desireToFaceTo(UnitFacings.getFacingInt(this.coordinate, absoluteMapCoordinates));

        setToGoalResolverState();
    }

    @Override
    public void takeDamage(int hitPoints, Entity origin) {
        if (player.isCPU()) {
            if (origin == null) {
                Coordinate target = coordinate;
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
        hitPointBasedDestructibility.reduce(hitPoints);
    }

    public Coordinate getRandomVectorToMoveTo() {
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
        return hitPointBasedDestructibility.getCurrent();
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
            if (this.harvested != null) {
                renderQueue.putEntityGui(this.harvested, getCoordinateWithOffset().min(Vector2D.create(0, 6)));
            }
            renderQueue.putEntityGui(this.fadingSelection, getCoordinateWithOffset());
        } else {
            if (fadingSelection.hasFocus()) {
                if (this.harvested != null) {
                    renderQueue.putEntityGui(this.harvested, getCoordinateWithOffset().min(Vector2D.create(0, 6)));
                }
                renderQueue.putEntityGui(this.hitPointBasedDestructibility, getCoordinateWithOffset());
            }
        }
    }

    public Coordinate getCoordinateWithOffset() {
        return this.getCoordinate().add(this.offset);
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

    /**
     * Same as {@link #moveTo(Coordinate)}
     *
     * @param target
     */
    public void harvestAt(Coordinate target) {
        moveTo(target);
    }

    private void setState(UnitState state) {
        if (this.state.getClass().equals(state.getClass())) {
            // do not set, same type
        } else {
            log("set state from [" + this.state + "] to [" + state + "]");
            this.state = state;
        }
    }

    public void die() {
        // a unit can die when a structure dies. A unit does not die without the structure.
        // but in case it happens, we do need to clean up references
        if (hasEntered != null) {
            hasEntered.leaveOtherEntity();
        }
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

    /**
     * Order unit to move to a cell. Puts it into the MoveToCellState and remembers the intent
     * to move to this cell so that other units will not attempt the same movement.
     *
     * @param nextIntendedCoordinatesToMoveTo
     */
    public void moveToCell(Coordinate nextIntendedCoordinatesToMoveTo) {
        // TODO: Make this depended on some 'walk/move animation flag'
        if (entityData.hasMoveAnimation) {
            startAnimating();
        } else {
            stopAndResetAnimating();
        }

        nextTargetToMoveTo = nextIntendedCoordinatesToMoveTo;
        bodyFacing.desireToFaceTo(UnitFacings.getFacingInt(coordinate, nextTargetToMoveTo));

        UnitMoveIntents.instance.addIntent(nextTargetToMoveTo, this);

        setMoveToCellState();
    }

    public void setMoveToCellState() {
        setState(new MoveToCellState(this, entityRepository, map));
    }

    /**
     * Unit arrived at cell. Will put unit in GoalResolverState and removes the intent it
     * previously had given.
     *
     * @param coordinateToMoveTo
     */
    public void arrivedAtCell(Coordinate coordinateToMoveTo) {
        this.coordinate = coordinateToMoveTo;
        this.nextTargetToMoveTo = coordinateToMoveTo;

        UnitMoveIntents.instance.removeAllIntentsBy(this);

        // TODO: replace with some event "unit moved to coordinate" which is picked up
        // elsewhere (Listener?)
        map.revealShroudFor(this.coordinate.toMapCoordinate(), getSight(), player);

        setToGoalResolverState();
    }

    public void setToGoalResolverState() {
        setState(new GoalResolverState(this, entityRepository, map));
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

    public Vector2D getTarget() {
        return target;
    }

    public RenderQueueEnrichableWithFacingLogic getBodyFacing() {
        return bodyFacing;
    }

    public boolean canHarvest() {
        if (!isHarvester()) return false;
        Cell unitCell = map.getCellFor(this);
        return unitCell.isHarvestable();
    }

    public void harvestCell(float deltaInSeconds) {
//        log("Harvesting");
        startAnimating(); // TODO-HARVESTER: make this 'harvesting animation'
        // TODO-HARVESTER: make this time based, like movespeed
        // TODO-HARVESTER: get this from entityData
        harvest(1);
        bodyFacing.update(deltaInSeconds);
    }

    public void harvest(int amount) {
        Cell unitCell = map.getCellFor(this);
        lastSeenSpiceAt = coordinate;
        harvested.add(unitCell.harvest(amount)); // note, harvest method may return lower number than desired harvest amount
    }

    public boolean isDoneHarvesting() {
        return harvested.isMaxed(); //harvested >= 700; // TODO-HARVESTER: make harvest capacity configurable (entityData)
    }

    public void findNearestRefineryToReturnSpice() {
        setState(new FindNearestRefineryToReturnSpiceState(this, entityRepository, map));
    }

    /**
     * <p>
     *      Will make this unit move to the refinery.
     * </p>
     * <p>
     *     If there are no delivery claims made for this refinery, then we claim it now and deliver at refinery.
     * </p>
     * <p>
     *     If there is a delivery claim made for this refinery, then we will not make a claim, but move close to the
     *     refinery instead.
     * </p>
     *
     * @param refinery
     */
    public void returnToRefinery(Entity refinery) {
        if (!refinery.isRefinery()) throw new IllegalArgumentException("Can only return to refinery type of entity");

        if (HarvesterDeliveryIntents.instance.canDeliverAt(refinery, this)) {
            System.out.println("returnToRefinery) Will deliver to refinery " + refinery);
            Coordinate closestCoordinateTo = refinery.getClosestCoordinateTo(getCenteredCoordinate());
            moveTo(closestCoordinateTo);
            // important to add intention after moveTo, because moveTo removes all intentions
            HarvesterDeliveryIntents.instance.addDeliveryIntentTo(refinery, this);
        } else {
            System.out.println("returnToRefinery) Move close to refinery " + refinery);
            Coordinate closestCoordinateTo = refinery.getClosestCoordinateAround(getCenteredCoordinate());
            moveTo(closestCoordinateTo);
        }
    }

    public void emptyHarvestedSpiceAt(Entity refinery) {
        if (!refinery.isRefinery()) throw new IllegalArgumentException("Cannot empty harvester at non-refinery entity " + refinery);
        // do not remove intent yet, do that once we are finished
        setState(new EmptyHarvesterState(this, entityRepository, map, refinery));
    }

    public boolean hasSpiceToUnload() {
        return !harvested.isZero();
    }

    public void depositSpice(float deltaInSeconds) {
        float amountToDeposit = entityData.getRelativeDepositSpeed(deltaInSeconds); // TODO-HARVESTER: deposit speed
        this.harvested.reduce(amountToDeposit);
        this.player.addCredits(amountToDeposit);
    }

    public Coordinate lastSeenSpiceAt() {
        return lastSeenSpiceAt;
    }

    public RenderQueueEnrichableWithFacingLogic getCannonFacing() {
        return cannonFacing;
    }

    public void setTurnBodyState() {
        setState(new TurnBodyTowardsState(this, entityRepository, map));
    }

    public boolean shouldTurnBody() {
        return !bodyFacing.isFacingDesiredFacing();
    }

    public boolean isDying() {
        return DyingState.DYING_STATE.equals(this.state.toString());
    }

    public void updateBodyFacing(float deltaInSeconds) {
        if (bodyFacing == null) return;
        bodyFacing.update(deltaInSeconds);
    }
}
