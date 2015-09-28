package com.fundynamic.d2tm.game.controls;


import com.fundynamic.d2tm.game.entities.EntityData;
import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.EntityType;
import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.math.Random;

public class PlacingUnitMouse extends AbstractMouseBehavior {

    private final EntityRepository entityRepository;
    private EntityData entityToPlace;

    public PlacingUnitMouse(Mouse mouse, EntityRepository entityRepository) {
        super(mouse);
        this.entityRepository = entityRepository;
        selectRandomlySomethingToPlace();
    }

    @Override
    public void leftClicked() {
        Cell hoverCell = mouse.getHoverCell();
        entityRepository.placeOnMap(hoverCell.getCoordinatesAsAbsoluteVector2D(), entityToPlace, mouse.getControllingPlayer());
        selectRandomlySomethingToPlace();
    }

    @Override
    public void rightClicked() {
        mouse.setMouseBehavior(new NormalMouse(mouse));
    }

    @Override
    public void mouseMovedToCell(Cell cell) {
        mouse.setHoverCell(cell);
    }

    private void selectRandomlySomethingToPlace() {
        entityToPlace = entityRepository.getEntityData(EntityType.UNIT, Random.getRandomBetween(0, 2));
        if (entityToPlace != null) {
            mouse.setMouseImage(entityToPlace.getFirstImage(), 0, 0);
        }
    }

    @Override
    public String toString() {
        return "PlacingUnitMouse{" +
                "entityToPlace=" + entityToPlace +
                '}';
    }
}
