package com.fundynamic.d2tm.game.controls.battlefield;


import com.fundynamic.d2tm.game.entities.EntitiesData;
import com.fundynamic.d2tm.game.entities.EntityData;
import com.fundynamic.d2tm.game.entities.EntityType;
import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.game.rendering.gui.battlefield.BattleField;
import org.newdawn.slick.Graphics;

public class PlacingStructureMouse extends AbstractBattleFieldMouseBehavior {

    private EntityData entityToPlace;

    public PlacingStructureMouse(BattleField battleField) {
        super(battleField);
        selectRandomlySomethingToPlace();
    }

    @Override
    public void leftClicked() {
        Cell hoverCell = getHoverCell();
        entityRepository.placeOnMap(hoverCell.getCoordinates(), entityToPlace, mouse.getControllingPlayer());
        selectRandomlySomethingToPlace();
    }

    @Override
    public void rightClicked() {
        setMouseBehavior(new NormalMouse(battleField));
    }

    @Override
    public void mouseMovedToCell(Cell cell) {
        setHoverCell(cell);
    }

    @Override
    public void render(Graphics graphics) {
//        graphics.setColor(Color.green);
//        graphics.setLineWidth(1.1f);
//        graphics.drawRect(x, y, entityToPlace.getWidth(), entityToPlace.getHeight());
        graphics.drawImage(entityToPlace.image, mouseCoordinates.getXAsInt(), mouseCoordinates.getYAsInt());
    }

    private void selectRandomlySomethingToPlace() {
        entityToPlace = entityRepository.getEntityData(EntityType.STRUCTURE, EntitiesData.CONSTRUCTION_YARD);
        if (entityToPlace != null) {
            mouse.setMouseImage(entityToPlace.getFirstImage(), 16, 16);
        }
    }

    @Override
    public String toString() {
        return "PlacingStructureMouse{" +
                "entityToPlace=" + entityToPlace +
                '}';
    }
}
