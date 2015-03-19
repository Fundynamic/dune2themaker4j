package com.fundynamic.d2tm.game.controls;


import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.math.Random;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

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

    @Override
    public void render(Graphics graphics, int x, int y) {
        graphics.setColor(Color.magenta);
        graphics.setLineWidth(1.1f);
        entityToPlace.getFirstImage().draw(x, y);
        graphics.drawRect(x, y, entityToPlace.width, entityToPlace.height);
    }

    private void selectRandomlySomethingToPlace() {
        entityToPlace = entityRepository.getEntityData(EntityRepository.EntityType.UNIT, Random.getRandomBetween(0, 2));
    }

}
