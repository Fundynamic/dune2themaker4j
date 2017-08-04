package com.fundynamic.d2tm.game.entities.units.states;


import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.units.Unit;
import com.fundynamic.d2tm.game.map.Map;

public class HarvestingState extends UnitState {

    public HarvestingState(Unit unit, EntityRepository entityRepository, Map map) {
        super(unit, entityRepository, map);
    }

    @Override
    public void update(float deltaInSeconds) {
        // we can harvest
        if (unit.canHarvest()) {
            unit.harvestCell();

            if (!unit.isAnimating()) {
                unit.startAnimating();
            }
        } else {
            // can no longer harvest, seek spice again
            unit.seekSpice();
        }
    }

    @Override
    public String toString() {
        return "HarvestingState";
    }

}
