package com.fundynamic.d2tm.game.entities.units.states;


import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.units.Unit;
import com.fundynamic.d2tm.game.map.Map;

public class IdleState extends UnitState {
    public IdleState(Unit unit, EntityRepository entityRepository, Map map) {
        super(unit, entityRepository, map);
    }

    // TODO boredom (after x time turn around randomly)

    @Override
    public void update(float deltaInSeconds) {
        if (unit.isHarvester()) {
            unit.seekSpice();
        }
    }

    @Override
    public String toString() {
        return "IdleState";
    }

}
