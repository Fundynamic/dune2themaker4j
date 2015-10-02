package com.fundynamic.d2tm.game.entities.units;

import com.fundynamic.d2tm.game.behaviors.*;
import com.fundynamic.d2tm.game.entities.*;
import com.fundynamic.d2tm.game.entities.predicates.PredicateBuilder;
import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.math.Random;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

public class Unit extends Entity implements Selectable, Moveable, Destructible, Destroyer {

    // Behaviors
    private final FadingSelection fadingSelection;
    private final HitPointBasedDestructibility hitPointBasedDestructibility;

    private Vector2D target;
    private Vector2D nextTargetToMoveTo;

    // Implementation
    private final Map map;


    // Drawing 'movement' from cell to cell
    private Vector2D offset;
    private int facing;
    private float moveSpeed;

    public Unit(Map map, Vector2D absoluteMapCoordinates, Image image, Player player, EntityData entityData, EntityRepository entityRepository) {
        this(
                map,
                absoluteMapCoordinates,
                new SpriteSheet(image, entityData.width, entityData.height),
                new FadingSelection(entityData.width, entityData.height),
                new HitPointBasedDestructibility(entityData.hitPoints),
                player,
                entityData,
                entityRepository
        );
    }

    // TODO: Simplify constructor
    public Unit(Map map, Vector2D absoluteMapCoordinates, SpriteSheet spriteSheet, FadingSelection fadingSelection, HitPointBasedDestructibility hitPointBasedDestructibility, Player player, EntityData entityData, EntityRepository entityRepository) {
        super(absoluteMapCoordinates, spriteSheet, entityData.sight, player, entityRepository);
        this.map = map;

        int possibleFacings = spriteSheet.getHorizontalCount();
        this.facing = Random.getRandomBetween(0, possibleFacings);
        this.fadingSelection = fadingSelection;
        this.hitPointBasedDestructibility = hitPointBasedDestructibility;
        this.target = absoluteMapCoordinates;
        this.nextTargetToMoveTo = absoluteMapCoordinates;
        this.offset = Vector2D.zero();
        this.moveSpeed = entityData.moveSpeed;
    }

    // TODO: Simplify constructor
    public Unit(Map map, Vector2D mapCoordinates, SpriteSheet spriteSheet,
                Player player, int sight, int facing,
                Vector2D target, Vector2D nextTargetToMoveTo, Vector2D offset,
                int hitPoints, FadingSelection fadingSelection, EntityRepository entityRepository) {
        super(mapCoordinates, spriteSheet, sight, player, entityRepository);
        this.offset = offset;
        this.moveSpeed = 1.0F;
        this.map = map;
        this.facing = facing;
        this.nextTargetToMoveTo = nextTargetToMoveTo;
        this.target = target;
        this.fadingSelection = fadingSelection;
        this.hitPointBasedDestructibility = new HitPointBasedDestructibility(hitPoints);
    }

    @Override
    public void render(Graphics graphics, int x, int y) {
        if (graphics == null) throw new IllegalArgumentException("Graphics must be not-null");
        Image sprite = getSprite();
        // todo: GET RID OF OFFSET
        int drawY = y + offset.getYAsInt();
        int drawX = x + offset.getXAsInt();
        graphics.drawImage(sprite, drawX, drawY);
        this.fadingSelection.render(graphics, drawX, drawY);
    }

    @Override
    public void update(float deltaInMs) {
        if (this.isDestroyed()) {
            System.out.println("I (" + this.toString() + ") am dead, so I won't update anymore.");
            return;
        }
        this.fadingSelection.update(deltaInMs);
        if (shouldBeSomewhereElse()) {
            if (isWaitingForNextCellToDetermine()) {
                decideWhatCellToMoveToNextOrStopMovingWhenNotPossible();
            } else {
                // TODO: "make it turn to facing"
                facing = determineFacingFor(nextTargetToMoveTo).getValue();
                moveToNextCellPixelByPixel();
            }
        }
    }

    private void moveToNextCellPixelByPixel() {
        float offsetX = offset.getX();
        float offsetY = offset.getY();
        if (nextTargetToMoveTo.getXAsInt() < absoluteMapCoordinates.getXAsInt()) offsetX -= moveSpeed;
        if (nextTargetToMoveTo.getXAsInt() > absoluteMapCoordinates.getXAsInt()) offsetX += moveSpeed;
        if (nextTargetToMoveTo.getYAsInt() < absoluteMapCoordinates.getYAsInt()) offsetY -= moveSpeed;
        if (nextTargetToMoveTo.getYAsInt() > absoluteMapCoordinates.getYAsInt()) offsetY += moveSpeed;
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
            moveToCell(absoluteMapCoordinates.add(vecToAdd));
        }
        offset = Vector2D.create(offsetX, offsetY);
    }

    private void decideWhatCellToMoveToNextOrStopMovingWhenNotPossible() {
        int nextXCoordinate = absoluteMapCoordinates.getXAsInt();
        int nextYCoordinate = absoluteMapCoordinates.getYAsInt();
        if (target.getXAsInt() < absoluteMapCoordinates.getXAsInt()) nextXCoordinate -= 32F;
        if (target.getXAsInt() > absoluteMapCoordinates.getXAsInt()) nextXCoordinate += 32F;
        if (target.getYAsInt() < absoluteMapCoordinates.getYAsInt()) nextYCoordinate -= 32F;
        if (target.getYAsInt() > absoluteMapCoordinates.getYAsInt()) nextYCoordinate += 32F;

        Vector2D intendedMapCoordinatesToMoveTo = new Vector2D(nextXCoordinate, nextYCoordinate);
        Cell intendedCellToMoveTo = map.getCellByAbsoluteMapCoordinates(intendedMapCoordinatesToMoveTo);

        if (!intendedCellToMoveTo.isOccupied(this)) {
            this.nextTargetToMoveTo = intendedMapCoordinatesToMoveTo;
            intendedCellToMoveTo.setEntity(this); // claim this cell so we make sure nobody else can move here/take it.
        }
    }

    private boolean isWaitingForNextCellToDetermine() {
        return nextTargetToMoveTo == absoluteMapCoordinates;
    }

    private void moveToCell(Vector2D vectorToMoveTo) {
        Cell mapCell = map.getCellByAbsoluteMapCoordinates(absoluteMapCoordinates);
        mapCell.removeEntity();

        this.absoluteMapCoordinates = vectorToMoveTo;
        this.nextTargetToMoveTo = vectorToMoveTo;

        map.revealShroudFor(absoluteMapCoordinates, sight, player);

        mapCell = map.getCellByAbsoluteMapCoordinates(absoluteMapCoordinates);
        mapCell.setEntity(this);
    }

    private boolean shouldBeSomewhereElse() {
        return !this.target.equals(absoluteMapCoordinates);
    }

    public Image getSprite() {
        return spriteSheet.getSprite(facing, 0);
    }

    @Override
    public String toString() {
        return "Unit [" +
                "sight=" + super.sight +
                ", player=" + super.player +
                ", facing=" + facing +
                ", hitPoints=" + hitPointBasedDestructibility +
                ", absoluteMapCoordinates=" + super.absoluteMapCoordinates +
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
    }

    public UnitFacings determineFacingFor(Vector2D coordinatesToFaceTo) {
        boolean left = coordinatesToFaceTo.getXAsInt() < absoluteMapCoordinates.getXAsInt();
        boolean right = coordinatesToFaceTo.getXAsInt() > absoluteMapCoordinates.getXAsInt();
        boolean up = coordinatesToFaceTo.getYAsInt() < absoluteMapCoordinates.getYAsInt();
        boolean down = coordinatesToFaceTo.getYAsInt() > absoluteMapCoordinates.getYAsInt();

        if (up && left) return UnitFacings.LEFT_UP;
        if (up && right) return UnitFacings.RIGHT_UP;
        if (down && left) return UnitFacings.LEFT_DOWN;
        if (down && right) return UnitFacings.RIGHT_DOWN;
        if (up) return UnitFacings.UP;
        if (down) return UnitFacings.DOWN;
        if (left) return UnitFacings.LEFT;
        if (right) return UnitFacings.RIGHT;

        return UnitFacings.byId(facing);
    }

    @Override
    public void takeDamage(int hitPoints) {
        hitPointBasedDestructibility.takeDamage(hitPoints);
    }

    @Override
    public boolean isDestroyed() {
        return hitPointBasedDestructibility.isDestroyed();
    }

    @Override
    public void attack(Entity entity) {
        if (!entity.isDestructible()) {
            System.out.println("I (" + this.toString() + ") attack an entity that is not destructible -> " + entity);
            return;
        }

        // spawn projectile from this cell , to another cell.
//        entityRepository.placeOnMap(absoluteMapCoordinates, EntityType.PROJECTILE, 0, player);

        Destructible destructible = (Destructible) entity;
        destructible.takeDamage(Random.getRandomBetween(50, 150));
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
}
