package com.fundynamic.d2tm.game.entities;

import com.fundynamic.d2tm.game.AbstractD2TMTest;
import com.fundynamic.d2tm.game.entities.entitiesdata.EntitiesData;
import com.fundynamic.d2tm.game.entities.structures.Structure;
import com.fundynamic.d2tm.game.entities.units.Unit;
import com.fundynamic.d2tm.game.types.EntityData;
import com.fundynamic.d2tm.math.MapCoordinate;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


public class EntityDataTest extends AbstractD2TMTest {

    @Test
    public void getAllCellsAsVectorsReturnsListOfOneVectorForEntityQUAD() {
        Unit quad = entityRepository.placeUnitOnMap(MapCoordinate.create(10, 15), EntitiesData.QUAD, player);
        List<MapCoordinate> allCellsAsVectors = quad.getAllCellsAsCoordinates();
        assertThat(allCellsAsVectors.size(), is(1));

        assertThat(allCellsAsVectors.get(0), is(MapCoordinate.create(10, 15)));
    }

    @Test
    public void getAllCellsAsVectorsReturnsListOfOneVectorForEntityTRIKE() {
        Unit trike = entityRepository.placeUnitOnMap(MapCoordinate.create(10, 15), EntitiesData.TRIKE, player);
        List<MapCoordinate> allCellsAsVectors = trike.getAllCellsAsCoordinates();
        assertThat(allCellsAsVectors.size(), is(1));

        assertThat(allCellsAsVectors.get(0), is(MapCoordinate.create(10, 15)));
    }

    @Test
    public void getAllCellsAsVectorsReturnsListOfSixVectorsForEntityREFINERY() {
        Structure refinery = entityRepository.placeStructureOnMap(MapCoordinate.create(15, 20), EntitiesData.REFINERY, player);
        List<MapCoordinate> allCellsAsVectors = refinery.getAllCellsAsCoordinates();
        assertThat(allCellsAsVectors.size(), is(6));

        // refinery is 3x2. Starting from 15, 20
        assertThat(allCellsAsVectors, hasItem(MapCoordinate.create(15, 20)));
        assertThat(allCellsAsVectors, hasItem(MapCoordinate.create(15 + 1, 20)));
        assertThat(allCellsAsVectors, hasItem(MapCoordinate.create(15 + 2, 20)));

        assertThat(allCellsAsVectors, hasItem(MapCoordinate.create(15, 20 + 1)));
        assertThat(allCellsAsVectors, hasItem(MapCoordinate.create(15 + 1, 20 + 1)));
        assertThat(allCellsAsVectors, hasItem(MapCoordinate.create(15 + 2, 20 + 1)));
    }

    @Test
    public void emptyBuildList() {
        EntityData entityData = new EntityData();
        entityData.buildList = "";

        List<String> entityDataKeysToBuild = entityData.getEntityDataKeysToBuild();
        Assert.assertEquals(0, entityDataKeysToBuild.size());
    }

    @Test
    public void singleBuildList() {
        EntityData entityData = new EntityData();
        entityData.buildList = "WINDTRAP";

        List<String> entityDataKeysToBuild = entityData.getEntityDataKeysToBuild();
        Assert.assertEquals(1, entityDataKeysToBuild.size());
        Assert.assertEquals("WINDTRAP", entityDataKeysToBuild.get(0));
    }

    @Test
    public void multipleBuildList() {
        EntityData entityData = new EntityData();
        entityData.buildList = "WINDTRAP,REFINERY";

        List<String> entityDataKeysToBuild = entityData.getEntityDataKeysToBuild();
        Assert.assertEquals(2, entityDataKeysToBuild.size());
        Assert.assertEquals("WINDTRAP", entityDataKeysToBuild.get(0));
        Assert.assertEquals("REFINERY", entityDataKeysToBuild.get(1));
    }

    @Test
    public void singleBuildListWithSpaces() {
        EntityData entityData = new EntityData();
        entityData.buildList = "  WINDTRAP  ";

        List<String> entityDataKeysToBuild = entityData.getEntityDataKeysToBuild();
        Assert.assertEquals(1, entityDataKeysToBuild.size());
        Assert.assertEquals("WINDTRAP", entityDataKeysToBuild.get(0));
    }

    @Test
    public void multipleBuildListWithSpaces() {
        EntityData entityData = new EntityData();
        entityData.buildList = " WINDTRAP , REFINERY ";

        List<String> entityDataKeysToBuild = entityData.getEntityDataKeysToBuild();
        Assert.assertEquals(2, entityDataKeysToBuild.size());
        Assert.assertEquals("WINDTRAP", entityDataKeysToBuild.get(0));
        Assert.assertEquals("REFINERY", entityDataKeysToBuild.get(1));
    }

}