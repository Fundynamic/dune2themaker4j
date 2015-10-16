package com.fundynamic.d2tm.game.controls;

import com.fundynamic.d2tm.game.entities.units.Unit;
import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.math.Vector2D;
import org.junit.Test;
import org.newdawn.slick.SlickException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;


public class MovableSelectedMouseTest extends AbstractMouseBehaviorTest {

    @Test
    public void leftClickedSelectsUnitOnHoverCell() throws SlickException {
        Cell cell = makeCell(1, 1);
        mouse.setHoverCell(cell);

        Vector2D coordinatesAsAbsoluteVector2D = cell.getCoordinatesAsAbsoluteVector2D();
        Unit unit = makeUnit(player, 100, coordinatesAsAbsoluteVector2D);
        assertThat(unit.isSelected(), is(false));

        MovableSelectedMouse movableSelectedMouse = new MovableSelectedMouse(mouse, entityRepository);

        movableSelectedMouse.leftClicked();
        assertThat(unit.isSelected(), is(true));
    }

    @Test
    public void movesSelectedUnitsToCellThatIsNotOccupiedByOtherCell() throws SlickException {
        Cell cell = makeCell(2, 2);
        mouse.setHoverCell(cell);

        Vector2D mapCoordinates = Vector2D.create(1, 1);
        Unit unit = makeUnit(player, 100, mapCoordinates.scale(32F));
        unit.select();

        // TODO: This is ugly because absolute coordinates are used here versus map coordinates above in test
        assertEquals(unit.getNextTargetToMoveTo(), Vector2D.create(32, 32));

        MovableSelectedMouse movableSelectedMouse = new MovableSelectedMouse(mouse, mouse.getEntityRepository());

        movableSelectedMouse.leftClicked();

        unit.update(1);

        assertEquals(unit.getNextTargetToMoveTo(), Vector2D.create(64, 64));
    }

}