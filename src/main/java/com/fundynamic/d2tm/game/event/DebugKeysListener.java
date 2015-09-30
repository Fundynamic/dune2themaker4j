package com.fundynamic.d2tm.game.event;


import com.fundynamic.d2tm.game.controls.Mouse;
import com.fundynamic.d2tm.game.controls.PlacingProjectileMouse;
import com.fundynamic.d2tm.game.controls.PlacingStructureMouse;
import com.fundynamic.d2tm.game.controls.PlacingUnitMouse;
import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.rendering.Viewport;
import org.newdawn.slick.Input;

public class DebugKeysListener extends AbstractKeyListener {

    private final Mouse mouse;
    private final EntityRepository entityRepsitory;
    private final Viewport viewport;
    private final Player player;

    public DebugKeysListener(Mouse mouse, Viewport viewport, EntityRepository entityRepsitory, Player player) {
        this.mouse = mouse;
        this.viewport = viewport;
        this.entityRepsitory = entityRepsitory;
        this.player = player;
    }


    @Override
    public void keyPressed(int key, char c) {
        if (key == Input.KEY_S) {
            System.out.println("Debug key: Place structure");
            mouse.setMouseBehavior(new PlacingStructureMouse(mouse, entityRepsitory));
        }
        if (key == Input.KEY_P) {
            System.out.println("Debug key: Place projectile");
            mouse.setMouseBehavior(new PlacingProjectileMouse(mouse, entityRepsitory));
        }
        if (key == Input.KEY_U) {
            System.out.println("Debug key: Place unit");
            mouse.setMouseBehavior(new PlacingUnitMouse(mouse, entityRepsitory));
        }
        if (key == Input.KEY_D) {
            System.out.println("Debug key: Toggle view debug info.");
            viewport.toggleDrawDebugMode();
        }
        if (key == Input.KEY_R) {
            System.out.println("Revealing all shroud for player: " + player);
            Map map = viewport.getMap();
            map.revealAllShroudFor(player);
        }
    }

    @Override
    public void keyReleased(int key, char c) {
        // no logic yet
    }

}
