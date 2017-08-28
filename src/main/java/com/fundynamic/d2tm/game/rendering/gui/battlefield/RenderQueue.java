package com.fundynamic.d2tm.game.rendering.gui.battlefield;


import com.fundynamic.d2tm.game.behaviors.EnrichableAbsoluteRenderable;
import com.fundynamic.d2tm.game.behaviors.Renderable;
import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.EntityType;
import com.fundynamic.d2tm.math.Coordinate;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.Graphics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *     This class is responsible for rendering {@link Entity} objects in a layered manner (z-index like).
 * </p>
 * <b>How it works</b>
 * <p>
 *     When new entities are added {@link #put(int, EnrichableAbsoluteRenderable, Coordinate)} to the queue a few things happen:
 * </p>
 * <ol>
 *     <li>the layer is determined by the {@link EntityType}</li>
 *     <li>its method {@code enrichRenderQueue} is called (example: UI related graphics, decals (smoke by damage of entity for instance)</li>
 *     <li>its drawing positions (on screen) are calculated and wrapped in a {@link ThingToRender} object</li>
 * </ol>
 * </p>
 * <p>
 *     Once all entities are added to the queue the {@code render} method is called. The x, y position to draw to
 *     are calculated by the RenderQueue therefor the Entity class should implement {@link EnrichableAbsoluteRenderable}
 * </p>
 * <b>
 *     Usages
 * </b>
 * <p>A render queue is used within a {@link BattleField} where it makes sense to render Entity objects</p>
 */
public class RenderQueue {

    public static final int ENTITY_GUI_LAYER = 4;
    private static int MAX_LAYERS = 10;

    private Vector2D cameraPosition;

    private HashMap<Integer, List<ThingToRender>> thingsToRender = new HashMap<>();

    private static Map<EntityType, Integer> entityTypeToLayerMap = new HashMap<>();

    static {
        entityTypeToLayerMap.put(EntityType.NONE, 0);
        entityTypeToLayerMap.put(EntityType.UNIT, 2);
        entityTypeToLayerMap.put(EntityType.STRUCTURE, 1);
        entityTypeToLayerMap.put(EntityType.PROJECTILE, 3);
        entityTypeToLayerMap.put(EntityType.SUPERPOWER, 3); // super power shares same layer as projectile (TODO: check)
        // layer 4 == gui (health bar and stuff)
        entityTypeToLayerMap.put(EntityType.PARTICLE, 5);
    }

    public RenderQueue(Vector2D cameraPosition) {
        this.cameraPosition = cameraPosition;

        initThingsToRender();
    }

    private void initThingsToRender() {
        for (int i = 0; i < MAX_LAYERS; i++) {
            thingsToRender.put(i, new ArrayList<>());
        }
    }

    public void updateCameraPosition(Vector2D cameraPosition) {
        this.cameraPosition = cameraPosition;
    }

    public void clear() {
        initThingsToRender();
    }

    /**
     * Add a list of entities to the queue.
     *
     * @param entities
     */
    public void put(List<Entity> entities) {
        for (Entity entity : entities) {
            int layer = entityTypeToLayerMap.get(entity.getEntityType());
            put(layer, entity, entity.getCoordinate());
        }
    }

    /**
     * A short hand method to add
     * @param renderQueueEnrichable
     */
    public void putEntityGui(EnrichableAbsoluteRenderable renderQueueEnrichable, Coordinate renderableCoordinate) {
        put(ENTITY_GUI_LAYER, renderQueueEnrichable, renderableCoordinate);
    }

    /**
     * Add a @{link EnrichableAbsoluteRenderable} thing to draw
     * @param layer
     * @param renderQueueEnrichable
     */
    public void put(int layer, EnrichableAbsoluteRenderable renderQueueEnrichable, Coordinate renderableCoordinate) {
        Coordinate screenCoordinate = translate(renderableCoordinate);
//        int drawX = renderableCoordinate.getXAsInt() - cameraPosition.getXAsInt();
//        int drawY = renderableCoordinate.getYAsInt() - cameraPosition.getYAsInt();
        int drawX = screenCoordinate.getXAsInt();
        int drawY = screenCoordinate.getYAsInt();

        ThingToRender thingToRender = new ThingToRender(drawX, drawY, renderQueueEnrichable);
        thingsToRender.get(layer).add(thingToRender);

        renderQueueEnrichable.enrichRenderQueue(this);
    }

    public Coordinate translate(Coordinate coordinate) {
        return coordinate.min(
                    Vector2D.create(cameraPosition.getXAsInt(), cameraPosition.getYAsInt())
            );
    }

    /**
     * Once all 'things to render' are computed, just draw them on screen layer for layer
     * @param graphics
     */
    public void render(Graphics graphics) {
        for (int layer = 0; layer < MAX_LAYERS; layer++) {
            List<ThingToRender> thingToRenderForLayer = thingsToRender.get(layer);
            if (thingToRenderForLayer == null || thingToRenderForLayer.isEmpty()) continue;

            for (ThingToRender thingToRender : thingToRenderForLayer) {
                thingToRender.render(graphics);
            }
        }
    }

    public List<ThingToRender> getThingsToRender(int layer) {
        return thingsToRender.get(layer);
    }

    /**
     * This is an object that wraps an {@link EnrichableAbsoluteRenderable} where the screen coordinates
     * are already calculated.
     */
    public class ThingToRender implements Renderable {
        public int screenX, screenY;                                // screen coordinates
        public EnrichableAbsoluteRenderable renderQueueEnrichable;  // what to render

        public ThingToRender(int screenX, int screenY, EnrichableAbsoluteRenderable renderQueueEnrichable) {
            this.screenX = screenX;
            this.screenY = screenY;
            this.renderQueueEnrichable = renderQueueEnrichable;
        }

        public void render(Graphics graphics) {
            try {
                renderQueueEnrichable.render(graphics, screenX, screenY);
            } catch (RuntimeException re) {
                System.err.println("Error rendering " + renderQueueEnrichable + " -> " + re);
                re.printStackTrace();
            }
        }
    }
}
