package com.fundynamic.d2tm.game.entities.units.states;

import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.units.Unit;
import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.math.MapCoordinate;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class SeekHarvestableResourceState extends UnitState {

    public SeekHarvestableResourceState(Unit unit, EntityRepository entityRepository, Map map) {
        super(unit, entityRepository, map);
    }

    @Override
    public void update(float deltaInSeconds) {
        Set<MapCoordinate> areaInCoordinates = unit.getAllSurroundingCellsAsMapCoordinatesStartingFromTopLeft(5);
        List<Cell> harvestableCells = areaInCoordinates
                .stream()
                .filter(mapCoordinate -> map.isWithinPlayableMapBoundaries(mapCoordinate))
                .map(mapCoordinate -> map.getCell(mapCoordinate))
                .filter(cell -> cell.isHarvestable() && cell.isPassable(unit))
                .filter(cell -> unit.isCellPassableForMe(cell.getCoordinate()))
                .sorted((c1, c2) ->  Float.compare(c1.distance(unit), c2.distance(unit)))
                .collect(Collectors.toList());

        if (harvestableCells.isEmpty()) {
            return;
        }

        unit.harvestAt(harvestableCells.get(0).getCoordinate());
    }

    @Override
    public String toString() {
        return "SeekHarvestableResourceState";
    }

}
