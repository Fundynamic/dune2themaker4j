package com.fundynamic.d2tm.game.entities.units.states;

import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.units.Unit;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.types.EntityData;
import com.fundynamic.d2tm.math.Coordinate;

public class FireAtEntityState extends UnitState {

    private Entity entityToAttack;

    private float attackTimer; // needed for attackRate

    public FireAtEntityState(Unit unit, EntityRepository entityRepository, Map map, Entity entityToAttack) {
        super(unit, entityRepository, map);
        this.entityToAttack = entityToAttack;
    }

    @Override
    public void update(float deltaInSeconds) {
        // if no longer in range, get back into goal resolver state
        if (!unit.isEntityInAttackRange(entityToAttack)) {
            unit.setToGoalResolverState();
            return;
        }

        if (entityToAttack.isDestroyed()) {
            unit.setToGoalResolverState();
            return;
        }

        // (re)calculate desired facings towards enemy
        unit.updateDesiredFacingsToEntity(entityToAttack);

        // update cannon and/or body when not ready to fire yet
        if (!unit.isReadyToFire()) {
            unit.updateBodyAndCannonFacing(deltaInSeconds);
            return;
        }

        EntityData entityData = unit.getEntityData();

        // you may fire when ready!
        attackTimer += entityData.getRelativeAttackRate(deltaInSeconds);

        // fire projectiles! - we use this while loop so that in case if insane high number of attack
        // rates we can keep up with slow FPS
        while (attackTimer > 1.0F) {
            Coordinate target = entityToAttack.getRandomPositionWithin();
            unit.fireWeaponTowards(target);
            attackTimer -= 1.0F;
        }
    }

}
