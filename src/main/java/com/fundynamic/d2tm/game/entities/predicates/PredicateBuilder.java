package com.fundynamic.d2tm.game.entities.predicates;


import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.EntityType;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.entities.Predicate;
import com.fundynamic.d2tm.math.Coordinate;
import com.fundynamic.d2tm.math.Rectangle;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
        predicates.add(BelongsToPlayer.instance((player)));
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

    public PredicateBuilder belongsToPlayer(Player playerItShouldBelongTo) {
        predicates.add(BelongsToPlayer.instance(playerItShouldBelongTo));
        return this;
    }

    public PredicateBuilder isEntityBuilder() {
        predicates.add(IsEntityBuilder.instance);
        return this;
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

    public PredicateBuilder isNotWithinAnotherEntity() {
        predicates.add(new NotPredicate(IsWithinAnotherEntity.instance));
        return this;
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

    /**
     * Alias for {@link #isAlive()}
     * @return
     */
    public PredicateBuilder isNotDestroyed() {
        predicates.add(Predicate.isNotDestroyed());
        return this;
    }

    public PredicateBuilder vectorWithin(Coordinate absoluteMapCoordinates) {
        predicates.add(new CoordinateIsWithinEntity(absoluteMapCoordinates));
        return this;
    }

    public PredicateBuilder vectorWithin(Coordinate ...absoluteMapCoordinates) {
        // Use or condition (either of the coordinates can be true)
        AnyPredicate coordinatesPredicate = new AnyPredicate();
        for (Coordinate coordinate: absoluteMapCoordinates) {
            coordinatesPredicate.addPredicate((new CoordinateIsWithinEntity(coordinate)));
        }
        predicates.add(coordinatesPredicate);
        return this;
    }

    public PredicateBuilder vectorWithin(List<Coordinate> absoluteMapCoordinates) {
        return vectorWithin(absoluteMapCoordinates.toArray(new Coordinate[absoluteMapCoordinates.size()]));
    }

    public PredicateBuilder vectorWithin(Set<Coordinate> absoluteMapCoordinates) {
        return vectorWithin(absoluteMapCoordinates.toArray(new Coordinate[absoluteMapCoordinates.size()]));
    }

    public PredicateBuilder isAlive() {
        predicates.add(Predicate.isAlive());
        return this;
    }

    public PredicateBuilder isDestructible() {
        predicates.add(Predicate.isDestructible());
        return this;
    }

    public PredicateBuilder isRefinery() {
        predicates.add(Predicate.isRefinery());
        return this;
    }

    public PredicateBuilder ofTypes(EntityType[] types) {
        predicates.add(Predicate.ofTypes(types));
        return this;
    }

    public PredicateBuilder withinRange(Coordinate coordinate, float range) {
        predicates.add(new DistanceFromCoordinateToEntity(coordinate, range));
        return this;
    }
}
