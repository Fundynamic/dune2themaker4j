package com.fundynamic.d2tm.game.controls.battlefield;


import com.fundynamic.d2tm.game.controls.Mouse;
import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.entitybuilders.PlacementBuildableEntity;
import com.fundynamic.d2tm.game.entities.superpowers.SuperPower;
import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.game.rendering.gui.battlefield.BattleField;
import com.fundynamic.d2tm.game.types.EntityData;
import com.fundynamic.d2tm.math.Coordinate;
import org.newdawn.slick.Graphics;

/**
 *
 * <h2>When activated?</h2>
 * <p>
 * Whenever a super power is selected to deploy on the battlefield.
 * </p>
 * <h2>What does this do?</h2>
 * <h3>Left-click</h3>
 * <p>
 * Deploys super power on battlefield and initiates it.
 * </p>
 * <h3>Right-click</h3>
 * <p>
 * Goes back to NormalMouse mode.
 * </p>
 *
 */
public class LaunchSuperPowerMouse extends AbstractBattleFieldMouseBehavior {

    private EntityData superPowerEntityData;
    private Entity entityWhoConstructsIt;
    private EntityRepository entityRepository;

    public LaunchSuperPowerMouse(BattleField battleField, PlacementBuildableEntity placementBuildableEntity) {
        super(battleField);
        this.entityRepository = battleField.getEntityRepository();

        this.superPowerEntityData = placementBuildableEntity.getEntityData();
        this.entityWhoConstructsIt = placementBuildableEntity.getEntityWhoConstructsThis();

        mouse.setMouseImageAttack();
    }

    @Override
    public void leftClicked() {
        Coordinate target = battleField.getAbsoluteCoordinateTopLeftOfTarget(superPowerEntityData, mouseCoordinates);

        Coordinate startCoordinate = Coordinate.zero();
        if (entityWhoConstructsIt != null) {
            startCoordinate = entityWhoConstructsIt.getCenteredCoordinate();
        }

        startCoordinate = startCoordinate.min(superPowerEntityData.getHalfSize());

        SuperPower superPower = entityRepository.spawnSuperPower(startCoordinate, superPowerEntityData, player, target);

        // tell battlefield of the created entity
        battleField.entityPlacedOnMap(superPower);
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
    public String toString() {
        return "PlacingStructureMouse{" +
                "superPowerEntityData=" + superPowerEntityData +
                '}';
    }

}
