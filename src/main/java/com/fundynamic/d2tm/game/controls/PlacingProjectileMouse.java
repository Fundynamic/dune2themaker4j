package com.fundynamic.d2tm.game.controls;


import com.fundynamic.d2tm.game.entities.*;
import com.fundynamic.d2tm.game.entities.projectiles.Projectile;
import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.math.Random;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import java.util.ArrayList;
import java.util.Set;

public class PlacingProjectileMouse extends AbstractMouseBehavior {

    private final EntityRepository entityRepository;
    private EntityData entityToPlace;

    public PlacingProjectileMouse(Mouse mouse, EntityRepository entityRepository) {
        super(mouse);
        this.entityRepository = entityRepository;
        selectRandomlySomethingToPlace();
    }

    @Override
    public void leftClicked() {
        Cell hoverCell = mouse.getHoverCell();
        Entity entityPlacedOnMap = entityRepository.placeOnMap(hoverCell.getCoordinatesAsAbsoluteVector2D(), entityToPlace, mouse.getControllingPlayer());
        Projectile projectile = (Projectile) entityPlacedOnMap;

        // temporarily give some move to command to a projectile
        Set<Entity> entities = entityRepository.filter(Predicate.builder().ofType(EntityType.STRUCTURE));
        ArrayList<Entity> ents = new ArrayList(entities);
        Entity randomUnit = ents.get(0);
//        Entity randomUnit = ents.get(Random.getRandomBetween(0, ents.size()));
        projectile.moveTo(randomUnit.getAbsoluteMapCoordinates());

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
        entityToPlace = entityRepository.getEntityData(EntityType.PROJECTILE, EntityRepository.ROCKET);
        if (entityToPlace != null) {
            mouse.setMouseImage(entityToPlace.getFirstImage(), 16, 16);
        }
    }

    @Override
    public String toString() {
        return "PlacingProjectileMouse{" +
                "entityToPlace=" + entityToPlace +
                '}';
    }
}
