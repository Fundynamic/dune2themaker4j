package com.fundynamic.d2tm.game.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Global state class that remembers all entities that have an intent to enter another structure
 */
public class EnterStructureIntent {

    public static EnterStructureIntent instance = new EnterStructureIntent();

    // KEY = Entity that is being 'claimed' (to enter). Ie, a Harvester could 'claim' a Refinery. Key == Refinery, or Repair facility, etc.
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

    public boolean hasIntentToEnterAt(Entity what, Entity who) {
        Entity entity = intentions.get(what);
        return who.equals(entity);
    }

    public boolean containsIntentToEnterAt(Entity what) {
        return intentions.containsKey(what);
    }

    public boolean canEnterAt(Entity what, Entity who) {
        Entity entity = intentions.get(what);
        return entity == null || // no entity on target, so thus claimable
                who.equals(entity); // entity must match 'who', always possible to claim target that is owned by itself
    }

    public void removeEnterIntent(Entity what) {
        if (!intentions.containsKey(what)) {
            throw new IllegalArgumentException("Cannot remove intent for " + what + ", because it is not known. These are known: " + intentions);
        }
        intentions.remove(what);
    }

    public void removeDeliveryIntentFriendly(Entity what) {
        intentions.remove(what);
    }

    public void removeAllIntentsBy(Entity entityWhoPossiblyClaimed) {
        List<Entity> entitiesToRemove = new ArrayList<>();

        // find all entities that where claimed by this entity
        for (Entity what : intentions.keySet()) {
            Entity who = intentions.get(what);
            if (who.equals(entityWhoPossiblyClaimed)) {
                entitiesToRemove.add(what);
            }
        }

        // remove all from intentions
        entitiesToRemove.forEach(this::removeDeliveryIntentFriendly);
    }

    /**
     * Returns entity which placed the intention
     * @param what
     * @return
     */
    public Entity getEnterIntentFrom(Entity what) {
        return intentions.get(what);
    }
}
