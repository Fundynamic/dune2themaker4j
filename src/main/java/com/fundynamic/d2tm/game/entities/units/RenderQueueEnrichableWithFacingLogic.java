package com.fundynamic.d2tm.game.entities.units;

import com.fundynamic.d2tm.game.behaviors.EnrichableAbsoluteRenderable;
import com.fundynamic.d2tm.game.behaviors.Updateable;
import com.fundynamic.d2tm.game.rendering.gui.battlefield.RenderQueue;
import com.fundynamic.d2tm.game.types.EntityData;
import com.fundynamic.d2tm.math.Random;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

import static com.fundynamic.d2tm.game.map.Cell.TILE_SIZE;

/**
 * A spritesheet, which contains images that represent several 'facings' of a unit. Ie the body or
 * barrel of a unit.
 *
 * This object contains state, so it has current facing, a desired facing, a think method that enables 'rotating towards'
 * the desired facing and so on.
 *
 * It also can be rendered
 */
public class RenderQueueEnrichableWithFacingLogic extends SpriteSheet implements EnrichableAbsoluteRenderable, Updateable {

    private int maxFrames = 0;
    private int maxFramesWithoutFirst = 0;

    private float currentFacing;
    private int desiredFacing;

    private float turnSpeed;
    private float animationSpeed;

    private float frame;

    private boolean animating;

    // used when image to draw is bigger or smaller than the TILE_SIZE, so the image always is drawn centered.
    private Vector2D drawCorrectionVec;

    public RenderQueueEnrichableWithFacingLogic(Image image, EntityData entityData, float turnSpeed) {
        super(image, entityData.getWidth(), entityData.getHeight());
        this.turnSpeed = turnSpeed;
        this.animationSpeed = entityData.animationSpeed;

        this.drawCorrectionVec = Vector2D.create(
                (TILE_SIZE - entityData.getWidth()) / 2,
                (TILE_SIZE - entityData.getHeight()) / 2
        );

        int possibleFacings = getHorizontalCount();
        this.maxFrames = getVerticalCount();
        this.maxFramesWithoutFirst = maxFrames - 1;
        this.animating = false;

        this.frame = 0; // Random.getRandomBetween(0, maxFrames)
        this.currentFacing = Random.getRandomBetween(0, possibleFacings);

        this.desiredFacing = (int) currentFacing;
    }

    @Override
    public void render(Graphics graphics, int x, int y) {
        Image sprite = getBodyFacing(currentFacing);
        graphics.drawImage(sprite, x + drawCorrectionVec.getXAsInt(), y + drawCorrectionVec.getYAsInt());
    }

    boolean isAnimating() {
        return animating;
    }

    void startAnimating() {
        animating = true;
    }

    void stopAndResetAnimating() {
        animating = false;
        frame = 0;
    }

    @Override
    public void enrichRenderQueue(RenderQueue renderQueue) {
        // nothing to do here
    }

    public Image getBodyFacing(float facing) {
        return getSprite((int) facing, (int) frame);
    }

    public boolean isFacingDesiredFacing() {
        return desiredFacing == (int) currentFacing;
    }

    @Override
    public void update(float deltaInSeconds) {
        if (!isFacingDesiredFacing()) {
            float relativeSpeed = EntityData.getRelativeSpeed(turnSpeed, deltaInSeconds);
            currentFacing = UnitFacings.turnTo(currentFacing, desiredFacing, relativeSpeed);
        }
        if (animating) {
            updateAnimation(deltaInSeconds);
        }
    }

    // set the desire where to face to (which requires turning to happen, in the update() method)
    public void desireToFaceTo(int desiredFacing) {
        this.desiredFacing = desiredFacing;
    }

    // set facing, desired == facing == given value
    public void faceTowards(int desiredFacing) {
        this.desiredFacing = desiredFacing;
        this.currentFacing = desiredFacing;
    }

    // usually, when we have a cannon, it is required to face the enemy before we can shoot
    public boolean isRequiredToFaceEnemyBeforeShooting() {
        return true;
    }

    private void updateAnimation(float deltaInSeconds) {
        frame += EntityData.getRelativeSpeed(animationSpeed, deltaInSeconds);
        if (frame >= maxFrames) {
            frame -= maxFramesWithoutFirst;
        }
    }

    @Override
    public String toString() {
        return "RenderQueueEnrichableWithFacingLogic{" +
                "maxFrames=" + maxFrames +
                ", maxFramesWithoutFirst=" + maxFramesWithoutFirst +
                ", currentFacing=" + currentFacing +
                ", desiredFacing=" + desiredFacing +
                ", turnSpeed=" + turnSpeed +
                ", animationSpeed=" + animationSpeed +
                ", frame=" + frame +
                ", animating=" + animating +
                ", drawCorrectionVec=" + drawCorrectionVec +
                '}';
    }
}
