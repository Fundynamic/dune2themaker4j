package com.fundynamic.d2tm.game.entities;


/**
 * This is an interface method allowing the usage of anonymous classes instead of a lambda. Which was used
 * in the time Java 8 was not used yet. This can be removed in favor of lambda's.
 */
public interface EntityHandler {

    void handle(Entity entity);

}
