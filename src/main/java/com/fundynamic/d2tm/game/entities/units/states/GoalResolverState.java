package com.fundynamic.d2tm.game.entities.units.states;


import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.units.Unit;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.math.Coordinate;

public class GoalResolverState extends UnitState {

    public GoalResolverState(Unit unit, EntityRepository entityRepository, Map map) {
        super(unit, entityRepository, map);
    }

    @Override
    public void update(float deltaInSeconds) {
        if (unit.hasNoNextCellToMoveTo()) {
            Coordinate nextIntendedCoordinatesToMoveTo = unit.getNextIntendedCellToMoveToTarget(unit.getTarget());

            if (unit.canMoveToCell(nextIntendedCoordinatesToMoveTo)) {
                unit.moveToCell(nextIntendedCoordinatesToMoveTo);
            }
        } else {
            System.out.println("Has cell to move to, what am I doing here!?");
        }
    }

    @Override
    public String toString() {
        return "GoalResolverState";
    }

}
