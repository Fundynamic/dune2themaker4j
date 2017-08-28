package com.fundynamic.d2tm.game.entities.units.states;


import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.units.Unit;
import com.fundynamic.d2tm.game.map.Map;

/**
 * Between moving from one cell to another, first the unit must 'turn' its body
 */
public class TurnBodyTowardsState extends UnitState {
    public TurnBodyTowardsState(Unit unit, EntityRepository entityRepository, Map map) {
        super(unit, entityRepository, map);
    }

    @Override
    public void update(float deltaInSeconds) {
        if (!unit.shouldTurnBody()) {
            unit.setMoveToCellState();
            return;
        }

        unit.updateBodyFacing(deltaInSeconds);
    }

    @Override
    public String toString() {
        return "TurnBodyTowardsState, state = " + unit.getBodyFacing();
    }

}
