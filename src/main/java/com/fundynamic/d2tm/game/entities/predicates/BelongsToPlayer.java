package com.fundynamic.d2tm.game.entities.predicates;


import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.entities.Predicate;

public class BelongsToPlayer extends Predicate<Entity> {

    private final Player playerItShouldBelongTo;

    public BelongsToPlayer(Player playerItShouldBelongTo) {
        this.playerItShouldBelongTo = playerItShouldBelongTo;
    }

    @Override
    public boolean test(Entity entity) {
        return entity.getPlayer().equals(playerItShouldBelongTo);
    }

    @Override
    public String toString() {
        return "BelongsToPlayer{" +
                "playerItShouldBelongTo=" + playerItShouldBelongTo +
                '}';
    }
}
