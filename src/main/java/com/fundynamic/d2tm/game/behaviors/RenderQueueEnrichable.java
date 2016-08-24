package com.fundynamic.d2tm.game.behaviors;

import com.fundynamic.d2tm.game.rendering.gui.battlefield.RenderQueue;

/**
 * This interface gives the ability to enrich a rendering queue, used in conjuction with
 * a renderer to accumulate extra renderable information.
 */
public interface RenderQueueEnrichable {

    void enrichRenderQueue(RenderQueue renderQueue);

}
