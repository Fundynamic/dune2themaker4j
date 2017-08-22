package com.fundynamic.d2tm.game.behaviors;

import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.rendering.gui.battlefield.RenderQueue;
import com.fundynamic.d2tm.math.Coordinate;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class LineBetweenEntities implements EnrichableAbsoluteRenderable {

    private Entity to;
    private RenderQueue renderQueue;

    public LineBetweenEntities(Entity to, RenderQueue renderQueue) {
        this.to = to;
        this.renderQueue = renderQueue;
    }

    @Override
    public void render(Graphics graphics, int x, int y) {
        if (to == null) return;

        graphics.setLineWidth(2.0f);
        graphics.setColor(Color.white);

        Coordinate screenCoordinate = renderQueue.translate(to.getCenteredCoordinate());
        graphics.drawLine(x, y, screenCoordinate.getX(), screenCoordinate.getY());
    }

    @Override
    public void enrichRenderQueue(RenderQueue renderQueue) {
        // nothing to enrich
    }

}
