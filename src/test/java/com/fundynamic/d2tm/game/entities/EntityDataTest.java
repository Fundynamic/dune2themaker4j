package com.fundynamic.d2tm.game.entities;

import com.fundynamic.d2tm.Game;
import com.fundynamic.d2tm.game.AbstractD2TMTest;
import com.fundynamic.d2tm.game.entities.structures.Structure;
import com.fundynamic.d2tm.game.entities.units.Unit;
import com.fundynamic.d2tm.math.Coordinate;
import org.junit.Test;

import java.util.List;

import static com.fundynamic.d2tm.math.Coordinate.create;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


public class EntityDataTest extends AbstractD2TMTest {

    @Test
    public void getAllCellsAsVectorsReturnsListOfOneVectorForEntityQUAD() {
        Unit quad = entityRepository.placeUnitOnMap(Coordinate.create(10, 15), "QUAD", player);
        List<Coordinate> allCellsAsVectors = quad.getAllCellsAsCoordinates();
        assertThat(allCellsAsVectors.size(), is(1));

        assertThat(allCellsAsVectors.get(0), is(create(10, 15)));
    }

    @Test
    public void getAllCellsAsVectorsReturnsListOfOneVectorForEntityTRIKE() {
        Unit trike = entityRepository.placeUnitOnMap(Coordinate.create(10, 15), "TRIKE", player);
        List<Coordinate> allCellsAsVectors = trike.getAllCellsAsCoordinates();
        assertThat(allCellsAsVectors.size(), is(1));

        assertThat(allCellsAsVectors.get(0), is(create(10, 15)));
    }

    @Test
    public void getAllCellsAsVectorsReturnsListOfSixVectorsForEntityREFINERY() {
        Structure refinery = entityRepository.placeStructureOnMap(Coordinate.create(15, 20), "REFINERY", player);
        List<Coordinate> allCellsAsVectors = refinery.getAllCellsAsCoordinates();
        assertThat(allCellsAsVectors.size(), is(6));

        // refinery is 3x2. Starting from 15, 20
        assertThat(allCellsAsVectors, hasItem(create(15, 20)));
        assertThat(allCellsAsVectors, hasItem(create(15 + Game.TILE_SIZE, 20)));
        assertThat(allCellsAsVectors, hasItem(create(15 + Game.TILE_SIZE + Game.TILE_SIZE, 20)));

        assertThat(allCellsAsVectors, hasItem(create(15, 20 + Game.TILE_SIZE)));
        assertThat(allCellsAsVectors, hasItem(create(15 + Game.TILE_SIZE, 20 + Game.TILE_SIZE)));
        assertThat(allCellsAsVectors, hasItem(create(15 + Game.TILE_SIZE + Game.TILE_SIZE, 20 + Game.TILE_SIZE)));
    }

}