package com.fundynamic.d2tm.game.entities.units.states;


import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.units.Unit;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.math.MapCoordinate;

/**
 * Determine if
 */
public class FindNearestRefineryToReturnSpiceState extends UnitState {

    private MapCoordinate whereWeCameFrom;

    public FindNearestRefineryToReturnSpiceState(Unit unit, EntityRepository entityRepository, Map map) {
        super(unit, entityRepository, map);
        whereWeCameFrom = unit.getCoordinate().toMapCoordinate();
        if (!unit.isHarvester()) throw new IllegalStateException("A non harvester unit (" + unit + ") tried to enter FindNearestRefineryToReturnSpiceState");
    }

    @Override
    public void update(float deltaInSeconds) {

    }

    @Override
    public String toString() {
        return "FindNearestRefineryToReturnSpiceState";
    }

}
