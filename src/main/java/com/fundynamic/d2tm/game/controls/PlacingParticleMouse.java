package com.fundynamic.d2tm.game.controls;


import com.fundynamic.d2tm.game.entities.EntitiesData;
import com.fundynamic.d2tm.game.entities.EntityData;
import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.EntityType;
import com.fundynamic.d2tm.game.map.Cell;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class PlacingParticleMouse extends AbstractMouseBehavior {

    private final EntityRepository entityRepository;
    private EntityData entityToPlace;

    public PlacingParticleMouse(Mouse mouse, EntityRepository entityRepository) {
        super(mouse);
        this.entityRepository = entityRepository;
        selectRandomlySomethingToPlace();
    }

    @Override
    public void leftClicked() {
        Cell hoverCell = mouse.getHoverCell();
        if (hoverCell == null) return;

        entityRepository.placeOnMap(hoverCell.getCoordinates(), entityToPlace, mouse.getControllingPlayer());

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
        graphics.drawRect(x, y, entityToPlace.getWidth(), entityToPlace.getHeight());
    }

    private void selectRandomlySomethingToPlace() {
        entityToPlace = entityRepository.getEntityData(EntityType.PARTICLE, EntitiesData.EXPLOSION_SMALL_UNIT);
        if (entityToPlace != null) {
            mouse.setMouseImage(entityToPlace.getFirstImage(), 24, 24);
        }
    }

    @Override
    public String toString() {
        return "PlacingParticleMouse{" +
                "entityToPlace=" + entityToPlace +
                '}';
    }
}
