package com.fundynamic.d2tm.game.map;


import com.fundynamic.d2tm.game.terrain.impl.EmptyTerrain;
import org.mockito.Mockito;
import org.newdawn.slick.Image;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CellFactory {

    public static MapCell makeCell() {
        return makeCell(0, 0);
    }

    public static MapCell makeCell(int x, int y) {
        Map map = Mockito.mock(Map.class);

        // Ugly leaky abstraction here!
        MapCell cell = mock(MapCell.class);
        EmptyTerrain emptyTerrain = new EmptyTerrain(mock(Image.class));
        when(cell.getTerrain()).thenReturn(emptyTerrain);
        when(map.getCell(x, y)).thenReturn(cell);

        return new MapCell(map, x, y);
    }

    public static MapCell stubCellForMap(Map map) {
        // Ugly leaky abstraction here!
        MapCell cell = mock(MapCell.class);
        EmptyTerrain emptyTerrain = new EmptyTerrain(mock(Image.class));
        when(cell.getTerrain()).thenReturn(emptyTerrain);
        when(map.getCell(anyInt(), anyInt())).thenReturn(cell);
        return cell;
    }

}
