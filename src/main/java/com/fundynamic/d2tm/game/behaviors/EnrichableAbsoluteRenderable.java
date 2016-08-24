package com.fundynamic.d2tm.game.behaviors;


/**
 * <p>
 *     Something that can render itself (and told where to render itself by the given x,y coordinates) AND
 *     is able to enrich a {@link com.fundynamic.d2tm.game.rendering.gui.battlefield.RenderQueue}.
 * </p>
 * <p>
 *     This is a composite interface of {@link AbsoluteRenderable} and {@link RenderQueueEnrichable}
 * </p>
 *
 *
 */
public interface EnrichableAbsoluteRenderable extends AbsoluteRenderable, RenderQueueEnrichable {
}
