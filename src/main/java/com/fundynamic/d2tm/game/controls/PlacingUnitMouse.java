package com.fundynamic.d2tm.game.controls;


import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.math.Random;
import com.fundynamic.d2tm.math.Vector2D;

public class PlacingUnitMouse extends AbstractMouseBehavior {

    private final EntityRepository entityRepository;
    private EntityRepository.EntityData entityToPlace;

    public PlacingUnitMouse(Mouse mouse, EntityRepository entityRepository) {
        super(mouse);
        this.entityRepository = entityRepository;
        selectRandomlySomethingToPlace();
    }

    @Override
    public void leftClicked() {
        Cell hoverCell = mouse.getHoverCell();
        entityRepository.placeOnMap(hoverCell.getCoordinatesAsVector2D(), entityToPlace, mouse.getControllingPlayer());
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
        entityToPlace = entityRepository.getEntityData(EntityRepository.EntityType.UNIT, Random.getRandomBetween(0, 2));
        mouse.setMouseImage(entityToPlace.getFirstImage(), 0, 0);
    }

}
