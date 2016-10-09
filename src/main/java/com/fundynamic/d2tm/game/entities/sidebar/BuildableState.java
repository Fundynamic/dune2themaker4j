package com.fundynamic.d2tm.game.entities.sidebar;


public enum BuildableState {
    DISABLED,           // unable to build
    SELECTABLE,         // selectable / buildable
    SELECTABLE_TOO_EXPENSIVE, // selectable / buildable but too expensive
    BUILDING,           // busy 'building'
    BUILDING_FINISHED_SPAWNABLE, // done building, can be spawned now
    BUILDING_FINISHED_AWAITS_PLACEMENT, // done building, can be placed now
}
