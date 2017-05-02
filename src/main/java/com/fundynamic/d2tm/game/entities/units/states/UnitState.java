package com.fundynamic.d2tm.game.entities.units.states;


import com.fundynamic.d2tm.game.behaviors.Updateable;
import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.units.Unit;
import com.fundynamic.d2tm.game.map.Map;

public abstract class UnitState implements Updateable {

    protected Unit unit;
    protected EntityRepository entityRepository;
    protected Map map;

    public UnitState(Unit unit, EntityRepository entityRepository, Map map) {
        this.unit = unit;
        this.entityRepository = entityRepository;
        this.map = map;
    }

}
