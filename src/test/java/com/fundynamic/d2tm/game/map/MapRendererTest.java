package com.fundynamic.d2tm.game.map;

import com.fundynamic.d2tm.game.TestableImage;
import com.fundynamic.d2tm.game.math.Vector2D;
import com.fundynamic.d2tm.graphics.Shroud;
import com.fundynamic.d2tm.graphics.ShroudFacing;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class MapRendererTest {

    private static final int TILE_WIDTH = 32;
    private static final int TILE_HEIGHT = 32;

    private static final int ANY_WIDTH = 0;
    private static final int ANY_HEIGHT = 0;
    private static final Cell ANY_CELL = mock(Cell.class);


    @Mock
    private Graphics graphics;

    @Mock
    private Image tileImage;


    @Test(timeout = 1000)
    public void rendersCells() throws SlickException {
        MapRenderer renderer = makeMapRenderer();

        Image imageToRenderOn = mock(Image.class);
        Graphics graphicsRenderer = mock(Graphics.class);
        when(imageToRenderOn.getGraphics()).thenReturn(graphicsRenderer);

        Vector2D viewingVector = Vector2D.zero();
        Vector2D resolution = Vector2D.create(800, 600);

        Map map = makeMap(64, 64);

        renderer.render(imageToRenderOn, viewingVector, resolution, map);

        int cellsToDrawHorizontally = (800 / TILE_WIDTH) + 2; // 2 extra for 'rounding' purposes at right
        int cellsToDrawVertically = (600 / TILE_HEIGHT) + 2; // 2 extra for 'rounding' purposes at bottom
        int numberOfCellsToDraw = cellsToDrawHorizontally * cellsToDrawVertically;

        verify(graphicsRenderer, times(numberOfCellsToDraw)).drawImage(Mockito.<Image>any(), anyFloat(), anyFloat());
    }

    private TestingMapRenderer makeMapRenderer() throws SlickException {
        return new TestingMapRenderer();
    }

    private class TestingMapRenderer extends MapRenderer {

        private TestingMapRenderer() {
            super(TILE_WIDTH, TILE_HEIGHT, mock(Shroud.class));
        }

        @Override
        protected ShroudFacing determineShroudFacing(Map map, int x, int y) {
            return null;
        }
    }

    private Map makeMap(int width, int height) {
        Map map = mock(Map.class);
        when(map.getWidth()).thenReturn(width);
        when(map.getHeight()).thenReturn(height);
        when(map.getCell(anyInt(), anyInt())).thenReturn(ANY_CELL);
        return map;
    }

    private Cell makeCell(Image image) throws SlickException {
        Cell cell = mock(Cell.class);
        when(cell.getTileImage()).thenReturn(image);
        return cell;
    }
}