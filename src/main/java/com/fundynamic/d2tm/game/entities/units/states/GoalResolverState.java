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
            System.out.println("Unit has no next cell to move to");
            Coordinate nextIntendedCoordinatesToMoveTo = unit.getNextIntendedCellToMoveToTarget(unit.getTarget());

            System.out.println("Unit has decided that the next cell to move to is " + nextIntendedCoordinatesToMoveTo);
            if (unit.canMoveToCell(nextIntendedCoordinatesToMoveTo)) {
                System.out.println("Unit can move to next cell");
                unit.moveToCell(nextIntendedCoordinatesToMoveTo);
            } else {
                System.out.println("Unit cannot move to next cell, will retry next cycle");
            }
        } else {
            System.out.println("Unit next cell to move to");
            Coordinate nextIntendedCoordinatesToMoveTo = new Coordinate(unit.getNextTargetToMoveTo());
            System.out.println("Unit has cell to move to that is: " + nextIntendedCoordinatesToMoveTo);
            if (unit.canMoveToCell(nextIntendedCoordinatesToMoveTo)) {
                System.out.println("Unit can move to next cell");
                unit.moveToCell(nextIntendedCoordinatesToMoveTo);
            } else {
                System.out.println("Unit cannot move to cell, will idle... !?");
                unit.stopAndResetAnimating();
            }
        }
    }

    @Override
    public String toString() {
        return "GoalResolverState";
    }

}
