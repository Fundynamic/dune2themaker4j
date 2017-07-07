package com.fundynamic.d2tm.game.controls.battlefield;


import com.fundynamic.d2tm.game.controls.Mouse;
import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.EntityData;
import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.entitybuilders.PlacementBuildableEntity;
import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.game.rendering.gui.battlefield.BattleField;
import com.fundynamic.d2tm.math.Coordinate;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.Graphics;

public class LaunchSuperPowerMouse extends AbstractBattleFieldMouseBehavior {

    private EntityData entityDataToPlace;
    private Entity entityWhoConstructsIt;
    private EntityRepository entityRepository;

    public LaunchSuperPowerMouse(BattleField battleField, PlacementBuildableEntity placementBuildableEntity) {
        super(battleField);
        this.entityRepository = battleField.getEntityRepository();

        this.entityDataToPlace = placementBuildableEntity.getEntityData();
        this.entityWhoConstructsIt = placementBuildableEntity.getEntityWhoConstructsThis();

        mouse.setMouseImage(Mouse.MouseImages.ATTACK, 16, 16);
    }

    @Override
    public void leftClicked() {
        Coordinate target = getAbsoluteCoordinateTopLeftOfTarget();

        Coordinate startCoordinate = entityWhoConstructsIt.getCenteredCoordinate();
        startCoordinate = startCoordinate.min(entityDataToPlace.getHalfSize());

        entityRepository.spawnSuperPower(startCoordinate, entityDataToPlace, player, target);
    }

    public Coordinate getAbsoluteCoordinateTopLeftOfTarget() {
        // first get absolute viewport coordinates, we can calculate on the battlefield with that
        Coordinate viewportCoordinate = battleField.translateScreenToViewportCoordinate(mouseCoordinates);

        // now substract half of the structure to place, so we make the structure to place center beneath the mouse
        Vector2D halfSize = entityDataToPlace.getHalfSize();
        Coordinate topLeftOfStructure = viewportCoordinate.min(halfSize);

        Cell topLeftCellOfStructure = battleField.getCellByAbsoluteViewportCoordinate(topLeftOfStructure);
        return topLeftCellOfStructure.getCoordinates();
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
