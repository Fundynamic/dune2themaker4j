package com.fundynamic.d2tm.game.event;


import com.fundynamic.d2tm.game.behaviors.Destructible;
import com.fundynamic.d2tm.game.entities.EntitiesSet;
import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.rendering.gui.battlefield.BattleField;
import org.newdawn.slick.Input;

public class DebugKeysListener extends AbstractKeyListener {

    private final BattleField battleField;
    private final Player player;
    private final EntityRepository entityRepository;

    public DebugKeysListener(BattleField battleField, Player player, EntityRepository entityRepository) {
        this.battleField = battleField;
        this.player = player;
        this.entityRepository = entityRepository;
    }


    @Override
    public void keyPressed(int key, char c) {
        if (key == Input.KEY_R) {
            System.out.println("Revealing all shroud for player: " + player);
            Map map = battleField.getMap();
            map.revealAllShroudFor(player);
        }

        if (key == Input.KEY_D) {
            EntitiesSet entities = entityRepository.findDestructibleSelectedEntitiesForPlayer(player);
            entities.forEach( e -> ((Destructible)e).takeDamage(((Destructible) e).getHitPoints(), null));
        }
    }

    @Override
    public void keyReleased(int key, char c) {
        // no logic yet
    }

}
