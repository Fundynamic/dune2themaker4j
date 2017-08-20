package com.fundynamic.d2tm.game.entities.units.states;


import com.fundynamic.d2tm.game.entities.EntitiesSet;
import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.units.Unit;
import com.fundynamic.d2tm.game.map.Map;

public class IdleHarvesterState extends UnitState {
    public IdleHarvesterState(Unit unit, EntityRepository entityRepository, Map map) {
        super(unit, entityRepository, map);
    }

    @Override
    public void update(float deltaInSeconds) {
        if (unit.isDoneHarvesting()) {
            unit.findNearestRefineryToReturnSpice();
        } else if (unit.canHarvest()) {
            unit.harvesting();
        } else {
            EntitiesSet entitiesAt = entityRepository.findEntitiesAt(unit.getCoordinate());
            EntitiesSet refineriesAtUnitLocation = entitiesAt.filterRefineries();
            if (refineriesAtUnitLocation.hasAny()) {
                // we have arrived at a refinery
                Entity refinery = refineriesAtUnitLocation.getFirst();
                unit.emptyHarvestedSpiceAt(refinery);
                unit.enterOtherEntity(refinery);
                return;
            }
            // TODO: When seekspice fails, we should time this, so we won't run this code
            // every frame
            unit.seekSpice();
        }
    }

    @Override
    public String toString() {
        return "IdleHarvesterState";
    }

}
