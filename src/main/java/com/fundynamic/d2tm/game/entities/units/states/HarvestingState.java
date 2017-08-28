package com.fundynamic.d2tm.game.entities.units.states;


import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.units.Unit;
import com.fundynamic.d2tm.game.map.Map;

/**
 * <p>
 * A unit is put in this state when a harvester unit can harvest the cell it is now placed at.
 * From here, the unit will either harvest (as long as it can). When it is full, it will stop harvesting and find
 * a way back to the refinery by entering the {@link FindNearestRefineryToReturnSpiceState}.
 * </p>
 * <p>
 *     When the current cell is no longer harvestable (no more spice), as long as the unit is not done yet, it will enter the
 *     {@link SeekHarvestableResourceState} to look for other spice cells.
 * </p>
 */
public class HarvestingState extends UnitState {

    public HarvestingState(Unit unit, EntityRepository entityRepository, Map map) {
        super(unit, entityRepository, map);
    }

    @Override
    public void update(float deltaInSeconds) {
        if (unit.isDoneHarvesting()) {
            unit.stopAndResetAnimating();
            unit.findNearestRefineryToReturnSpice();
        } else {
            if (unit.canHarvest()) {
                unit.harvestCell(deltaInSeconds); // actual harvesting of spice here
            } else {
                unit.seekSpice();
            }
        }
    }

    @Override
    public String toString() {
        return "HarvestingState";
    }

}
