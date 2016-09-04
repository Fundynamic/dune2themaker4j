package com.fundynamic.d2tm.game.entities.sidebar;

import com.fundynamic.d2tm.game.behaviors.EntityBuilder;
import com.fundynamic.d2tm.game.behaviors.FadingSelection;
import com.fundynamic.d2tm.game.rendering.gui.GuiElement;
import com.fundynamic.d2tm.math.Vector2D;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class RenderableBuildableEntity extends GuiElement {

    private FadingSelection fadingSelection;

    public static final int WIDTH = 64;
    public static final int HEIGHT = 48;

    private final BuildableEntity buildableEntity;

    public RenderableBuildableEntity(BuildableEntity buildableEntity, int x, int y) {
        super(x, y, WIDTH, HEIGHT);
        this.buildableEntity = buildableEntity;

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
        if (buildableEntity.hasBuildIcon()) {
            graphics.drawImage(buildableEntity.getBuildIcon(), xAsInt, yAsInt);
        } else {
            graphics.fillRect(xAsInt, yAsInt, WIDTH, HEIGHT);
        }

        BuildableState buildableState = buildableEntity.getBuildableState();
        if (buildableState == BuildableState.DISABLED) {
            graphics.setColor(new Color(0,0,0, 128));
            graphics.fillRect(xAsInt, yAsInt, getWidthAsInt(), getHeightAsInt());
        }

        if (buildableState == BuildableState.BUILDING) {
            graphics.setColor(new Color(255,255,255, 128)); // ugly way of letting know we are building this
            graphics.fillRect(xAsInt, yAsInt, getWidthAsInt(), getHeightAsInt());
            graphics.setColor(Color.black);
            graphics.drawString("PLACE", xAsInt + 3, yAsInt + 17);
            graphics.setColor(Color.white);
            graphics.drawString("PLACE", xAsInt + 2, yAsInt + 16);
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

    public boolean hasFocus() {
        return hasFocus;
    }

    public BuildableEntity getBuildableEntity() {
        return buildableEntity;
    }
}
