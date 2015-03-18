package com.fundynamic.d2tm.game.event;


import com.fundynamic.d2tm.game.controls.Mouse;
import com.fundynamic.d2tm.game.controls.PlacingStructureMouse;
import com.fundynamic.d2tm.game.controls.PlacingUnitMouse;
import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.Player;
import org.newdawn.slick.Input;

public class DebugKeysListener extends AbstractKeyListener {

    private final Mouse mouse;
    private final Player player;
    private final EntityRepository entityRepsitory;

    public DebugKeysListener(Mouse mouse, Player player, EntityRepository entityRepsitory) {
        this.mouse = mouse;
        this.player = player;
        this.entityRepsitory = entityRepsitory;
    }


    @Override
    public void keyPressed(int key, char c) {
        if (key == Input.KEY_S) {
            mouse.setMouseBehavior(new PlacingStructureMouse(player, mouse, entityRepsitory));
        }
        if (key == Input.KEY_U) {
            mouse.setMouseBehavior(new PlacingUnitMouse(player, mouse, entityRepsitory));
        }
    }

    @Override
    public void keyReleased(int key, char c) {

    }

}
