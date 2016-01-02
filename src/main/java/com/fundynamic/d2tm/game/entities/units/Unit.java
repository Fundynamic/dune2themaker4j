package com.fundynamic.d2tm.game.entities.units;

import com.fundynamic.d2tm.Game;
import com.fundynamic.d2tm.game.behaviors.*;
import com.fundynamic.d2tm.game.entities.*;
import com.fundynamic.d2tm.game.entities.predicates.PredicateBuilder;
import com.fundynamic.d2tm.game.entities.projectiles.Projectile;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.rendering.RenderQueue;
import com.fundynamic.d2tm.math.Coordinate;
import com.fundynamic.d2tm.math.Random;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import static com.fundynamic.d2tm.Game.TILE_SIZE;

/**
 * Should become observable with RxJava!
 */
public class Unit extends Entity implements Selectable, Moveable, Destructible, Destroyer {

    // Behaviors
    private FadingSelection fadingSelection;

    // use contexts!?
    protected final HitPointBasedDestructibility hitPointBasedDestructibility;

    private RenderableWithFacingLogic bodyFacing;
    private RenderableWithFacingLogic barrelFacing;

    private Vector2D target;
    private Vector2D nextTargetToMoveTo;

    // Dependencies
    private final Map map;

    // Drawing 'movement' from cell to cell
    private Vector2D offset;

    private Entity entityToAttack;
    private float attackTimer; // needed for attackRate

    private boolean hasSpawnedExplosions;

    public Unit(Map map, Coordinate coordinate, RenderableWithFacingLogic unitSpriteSheet, RenderableWithFacingLogic barrelSpriteSheet, FadingSelection fadingSelection, HitPointBasedDestructibility hitPointBasedDestructibility, Player player, EntityData entityData, EntityRepository entityRepository) {
        super(coordinate, unitSpriteSheet, entityData, player, entityRepository);
        this.map = map;
        this.bodyFacing = unitSpriteSheet;
        this.barrelFacing = barrelSpriteSheet;

        this.fadingSelection = fadingSelection;
        this.hitPointBasedDestructibility = hitPointBasedDestructibility;
        this.target = coordinate;
        this.nextTargetToMoveTo = coordinate;
        this.offset = Vector2D.zero();
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
        barrelFacing.render(graphics, drawX, drawY);
    }


    @Override
    public void update(float deltaInSeconds) {
        if (this.isDestroyed()) {
            return;
        }

        if (shouldAttack()) {
            chaseOrAttack(deltaInSeconds);
        }

        if (shouldMove()) {
            moveCloserToTarget(deltaInSeconds);
        }

        if (shouldExplode()) {
            explodeAndDie();
        }

        if (!barrelFacing.isFacingDesiredFacing()) {
            barrelFacing.update(deltaInSeconds);
        }

        fadingSelection.update(deltaInSeconds);
    }

    public boolean shouldMove() {
        return !this.target.equals(coordinate);
    }

    public boolean shouldAttack() {
        // we consider attacking when we have stopped moving, and when we have some entity to attack
        return !shouldMove() && entityToAttack != null;
    }

    public void moveCloserToTarget(float deltaInSeconds) {
        if (hasNoNextCellToMoveTo()) {
            decideWhatCellToMoveToNextOrStopMovingWhenNotPossible(target);
        } else {
            if (bodyFacing.isFacingDesiredFacing()) {
                moveToNextCellPixelByPixel(deltaInSeconds);
            } else {
                bodyFacing.update(deltaInSeconds);
            }
        }
    }

    public void chaseOrAttack(float deltaInSeconds) {
        float attackRange = entityData.attackRange;
        // ok, we don't need to move, so lets see if we are in range
        if (distance(entityToAttack) < attackRange) {
            // in range!!
            bodyFacing.desireToFaceTo(UnitFacings.getFacingInt(coordinate, entityToAttack.getCoordinate()));

            if (((Destructible) entityToAttack).isDestroyed()) {
                // target is destroyed, so stop attacking...
                entityToAttack = null;
            } else {
                // target is not yet destroyed

                if (bodyFacing.isFacingDesiredFacing()) { // unit is facing target, commence attacking

                    // weird check here, but we should have a weapon before we can fire...
                    if (entityData.hasWeaponId()) {

                        attackTimer += entityData.getRelativeAttackRate(deltaInSeconds);

                        // fire projectiles! - we use this while loop so that in case if insane high number of attack
                        // rates we can keep up with slow FPS
                        while(attackTimer > 1.0F) {
                            Projectile projectile = entityRepository.placeProjectile(coordinate.add(getHalfSize()), entityData.weaponId, player);
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
            target = decideWhatCellToMoveToNextOrStopMovingWhenNotPossible(entityToAttack.getCoordinate());
        }
    }

    private boolean shouldExplode() {
        return hitPointBasedDestructibility.hasDied() && !hasExploded();
    }

    public boolean hasExploded() {
        return hasSpawnedExplosions;
    }

    public void explodeAndDie() {
        hasSpawnedExplosions = true;
//        entityRepository.explodeAtCoordinate(coordinate, entityData.explosionId, player);
        entityRepository.placeExplosionWithCenterAt(getCenteredCoordinate(), player, entityData.explosionId);
    }

    private void moveToNextCellPixelByPixel(float deltaInSeconds) {
        float offsetX = offset.getX();
        float offsetY = offset.getY();

        if (nextTargetToMoveTo.getXAsInt() < coordinate.getXAsInt()) offsetX -= entityData.getRelativeMoveSpeed(deltaInSeconds);
        if (nextTargetToMoveTo.getXAsInt() > coordinate.getXAsInt()) offsetX += entityData.getRelativeMoveSpeed(deltaInSeconds);
        if (nextTargetToMoveTo.getYAsInt() < coordinate.getYAsInt()) offsetY -= entityData.getRelativeMoveSpeed(deltaInSeconds);
        if (nextTargetToMoveTo.getYAsInt() > coordinate.getYAsInt()) offsetY += entityData.getRelativeMoveSpeed(deltaInSeconds);

        Vector2D vecToAdd = Vector2D.zero();
        if (offsetX > 31) {
            offsetX = 0;
            vecToAdd = vecToAdd.add(Vector2D.create(32, 0));
        }
        if (offsetX < -31) {
            offsetX = 0;
            vecToAdd = vecToAdd.add(Vector2D.create(-32, 0));
        }
        if (offsetY > 31) {
            offsetY = 0;
            vecToAdd = vecToAdd.add(Vector2D.create(0, 32));
        }
        if (offsetY < -31) {
            offsetY = 0;
            vecToAdd = vecToAdd.add(Vector2D.create(0, -32));
        }

        // Arrived at intended next target cell
        if (!vecToAdd.equals(Vector2D.zero())) {
            moveToCell(coordinate.add(vecToAdd));
        }
        offset = Vector2D.create(offsetX, offsetY);
    }

    private Vector2D decideWhatCellToMoveToNextOrStopMovingWhenNotPossible(Vector2D target) {
        int nextXCoordinate = coordinate.getXAsInt();
        int nextYCoordinate = coordinate.getYAsInt();
        if (target.getXAsInt() < coordinate.getXAsInt()) nextXCoordinate -= TILE_SIZE;
        if (target.getXAsInt() > coordinate.getXAsInt()) nextXCoordinate += TILE_SIZE;
        if (target.getYAsInt() < coordinate.getYAsInt()) nextYCoordinate -= TILE_SIZE;
        if (target.getYAsInt() > coordinate.getYAsInt()) nextYCoordinate += TILE_SIZE;

        Vector2D intendedMapCoordinatesToMoveTo = new Vector2D(nextXCoordinate, nextYCoordinate);

        EntitiesSet entities = entityRepository.findEntitiesOfTypeAtVector(intendedMapCoordinatesToMoveTo, EntityType.UNIT);

        if (entities.isEmpty() && !UnitMoveIntents.hasIntentFor(intendedMapCoordinatesToMoveTo)) {
            nextTargetToMoveTo = intendedMapCoordinatesToMoveTo;
            bodyFacing.desireToFaceTo(UnitFacings.getFacingInt(coordinate, nextTargetToMoveTo));
            UnitMoveIntents.addIntent(nextTargetToMoveTo);
            return intendedMapCoordinatesToMoveTo;
        }
        return coordinate;
    }

    private boolean hasNoNextCellToMoveTo() {
        return nextTargetToMoveTo == coordinate;
    }

    private void moveToCell(Coordinate coordinateToMoveTo) {
        this.coordinate = coordinateToMoveTo;
        this.nextTargetToMoveTo = coordinateToMoveTo;

        UnitMoveIntents.removeIntent(coordinateToMoveTo);

        // TODO: replace with some event "unit moved to coordinate" which is picked up
        // elsewhere (Listener?)
        map.revealShroudFor(this.coordinate, getSight(), player);
    }

    @Override
    public String toString() {
        return "Unit [" +
                "sight=" + getSight() +
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

    @Override
    public void moveTo(Vector2D absoluteMapCoordinates) {
        this.target = absoluteMapCoordinates;
        this.entityToAttack = null; // forget about attacking
        this.barrelFacing.desireToFaceTo(UnitFacings.getFacingInt(this.coordinate, absoluteMapCoordinates));
    }

    @Override
    public void takeDamage(int hitPoints) {
        if (player.isCPU()) {
            // ahh we're hit!!! lets get outta here!!
            int correctX = Random.getRandomBetween(-1, 2) * Game.TILE_SIZE;
            int correctY = Random.getRandomBetween(-1, 2) * Game.TILE_SIZE;
            Vector2D target = coordinate.add(Vector2D.create(correctX, correctY));
            System.out.println("correctX: " + correctX);
            System.out.println("correctY: " + correctY);
            moveTo(target);
        }
        hitPointBasedDestructibility.takeDamage(hitPoints);
    }

    @Override
    public boolean isDestroyed() {
        return hasSpawnedExplosions && hitPointBasedDestructibility.hasDied();
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
        this.barrelFacing.faceTowards(facing);
    }
}
