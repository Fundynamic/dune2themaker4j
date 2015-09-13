package com.fundynamic.d2tm.game.entities.units;

import com.fundynamic.d2tm.game.behaviors.*;
import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.EntityData;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.entities.Predicate;
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
    private Vector2D nextCellToMoveTo;

    // Implementation
    private final Map map;


    // Drawing 'movement' from cell to cell
    private Vector2D offset;
    private int facing;
    private float moveSpeed;

    public Unit(Map map, Vector2D mapCoordinates, Image image, Player player, EntityData entityData) {
        this(
                map,
                mapCoordinates,
                new SpriteSheet(image, entityData.width, entityData.height),
                new FadingSelection(entityData.width, entityData.height),
                new HitPointBasedDestructibility(entityData.hitPoints),
                player,
                entityData
        );
    }

    // TODO: Simplify constructor
    public Unit(Map map, Vector2D mapCoordinates, SpriteSheet spriteSheet, FadingSelection fadingSelection, HitPointBasedDestructibility hitPointBasedDestructibility, Player player, EntityData entityData) {
        super(mapCoordinates, spriteSheet, entityData.sight, player);
        this.map = map;

        int possibleFacings = spriteSheet.getHorizontalCount();
        this.facing = Random.getRandomBetween(0, possibleFacings);
        this.fadingSelection = fadingSelection;
        this.hitPointBasedDestructibility = hitPointBasedDestructibility;
        this.target = mapCoordinates;
        this.nextCellToMoveTo = mapCoordinates;
        this.offset = Vector2D.zero();
        this.moveSpeed = entityData.moveSpeed;
    }

    // TODO: Simplify constructor
    public Unit(Map map, Vector2D mapCoordinates, SpriteSheet spriteSheet,
                Player player, int sight, int facing,
                Vector2D target, Vector2D nextCellToMoveTo, Vector2D offset,
                int hitPoints, FadingSelection fadingSelection) {
        super(mapCoordinates, spriteSheet, sight, player);
        this.offset = offset;
        this.moveSpeed = 1.0F;
        this.map = map;
        this.facing = facing;
        this.nextCellToMoveTo = nextCellToMoveTo;
        this.target = target;
        this.fadingSelection = fadingSelection;
        this.hitPointBasedDestructibility = new HitPointBasedDestructibility(hitPoints);
    }

    @Override
    public void render(Graphics graphics, int x, int y) {
        if (graphics == null) throw new IllegalArgumentException("Graphics must be not-null");
        Image sprite = getSprite();
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
                facing = determineFacingFor(nextCellToMoveTo).getValue();
                moveToNextCellPixelByPixel();
            }
        }
    }

    private void moveToNextCellPixelByPixel() {
        float offsetX = offset.getX();
        float offsetY = offset.getY();
        if (nextCellToMoveTo.getXAsInt() < mapCoordinates.getXAsInt()) offsetX -= moveSpeed;
        if (nextCellToMoveTo.getXAsInt() > mapCoordinates.getXAsInt()) offsetX += moveSpeed;
        if (nextCellToMoveTo.getYAsInt() < mapCoordinates.getYAsInt()) offsetY -= moveSpeed;
        if (nextCellToMoveTo.getYAsInt() > mapCoordinates.getYAsInt()) offsetY += moveSpeed;
        Vector2D vecToAdd = Vector2D.zero();
        if (offsetX > 31) {
            offsetX = 0;
            vecToAdd = vecToAdd.add(Vector2D.create(1, 0));
        }
        if (offsetX < -31) {
            offsetX = 0;
            vecToAdd = vecToAdd.add(Vector2D.create(-1, 0));
        }
        if (offsetY > 31) {
            offsetY = 0;
            vecToAdd = vecToAdd.add(Vector2D.create(0, 1));
        }
        if (offsetY < -31) {
            offsetY = 0;
            vecToAdd = vecToAdd.add(Vector2D.create(0, -1));
        }
        if (!vecToAdd.equals(Vector2D.zero())) {
            moveToCell(mapCoordinates.add(vecToAdd));
        }
        offset = Vector2D.create(offsetX, offsetY);
    }

    private void decideWhatCellToMoveToNextOrStopMovingWhenNotPossible() {
        int nextCellX = mapCoordinates.getXAsInt();
        int nextCellY = mapCoordinates.getYAsInt();
        if (target.getXAsInt() < mapCoordinates.getXAsInt()) nextCellX--;
        if (target.getXAsInt() > mapCoordinates.getXAsInt()) nextCellX++;
        if (target.getYAsInt() < mapCoordinates.getYAsInt()) nextCellY--;
        if (target.getYAsInt() > mapCoordinates.getYAsInt()) nextCellY++;
        Vector2D intendedMapCoordinatesToMoveTo = new Vector2D(nextCellX, nextCellY);
        Cell intendedCellToMoveTo = map.getCell(intendedMapCoordinatesToMoveTo);
        if (intendedCellToMoveTo.isOccupied(this)) {
            // Do nothing, which effectively means it 'waits' and will try again next tick.
        } else {
//            System.out.println("Next cell to move to is " + intendedMapCoordinatesToMoveTo);
            this.nextCellToMoveTo = intendedMapCoordinatesToMoveTo;
            intendedCellToMoveTo.setEntity(this); // claim this cell so we make sure nobody else can move here/take it.
        }
    }

    private boolean isWaitingForNextCellToDetermine() {
        return nextCellToMoveTo == mapCoordinates;
    }

    private void moveToCell(Vector2D vectorToMoveTo) {
//        System.out.println("Moving to cell " + vectorToMoveTo);
        map.getCell(mapCoordinates).removeEntity();
        this.mapCoordinates = vectorToMoveTo;
        this.nextCellToMoveTo = vectorToMoveTo;
        map.revealShroudFor(mapCoordinates, sight, player);
        map.getCell(mapCoordinates).setEntity(this);
    }

    private boolean shouldBeSomewhereElse() {
        return !this.target.equals(mapCoordinates);
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
                ", mapCoordinates=" + super.mapCoordinates +
                ", absoluteMapPixelCoordinates=" + getAbsoluteMapPixelCoordinates() +
                "]\n";
    }

    /**
     * Unit is marked as selected. To get all selected units for player, use the {@link com.fundynamic.d2tm.game.entities.EntityRepository#filter(Predicate)} and
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
    public void moveTo(Vector2D target) {
        this.target = target;
    }

    public UnitFacings determineFacingFor(Vector2D coordinatesToFaceTo) {
        boolean left = coordinatesToFaceTo.getXAsInt() < mapCoordinates.getXAsInt();
        boolean right = coordinatesToFaceTo.getXAsInt() > mapCoordinates.getXAsInt();
        boolean up = coordinatesToFaceTo.getYAsInt() < mapCoordinates.getYAsInt();
        boolean down = coordinatesToFaceTo.getYAsInt() > mapCoordinates.getYAsInt();

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
        Destructible destructible = (Destructible) entity;
        destructible.takeDamage(Random.getRandomBetween(50, 150));
    }

    public Vector2D getOffset() {
        return offset;
    }

    public Vector2D getNextCellToMoveTo() {
        return nextCellToMoveTo;
    }
}
