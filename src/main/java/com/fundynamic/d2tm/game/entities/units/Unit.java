package com.fundynamic.d2tm.game.entities.units;

import com.fundynamic.d2tm.game.behaviors.Moveable;
import com.fundynamic.d2tm.game.behaviors.Selectable;
import com.fundynamic.d2tm.game.behaviors.FadingSelection;
import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.math.Random;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

public class Unit extends Entity implements Selectable, Moveable {

    // Behaviors
    private final FadingSelection fadingSelection;
    private Vector2D target;
    private Vector2D nextCellToMoveTo;

    // Implementation
    private final Map map;

    // Drawing 'movement' from cell to cell
    private Vector2D offset;
    private int facing;
    private float moveSpeed;

    public Unit(Map map, Vector2D mapCoordinates, Image image, int width, int height, int sight, float moveSpeed, Player player) {
        this(map, mapCoordinates, new SpriteSheet(image, width, height), new FadingSelection(width, height), sight, moveSpeed, player);
    }

    public Unit(Map map, Vector2D mapCoordinates, SpriteSheet spriteSheet, FadingSelection fadingSelection, int sight, float moveSpeed, Player player) {
        super(mapCoordinates, spriteSheet, sight, player);
        this.map = map;

        int possibleFacings = spriteSheet.getHorizontalCount();
        this.facing = Random.getRandomBetween(0, possibleFacings);
        this.fadingSelection = fadingSelection;
        this.target = mapCoordinates;
        this.nextCellToMoveTo = mapCoordinates;
        this.offset = Vector2D.zero();
        this.moveSpeed = moveSpeed;
    }

    public Unit(Map map, Vector2D mapCoordinates, SpriteSheet spriteSheet, int width, int height, Player player, int sight, int facing, Vector2D target, Vector2D nextCellToMoveTo, Vector2D offset) {
        super(mapCoordinates, spriteSheet, sight, player);
        this.offset = offset;
        this.map = map;
        this.facing = facing;
        this.nextCellToMoveTo = nextCellToMoveTo;
        this.target = target;
        this.fadingSelection = new FadingSelection(width, height);
    }

    @Override
    public void render(Graphics graphics, int x, int y) {
        Image sprite = getSprite();
        int drawY = y + offset.getYAsInt();
        int drawX = x + offset.getXAsInt();
        graphics.drawImage(sprite, drawX, drawY);
        this.fadingSelection.render(graphics, drawX, drawY);
    }

    @Override
    public void update(float deltaInMs) {
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
            stopMoving();
        } else {
            System.out.println("Next cell to move to is " + intendedMapCoordinatesToMoveTo);
            this.nextCellToMoveTo = intendedMapCoordinatesToMoveTo;
            intendedCellToMoveTo.setEntity(this); // claim this cell so we make sure nobody else can move here/take it.
        }
    }

    private boolean isWaitingForNextCellToDetermine() {
        return nextCellToMoveTo == mapCoordinates;
    }

    private void stopMoving() {
        this.nextCellToMoveTo = this.mapCoordinates;
        this.target = this.mapCoordinates;
    }

    private void moveToCell(Vector2D vectorToMoveTo) {
        System.out.println("Moving to cell " + vectorToMoveTo);
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
        return "Unit{" +
                "facing=" + facing +
                '}';
    }

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
        System.out.println("Ordered to move to " + target);
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
}
