package com.fundynamic.d2tm.game.entities;


import com.fundynamic.d2tm.game.rendering.gui.battlefield.Recolorer;
import com.fundynamic.d2tm.math.MapCoordinate;
import com.fundynamic.d2tm.math.Vector2D;

import java.util.HashMap;
import java.util.Map;

public class Player {

    private final String name;
    private final Recolorer.FactionColor factionColor;

    private Map<MapCoordinate, Boolean> shrouded;
    private EntitiesSet entitiesSet; // short-hand to player owned entities

    public Player(String name, Recolorer.FactionColor factionColor) {
        this.name = name;
        this.factionColor = factionColor;
        this.shrouded = new HashMap<>();
        this.entitiesSet = new EntitiesSet();
    }

    public Recolorer.FactionColor getFactionColor() {
        return factionColor;
    }

    public boolean isShrouded(Vector2D position) {
        Boolean value = shrouded.get(position);
        return value != null ? value : true;
    }

    public void revealShroudFor(MapCoordinate position) {
        shrouded.put(position, false);
    }

    public void addEntity(Entity entity) {
        entitiesSet.add(entity);
    }

    public boolean removeEntity(Entity entity) {
        return entitiesSet.remove(entity);
    }

    public int aliveEntities() {
        return entitiesSet.filter(Predicate.isNotDestroyed()).size();
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", factionColor=" + factionColor +
                '}';
    }

    public boolean isCPU() {
        return "CPU".equalsIgnoreCase(name);
    }
}
