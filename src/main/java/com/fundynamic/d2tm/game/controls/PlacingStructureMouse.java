package com.fundynamic.d2tm.game.controls;


import com.fundynamic.d2tm.game.entities.EntityData;
import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.EntityType;
import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.math.Random;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class PlacingStructureMouse extends AbstractMouseBehavior {

    private final EntityRepository entityRepository;
    private EntityData entityToPlace;

    public PlacingStructureMouse(Mouse mouse, EntityRepository entityRepository) {
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

    public void render(Graphics graphics, int x, int y) {
        graphics.setColor(Color.green);
        graphics.setLineWidth(1.1f);
        graphics.drawRect(x, y, entityToPlace.width, entityToPlace.height);
    }

    private void selectRandomlySomethingToPlace() {
        entityToPlace = entityRepository.getEntityData(EntityType.STRUCTURE, Random.getRandomBetween(0, 2));
        mouse.setMouseImage(entityToPlace.getFirstImage(), 16, 16);
    }

    @Override
    public String toString() {
        return "PlacingStructureMouse{" +
                "entityToPlace=" + entityToPlace +
                '}';
    }
}
