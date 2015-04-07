package com.fundynamic.d2tm.game.entities.predicates;


import com.fundynamic.d2tm.game.behaviors.Selectable;
import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.entities.Predicate;
import com.fundynamic.d2tm.game.entities.Rectangle;

import java.util.LinkedList;
import java.util.List;

public class PredicateBuilder {

    private List<Predicate<Entity>> predicates;

    public PredicateBuilder() {
        this.predicates = new LinkedList<>();
    }

    public PredicateBuilder forPlayer(Player player) {
        predicates.add(new EntityBelongsToPlayer(player));
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
        predicates.add(new Predicate<Entity>() {

            @Override
            public boolean test(Entity entity) {
                return entity.isSelectable();
            }

        });
        return this;
    }

    public PredicateBuilder isUpdateable() {
        predicates.add(new Predicate<Entity>() {

            @Override
            public boolean test(Entity entity) {
                return entity.isUpdateable();
            }

        });
        return this;
    }

    public PredicateBuilder isSelected() {
        predicates.add(new Predicate<Entity>() {

            @Override
            public boolean test(Entity entity) {
                if (entity.isSelectable()) {
                    return ((Selectable) entity).isSelected();
                }
                return false;
            }

        });
        return this;
    }
}
