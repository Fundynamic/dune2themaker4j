package com.fundynamic.d2tm.game.entities;


import com.fundynamic.d2tm.game.behaviors.Updateable;
import com.fundynamic.d2tm.game.rendering.gui.battlefield.Recolorer;
import com.fundynamic.d2tm.math.MapCoordinate;
import com.fundynamic.d2tm.math.Vector2D;

import java.util.HashMap;
import java.util.Map;

public class Player implements Updateable {

    private final String name;
    private final Recolorer.FactionColor factionColor;

    private Map<MapCoordinate, Boolean> shrouded;
    private EntitiesSet entitiesSet; // short-hand to player owned entities

    private EntitiesSet powerProducingEntities; // an easy way to query all power producing entities
    private EntitiesSet powerConsumingEntities; // an easy way to query all power consuming entities

    private float credits;
    private int animatedCredits;
    private int totalPowerProduced = 0;
    private int totalPowerConsumption = 0;

    public Player(String name, Recolorer.FactionColor factionColor) {
        this(name, factionColor, 2000);
    }

    public Player(String name, Recolorer.FactionColor factionColor, int startingCredits) {
        this.name = name;
        this.factionColor = factionColor;
        this.shrouded = new HashMap<>();
        this.entitiesSet = new EntitiesSet();
        this.powerProducingEntities = entitiesSet;
        this.powerConsumingEntities = entitiesSet;
        this.credits = startingCredits;
        this.animatedCredits = startingCredits;
    }

    public Recolorer.FactionColor getFactionColor() {
        return factionColor;
    }

    public boolean isShrouded(Vector2D position) {
        Boolean value = shrouded.get(position);
        return value != null ? value : true;
    }

    /**
     * Removes shroud for {@link MapCoordinate}.
     *
     * @param position
     */
    public void revealShroudFor(MapCoordinate position) {
        shrouded.put(position, false);
    }

    public void addEntity(Entity entity) {
        entitiesSet.add(entity);
        if (entity.getEntityData().producesPower()) {
            powerProducingEntities.add(entity);
        }
        if (entity.getEntityData().consumesPower()) {
            powerConsumingEntities.add(entity);
        }
        calculatePowerProducedAndConsumed();
    }

    public boolean removeEntity(Entity entity) {
        if (powerProducingEntities.contains(entity)) powerProducingEntities.remove(entity);
        if (powerConsumingEntities.contains(entity)) powerConsumingEntities.remove(entity);
        calculatePowerProducedAndConsumed();
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

    /**
     * Make this {@link MapCoordinate} shrouded.
     *
     * @param mapCoordinate
     */
    public void shroud(MapCoordinate mapCoordinate) {
        shrouded.put(mapCoordinate, true);
    }

    public void addCredits(float credits) {
        this.credits += credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
        this.animatedCredits = credits;
    }

    public boolean canBuy(int cost) {
        return cost <= credits;
    }

    public boolean spend(int amount) {
        if (canBuy(amount)) {
            credits -= amount;
            return true;
        }
        return false;
    }

    public int getCredits() {
        return (int)credits;
    }

    public int getAnimatedCredits() {
        return animatedCredits;
    }

    @Override
    public void update(float deltaInSeconds) {
        animatedCredits = (int) credits;
    }

    public int getTotalPowerProduced() {
        return totalPowerProduced;
    }

    public int getTotalPowerConsumption() {
        return totalPowerConsumption;
    }

    public boolean isLowPower() {
        return getPowerBalance() < 0;
    }

    private void calculatePowerProducedAndConsumed() {
        totalPowerProduced = powerProducingEntities.stream().filter(e->!e.isDestroyed()).mapToInt(e -> e.getPowerProduction()).sum();
        totalPowerConsumption = powerConsumingEntities.stream().filter(e->!e.isDestroyed()).mapToInt(e -> e.getPowerConsumption()).sum();
    }

    public void entityTookDamage(Entity entity) {
        if (entity.getEntityData().producesPower() || entity.getEntityData().consumesPower()) {
            calculatePowerProducedAndConsumed();
        }
    }

    public int getPowerBalance() {
        return totalPowerProduced - totalPowerConsumption;
    }
}
