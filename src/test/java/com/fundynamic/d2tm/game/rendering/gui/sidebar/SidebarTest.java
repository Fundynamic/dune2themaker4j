package com.fundynamic.d2tm.game.rendering.gui.sidebar;

import com.fundynamic.d2tm.Game;
import com.fundynamic.d2tm.game.AbstractD2TMTest;
import com.fundynamic.d2tm.game.entities.entitiesdata.EntitiesData;
import com.fundynamic.d2tm.game.entities.structures.Structure;
import com.fundynamic.d2tm.game.map.MapEditor;
import com.fundynamic.d2tm.game.state.PlayingState;
import com.fundynamic.d2tm.game.terrain.impl.DuneTerrain;
import com.fundynamic.d2tm.game.terrain.impl.DuneTerrainFactory;
import com.fundynamic.d2tm.graphics.Theme;
import com.fundynamic.d2tm.math.Coordinate;
import com.fundynamic.d2tm.math.MapCoordinate;
import com.fundynamic.d2tm.math.Vector2D;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * This is a fairly big test:
 * - It assumes a game is set up in 800X600 resolution, with a sidebar and a battlefield (like the 'real game')
 * - It reads THE REAL RULES.INI FILE!!
 * - It places a structure on the map, within view on the battlefield (map coordinate 10, 10)
 * - It moves the mouse to the structure, this triggers all kinds of 'active gui logic'
 * - It clicks left mouse button, so it selects it, this triggers all the 'communicate event through the active gui logic and so forth'
 * - Since CONSTYARD is now selected, it should now trigger all kinds of rendering logic for the sidebar
 * - It moves the mouse to the sidebar, triggering sidebar activation logic.
 * - It moves it into the sidebar upon the FIRST CONSTRUCTABLE ICON. This triggers yet more logic.
 * - It clicks left mouse button, starting the construction of a REFINERY
 * - It 'updates' the state for 10 seconds, making the construction complete.
 * - It clicks left mouse button, again, to 'place the structure on map'
 * - It moves the mouse BACK on the battlefield, RIGHT to the CONSTYARD
 * - It clicks left mouse button, to place the structure
 * - It asserts an entity has been constructed

 */
public class SidebarTest extends AbstractD2TMTest {

    public static final int STRUCTURE_MAP_COORDINATE_X = 10;
    public static final int STRUCTURE_MAP_COORDINATE_Y = 10;

    @Test
    public void selectingConstructionYardActivatesSidebar() {
        // make a map full of rock to make sure this test passes
        MapEditor mapEditor = new MapEditor(new DuneTerrainFactory(Mockito.mock(Theme.class)));
        mapEditor.fillMapWithTerrain(map, DuneTerrain.TERRAIN_ROCK);

        // Place structure
        MapCoordinate mapCoordinate = MapCoordinate.create(STRUCTURE_MAP_COORDINATE_X, STRUCTURE_MAP_COORDINATE_Y);
        Structure structure = entityRepository.placeStructureOnMap(
                mapCoordinate,
                EntitiesData.CONSTRUCTION_YARD,
                player
        );

        // 16 == half a cell
        Coordinate coordinate = mapCoordinate.toCoordinate().add(new Vector2D(16, 16));
        int structureX = coordinate.getXAsInt();
        int structureY = PlayingState.HEIGHT_OF_TOP_BAR + coordinate.getYAsInt();

        // Move mouse to the given X and Y coordinate on screen
        mouse.movedTo(Coordinate.create(structureX, structureY));

        // Click structure....
        mouse.leftClicked();

        // ... which should select it. (first assertion)
        Assert.assertTrue(structure.isSelected());

        // update state of guiComposite & structure
        guiComposite.update(1.0f); // 1 second passed
        structure.update(1.0f); // 1 second passed

        // also 'render' it once
        // this should render the battlefield, the sidebar, and its build icons
        guiComposite.render(graphics);


        int withinSidebarX = Game.SCREEN_WIDTH - PlayingState.WIDTH_OF_SIDEBAR;
        int withinSidebarY = PlayingState.HEIGHT_OF_TOP_BAR + 11;

        // move into sidebar logic
        mouse.movedTo(Vector2D.create(withinSidebarX, withinSidebarY));

        // move to 'buildable icon'
        withinSidebarX = (Game.SCREEN_WIDTH - PlayingState.WIDTH_OF_SIDEBAR) + 20;
        withinSidebarY = PlayingState.HEIGHT_OF_TOP_BAR + 20;

        // move into sidebar logic, but now also on buildable icon
        mouse.movedTo(Vector2D.create(withinSidebarX, withinSidebarY));

        // The mouse should be now hovered over the FIRST BUILDABLE ICON from a CONSTYARD
        // Ie, if the buildlist="" order changes ever, then this assumption is now wrong.
        // At this moment the first building is a REFINERY, with a build time of 10 seconds.

        // First, lets construct it by left clicking:
        mouse.leftClicked();

        // now update the state a few times
        guiComposite.update(9.0f); // 9 seconds passed
        structure.update(9.0f); // 9 seconds passed

        guiComposite.update(1.0f); // 1 second passed
        structure.update(1.0f); // 1 second passed

        // technically we should be 'about done' with building the structure
        // wait one more second to be sure

        guiComposite.update(1.0f); // 1 second passed
        structure.update(1.0f); // 1 second passed

        // now click on the icon again, this should mean we can now PLACE the structure!
        mouse.leftClicked();

        // now we move back into the battlefield, just a bit right of the CONSTYARD, where we assume
        // there is enough space to place this thing (a refinery is 3 wide and 2 high)
        MapCoordinate mapCoordinateToTheRight = MapCoordinate.create(mapCoordinate.getXAsInt() + 2, mapCoordinate.getYAsInt());

        Coordinate coordinateRightOfConstyard = mapCoordinateToTheRight.toCoordinate().add(new Vector2D(1, 1)); // one pixel
        int structurePlacementX = coordinateRightOfConstyard.getXAsInt();
        int structurePlacementY = PlayingState.HEIGHT_OF_TOP_BAR + coordinateRightOfConstyard.getYAsInt();

        // move the mouse to that spot on the battlefield
        mouse.movedTo(Vector2D.create(structurePlacementX, structurePlacementY));

        // once more update states
        guiComposite.update(1.0f); // 1 second passed
        structure.update(1.0f); // 1 second passed

        // render once more:
        // renders 'place it' on icon, renders the structure 'to be placed' thing
        guiComposite.render(graphics);

        // first lets count the amount of entities created:
        int entitiesCount = entityRepository.getEntitiesCount();

        // now place the structure!
        mouse.leftClicked();

        // it should now have increased by one!
        Assert.assertEquals(entitiesCount + 1, entityRepository.getEntitiesCount());
    }
}