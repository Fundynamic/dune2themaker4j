package com.fundynamic.d2tm.game.entities;

import com.fundynamic.d2tm.game.entities.units.Unit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Global state class that remembers all harvesters that intent to deliver spice at a Refinery
 */
public class HarvesterDeliveryIntents {

    public static HarvesterDeliveryIntents instance = new HarvesterDeliveryIntents();

    // KEY = Entity that is being 'claimed'. Ie, a Harvester could 'claim' a Refinery. Key == Refinery
    // VALUE = Entity who made the claim. Ie, Harvester who claims Refinery, Value == Harvester
    private Map<Entity, Entity> intentions = new HashMap<>();

    public void addDeliveryIntentTo(Entity what, Entity who) {
        Entity whoClaimedWhatAlready = intentions.get(what);
        if (whoClaimedWhatAlready != null && !whoClaimedWhatAlready.equals(who)) {
            throw new IllegalStateException("Entity " + who + " intended to place delivery intent for " + what + " but it was already claimed by " + whoClaimedWhatAlready + ", therefor the claim was invalid.");
        }
        System.out.println("addDeliveryIntent at " + what + " by " + who);
        intentions.put(what, who);
    }

    public boolean hasDeliveryIntentAt(Entity what, Entity who) {
        Entity entity = intentions.get(what);
        return who.equals(entity);
    }

    public boolean hasDeliveryIntentAt(Entity what) {
        return intentions.containsKey(what);
    }

    public boolean canDeliverAt(Entity what, Entity who) {
        Entity entity = intentions.get(what);
        return entity == null || // no entity on target, so thus claimable
                who.equals(entity); // entity must match 'who', always possible to claim target that is owned by itself
    }

    public void removeDeliveryIntent(Entity what) {
        if (!intentions.containsKey(what)) {
            throw new IllegalArgumentException("Cannot remove intent for " + what + ", because it is not known. These are known: " + intentions);
        }
        System.out.println("removeDeliveryIntent at " + what);
        intentions.remove(what);
    }

    public void removeAllIntentsBy(Entity entityWhoPossiblyClaimed) {
        List<Entity> entitiesToRemove = new ArrayList<>();

        // find all entities that where claimed by this entity
        for (Entity what : intentions.keySet()) {
            Entity who = intentions.get(what);
            if (who.equals(entityWhoPossiblyClaimed)) {
                entitiesToRemove.add(entityWhoPossiblyClaimed);
            }
        }

        // remove all from intentions
        entitiesToRemove.forEach(this::removeDeliveryIntent);
    }

    public Entity getDeliveryIntentTo(Entity whoPlacedTheIntent) {
        for (Entity what : intentions.keySet()) {
            Entity who = intentions.get(what);
            if (who.equals(whoPlacedTheIntent)) {
                return what;
            }
        }
        throw new IllegalArgumentException("There is no intent claimed by " + whoPlacedTheIntent);
    }
}
