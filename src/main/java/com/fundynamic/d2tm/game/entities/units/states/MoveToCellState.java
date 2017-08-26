package com.fundynamic.d2tm.game.entities.units.states;


import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.units.Unit;
import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.types.EntityData;
import com.fundynamic.d2tm.math.Coordinate;
import com.fundynamic.d2tm.math.Vector2D;

public class MoveToCellState extends UnitState {

    public MoveToCellState(Unit unit, EntityRepository entityRepository, Map map) {
        super(unit, entityRepository, map);
    }

    @Override
    public void update(float deltaInSeconds) {
        if (unit.shouldTurnBody()) {
            unit.setTurnBodyState();
            return;
        }

        if (unit.hasNoNextCellToMoveTo()) {
            unit.setToGoalResolverState();
            return;
        }

        moveToNextCellPixelByPixel(deltaInSeconds);
    }

    @Override
    public String toString() {
        return "MoveToCellState, [nextTargetToMoveTo = " + unit.getNextTargetToMoveTo() + ", offset=" + unit.getOffset() + "]";
    }

    private void moveToNextCellPixelByPixel(float deltaInSeconds) {
        // TODO: Make this depended on some 'walk/move animation flag'
        if (unit.hasMoveAnimation()) {
            unit.startAnimating();
            unit.updateBodyFacing(deltaInSeconds);
        } else {
            unit.stopAndResetAnimating();
        }

        Coordinate coordinate = unit.getCoordinate();
        Vector2D offset = unit.getOffset();
        Coordinate nextTargetToMoveTo = unit.getNextTargetToMoveTo();
        EntityData entityData = unit.getEntityData();

        if (!map.isWithinPlayableMapBoundaries(nextTargetToMoveTo.toMapCoordinate())) {
            System.err.println("A next target to move to was set out of map bounds! ERROR. Going back to GoalResolver");
            unit.setToGoalResolverState();
            return;
        }

        float offsetX = offset.getX();
        float offsetY = offset.getY();

        // TODO: move like projectiles!?
        if (nextTargetToMoveTo.getXAsInt() < coordinate.getXAsInt()) offsetX -= entityData.getRelativeMoveSpeed(deltaInSeconds);
        if (nextTargetToMoveTo.getXAsInt() > coordinate.getXAsInt()) offsetX += entityData.getRelativeMoveSpeed(deltaInSeconds);
        if (nextTargetToMoveTo.getYAsInt() < coordinate.getYAsInt()) offsetY -= entityData.getRelativeMoveSpeed(deltaInSeconds);
        if (nextTargetToMoveTo.getYAsInt() > coordinate.getYAsInt()) offsetY += entityData.getRelativeMoveSpeed(deltaInSeconds);

        Vector2D vecToAdd = Vector2D.zero();

        if (offsetX > Cell.TILE_SIZE_ZERO_BASED) {
            offsetX = 0;
            vecToAdd = vecToAdd.add(Vector2D.create(Cell.TILE_SIZE, 0));
        }
        if (offsetX < -Cell.TILE_SIZE_ZERO_BASED) {
            offsetX = 0;
            vecToAdd = vecToAdd.add(Vector2D.create(-Cell.TILE_SIZE, 0));
        }
        if (offsetY > Cell.TILE_SIZE_ZERO_BASED) {
            offsetY = 0;
            vecToAdd = vecToAdd.add(Vector2D.create(0, Cell.TILE_SIZE));
        }
        if (offsetY < -Cell.TILE_SIZE_ZERO_BASED) {
            offsetY = 0;
            vecToAdd = vecToAdd.add(Vector2D.create(0, -Cell.TILE_SIZE));
        }

        // Arrived at intended next target cell
        if (!vecToAdd.isZero()) {
            unit.log("Arrived at cell");
            unit.arrivedAtCell(coordinate.add(vecToAdd));
        } else {
//            unit.log("My offset is " + vecToAdd);
        }

        unit.setOffset(Vector2D.create(offsetX, offsetY));
    }

}
