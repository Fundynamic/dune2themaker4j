package com.fundynamic.d2tm.game.controls.battlefield;


import com.fundynamic.d2tm.Game;
import com.fundynamic.d2tm.game.entities.*;
import com.fundynamic.d2tm.game.entities.entitybuilders.PlacementBuildableEntity;
import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.game.rendering.gui.battlefield.BattleField;
import com.fundynamic.d2tm.game.terrain.ConstructionGround;
import com.fundynamic.d2tm.math.Coordinate;
import com.fundynamic.d2tm.math.MapCoordinate;
import com.fundynamic.d2tm.math.Vector2D;
import com.fundynamic.d2tm.utils.Colors;
import org.newdawn.slick.Graphics;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class PlacingStructureMouse extends AbstractBattleFieldMouseBehavior {

    private EntityData entityDataToPlace;
    private Entity entityWhoConstructsIt;
    private List<PlaceableMapCoordinateCandidate> mapCoordinatesForEntityToPlace;
    private EntityRepository entityRepository;

    public PlacingStructureMouse(BattleField battleField, PlacementBuildableEntity placementBuildableEntity) {
        super(battleField);
        this.entityRepository = battleField.getEntityRepository();

        this.entityDataToPlace = placementBuildableEntity.getEntityData();
        this.entityWhoConstructsIt = placementBuildableEntity.getEntityWhoConstructsThis();
        this.mapCoordinatesForEntityToPlace = new ArrayList<>();
    }

    @Override
    public void leftClicked() {
        boolean isPassable = !this.mapCoordinatesForEntityToPlace.stream().anyMatch(c -> c.isPassable == false);
        if (!isPassable) return;

        Entity entity = entityRepository.placeOnMap(getAbsoluteCoordinateTopLeftOfStructureToPlace(), entityDataToPlace, mouse.getControllingPlayer());
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
        if (mapCoordinatesForEntityToPlace.isEmpty()) return;

        MapCoordinate topLeftMapCoordinate = mapCoordinatesForEntityToPlace.get(0).mapCoordinate;

        Coordinate coordinateTopLeft = battleField.translateAbsoluteMapCoordinateToViewportCoordinate(topLeftMapCoordinate.toCoordinate());
        graphics.drawImage(entityDataToPlace.getFirstImage(), coordinateTopLeft.getXAsInt(), coordinateTopLeft.getYAsInt());

        for (PlaceableMapCoordinateCandidate placeableMapCoordinateCandidate : mapCoordinatesForEntityToPlace) {
            Coordinate absoluteMapCoordinate = placeableMapCoordinateCandidate.mapCoordinate.toCoordinate();
            Coordinate coordinate = battleField.translateAbsoluteMapCoordinateToViewportCoordinate(absoluteMapCoordinate);
            if (placeableMapCoordinateCandidate.isPassable) {
                graphics.setColor(Colors.GREEN_ALPHA_128);
            } else {
                graphics.setColor(Colors.RED_ALPHA_128);
            }
            graphics.fillRect(coordinate.getXAsInt(), coordinate.getYAsInt(), Game.TILE_SIZE, Game.TILE_SIZE);
        }
    }

    @Override
    public void movedTo(Vector2D coordinates) {
        super.movedTo(coordinates);

        Coordinate absoluteMapCoordinateOfTopleftOfStructure = getAbsoluteCoordinateTopLeftOfStructureToPlace();

        // first determine all cells that will be occupied
        mapCoordinatesForEntityToPlace = new ArrayList<>();

        List<MapCoordinate> allMapCoordinates = this.entityDataToPlace.getAllCellsAsCoordinates(absoluteMapCoordinateOfTopleftOfStructure);

        mapCoordinatesForEntityToPlace =
                allMapCoordinates
                        .stream()
                        .map(mapCoordinate -> new PlaceableMapCoordinateCandidate(mapCoordinate, false))
                        .collect(toList());

        // Now, do checks if the structure may be placed
        for (PlaceableMapCoordinateCandidate placeableMapCoordinateCandidate : mapCoordinatesForEntityToPlace) {
            Coordinate absoluteMapCoordinate = placeableMapCoordinateCandidate.mapCoordinate.toCoordinate();
            Coordinate coordinate = battleField.translateAbsoluteMapCoordinateToViewportCoordinate(absoluteMapCoordinate);

            Cell cell = battleField.getCellByAbsoluteViewportCoordinate(coordinate);

            // first check if it is visible (easiest check)
            boolean isPlaceable = cell.isVisibleFor(player);

            // visible? then check if it may be constructed (is it construction ground?)
            if (isPlaceable && (cell.getTerrain() instanceof ConstructionGround) == false) {
                isPlaceable = false;
            }

            // TODO: distance checking
            Coordinate constructingEntityCoordinate = battleField.translateAbsoluteMapCoordinateToViewportCoordinate(entityWhoConstructsIt.getCenteredCoordinate());

            // still placeable? good. Final (expensive) check -> any other units that may block this?
            if (isPlaceable) {
                EntitiesSet entitiesAtMapCoordinate = entityRepository.findAliveEntitiesOfTypeAtVector(absoluteMapCoordinate, EntityType.STRUCTURE, EntityType.UNIT);
                isPlaceable = entitiesAtMapCoordinate.isEmpty();
            }
            placeableMapCoordinateCandidate.isPassable = isPlaceable;
        }

    }

    public Coordinate getAbsoluteCoordinateTopLeftOfStructureToPlace() {
        // first get absolute viewport coordinates, we can calculate on the battlefield with that
        Coordinate viewportCoordinate = battleField.translateScreenToViewportCoordinate(mouseCoordinates);

        // now substract half of the structure to place, so we make the structure to place center beneath the mouse
        Vector2D halfSize = entityDataToPlace.getHalfSize();
        Coordinate topLeftOfStructure = viewportCoordinate.min(halfSize);

        Cell topLeftCellOfStructure = battleField.getCellByAbsoluteViewportCoordinate(topLeftOfStructure);
        return topLeftCellOfStructure.getCoordinates();
    }

    @Override
    public String toString() {
        return "PlacingStructureMouse{" +
                "entityDataToPlace=" + entityDataToPlace +
                '}';
    }

    private class PlaceableMapCoordinateCandidate  {

        public MapCoordinate mapCoordinate;
        public boolean isPassable = false;

        public PlaceableMapCoordinateCandidate(MapCoordinate mapCoordinate, boolean passable) {
            this.mapCoordinate = mapCoordinate;
            this.isPassable = passable;
        }
    }
}
