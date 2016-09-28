package com.fundynamic.d2tm.game.entities.sidebar;

import com.fundynamic.d2tm.game.behaviors.FadingSelection;
import com.fundynamic.d2tm.game.entities.entitybuilders.AbstractBuildableEntity;
import com.fundynamic.d2tm.game.rendering.gui.GuiElement;
import com.fundynamic.d2tm.math.Vector2D;
import com.fundynamic.d2tm.utils.Colors;
import com.fundynamic.d2tm.utils.SlickUtils;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class RenderableBuildableEntity extends GuiElement {

    private FadingSelection fadingSelection;

    public static final int WIDTH = 64;
    public static final int HEIGHT = 48;

    private final AbstractBuildableEntity abstractBuildableEntity;

    public RenderableBuildableEntity(AbstractBuildableEntity abstractBuildableEntity, int x, int y) {
        super(x, y, WIDTH, HEIGHT);
        this.abstractBuildableEntity = abstractBuildableEntity;

        this.fadingSelection = new FadingSelection(getWidthAsInt(), getHeightAsInt(), 3) {

            // Just some toying around here.
            // TODO: Make house specific coloring? (here it is R value for Harkonnen fading)
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
        if (abstractBuildableEntity.hasBuildIcon()) {
            graphics.drawImage(abstractBuildableEntity.getBuildIcon(), xAsInt, yAsInt);
        } else {
            graphics.fillRect(xAsInt, yAsInt, WIDTH, HEIGHT);
        }

        BuildableState buildableState = abstractBuildableEntity.getBuildableState();
        if (buildableState == BuildableState.DISABLED) {
            graphics.setColor(Colors.BLACK_ALPHA_128);
            graphics.fillRect(xAsInt, yAsInt, getWidthAsInt(), getHeightAsInt());
        }

        if (buildableState == BuildableState.BUILDING) {
            graphics.setColor(Colors.WHITE_ALPHA_128); // ugly way of letting know we are building this
            graphics.fillRect(xAsInt, yAsInt, getWidthAsInt(), getHeightAsInt());
            SlickUtils.drawPercentage(graphics, Color.white, abstractBuildableEntity.getProgress(), xAsInt + 2, yAsInt + 16);
        }

        if (buildableState == BuildableState.BUILDING_FINISHED_AWAITS_PLACEMENT) {
            graphics.setColor(Colors.WHITE_ALPHA_128); // ugly way of letting know we are building this
            graphics.fillRect(xAsInt, yAsInt, getWidthAsInt(), getHeightAsInt());
            SlickUtils.drawShadowedText(graphics, Color.white, "PLACE", xAsInt + 2, yAsInt + 16);
        }

        if (buildableState == BuildableState.BUILDING_FINISHED_SPAWNABLE) {
            graphics.setColor(Colors.RED); // ugly way of letting know we are building this
            graphics.fillRect(xAsInt, yAsInt, getWidthAsInt(), getHeightAsInt());
            SlickUtils.drawShadowedText(graphics, Color.white, "SPAWN", xAsInt + 2, yAsInt + 16);
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

        // HACK HACK: fadingSelection renders only upon selected, and not on focus by default
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

    public boolean hasFocus() {
        return hasFocus;
    }

    public AbstractBuildableEntity getAbstractBuildableEntity() {
        return abstractBuildableEntity;
    }
}
