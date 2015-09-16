package com.fundynamic.d2tm.game.map;


import com.fundynamic.d2tm.game.terrain.Terrain;
import com.fundynamic.d2tm.game.terrain.impl.EmptyTerrain;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CellFactory {

    public static Cell makeCell() {
        return makeCell(0, 0);
    }

    public static Cell makeCell(int x, int y) {
        Map map = mock(Map.class);
        Terrain terrain = mock(Terrain.class);
        Cell cell = new Cell(map, terrain, x, y);
        return cell;
    }

    public static Cell stubCellForMap(Map map) {
        Cell cell = mock(Cell.class);
        when(cell.getTerrain()).thenReturn(new EmptyTerrain(null));
        when(map.getCell(anyInt(), anyInt())).thenReturn(cell);
        return cell;
    }

}
