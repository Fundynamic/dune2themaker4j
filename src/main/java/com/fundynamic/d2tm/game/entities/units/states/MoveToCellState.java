package com.fundynamic.d2tm.game.entities.units.states;


import com.fundynamic.d2tm.game.types.EntityData;
import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.units.Unit;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.math.Coordinate;
import com.fundynamic.d2tm.math.Vector2D;

public class MoveToCellState extends UnitState {

    public MoveToCellState(Unit unit, EntityRepository entityRepository, Map map) {
        super(unit, entityRepository, map);
    }

    @Override
    public void update(float deltaInSeconds) {
        if (unit.getBodyFacing().isFacingDesiredFacing()) {
            moveToNextCellPixelByPixel(deltaInSeconds);
        }
    }

    @Override
    public String toString() {
        return "MoveToCellState";
    }

    private void moveToNextCellPixelByPixel(float deltaInSeconds) {
        Coordinate coordinate = unit.getCoordinate();
        Vector2D offset = unit.getOffset();
        Vector2D nextTargetToMoveTo = unit.getNextTargetToMoveTo();
        EntityData entityData = unit.getEntityData();

        float offsetX = offset.getX();
        float offsetY = offset.getY();

        // TODO: move like projectiles!?
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
            unit.arrivedAtCell(coordinate.add(vecToAdd));
        }

        unit.setOffset(Vector2D.create(offsetX, offsetY));
    }

}
