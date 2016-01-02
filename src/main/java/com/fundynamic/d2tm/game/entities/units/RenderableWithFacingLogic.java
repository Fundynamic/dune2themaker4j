package com.fundynamic.d2tm.game.entities.units;

import com.fundynamic.d2tm.game.behaviors.Renderable;
import com.fundynamic.d2tm.game.behaviors.Updateable;
import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.EntityData;
import com.fundynamic.d2tm.game.rendering.RenderQueue;
import com.fundynamic.d2tm.math.Random;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

/**
 * A spritesheet, which contains images that represent several 'facings' of a unit. Ie the body or
 * barrel of a unit.
 *
 * This object contains state, so it has current facing, a desired facing, a think method that enables 'rotating towards'
 * the desired facing and so on.
 *
 * It also can be rendered
 */
public class RenderableWithFacingLogic extends SpriteSheet implements Renderable, Updateable {

    private int possibleFacings = 0;

    private float facing;
    private int desiredFacing;

    private EntityData entityData;

    public RenderableWithFacingLogic(Image image, EntityData entityData) {
        super(image, entityData.getWidth(), entityData.getHeight());
        this.entityData = entityData;

        this.possibleFacings = getHorizontalCount();
        this.facing = Random.getRandomBetween(0, possibleFacings);
        this.desiredFacing = (int) facing;
    }

    @Override
    public void render(Graphics graphics, int x, int y) {
        Image sprite = getBodyFacing(facing);
        graphics.drawImage(sprite, x, y);
    }

    @Override
    public void enrichRenderQueue(RenderQueue renderQueue) {
        // barrel logic? because it goes over all other things?
    }

    public Image getBodyFacing(float facing) {
        return getSprite((int) facing, 0);
    }

    public boolean isFacingDesiredFacing() {
        return desiredFacing == (int) facing;
    }

    public boolean isFacing(int desiredFacing) {
        return (int) this.facing == desiredFacing;
    }

    @Override
    public void update(float deltaInSeconds) {
        facing = UnitFacings.turnTo(facing, desiredFacing, entityData.getRelativeTurnSpeed(deltaInSeconds));
    }

    public void faceTowards(int desiredFacing) {
        this.desiredFacing = desiredFacing;
    }
}
