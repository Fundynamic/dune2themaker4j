package com.fundynamic.d2tm.game.entities;

import com.fundynamic.d2tm.math.Coordinate;
import com.fundynamic.d2tm.math.MapCoordinate;

import java.util.HashMap;
import java.util.Map;

/**
 * Global state class that remembers all unit intentions to move to a certain MapCoordinate.
 * This way we can prevent multiple units to move to the same cell at the same time. The first
 * unit that 'claims' an intent wins. Other units will then 'know' it is 'occupied' by some other unit.
 * We need to remember the Entity reference so that an intent will not block for the Entity that
 * made the claim.
 */
public class UnitMoveIntents {

    public static UnitMoveIntents instance = new UnitMoveIntents();

    private Map<MapCoordinate, Entity> intendedVectors = new HashMap<>();

    public void addIntent(Coordinate target, Entity who) {
        addIntent(target.toMapCoordinate(), who);
    }

    public void addIntent(MapCoordinate target, Entity who) {
        Entity entity = intendedVectors.get(target);
        if (entity != null && !entity.equals(who)) {
            throw new IllegalStateException("Entity " + who + " intended to claim vector " + target + " to move to, but it was already claimed by " + entity + ", therefor the claim by " + who + " was invalid. Some bug in code?");
        }
        intendedVectors.put(target, who);
    }

    public boolean isVectorClaimableBy(Coordinate target, Entity who) {
        return isVectorClaimableBy(target.toMapCoordinate(), who);
    }

    public boolean isVectorClaimableBy(MapCoordinate target, Entity who) {
        Entity entity = intendedVectors.get(target);
        return entity == null || // no entity on target, so thus claimable
               who.equals(entity); // entity must match 'who', always possible to claim target that is owned by itself
    }

    public void removeIntent(Coordinate target) {
        removeIntent(target.toMapCoordinate());
    }

    public void removeIntent(MapCoordinate target) {
        if (!intendedVectors.containsKey(target)) {
            throw new IllegalArgumentException("Cannot remove intent for vector " + target + ", because it is not known. These are known: " + intendedVectors);
        }
        intendedVectors.remove(target);
    }

}
