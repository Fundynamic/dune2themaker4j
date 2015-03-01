package com.fundynamic.d2tm.game.controls;

import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.math.Vector2D;
import com.fundynamic.d2tm.game.entities.structures.Structure;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

import static com.fundynamic.d2tm.game.map.CellFactory.makeCell;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNull;

public class MouseTest {

    private Cell defaultHoverCell;
    private Mouse mouse;

    @Before
    public void setUp() {
        defaultHoverCell = makeCell();
        mouse = new Mouse(defaultHoverCell);
    }

    @Test
    public void returnsCellItHoversOver() {
        Assert.assertSame(defaultHoverCell, mouse.getHoverCell());
    }

    @Test
    public void updatesHoverCell() {
        Cell newHoverCell = makeCell();
        mouse.setHoverCell(newHoverCell);
        Assert.assertSame(newHoverCell, mouse.getHoverCell());
    }

    @Test (expected = IllegalArgumentException.class)
    public void throwsIllegalArgumentWhenHoverCellIsNull() {
        mouse.setHoverCell(null);
    }

    @Test
    public void selectStructureOnCell() {
        Structure structure = makeStructure();
        defaultHoverCell.setEntity(structure);
        mouse.selectEntity();
        assertEquals(structure, mouse.getLastSelectedEntity());
        assertTrue(mouse.hasAnyEntitySelected());
    }

    @Test
    public void lastSelectedStructureIsNullWhenNoStructureIsSelected() {
        assertNull(mouse.getLastSelectedEntity());
    }

    @Test
    public void selectStructureOnCellWithoutStructureWillNotInfluenceLastSelectedStructure() {
        Structure structure = makeStructure();
        defaultHoverCell.setEntity(structure);
        mouse.selectEntity();

        Cell hoverCellWithoutStructure = makeCell();
        mouse.setHoverCell(hoverCellWithoutStructure);
        mouse.selectEntity(); // this should ignore the fact that the new hover cell has no structure

        assertEquals(structure, mouse.getLastSelectedEntity());
    }

    @Test
    public void deselectStructureDeselectsPreviouslySelectedStructure() {
        Structure structure = makeStructure();
        defaultHoverCell.setEntity(structure);
        mouse.selectEntity();
        mouse.deselectEntity();

        assertNull(mouse.getLastSelectedEntity());
        assertFalse(structure.isSelected());
    }

    @Test
    public void deselectStructureIgnoresNullHoverCell() {
        mouse = new Mouse(null);
        mouse.deselectEntity();
        assertNull(mouse.getLastSelectedEntity());
    }

    @Test
    public void deselectStructureIgnoresWhenNoStructureWasSelectedInTheFirstPlace() {
        mouse.deselectEntity();
        assertNull(mouse.getLastSelectedEntity());
    }

    @Test
    public void hoversOverSelectableEntityReturnsTrueWhenHoveringOverSelectableEntity() {
        Structure structure = makeStructure();
        defaultHoverCell.setEntity(structure);
        Assert.assertTrue(mouse.hoversOverSelectableEntity());
    }

    @Test
    public void hoversOverSelectableEntityReturnsFalseWhenHoveringOverNonSelectableEntity() {
        NotSelectableEntity notSelectableEntity = new NotSelectableEntity(Vector2D.zero(), Mockito.mock(SpriteSheet.class));
        defaultHoverCell.setEntity(notSelectableEntity);
        Assert.assertFalse(mouse.hoversOverSelectableEntity());
    }

    private Structure makeStructure() {
        return new Structure(Vector2D.zero(), Mockito.mock(Image.class), 64, 64, 2);
    }

    private class NotSelectableEntity extends Entity {

        public NotSelectableEntity(Vector2D mapCoordinates, SpriteSheet spriteSheet) {
            super(mapCoordinates, spriteSheet, 2);
        }

        @Override
        public void render(Graphics graphics, int x, int y) {

        }

        @Override
        public void update(float deltaInMs) {

        }
    }
}