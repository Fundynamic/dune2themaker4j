package com.fundynamic.d2tm.game.entities.predicates;


import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.entities.Predicate;
import com.fundynamic.d2tm.game.entities.Rectangle;

import java.util.LinkedList;
import java.util.List;

/**
 * Builder to construct queries by using predicates.
 *
 * TODO: Make this smart enough so it won't create new predicates when it knows it has created this collection of
 * predicates before...
 */
public class PredicateBuilder {

    private List<Predicate<Entity>> predicates;

    public PredicateBuilder() {
        this.predicates = new LinkedList<>();
    }

    public PredicateBuilder forPlayer(Player player) {
        predicates.add(new BelongsToPlayer(player));
        return this;
    }

    public PredicateBuilder withinArea(Rectangle rectangle) {
        predicates.add(new EntityIsWithinAreaOfAbsoluteCoordinates(rectangle));
        return this;
    }

    public Predicate build() {
        return new AndPredicate(predicates);
    }

    public PredicateBuilder isSelectable() {
        predicates.add(Predicate.isSelectable());
        return this;
    }

    public PredicateBuilder isUpdateable() {
        predicates.add(Predicate.isUpdateable());
        return this;
    }

    public PredicateBuilder isSelected() {
        predicates.add(Predicate.isSelected());
        return this;
    }

    public PredicateBuilder isMovable() {
        predicates.add(IsMovable.instance);
        return this;
    }

    public PredicateBuilder selectableMovableForPlayer(Player player) {
        return isSelectable().
                forPlayer(player).
                isMovable();
    }

    public PredicateBuilder selectedMovableForPlayer(Player player) {
        return isSelected().
               forPlayer(player).
               isMovable();
    }

    public PredicateBuilder selectedAttackableForPlayer(Player player) {
        return isSelected().
                forPlayer(player).
                isAttackable();
    }

    public PredicateBuilder isDestroyed() {
        predicates.add(Predicate.isDestroyed());
        return this;
    }

    public PredicateBuilder isAttackable() {
        predicates.add(Predicate.isAttackable());
        return this;
    }

    public PredicateBuilder isNotDestroyed() {
        predicates.add(Predicate.isNotDestroyed());
        return this;
    }
}
