package com.fundynamic.d2tm.game.entities.units.states;


import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.units.Unit;
import com.fundynamic.d2tm.game.map.Map;

public class DyingState extends UnitState {


    public DyingState(Unit unit, EntityRepository entityRepository, Map map) {
        super(unit, entityRepository, map);
    }

    @Override
    public void update(float deltaInSeconds) {
        entityRepository.placeExplosionWithCenterAt(
                unit.getCenteredCoordinate(),
                unit.getPlayer(),
                unit.getEntityData().explosionId
        );

        unit.setState(new DeadState(unit, entityRepository, map));
    }

}
