package com.fundynamic.d2tm.game.behaviors;

import com.fundynamic.d2tm.game.entities.EntityData;
import com.fundynamic.d2tm.game.entities.sidebar.BuildableEntity;

import java.util.List;

/**
 * The implementing class is able to 'build' another entity. This basically means it holds a reference to the
 * type of entity it is 'building'. It also holds timers to make a construction duration possible.
 */
public interface EntityBuilder {

    // build
    // progress
    // list of buildable things

    List<BuildableEntity> getBuildList();

}
