package com.fundynamic.d2tm.game.controls;


import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.math.Random;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class PlacingStructureMouse extends AbstractMouseBehavior {

    private final EntityRepository entityRepository;
    private EntityRepository.EntityData entityToPlace;

    public PlacingStructureMouse(Player controllingPlayer, Mouse mouse, EntityRepository entityRepository) {
        super(controllingPlayer, mouse);
        this.entityRepository = entityRepository;
        selectRandomlySomethingToPlace();
    }

    @Override
    public void leftClicked() {
        Cell hoverCell = mouse.getHoverCell();
        entityRepository.placeOnMap(hoverCell.getCoordinatesAsVector2D(), entityToPlace, controllingPlayer);
        selectRandomlySomethingToPlace();
    }

    @Override
    public void rightClicked() {
        mouse.setMouseBehavior(new NormalMouse(controllingPlayer, mouse));
    }

    @Override
    public void mouseMovedToCell(Cell cell) {
        mouse.setHoverCell(cell);
    }

    @Override
    public void render(Graphics graphics, int x, int y) {
        graphics.setColor(Color.green);
        graphics.setLineWidth(1.1f);
        entityToPlace.getFirstImage().draw(x, y);
        graphics.drawRect(x, y, entityToPlace.width, entityToPlace.height);
    }

    private void selectRandomlySomethingToPlace() {
        entityToPlace = entityRepository.getEntityData(EntityRepository.EntityType.STRUCTURE, Random.getRandomBetween(0, 2));
    }

}
