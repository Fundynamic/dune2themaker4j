package com.fundynamic.d2tm.game.entities;

/**
 * <h1>General purpose</h1>
 * <p>
 *     The implementing class reacts to events happening to one or more entities. This is meant to be on a gui/abstract
 *     level. For instance: "one or more entities have been selected".
 * </p>
 * <h2>Selecting/deselectiny entities</h2>
 * <p>
 *      The {@link #entitiesDeselected(EntitiesSet)} and {@link #entitiesSelected(EntitiesSet)} methods are called
 *      by {@link com.fundynamic.d2tm.game.controls.battlefield.AbstractBattleFieldMouseBehavior} implementors for instance
 *      so that a {@link com.fundynamic.d2tm.game.rendering.gui.battlefield.BattleField} is aware of what happens and
 *      can interact with the gui accordingly.
 * </p>
 */
public interface EntityEventsListener {

    /**
     * The given {@link EntitiesSet} set of entities has been selected.
     * @param entities
     */
    void entitiesSelected(EntitiesSet entities);

    /**
     * The given {@link EntitiesSet} set of entities has been deselected.
     * @param entities
     */
    void entitiesDeselected(EntitiesSet entities);
}
