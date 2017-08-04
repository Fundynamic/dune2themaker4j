package com.fundynamic.d2tm.game.entities.units.states;

import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.units.Unit;
import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.math.Random;

import java.util.ArrayList;
import java.util.List;


public class SeekSpiceState extends UnitState {

    public SeekSpiceState(Unit unit, EntityRepository entityRepository, Map map) {
        super(unit, entityRepository, map);
    }

    @Override
    public void update(float deltaInSeconds) {
        Cell unitCell = map.getCellFor(unit);

        List<Cell> surroundingCells = unitCell.getSurroundingCells();

        List<Cell> possibleCandidateCells = new ArrayList<>();
        for (Cell cell : surroundingCells) {
            if (cell.isHarvestable()) {
                possibleCandidateCells.add(cell);
            }
        }

        Cell cell = Random.getRandomItem(possibleCandidateCells);
        if (cell != null) {
            unit.harvestAt(cell.getCoordinates());
        }
    }

    @Override
    public String toString() {
        return "SeekSpiceState";
    }

}
