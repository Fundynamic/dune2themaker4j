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
import org.newdawn.slick.SpriteSheet;

import static com.fundynamic.d2tm.Game.TILE_SIZE;

public class Unit extends Entity implements Selectable, Moveable, Destructible, Destroyer {

    // Behaviors
    private FadingSelection fadingSelection;

    // use contexts!?
    protected final HitPointBasedDestructibility hitPointBasedDestructibility;

    private Vector2D target;
    private Vector2D nextTargetToMoveTo;

    // Dependencies
    private final Map map;

    // Drawing 'movement' from cell to cell
    private Vector2D offset;
    private float facing;
    private int desiredFacing;

    private Entity entityToAttack;
    private float attackTimer; // needed for attackRate

    private boolean hasSpawnedExplosions;

    public Unit(Map map, Coordinate coordinate, SpriteSheet spriteSheet, Player player, EntityData entityData, EntityRepository entityRepository) {
        this(
                map,
                coordinate,
                spriteSheet,
                new FadingSelection(entityData.getWidth(), entityData.getHeight()),
                new HitPointBasedDestructibility(entityData.hitPoints, entityData.getWidth()),
                player,
                entityData,
                entityRepository
        );
    }

    // TODO: Simplify constructor
    public Unit(Map map, Coordinate coordinate, SpriteSheet spriteSheet, FadingSelection fadingSelection, HitPointBasedDestructibility hitPointBasedDestructibility, Player player, EntityData entityData, EntityRepository entityRepository) {
        super(coordinate, spriteSheet, entityData, player, entityRepository);
        this.map = map;

        int possibleFacings = spriteSheet.getHorizontalCount();
        this.facing = Random.getRandomBetween(0, possibleFacings);
        this.desiredFacing = (int) facing;
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
        Image sprite = getSprite();
        // todo: GET RID OF OFFSET
        int drawY = y + offset.getYAsInt();
        int drawX = x + offset.getXAsInt();
        graphics.drawImage(sprite, drawX, drawY);
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
            if (desiredFacing == (int) facing) {
                moveToNextCellPixelByPixel(deltaInSeconds);
            } else {
                facing = UnitFacings.turnTo(facing, desiredFacing, entityData.getRelativeTurnSpeed(deltaInSeconds));
            }
        }
    }

    public void chaseOrAttack(float deltaInSeconds) {
        float attackRange = entityData.attackRange;
        // ok, we don't need to move, so lets see if we are in range
        if (distance(entityToAttack) < attackRange) {
            // in range!!
            this.desiredFacing = determineFacingFor(entityToAttack.getCoordinate()).getValue();
            if (((Destructible) entityToAttack).isDestroyed()) {
                // target is destroyed, so stop attacking...
                entityToAttack = null;
            } else {
                // target is not yet destroyed

                // face target first
                if (desiredFacing == (int) facing) {

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
                    facing = UnitFacings.turnTo(facing, desiredFacing, entityData.getRelativeTurnSpeed(deltaInSeconds));
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
            this.nextTargetToMoveTo = intendedMapCoordinatesToMoveTo;
            this.desiredFacing = determineFacingFor(nextTargetToMoveTo).getValue();
            UnitMoveIntents.addIntent(nextTargetToMoveTo);
            return intendedMapCoordinatesToMoveTo;
        }
        return coordinate;
    }

    private boolean hasNoNextCellToMoveTo() {
        return nextTargetToMoveTo == coordinate;
    }

    private void moveToCell(Coordinate coordinate) {
        this.coordinate = coordinate;
        this.nextTargetToMoveTo = coordinate;

        UnitMoveIntents.removeIntent(coordinate);

        // TODO: replace with some event "unit moved to coordinate" which is picked up
        // elsewhere (Listener?)
        map.revealShroudFor(this.coordinate, getSight(), player);
    }

    public Image getSprite() {
        return spriteSheet.getSprite((int) facing, 0);
    }

    @Override
    public String toString() {
        return "Unit [" +
                "sight=" + getSight() +
                ", player=" + super.player +
                ", facing=" + facing +
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
    public void moveTo(Vector2D absoluteMapCoordinates) {
        this.target = absoluteMapCoordinates;
        this.entityToAttack = null; // forget about attacking
    }

    private UnitFacings determineFacingFor(Vector2D coordinatesToFaceTo) {
       return UnitFacings.getFacing(coordinate, coordinatesToFaceTo);
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

    public void setFacing(int facing) {
        this.facing = facing;
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
            renderQueue.putEntityGui(this.hitPointBasedDestructibility, this.getCoordinate().add(this.offset));
            renderQueue.putEntityGui(this.fadingSelection, this.getCoordinate().add(this.offset));
        }
    }

    @Override
    public Coordinate getCenteredCoordinate() {
        return super.getCenteredCoordinate().add(offset);
    }

}
