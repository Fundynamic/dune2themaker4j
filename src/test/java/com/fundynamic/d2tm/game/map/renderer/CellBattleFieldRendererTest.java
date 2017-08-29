package com.fundynamic.d2tm.game.map.renderer;

import com.fundynamic.d2tm.game.AbstractD2TMTest;
import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.game.rendering.gui.battlefield.CellViewportRenderer;
import com.fundynamic.d2tm.game.rendering.gui.battlefield.Renderer;
import com.fundynamic.d2tm.math.Vector2D;
import org.junit.Test;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import static com.fundynamic.d2tm.game.map.Cell.TILE_SIZE;
import static org.mockito.Mockito.*;

public class CellBattleFieldRendererTest extends AbstractD2TMTest {

    @Test(timeout = 2000)
    public void rendersCells() throws SlickException {
        int screenWidth = 800;
        int screenHeight = 600;

        CellViewportRenderer cellViewportRenderer = new CellViewportRenderer(map, new Vector2D(screenWidth, screenHeight));

        Vector2D viewingVector = Vector2D.zero();

        Renderer renderer = mock(Renderer.class);
        cellViewportRenderer.render(graphics, viewingVector, renderer);

        int cellsToDrawHorizontally = (screenWidth / TILE_SIZE);    // no extra because exactly 25 cells fit
        int cellsToDrawVertically = (screenHeight / TILE_SIZE) + 1; // 1 extra, because you see just more than 18 cells in height
        int numberOfCellsToDraw = cellsToDrawHorizontally * cellsToDrawVertically;

        // only interested in the amount of times we draw something, not where etc.
        verify(renderer, times(numberOfCellsToDraw)).draw(any(Graphics.class), any(Cell.class), anyInt(), anyInt());
    }

}