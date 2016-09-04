package com.fundynamic.d2tm.game.controls.battlefield;


import com.fundynamic.d2tm.game.entities.EntitiesData;
import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.EntityData;
import com.fundynamic.d2tm.game.entities.EntityType;
import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.game.rendering.gui.battlefield.BattleField;
import com.fundynamic.d2tm.math.Coordinate;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class PlacingStructureMouse extends AbstractBattleFieldMouseBehavior {

    private EntityData entityToPlace;

    public PlacingStructureMouse(BattleField battleField, EntityData entityData) {
        super(battleField);
        this.entityToPlace = entityData;
    }

    @Override
    public void leftClicked() {
        Cell hoverCell = getHoverCell();
        // TODO: Check if it may be placed or not...
        Entity entity = entityRepository.placeOnMap(hoverCell.getCoordinates(), entityToPlace, mouse.getControllingPlayer());
        battleField.entityPlacedOnMap(entity);
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
        Cell hoverCell = getHoverCell();
        if (hoverCell == null) return;

        Coordinate absoluteCoordinates = hoverCell.getCoordinates();
        Coordinate viewportCoordinate = battleField.translateAbsoluteMapCoordinateToViewportCoordinate(absoluteCoordinates);

        graphics.setColor(Color.green);
        float lineWidth = graphics.getLineWidth();
        graphics.setLineWidth(1.1f);
        graphics.drawImage(entityToPlace.getFirstImage(), viewportCoordinate.getXAsInt(), viewportCoordinate.getYAsInt());
        graphics.drawRect(viewportCoordinate.getXAsInt(), viewportCoordinate.getYAsInt(), entityToPlace.getWidth(), entityToPlace.getHeight());
        graphics.setLineWidth(lineWidth);
    }

    @Override
    public String toString() {
        return "PlacingStructureMouse{" +
                "entityToPlace=" + entityToPlace +
                '}';
    }
}
