package com.fundynamic.d2tm.game.map;

import com.fundynamic.d2tm.game.TestableImage;
import com.fundynamic.d2tm.game.math.Vector2D;
import com.fundynamic.d2tm.graphics.Shroud;
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

        int cellsToDrawHorizontally = (800 / TILE_WIDTH) + 1; // 1 extra for 'rounding' purposes at right
        int cellsToDrawVertically = (600 / TILE_HEIGHT) + 1; // 1 extra for 'rounding' purposes at bottom
        int numberOfCellsToDraw = cellsToDrawHorizontally * cellsToDrawVertically;

        verify(graphicsRenderer, times(numberOfCellsToDraw)).drawImage(Mockito.<Image>any(), anyFloat(), anyFloat());
    }


    @Test
    public void renderShouldCreateImage() throws Exception {
        MapRenderer renderer = makeMapRenderer();
        Map map = makeMap(ANY_WIDTH, ANY_HEIGHT);

        Image result = renderer.render(map);

        Assert.assertEquals(TestableImage.class, result.getClass());
    }

    @Test
    public void renderShouldReturnImageWithWidthOfNumberOfTilesTimesTileWidth1() throws Exception {
        MapRenderer renderer = makeMapRenderer();
        Map map = makeMap(1, ANY_HEIGHT);

        Image result = renderer.render(map);

        Assert.assertEquals(32, result.getWidth());
    }

    @Test
    public void renderShouldReturnImageWithWidthOfNumberOfTilesTimesTileWidth2() throws Exception {
        MapRenderer renderer = makeMapRenderer();
        Map map = makeMap(2, ANY_HEIGHT);

        Image result = renderer.render(map);

        Assert.assertEquals(64, result.getWidth());
    }

    @Test
    public void renderShouldReturnImageWithHeightOfNumberOfTilesTimesTileHeight1() throws Exception {
        MapRenderer renderer = makeMapRenderer();
        Map map = makeMap(ANY_WIDTH, 1);

        Image result = renderer.render(map);

        Assert.assertEquals(32, result.getHeight());
    }

    @Test
    public void renderShouldReturnImageWithHeightOfNumberOfTilesTimesTileHeight2() throws Exception {
        MapRenderer renderer = makeMapRenderer();
        Map map = makeMap(ANY_WIDTH, 2);

        Image result = renderer.render(map);

        Assert.assertEquals(64, result.getHeight());
    }

    @Test
    public void renderShouldDrawFirstCellOnOriginTile() throws Exception {
        MapRenderer renderer = makeMapRenderer();
        Map map = makeMap(1, 1);
        Cell cell = makeCell(tileImage);

        when(map.getCell(1, 1)).thenReturn(cell);
        renderer.render(map);

        verify(graphics).drawImage(same(tileImage), eq(0f), eq(0f));
    }

    @Test
    public void renderShouldDrawSecondCellOnTileRightOfOriginTile() throws Exception {
        MapRenderer renderer = makeMapRenderer();
        Map map = makeMap(2, 1);
        Cell cell = makeCell(tileImage);

        when(map.getCell(2, 1)).thenReturn(cell);
        renderer.render(map);

        verify(graphics).drawImage(same(tileImage), eq(32f), eq(0f));
    }

    @Test
    public void renderShouldDrawSecondCellOnTileBelowOfOriginTile() throws Exception {
        MapRenderer renderer = makeMapRenderer();
        Map map = makeMap(1, 2);
        Cell cell = makeCell(tileImage);

        when(map.getCell(1, 2)).thenReturn(cell);
        renderer.render(map);

        verify(graphics).drawImage(same(tileImage), eq(0f), eq(32f));
    }

    private TestingMapRenderer makeMapRenderer() throws SlickException {
        return new TestingMapRenderer();
    }

    private class TestingMapRenderer extends MapRenderer {

        private TestingMapRenderer() {
            super(TILE_WIDTH, TILE_HEIGHT, mock(Shroud.class));
        }

        @Override
        protected Image makeImage(int width, int height) throws SlickException {
            return new TestableImage(width, height, graphics);
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