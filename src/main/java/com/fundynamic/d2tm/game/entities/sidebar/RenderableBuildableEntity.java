package com.fundynamic.d2tm.game.entities.sidebar;

import com.fundynamic.d2tm.game.behaviors.FadingSelection;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.entities.entitybuilders.AbstractBuildableEntity;
import com.fundynamic.d2tm.game.rendering.gui.GuiElement;
import com.fundynamic.d2tm.math.Vector2D;
import com.fundynamic.d2tm.utils.Colors;
import com.fundynamic.d2tm.utils.SlickUtils;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class RenderableBuildableEntity extends GuiElement {

    public static final int WIDTH = 64;
    public static final int HEIGHT = 48;

    private FadingSelection fadingSelection;

    private final AbstractBuildableEntity abstractBuildableEntity;

    private final Player player;

    public RenderableBuildableEntity(AbstractBuildableEntity abstractBuildableEntity, Player player, int x, int y) {
        super(x, y, WIDTH, HEIGHT);
        this.player = player;
        this.abstractBuildableEntity = abstractBuildableEntity;

        Color factionColor = this.player.getFactionColor();
        this.fadingSelection = new FadingSelection(getWidth() - 1, getHeight(), 3) {

            /**
             * Make it fade based on house color
             * @param selectedIntensity
             * @return
             */
            @Override
            public Color getFadingColor(float selectedIntensity) {
                return new Color(
                        (factionColor.getRed() * selectedIntensity) / factionColor.getRed(),
                        factionColor.getGreen() * selectedIntensity / factionColor.getGreen(),
                        factionColor.getBlue() * selectedIntensity / factionColor.getBlue()
                );
            }
        };
    }

    @Override
    public void render(Graphics graphics) {
        int xAsInt = getTopLeftX();
        int yAsInt = getTopLeftY();
        if (abstractBuildableEntity.hasBuildIcon()) {
            graphics.drawImage(abstractBuildableEntity.getBuildIcon(), xAsInt, yAsInt);
        } else {
            graphics.fillRect(xAsInt, yAsInt, WIDTH, HEIGHT);
        }

        int priceYPosition = 3;

        BuildableState buildableState = abstractBuildableEntity.getBuildableState();
        if (buildableState == BuildableState.DISABLED) {
            graphics.setColor(Colors.BLACK_ALPHA_128);
            graphics.fillRect(xAsInt, yAsInt, getWidth(), getHeight());
        }

        if (buildableState == BuildableState.SELECTABLE_TOO_EXPENSIVE) {
            SlickUtils.drawShadowedText(graphics, Color.white, "$ " + abstractBuildableEntity.getBuildCost(), xAsInt + 2, yAsInt + priceYPosition);
            graphics.setColor(Colors.RED_ALPHA_128); // ugly way of letting know it is too expensive
            graphics.fillRect(xAsInt, yAsInt, getWidth(), getHeight());
        }

        if (buildableState == BuildableState.BUILDING) {
            graphics.setColor(Colors.WHITE_ALPHA_128); // ugly way of letting know we are building this
            graphics.fillRect(xAsInt, yAsInt, getWidth(), getHeight());
            SlickUtils.drawPercentage(graphics, Color.white, abstractBuildableEntity.getProgress(), xAsInt + 2, yAsInt + priceYPosition);
        }

        if (buildableState == BuildableState.BUILDING_FINISHED_AWAITS_PLACEMENT) {
            graphics.setColor(Colors.WHITE_ALPHA_128); // ugly way of letting know we are building this
            graphics.fillRect(xAsInt, yAsInt, getWidth(), getHeight());
            SlickUtils.drawShadowedText(graphics, Color.white, "PLACE", xAsInt + 2, yAsInt + priceYPosition);
        }

        if (buildableState == BuildableState.BUILDING_FINISHED_SPAWNABLE) {
            graphics.setColor(Colors.RED); // ugly way of letting know we are building this
            graphics.fillRect(xAsInt, yAsInt, getWidth(), getHeight());
            SlickUtils.drawShadowedText(graphics, Color.white, "SPAWN", xAsInt + 2, yAsInt + priceYPosition);
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
