package com.fundynamic.d2tm.game.controls;

import com.fundynamic.d2tm.game.entities.*;
import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.game.map.Map;
import org.newdawn.slick.*;

import static com.fundynamic.d2tm.game.map.CellFactory.makeCell;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

public class MouseTest {

    private Cell defaultHoverCell;
    private Mouse mouse;

//    @Before
//    public void setUp() {
//        defaultHoverCell = makeCell();
//        mouse = new Mouse(defaultHoverCell);
//    }
//
//    @Test
//    public void returnsCellItHoversOver() {
//        Assert.assertSame(defaultHoverCell, mouse.getHoverCell());
//    }
//
//    @Test
//    public void updatesHoverCell() {
//        Cell newHoverCell = makeCell();
//        mouse.mouseMovedToCell(newHoverCell);
//        Assert.assertSame(newHoverCell, mouse.getHoverCell());
//    }
//
//    @Test (expected = IllegalArgumentException.class)
//    public void throwsIllegalArgumentWhenHoverCellIsNull() {
//        mouse.mouseMovedToCell(null);
//    }
//
//    @Test
//    public void selectStructureOnCell() {
//        Structure structure = makeStructure();
//        defaultHoverCell.setEntity(structure);
//        mouse.selectEntity();
//        assertEquals(structure, mouse.getLastSelectedEntity());
//        assertTrue(mouse.hasAnyEntitySelected());
//        assertFalse(mouse.isMovingCursor());
//    }
//
//    @Test
//    public void movingCursorIsTrueWhenSelectingMoveableEntityOfPlayer() {
//        Player player = mock(Player.class);
//
//        Mouse mouse = new Mouse(player);
//        mouse.mouseMovedToCell(defaultHoverCell);
//
//        Unit unit = makeUnit(player);
//        defaultHoverCell.setEntity(unit);
//
//        // select entity
//        mouse.selectEntity();
//
//        // expect moving cursor (moveable entity)
//        assertTrue(mouse.isMovingCursor());
//    }
//
//    @Test
//    public void movingCursorIsFalseWhenSelectingMoveableEntityOfOtherPlayer() {
//        Player player = mock(Player.class);
//        Player otherPlayer = mock(Player.class);
//
//        Mouse mouse = new Mouse(player);
//        mouse.mouseMovedToCell(defaultHoverCell);
//
//        Unit unit = makeUnit(otherPlayer);
//        defaultHoverCell.setEntity(unit);
//
//        // select entity
//        mouse.selectEntity();
//
//        // expect moving cursor (moveable entity)
//        assertFalse(mouse.isMovingCursor());
//    }
//
//    @Test
//    public void lastSelectedStructureIsNullWhenNoStructureIsSelected() {
//        assertNull(mouse.getLastSelectedEntity());
//    }
//
//    @Test
//    public void selectStructureOnCellWithoutStructureWillNotInfluenceLastSelectedStructure() {
//        Structure structure = makeStructure();
//        defaultHoverCell.setEntity(structure);
//        mouse.selectEntity();
//
//        Cell hoverCellWithoutStructure = makeCell();
//        mouse.mouseMovedToCell(hoverCellWithoutStructure);
//        mouse.selectEntity(); // this should ignore the fact that the new hover cell has no structure
//
//        assertEquals(structure, mouse.getLastSelectedEntity());
//    }
//
//    @Test
//    public void deselectStructureDeselectsPreviouslySelectedStructure() {
//        Structure structure = makeStructure();
//        defaultHoverCell.setEntity(structure);
//        mouse.selectEntity();
//        mouse.deselectEntity();
//
//        assertNull(mouse.getLastSelectedEntity());
//        assertFalse(structure.isSelected());
//    }
//
//    @Test
//    public void deselectStructureIgnoresNullHoverCell() {
//        mouse = new Mouse((Cell) null);
//        mouse.deselectEntity();
//        assertNull(mouse.getLastSelectedEntity());
//    }
//
//    @Test
//    public void deselectStructureIgnoresWhenNoStructureWasSelectedInTheFirstPlace() {
//        mouse.deselectEntity();
//        assertNull(mouse.getLastSelectedEntity());
//    }
//
//    @Test
//    public void hoversOverSelectableEntityReturnsTrueWhenHoveringOverSelectableEntity() {
//        Structure structure = makeStructure();
//        defaultHoverCell.setEntity(structure);
//        Assert.assertTrue(mouse.hoversOverSelectableEntity());
//    }
//
//    @Test
//    public void hoversOverSelectableEntityReturnsFalseWhenHoveringOverNonSelectableEntity() {
//        NotSelectableEntity notSelectableEntity = new NotSelectableEntity(Vector2D.zero(), mock(SpriteSheet.class), mock(Player.class));
//        defaultHoverCell.setEntity(notSelectableEntity);
//        Assert.assertFalse(mouse.hoversOverSelectableEntity());
//    }
//
//    private Structure makeStructure() {
//        return new Structure(Vector2D.zero(), mock(Image.class), 64, 64, 2, mock(Player.class));
//    }
//
//    private Unit makeUnit(Player player) {
//        return new Unit(mock(Map.class), Vector2D.zero(), mock(Image.class), 32, 32, 3, 1.0f, player);
//    }
//
//    private class NotSelectableEntity extends Entity {
//
//        public NotSelectableEntity(Vector2D mapCoordinates, SpriteSheet spriteSheet, Player player) {
//            super(mapCoordinates, spriteSheet, 2, player);
//        }
//
//        @Override
//        public void render(Graphics graphics, int x, int y) {
//
//        }
//
//        @Override
//        public void update(float deltaInMs) {
//
//        }
//    }

    public static Mouse makeMouse(Map map, Player player) throws SlickException {
        EntityRepository entityRepository = EntityRepositoryTest.makeTestableEntityRepository(map);
        GameContainer gameContainer = mock(GameContainer.class);
        return new TestableMouse(player, gameContainer, entityRepository);
    }

}