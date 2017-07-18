package com.fundynamic.d2tm.game.controls.battlefield;


import com.fundynamic.d2tm.Game;
import com.fundynamic.d2tm.game.controls.Mouse;
import com.fundynamic.d2tm.game.entities.*;
import com.fundynamic.d2tm.game.entities.entitybuilders.PlacementBuildableEntity;
import com.fundynamic.d2tm.game.entities.predicates.PredicateBuilder;
import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.game.rendering.gui.battlefield.BattleField;
import com.fundynamic.d2tm.game.terrain.ConstructionGround;
import com.fundynamic.d2tm.math.Coordinate;
import com.fundynamic.d2tm.math.MapCoordinate;
import com.fundynamic.d2tm.math.Vector2D;
import com.fundynamic.d2tm.utils.Colors;
import com.fundynamic.d2tm.utils.SlickUtils;
import org.newdawn.slick.Graphics;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

import static com.fundynamic.d2tm.game.map.Cell.TILE_SIZE;

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
        if (!canPlaceEntity()) return;

        // tell battlefield of the created entity
        battleField.entityPlacedOnMap(
                entityRepository.placeOnMap( // place entity on map
                        getAbsoluteCoordinateTopLeftOfStructureToPlace(),
                        entityDataToPlace,
                        mouse.getControllingPlayer()
                )
        );
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

        if (entityDataToPlace.isTypeStructure()) {
            doLogic(graphics);
        } else if (entityDataToPlace.isTypeSuperPower()) {
            mouse.setMouseImage(Mouse.MouseImages.ATTACK, 16, 16);
        } else {
            MapCoordinate topLeftMapCoordinate = mapCoordinatesForEntityToPlace.get(0).mapCoordinate;
            Coordinate coordinateTopLeft = battleField.translateMapCoordinateToViewportCoordinate(topLeftMapCoordinate);
            SlickUtils.drawImage(graphics, entityDataToPlace.getFirstImage(), coordinateTopLeft);
        }
    }

    public void doLogic(Graphics graphics) {
        MapCoordinate topLeftMapCoordinate = mapCoordinatesForEntityToPlace.get(0).mapCoordinate;
        Coordinate coordinateTopLeft = battleField.translateMapCoordinateToViewportCoordinate(topLeftMapCoordinate);
        SlickUtils.drawImage(graphics, entityDataToPlace.getFirstImage(), coordinateTopLeft);
        // Now, do checks if the structure may be placed
        for (PlaceableMapCoordinateCandidate placeableMapCoordinateCandidate : mapCoordinatesForEntityToPlace) {
            Coordinate absoluteMapCoordinate = placeableMapCoordinateCandidate.mapCoordinate.toCoordinate().addHalfTile();
            Coordinate coordinate = battleField.translateAbsoluteMapCoordinateToViewportCoordinate(absoluteMapCoordinate);

            Cell cell = battleField.getCellByAbsoluteViewportCoordinate(coordinate);

            // first check if it is visible (easiest check)
            boolean isPlaceable = cell.isVisibleFor(player);

            // visible? then check if it may be constructed (is it construction ground?)
            if (isPlaceable && !(cell.getTerrain() instanceof ConstructionGround)) {
                isPlaceable = false;
            }

            // Calculate distance first, later do checking
            Entity closestFriendlyStructure = findClosestStructureOfPlayer(battleField.translateViewportCoordinateToAbsoluteMapCoordinate(coordinate));
            Coordinate constructingEntityCoordinate = battleField.translateAbsoluteMapCoordinateToViewportCoordinate(closestFriendlyStructure.getCenteredCoordinate());

            placeableMapCoordinateCandidate.distance = constructingEntityCoordinate.distance(coordinate);

            // render the lines when debug info is true
            if (Game.DEBUG_INFO) {
                graphics.setColor(Colors.WHITE);
                SlickUtils.drawLine(graphics, coordinate, constructingEntityCoordinate);
            }

            // still placeable? good. Final (expensive) check -> any other units that may block this?
            if (isPlaceable) {
                EntitiesSet entitiesAtMapCoordinate = entityRepository.findAliveEntitiesOfTypeAtVector(absoluteMapCoordinate, EntityType.STRUCTURE, EntityType.UNIT);
                isPlaceable = entitiesAtMapCoordinate.isEmpty();
            }

            if (!isPlaceable) {
                placeableMapCoordinateCandidate.placeableState = PlaceableState.BLOCKED;
            }
        }

        float maxDistance = entityWhoConstructsIt.getEntityData().buildRange; // from center of the structure that built this entity (to place)

        for (PlaceableMapCoordinateCandidate pmcc : mapCoordinatesForEntityToPlace) {
            if (pmcc.placeableState != PlaceableState.PLACEABLE) continue;
            if (pmcc.distance > maxDistance) {
                pmcc.placeableState = PlaceableState.OUT_OF_REACH;
            }
        }

        boolean canPlaceEntity = canPlaceEntity();

        // Render stuff!
        for (PlaceableMapCoordinateCandidate placeableMapCoordinateCandidate : mapCoordinatesForEntityToPlace) {
            Coordinate absoluteMapCoordinate = placeableMapCoordinateCandidate.mapCoordinate.toCoordinate();
            Coordinate coordinate = battleField.translateAbsoluteMapCoordinateToViewportCoordinate(absoluteMapCoordinate);

            if (canPlaceEntity) {
                graphics.setColor(Colors.GREEN_ALPHA_128);
            } else {
                switch (placeableMapCoordinateCandidate.placeableState) {
                    case PLACEABLE:
                        graphics.setColor(Colors.GREEN_ALPHA_128);
                        break;
                    case BLOCKED:
                        graphics.setColor(Colors.RED_ALPHA_128);
                        break;
                    case OUT_OF_REACH:
                        graphics.setColor(Colors.YELLOW_ALPHA_128);
                        break;
                }
            }


            graphics.fillRect(coordinate.getXAsInt(), coordinate.getYAsInt(), TILE_SIZE, TILE_SIZE);
        }
    }

    @Override
    public void movedTo(Vector2D coordinates) {
        super.movedTo(coordinates);

        // TODO: move this to an update method thing? because it is possible that state changes without us
        // moving the mouse. So this won't last unfortunately. (or we should do some kind of message thing so this
        // class knows the state changed and should re-calculate or something like that)

        Coordinate absoluteMapCoordinateOfTopleftOfStructure = getAbsoluteCoordinateTopLeftOfStructureToPlace();

        // first determine all cells that will be occupied
        mapCoordinatesForEntityToPlace = new ArrayList<>();

        List<MapCoordinate> allMapCoordinates = this.entityDataToPlace.getAllCellsAsCoordinates(absoluteMapCoordinateOfTopleftOfStructure);

        mapCoordinatesForEntityToPlace =
                allMapCoordinates
                        .stream()
                        .map(mapCoordinate -> new PlaceableMapCoordinateCandidate(mapCoordinate, PlaceableState.PLACEABLE))
                        .collect(toList());

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

    public boolean canPlaceEntity() {
        // blocked == not good
        // passable == good
        // out of reach, but one passable == good
        boolean blocked = this.mapCoordinatesForEntityToPlace.stream().anyMatch(c -> c.placeableState == PlaceableState.BLOCKED);
        if (blocked) return false;

        return this.mapCoordinatesForEntityToPlace.stream().anyMatch(c -> c.placeableState == PlaceableState.PLACEABLE);
    }

    /**
     * This method finds a closest player Structure to the given coordinate.
     * @param coordinate
     * @return
     */
    private Entity findClosestStructureOfPlayer(Coordinate coordinate) {
        // TODO: Optimize
        // Perhaps have a 'satisfying distance' ? ie, whenever it finds one within this distance, stop searching?
        // Do not loop over everything?
        // Move to EntityRepository?
        PredicateBuilder predicateBuilder = Predicate.builder().forPlayer(player).ofType(EntityType.STRUCTURE);
        EntitiesSet allStructuresForPlayer = entityRepository.filter(predicateBuilder.build());

        float closestDistanceFoundSoFar = 320000; // Get from Map!? (width * height) -> get rid of magic number
        Entity closestEntityFoundSoFar = null;

        for (Entity entity : allStructuresForPlayer) {
            float distance = entity.getCenteredCoordinate().distance(coordinate);
            if (distance < closestDistanceFoundSoFar) {
                closestEntityFoundSoFar = entity;
                closestDistanceFoundSoFar = distance;
            }
        }
        return closestEntityFoundSoFar;
    }

    /**
     * A class that holds state per map coordinate needed for placement. Mostly used for rendering.
     */
    private class PlaceableMapCoordinateCandidate  {

        public MapCoordinate mapCoordinate;
        public PlaceableState placeableState;
        public float distance = 99999;

        public PlaceableMapCoordinateCandidate(MapCoordinate mapCoordinate, PlaceableState state) {
            this.mapCoordinate = mapCoordinate;
            this.placeableState = state;
        }
    }

    enum PlaceableState {
        PLACEABLE,
        BLOCKED,
        OUT_OF_REACH
    }
}
