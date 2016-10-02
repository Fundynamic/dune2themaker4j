package com.fundynamic.d2tm.game.controls.battlefield;


import com.fundynamic.d2tm.game.entities.*;
import com.fundynamic.d2tm.game.entities.entitybuilders.PlacementBuildableEntity;
import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.game.rendering.gui.battlefield.BattleField;
import com.fundynamic.d2tm.game.terrain.ConstructionGround;
import com.fundynamic.d2tm.math.Coordinate;
import com.fundynamic.d2tm.math.MapCoordinate;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import java.util.List;

public class PlacingStructureMouse extends AbstractBattleFieldMouseBehavior {

    private EntityData entityDataToPlace;
    private Entity entityWhoConstructsIt;

    public PlacingStructureMouse(BattleField battleField, PlacementBuildableEntity placementBuildableEntity) {
        super(battleField);
        this.entityDataToPlace = placementBuildableEntity.getEntityData();
        this.entityWhoConstructsIt = placementBuildableEntity.getEntityWhoConstructsThis();
    }

    @Override
    public void leftClicked() {
//        Cell hoverCell = getHoverCell();
        Coordinate viewportCoordinate = battleField.translateScreenToViewportCoordinate(mouseCoordinates);

        // now substract half of the structure to place, so we make the structure to place center beneath the mouse
        Vector2D halfSize = entityDataToPlace.getHalfSize();
        Coordinate topLeftOfStructure = viewportCoordinate.min(halfSize);

        Coordinate coordinates = battleField.getCellByAbsoluteViewportCoordinate(topLeftOfStructure).getCoordinates();

        // TODO: Check if it may be placed or not...
        Entity entity = entityRepository.placeOnMap(coordinates, entityDataToPlace, mouse.getControllingPlayer());
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

        Coordinate constructingEntityCoordinate = battleField.translateAbsoluteMapCoordinateToViewportCoordinate(entityWhoConstructsIt.getCenteredCoordinate());

        graphics.setColor(Color.green);
        float lineWidth = graphics.getLineWidth();
        graphics.setLineWidth(1.1f);

        // first get absolute viewport coordinates, we can calculate on the battlefield with that
        Coordinate viewportCoordinate = battleField.translateScreenToViewportCoordinate(mouseCoordinates);

        // now substract half of the structure to place, so we make the structure to place center beneath the mouse
        Vector2D halfSize = entityDataToPlace.getHalfSize();
        Coordinate topLeftOfStructure = viewportCoordinate.min(halfSize);

//        Coordinate absoluteMapCoordinateOfTopleftOfStructure = battleField.translateViewportCoordinateToAbsoluteMapCoordinate(topLeftOfStructure);
        Cell topLeftCellOfStructure = battleField.getCellByAbsoluteViewportCoordinate(topLeftOfStructure);
        Coordinate absoluteMapCoordinateOfTopleftOfStructure = topLeftCellOfStructure.getCoordinates();

        EntityRepository entityRepository = battleField.getEntityRepository();

        List<MapCoordinate> allCellsAsCoordinates = this.entityDataToPlace.getAllCellsAsCoordinates(absoluteMapCoordinateOfTopleftOfStructure);
        for (MapCoordinate mapCoordinate : allCellsAsCoordinates) {
            Coordinate absoluteMapCoordinate = mapCoordinate.toCoordinate();
            Coordinate coordinate = battleField.translateAbsoluteMapCoordinateToViewportCoordinate(absoluteMapCoordinate);

            Cell cell = battleField.getCellByAbsoluteViewportCoordinate(coordinate);
            boolean placeable = cell.isVisibleFor(player);
            if ((cell.getTerrain() instanceof ConstructionGround) == false) {
                placeable = false;
            }

            if (placeable) {
                EntitiesSet entitiesAtMapCoordinate = entityRepository.findAliveEntitiesOfTypeAtVector(absoluteMapCoordinate, EntityType.STRUCTURE, EntityType.UNIT);
                placeable = entitiesAtMapCoordinate.isEmpty();
            }

            if (placeable) {
                graphics.setColor(Color.green);
                graphics.drawRect(coordinate.getXAsInt(), coordinate.getYAsInt(), 32, 32);
            }else {
                graphics.setColor(Color.red);
                graphics.drawRect(coordinate.getXAsInt(), coordinate.getYAsInt(), 32, 32);
                graphics.drawLine(coordinate.getXAsInt(), coordinate.getYAsInt(), coordinate.getXAsInt() + 32, coordinate.getYAsInt() + 32);
                graphics.drawLine(coordinate.getXAsInt(), coordinate.getYAsInt() + 32, coordinate.getXAsInt() + 32, coordinate.getYAsInt());
            }
        }

//        SlickUtils.drawLine(graphics, constructingEntityCoordinate, mouseCoordinates);

        graphics.setLineWidth(lineWidth);
    }

    @Override
    public void movedTo(Vector2D coordinates) {
        super.movedTo(coordinates);
    }

    @Override
    public String toString() {
        return "PlacingStructureMouse{" +
                "entityDataToPlace=" + entityDataToPlace +
                '}';
    }
}
