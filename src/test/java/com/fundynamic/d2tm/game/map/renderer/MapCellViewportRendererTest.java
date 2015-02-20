package com.fundynamic.d2tm.game.map.renderer;

import com.fundynamic.d2tm.game.map.CellFactory;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.map.MapCell;
import com.fundynamic.d2tm.math.Vector2D;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MapCellViewportRendererTest {

    private static final int TILE_WIDTH = 32;
    private static final int TILE_HEIGHT = 32;

    @Mock
    private Map map;

    @Test(timeout = 2000)
    public void rendersCells() throws SlickException {
        int screenWidth = 800;
        int screenHeight = 600;

        // Ugly leaky abstraction here!
        CellFactory.stubCellForMap(map);

        MapCellViewportRenderer mapCellViewportRenderer = new MapCellViewportRenderer(map, TILE_HEIGHT, TILE_WIDTH, new Vector2D(screenWidth, screenHeight));

        Vector2D viewingVector = Vector2D.zero();

        Renderer renderer = mock(Renderer.class);
        mapCellViewportRenderer.render(mock(Image.class), viewingVector, renderer);

        int cellsToDrawHorizontally = (screenWidth / TILE_WIDTH) + 2; // 2 extra for 'rounding' purposes at right
        int cellsToDrawVertically = (screenHeight / TILE_HEIGHT) + 2; // 2 extra for 'rounding' purposes at bottom
        int numberOfCellsToDraw = cellsToDrawHorizontally * cellsToDrawVertically;

        verify(renderer, times(numberOfCellsToDraw)).draw(any(Graphics.class), any(MapCell.class), anyInt(), anyInt());
    }

}