package com.fundynamic.d2tm.game.entities.units.states;


import com.fundynamic.d2tm.game.entities.EntitiesSet;
import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.structures.Structure;
import com.fundynamic.d2tm.game.entities.units.Unit;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.math.MapCoordinate;

import static com.fundynamic.d2tm.game.map.Cell.TILE_SIZE;

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
        int arbitraryTileRange = 24;
        EntitiesSet entities = entityRepository.findRefineriesWithinDistance(unit.getCenteredCoordinate(), arbitraryTileRange * TILE_SIZE, unit.getPlayer());

        if (entities.isEmpty()) {
            System.out.println("Unit unable to find refinery nearby to return to.");
            unit.idle();
            return;
        }

        Entity nearestUnoccupiedRefinery = entities.stream().filter(e -> {
            Structure refinery = (Structure) e;
            // TODO-HARVESTER: filter out refinery that is already occupied
            return e != null;
        }).findFirst().orElse(null);

        unit.returnToRefinery(nearestUnoccupiedRefinery);
    }

    @Override
    public String toString() {
        return "FindNearestRefineryToReturnSpiceState";
    }

}
