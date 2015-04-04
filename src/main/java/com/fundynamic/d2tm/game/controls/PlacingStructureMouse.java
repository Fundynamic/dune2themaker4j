package com.fundynamic.d2tm.game.controls;


import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.math.Random;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class PlacingStructureMouse extends AbstractMouseBehavior {

    private final EntityRepository entityRepository;
    private EntityRepository.EntityData entityToPlace;

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

    @Override
    public void draggedToCoordinates(Vector2D coordinates) {
        // DO NOTHING
    }

    @Override
    public void leftButtonReleased() {
        // DO NOTHING
    }

    public void render(Graphics graphics, int x, int y) {
        graphics.setColor(Color.green);
        graphics.setLineWidth(1.1f);
        graphics.drawRect(x, y, entityToPlace.width, entityToPlace.height);
    }

    private void selectRandomlySomethingToPlace() {
        entityToPlace = entityRepository.getEntityData(EntityRepository.EntityType.STRUCTURE, Random.getRandomBetween(0, 2));
        mouse.setMouseImage(entityToPlace.getFirstImage(), 16, 16);
    }

}
