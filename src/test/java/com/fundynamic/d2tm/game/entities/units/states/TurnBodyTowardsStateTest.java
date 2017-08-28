package com.fundynamic.d2tm.game.entities.units.states;

import com.fundynamic.d2tm.game.AbstractD2TMTest;
import com.fundynamic.d2tm.game.entities.units.Unit;
import com.fundynamic.d2tm.game.entities.units.UnitFacings;
import com.fundynamic.d2tm.math.Coordinate;
import org.junit.Test;

import static com.fundynamic.d2tm.game.map.Cell.TILE_SIZE;

public class TurnBodyTowardsStateTest extends AbstractD2TMTest {

    @Test
    public void update() {
        Unit unit = makeUnit(UnitFacings.UP, Coordinate.create(TILE_SIZE, TILE_SIZE));
        unit.getBodyFacing().desireToFaceTo(UnitFacings.DOWN.getValue());

        TurnBodyTowardsState turnBodyTowardsState = new TurnBodyTowardsState(unit, entityRepository, map);

        // update
        turnBodyTowardsState.update(0.1f);
    }

}