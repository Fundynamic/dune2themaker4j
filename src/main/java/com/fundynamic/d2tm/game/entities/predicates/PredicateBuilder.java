package com.fundynamic.d2tm.game.entities.predicates;


import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.EntityType;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.entities.Predicate;
import com.fundynamic.d2tm.math.Coordinate;
import com.fundynamic.d2tm.math.Rectangle;
import com.fundynamic.d2tm.math.Vector2D;

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

    /**
     * Shorthand for creating new EntityIsWithinAreaOfAbsoluteCoordinates
     *
     * @param rectangle
     * @return
     * @see com.fundynamic.d2tm.game.entities.predicates.EntityIsWithinAreaOfAbsoluteCoordinates
     */
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

    public PredicateBuilder selectedDestroyersForPlayer(Player player) {
        return isSelected().
                forPlayer(player).
                isDestroyer();
    }

    public PredicateBuilder isDestroyed() {
        predicates.add(Predicate.isDestroyed());
        return this;
    }

    public PredicateBuilder ofType(EntityType entityType) {
        predicates.add(Predicate.ofType(entityType));
        return this;
    }

    public PredicateBuilder isDestroyer() {
        predicates.add(Predicate.isDestroyer());
        return this;
    }

    public PredicateBuilder isNotDestroyed() {
        predicates.add(Predicate.isNotDestroyed());
        return this;
    }

    public PredicateBuilder vectorWithin(Vector2D absoluteMapCoordinates) {
        predicates.add(new Vector2DIsWithinEntity(absoluteMapCoordinates));
        return this;
    }

    public PredicateBuilder ofTypes(EntityType[] types) {
        predicates.add(Predicate.ofTypes(types));
        return this;
    }

    public PredicateBuilder withinRange(Coordinate coordinate, float range) {
        predicates.add(new DistanceToEntity(coordinate, range));
        return this;
    }
}
