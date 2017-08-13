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

    private float credits;
    private int animatedCredits;

    private float creditsTimer = 0F;

    public Player(String name, Recolorer.FactionColor factionColor) {
        this(name, factionColor, 2000);
    }

    public Player(String name, Recolorer.FactionColor factionColor, int startingCredits) {
        this.name = name;
        this.factionColor = factionColor;
        this.shrouded = new HashMap<>();
        this.entitiesSet = new EntitiesSet();
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
//        float desiredCredits = Math.round(this.credits);
        animatedCredits = (int) credits;
//        if (animatedCredits != desiredCredits) {
//            creditsTimer += deltaInSeconds;
//            while (creditsTimer > 0.0F && animatedCredits != desiredCredits) {
//                creditsTimer -= 0.01;
//                if (animatedCredits < desiredCredits) {
//                    animatedCredits += 1;
//                } else {
//                    animatedCredits -= 1;
//                }
//            }
//        }
    }
}
