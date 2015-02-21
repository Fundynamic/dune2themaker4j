package com.fundynamic.d2tm.game.map;


import com.fundynamic.d2tm.game.terrain.Terrain;
import com.fundynamic.d2tm.game.terrain.impl.EmptyTerrain;
import org.mockito.Mockito;
import org.newdawn.slick.Image;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CellFactory {

    public static Cell makeCell() {
        return makeCell(0, 0);
    }

    public static Cell makeCell(int x, int y) {
        Map map = Mockito.mock(Map.class);
        Terrain terrain = Mockito.mock(Terrain.class);
        Cell cell = new Cell(map, terrain, x, y);
        return cell;
//
//        // Ugly leaky abstraction here!
//        Cell cell = mock(MapCell.class);
//        EmptyTerrain emptyTerrain = new EmptyTerrain(mock(Image.class));
//        when(cell.getTerrain()).thenReturn(emptyTerrain);
//        when(map.getCell(x, y)).thenReturn(cell);
//
//        return new MapCell(map, x, y);
    }
//
    public static Cell stubCellForMap(Map map) {
        // Ugly leaky abstraction here!
        Cell cell = mock(Cell.class);
        EmptyTerrain emptyTerrain = new EmptyTerrain(mock(Image.class));
        when(cell.getTerrain()).thenReturn(emptyTerrain);
        when(map.getCell(anyInt(), anyInt())).thenReturn(cell);
        return cell;
    }

}
