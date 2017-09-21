package com.fundynamic.d2tm.game.scenario;

import com.fundynamic.d2tm.game.behaviors.Renderable;
import com.fundynamic.d2tm.game.behaviors.Updateable;
import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.entities.Predicate;
import com.fundynamic.d2tm.game.map.Map;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;

/**
 * <h1>General</h1>
 * <p>
 * A scenario is being 'played' by the player. A scenario is a whole concept of a game to play. It contains objectives,
 * a Map, the players, and it can trigger several events and so forth.
 * </p>
 */
public class Scenario implements Renderable, Updateable {

    private Map map;
    private EntityRepository entityRepository;

    private Player cpu;
    private Player human;

    private Predicate updatableEntitiesPredicate;
    private Predicate destroyedEntitiesPredicate;

    @Override
    public void render(Graphics graphics) {
        Font font = graphics.getFont();

        // TODO: Proper end-game conditions and dealing with them
        if (cpu.aliveEntities() < 1) {
            font.drawString(10, 220, "Enemy player has been destroyed. You have won the game.", Color.green);
        }

        if (human.aliveEntities() < 1) {
            font.drawString(10, 220, "All your units and structures are destroyed. You have lost the game.", Color.red);
        }
    }

    @Override
    public void update(float deltaInSeconds) {
        Predicate<Entity> updatableEntities = updatableEntitiesPredicate();
        for (Entity entity : entityRepository.filter(updatableEntities)) {
            entity.update(deltaInSeconds);
        }

        human.update(deltaInSeconds);
        cpu.update(deltaInSeconds);

        entityRepository.removeEntities(destroyedEntitiesPredicate());
    }

    private Predicate<Entity> updatableEntitiesPredicate() {
        if (this.updatableEntitiesPredicate == null) {
            this.updatableEntitiesPredicate = Predicate.builder().isUpdateable().build();
        }
        return this.updatableEntitiesPredicate;
    }

    private Predicate<Entity> destroyedEntitiesPredicate() {
        if (this.destroyedEntitiesPredicate == null) {
            this.destroyedEntitiesPredicate = Predicate.builder().isDestroyed().build();
        }
        return this.destroyedEntitiesPredicate;
    }

    public Player getHuman() {
        return human;
    }

    public EntityRepository getEntityRepository() {
        return entityRepository;
    }

    public static ScenarioBuilder builder() {
        return new ScenarioBuilder();
    }

    public Map getMap() {
        return map;
    }

    public static class ScenarioBuilder {
        private Player cpu;
        private Player human;
        private EntityRepository entityRepository;
        private Map map;

        public ScenarioBuilder withMap(Map map) {
            this.map = map;
            return this;
        }

        public ScenarioBuilder withCpuPlayer(Player player) {
            if (this.cpu != null) throw new IllegalArgumentException("You already have assigned a cpu player. Unable to assign more for now.");
            this.cpu = player;
            return this;
        }

        public ScenarioBuilder withHuman(Player player) {
            if (this.human != null) throw new IllegalArgumentException("You already have assigned a human player. Unable to assign more for now.");
            this.human = player;
            return this;
        }

        public Scenario build() {
            Scenario scenario = new Scenario();
            scenario.map = map;
            scenario.entityRepository = entityRepository;
            scenario.human = human;
            scenario.cpu = cpu;
            return scenario;
        }

        public ScenarioBuilder withEntityRepository(EntityRepository entityRepository) {
            this.entityRepository = entityRepository;
            return this;
        }
    }

}
