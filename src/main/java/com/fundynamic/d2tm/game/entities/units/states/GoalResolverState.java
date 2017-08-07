package com.fundynamic.d2tm.game.entities.units.states;


import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.units.Unit;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.math.Coordinate;

/**
 * Determine what to do to get to the given goal.
 */
public class GoalResolverState extends UnitState {

    public GoalResolverState(Unit unit, EntityRepository entityRepository, Map map) {
        super(unit, entityRepository, map);
    }

    @Override
    public void update(float deltaInSeconds) {
        if (!unit.shouldMove()) {
            unit.idle();
            unit.stopAndResetAnimating();
            return;
        }

        if (unit.hasNoNextCellToMoveTo()) {
            Coordinate nextIntendedCoordinatesToMoveTo = unit.getNextIntendedCellToMoveToTarget();
            if (unit.isCellPassableForMe(nextIntendedCoordinatesToMoveTo)) {
                if (!unit.isHarvester()) {
                    unit.startAnimating();
                }
                unit.moveToCell(nextIntendedCoordinatesToMoveTo);
            }
        } else {
            // still have a target, keep moving
            // we can get here when a unit is mid-way moving and is ordered to move somewhere else,
            // which will set the unit state to the GoalResolverState again.
            unit.moveToCell(Coordinate.create(unit.getNextTargetToMoveTo()));
        }
    }

    @Override
    public String toString() {
        return "GoalResolverState";
    }

}
