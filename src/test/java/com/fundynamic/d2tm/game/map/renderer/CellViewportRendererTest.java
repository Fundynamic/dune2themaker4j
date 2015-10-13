package com.fundynamic.d2tm.game.map.renderer;

import com.fundynamic.d2tm.game.AbstractD2TMTest;
import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.game.rendering.CellViewportRenderer;
import com.fundynamic.d2tm.game.rendering.Renderer;
import com.fundynamic.d2tm.math.Vector2D;
import org.junit.Test;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import static org.mockito.Mockito.*;

public class CellViewportRendererTest extends AbstractD2TMTest {

    @Test(timeout = 2000)
    public void rendersCells() throws SlickException {
        int screenWidth = 800;
        int screenHeight = 600;

        CellViewportRenderer cellViewportRenderer = new CellViewportRenderer(map, TILE_SIZE, TILE_SIZE, new Vector2D(screenWidth, screenHeight));

        Vector2D viewingVector = Vector2D.zero();

        Renderer renderer = mock(Renderer.class);
        cellViewportRenderer.render(mock(Image.class), viewingVector, renderer);

        int cellsToDrawHorizontally = (screenWidth / TILE_SIZE) + 2; // 2 extra for 'rounding' purposes at right
        int cellsToDrawVertically = (screenHeight / TILE_SIZE) + 2; // 2 extra for 'rounding' purposes at bottom
        int numberOfCellsToDraw = cellsToDrawHorizontally * cellsToDrawVertically;

        // only interested in the amount of times we draw something, not where etc.
        verify(renderer, times(numberOfCellsToDraw)).draw(any(Graphics.class), any(Cell.class), anyInt(), anyInt());
    }

}