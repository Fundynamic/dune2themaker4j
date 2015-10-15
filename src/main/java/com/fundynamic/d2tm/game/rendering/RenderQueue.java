package com.fundynamic.d2tm.game.rendering;


import com.fundynamic.d2tm.game.behaviors.Renderable;
import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.EntityType;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.Graphics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RenderQueue {

    private Vector2D viewportVec;

    private static int MAX_LAYERS = 10;

    private HashMap<Integer, List<ThingToRender>> thingsToRender = new HashMap<>();

    private static Map<EntityType, Integer> entityTypeToLayerMap = new HashMap<>();

    static {
        entityTypeToLayerMap.put(EntityType.UNIT, 1);
        entityTypeToLayerMap.put(EntityType.STRUCTURE, 1);
        entityTypeToLayerMap.put(EntityType.PROJECTILE, 3);
        // layer 4 == gui (health bar and stuff)
        entityTypeToLayerMap.put(EntityType.PARTICLE, 5);
    }

    public RenderQueue(Vector2D viewportVec) {
        this.viewportVec = viewportVec;
        for (int i = 0; i < MAX_LAYERS; i++) {
            thingsToRender.put(i, new ArrayList<ThingToRender>());
        }
    }

    public void put(List<Entity> entities) {
        for (Entity entity : entities) {
            int layer = entityTypeToLayerMap.get(entity.getEntityType());
            put(layer, entity, entity.getAbsoluteCoordinates());
            entity.enrichRenderQueue(this);
        }
    }

    public void putEntityGui(Renderable renderable, Vector2D absoluteCoordinates) {
        put(4, renderable, absoluteCoordinates);
    }

    public void put(int layer, Renderable renderable, Vector2D absoluteCoordinates) {
        int drawX = absoluteCoordinates.getXAsInt() - viewportVec.getXAsInt();
        int drawY = absoluteCoordinates.getYAsInt() - viewportVec.getYAsInt();
        ThingToRender thingToRender = new ThingToRender(drawX, drawY, renderable);
        thingsToRender.get(layer).add(thingToRender);
    }

    public void render(Graphics graphics) {
        for (int i = 0; i < MAX_LAYERS; i++) {
            List<ThingToRender> thingToRenderForLayer = thingsToRender.get(i);
            if (thingToRenderForLayer == null) continue;
            for (ThingToRender thingToRender : thingToRenderForLayer) {
                thingToRender.render(graphics);
            }
        }
    }

    class ThingToRender {
        int x, y;               // screen coordinates
        Renderable renderable;  // what to render

        public ThingToRender(int x, int y, Renderable renderable) {
            this.x = x;
            this.y = y;
            this.renderable = renderable;
        }

        public void render(Graphics graphics) {
            renderable.render(graphics, x, y);
        }
    }
}
