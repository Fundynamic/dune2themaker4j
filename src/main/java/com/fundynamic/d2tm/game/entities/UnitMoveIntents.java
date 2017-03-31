package com.fundynamic.d2tm.game.entities;

import com.fundynamic.d2tm.math.Vector2D;

import java.util.HashMap;
import java.util.Map;

public class UnitMoveIntents {

    public static UnitMoveIntents instance = new UnitMoveIntents();

    private Map<Vector2D, Entity> intendedVectors = new HashMap<>();

    public void addIntent(Vector2D target, Entity who) {
        Entity entity = intendedVectors.get(target);
        if (entity != null && !entity.equals(who)) {
            throw new IllegalStateException("Entity " + who + " intended to claim vector " + target + " to move to, but it was already claimed by " + entity + ", therefor the claim by " + who + " was invalid. Some bug in code?");
        }
        intendedVectors.put(target, who);
    }

    public boolean isVectorClaimableBy(Vector2D target, Entity who) {
        Entity entity = intendedVectors.get(target);
        return entity == null || // no entity on target, so thus claimable
               who.equals(entity); // entity must match 'who', always possible to claim target that is owned by itself
    }

    public void removeIntent(Vector2D target) {
        if (!intendedVectors.containsKey(target)) {
            throw new IllegalArgumentException("Cannot remove intent for vector " + target + ", because it is not known");
        }
        intendedVectors.remove(target);
    }

}
