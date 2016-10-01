package com.fundynamic.d2tm.game.entities.predicates;


import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.entities.Predicate;

import java.util.HashMap;
import java.util.Map;

public class BelongsToPlayer extends Predicate<Entity> {

    private static Map<Player, BelongsToPlayer> instances = new HashMap<>();

    private final Player playerItShouldBelongTo;

    private BelongsToPlayer(Player playerItShouldBelongTo) {
        this.playerItShouldBelongTo = playerItShouldBelongTo;
    }

    @Override
    public boolean test(Entity entity) {
        return entity.belongsToPlayer(playerItShouldBelongTo);
    }

    @Override
    public String toString() {
        return "BelongsToPlayer{" +
                "playerItShouldBelongTo=" + playerItShouldBelongTo +
                '}';
    }

    public static BelongsToPlayer Instance(Player playerItShouldBelongTo) {
        if (!instances.containsKey(playerItShouldBelongTo)) {
            instances.put(playerItShouldBelongTo, new BelongsToPlayer(playerItShouldBelongTo));
        }
        return instances.get(playerItShouldBelongTo);
    }

}
