package com.fundynamic.d2tm.game.rendering;

import com.fundynamic.d2tm.Game;
import com.fundynamic.d2tm.game.behaviors.FadingSelection;
import com.fundynamic.d2tm.game.behaviors.HitPointBasedDestructibility;
import com.fundynamic.d2tm.game.entities.EntityData;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.entities.units.Unit;
import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.map.MapTest;
import com.fundynamic.d2tm.math.Vector2D;
import org.junit.Test;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import static com.fundynamic.d2tm.math.Vector2D.create;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyFloat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.*;


public class EntityViewportRendererTest {

    @Test
    public void rendersEntityInView() throws SlickException {
        Map map = MapTest.makeMap();
        Player player = new Player("Stefan", Recolorer.FactionColor.BLUE);

        int viewportWidthInTiles = 10;
        int viewportHeightInTiles = 10;
        int viewportWidth = Game.TILE_WIDTH * viewportWidthInTiles;
        int viewportHeight = Game.TILE_HEIGHT * viewportHeightInTiles;
        EntityViewportRenderer entityViewportRenderer = new EntityViewportRenderer(map, Game.TILE_HEIGHT, Game.TILE_WIDTH, create(viewportWidth, viewportHeight));

        // this one should be rendered because it is within view
        makeUnit(map, player, create(1, 1));

        // this one should be rendered because it just outside view, and the renderer takes width + 1 cell into account
        makeUnit(map, player, create(viewportWidthInTiles + 1, viewportHeightInTiles + 1));

        // this one should NOT be rendered because it is outside view (+1) (vertically)
        makeUnit(map, player, create(viewportWidthInTiles + 1, viewportHeightInTiles + 2));

        // this one should NOT be rendered because it is outside view (+1) (horizontally)
        makeUnit(map, player, create(viewportWidthInTiles + 2, viewportHeightInTiles + 1));

        Graphics graphics = mock(Graphics.class);

        entityViewportRenderer.render(graphics, Vector2D.zero());

        verify(graphics, times(2)).drawImage(isA(Image.class), anyFloat(), anyFloat());
    }

    @Test
    public void rendersEntityOnceWhenOccupyingMultipleCellsDueMovement() throws SlickException {
        Map map = MapTest.makeMap();
        Player player = new Player("Stefan", Recolorer.FactionColor.BLUE);

        int viewportWidthInTiles = 10;
        int viewportHeightInTiles = 10;
        int viewportWidth = Game.TILE_WIDTH * viewportWidthInTiles;
        int viewportHeight = Game.TILE_HEIGHT * viewportHeightInTiles;
        EntityViewportRenderer entityViewportRenderer = new EntityViewportRenderer(map, Game.TILE_HEIGHT, Game.TILE_WIDTH, create(viewportWidth, viewportHeight));

        // this one should be rendered because it is within view
        Unit unit = makeUnit(map, player, create(1, 1));

        // it moves down-right, and it will occupy 2 cells by that logic
        Vector2D vectorToMoveTo = unit.getMapCoordinates().add(create(1, 1));
        unit.moveTo(vectorToMoveTo);
        unit.update(1);
        unit.update(1);

        // assert that the cell has been occupied
        Cell unitCell = map.getCell(unit.getMapCoordinates());
        Cell unitCellToMoveTo = map.getCell(vectorToMoveTo);
        assertTrue(unitCell.getEntity().equals(unit));
        assertTrue(unitCellToMoveTo.getEntity().equals(unit));

        // assert it only renders once
        Graphics graphics = mock(Graphics.class);

        entityViewportRenderer.render(graphics, Vector2D.zero());

        verify(graphics, times(1)).drawImage(isA(Image.class), anyFloat(), anyFloat());
    }

    /**
     * Replace with com.fundynamic.d2tm.game.entities.EntityRepositoryTest#createUnit(EntityRepository, Vector2D, Player) ?
     * @param map
     * @param player
     * @param mapCoordinates
     * @return
     */
    public static Unit makeUnit(Map map, Player player, Vector2D mapCoordinates) {
        FadingSelection fadingSelection = new FadingSelection(32, 32);
        HitPointBasedDestructibility hitPointBasedDestructibility = new HitPointBasedDestructibility(100);
        EntityData entityData = new EntityData(32, 32, 9);
        Unit unit = new Unit(map, mapCoordinates, makeSpriteSheet(), fadingSelection, hitPointBasedDestructibility, player, entityData);
        map.placeUnit(unit);
        return unit;
    }

    public static SpriteSheet makeSpriteSheet() {
        SpriteSheet spriteSheet = mock(SpriteSheet.class);
        Image image = mock(Image.class);

        when(spriteSheet.getSprite(anyInt(), anyInt())).thenReturn(image);
        return spriteSheet;
    }

}