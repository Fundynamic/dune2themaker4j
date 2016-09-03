package com.fundynamic.d2tm.game.entities.sidebar;

import com.fundynamic.d2tm.game.behaviors.FadingSelection;
import com.fundynamic.d2tm.game.rendering.gui.GuiElement;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class RenderableBuildableEntity extends GuiElement {

    private FadingSelection fadingSelection;

    public boolean hasFocus;
    public static final int WIDTH = 64;
    public static final int HEIGHT = 48;
    private final BuildableEntity buildableEntity;

    public RenderableBuildableEntity(BuildableEntity buildableEntity, int x, int y) {
        super(x, y, WIDTH, HEIGHT);
        this.buildableEntity = buildableEntity;
        this.fadingSelection = new FadingSelection(getWidthAsInt(), getHeightAsInt(), 3) {
            @Override
            public Color getFadingColor(float selectedIntensity) {
                return new Color(selectedIntensity, 0, 0);
            }
        };
    }

    @Override
    public void render(Graphics graphics) {
        int xAsInt = getTopLeftXAsInt();
        int yAsInt = getTopLeftYAsInt();
        if (buildableEntity.hasBuildIcon()) {
            graphics.drawImage(buildableEntity.getBuildIcon(), xAsInt, yAsInt);
        } else {
            graphics.fillRect(xAsInt, yAsInt, WIDTH, HEIGHT);
        }

        if (hasFocus) {
            fadingSelection.render(graphics, xAsInt, yAsInt);
        }
    }

    @Override
    public void leftClicked() {

    }

    @Override
    public void rightClicked() {

    }

    @Override
    public void draggedToCoordinates(Vector2D coordinates) {

    }

    @Override
    public void movedTo(Vector2D coordinates) {
        hasFocus = isVectorWithin(coordinates);
        // HACK HACK: because only then it will show
        if (hasFocus) {
            fadingSelection.select();
        } else {
            fadingSelection.deselect();
        }
    }

    @Override
    public void leftButtonReleased() {

    }

    @Override
    public void update(float deltaInSeconds) {
        if (hasFocus) {
            fadingSelection.update(deltaInSeconds);
        }
        // building stuff?
    }
}
