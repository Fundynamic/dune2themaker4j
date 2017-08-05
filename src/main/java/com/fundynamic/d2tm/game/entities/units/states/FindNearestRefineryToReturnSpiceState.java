package com.fundynamic.d2tm.game.entities.units.states;


import com.fundynamic.d2tm.game.entities.EntitiesSet;
import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.HarvesterDeliveryIntents;
import com.fundynamic.d2tm.game.entities.units.Unit;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.math.MapCoordinate;

import static com.fundynamic.d2tm.game.map.Cell.TILE_SIZE;

/**
 * Determine if
 */
public class FindNearestRefineryToReturnSpiceState extends UnitState {

    public FindNearestRefineryToReturnSpiceState(Unit unit, EntityRepository entityRepository, Map map) {
        super(unit, entityRepository, map);
        if (!unit.isHarvester()) throw new IllegalStateException("A non harvester unit (" + unit + ") tried to enter FindNearestRefineryToReturnSpiceState");
    }

    @Override
    public void update(float deltaInSeconds) {
        EntitiesSet entitiesAt = entityRepository.findEntitiesAt(unit.getCoordinate());
        EntitiesSet refineriesAtUnitLocation = entitiesAt.filterRefineries();
        if (refineriesAtUnitLocation.hasAny()) {
            // we have arrived at our destination, start dumping credits
            Entity refinery = refineriesAtUnitLocation.getFirst();
            unit.emptyHarvestedSpiceAt(refinery);
            unit.enterOtherEntity(refinery);
            return;
        }

        int arbitraryTileRange = 24;
        EntitiesSet entities = entityRepository.findRefineriesWithinDistance(unit.getCenteredCoordinate(), arbitraryTileRange * TILE_SIZE, unit.getPlayer());

        if (entities.isEmpty()) {
            System.out.println("Unit unable to find refinery nearby to return to.");
            unit.idle();
            return;
        }

        Entity nearestUnoccupiedRefinery = entities.stream().filter(e -> {
            return HarvesterDeliveryIntents.instance.canDeliverAt(e, unit); // only filter refineries that are not 'claimed' yet
        }).sorted((e1, e2) -> { // closest first
            return Float.compare(e1.distance(unit), e2.distance(unit));
        }).findFirst().orElse(null);


        // found nearby, free, refinery
        if (nearestUnoccupiedRefinery != null) {
            unit.returnToRefinery(nearestUnoccupiedRefinery);
            return;
        }

        // Darn it, no refinery is free. Move to the closest non-free refinery and await our luck there.

        nearestUnoccupiedRefinery = entities.stream().sorted((e1, e2) -> { // closest first
            return Float.compare(e1.distance(unit), e2.distance(unit));
        }).findFirst().orElse(null);

        unit.returnToRefinery(nearestUnoccupiedRefinery);
    }

    @Override
    public String toString() {
        return "FindNearestRefineryToReturnSpiceState";
    }

}
