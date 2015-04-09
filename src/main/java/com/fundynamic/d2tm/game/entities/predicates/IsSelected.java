package com.fundynamic.d2tm.game.entities.predicates;


import com.fundynamic.d2tm.game.behaviors.Selectable;
import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.Predicate;

public class IsSelected extends Predicate<Entity> {

    public static IsSelected instance = new IsSelected();

    @Override
    public boolean test(Entity entity) {
        if (entity.isSelectable()) {
            return ((Selectable) entity).isSelected();
        }
        return false;
    }

    @Override
    public String toString() {
        return "IsSelected";
    }
}
