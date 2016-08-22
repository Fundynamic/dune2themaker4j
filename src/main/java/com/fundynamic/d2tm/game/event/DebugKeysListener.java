package com.fundynamic.d2tm.game.event;


import com.fundynamic.d2tm.game.controls.Mouse;
import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.rendering.gui.battlefield.BattleField;
import org.newdawn.slick.Input;

public class DebugKeysListener extends AbstractKeyListener {

    private final BattleField battleField;
    private final Player player;

    public DebugKeysListener(BattleField battleField, Player player) {
        this.battleField = battleField;
        this.player = player;
    }


    @Override
    public void keyPressed(int key, char c) {
        if (key == Input.KEY_R) {
            System.out.println("Revealing all shroud for player: " + player);
            Map map = battleField.getMap();
            map.revealAllShroudFor(player);
        }
    }

    @Override
    public void keyReleased(int key, char c) {
        // no logic yet
    }

}
