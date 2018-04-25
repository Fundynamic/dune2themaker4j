package com.fundynamic.d2tm.game.rendering.gui.sidebar;


import com.fundynamic.d2tm.game.behaviors.EntityBuilder;
import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.sidebar.RenderableBuildableEntity;
import com.fundynamic.d2tm.math.Vector2D;
import com.fundynamic.d2tm.utils.Colors;
import com.fundynamic.d2tm.utils.SlickUtils;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/**
 * <p>
 *     The sidebar offers interactions with entities. For example, structures
 *     can interact with the sidebar to offer things to build.
 * </p>
 * <p>
 *
 * </p>
 */
public class Sidebar extends BattlefieldInteractableGuiElement {

    private BuildList buildList;
    private BuildListButton buttonUp;
    private BuildListButton buttonDown;

    private Entity selectedEntity; // the entityBuilderSelected and to show for this sidebar (the structure)

    public Sidebar(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void render(Graphics graphics) {
        graphics.setColor(Colors.SIDEBAR_BACKGROUND);
        graphics.fillRect(getTopLeftX(), getTopLeftY(), getWidth(), getHeight());

        graphics.setColor(Colors.SIDEBAR_BRIGHT_LEFT_UP_SIDE);
        graphics.drawLine(getTopLeftX(), getTopLeftY(), getTopLeftX(), getBottomRightY() - 1); // left side (up / down)
        graphics.drawLine(getTopLeftX(), getTopLeftY(), getTopLeftX() + getWidth(), getTopLeftY()); // top side

        graphics.setColor(Colors.SIDEBAR_DARK_RIGHT_DOWN_SIDE);
        graphics.drawLine(getTopLeftX() + getWidth(), getTopLeftY() + 1, getTopLeftX() + getWidth(), getBottomRightY() - 1); // right side (up / down)
        graphics.drawLine(getTopLeftX() + 1, getBottomRightY() - 1, getTopLeftX() + getWidth() - 1, getBottomRightY() - 1); // bottom side

        if (selectedEntity != null) {
            SlickUtils.drawShadowedText(graphics, Color.white, selectedEntity.getEntityData().name, getTopLeftX() + 4, getTopLeftY() + 4);
        }

        // interacts with BuildList
        if (buttonUp != null) {
            buttonUp.render(graphics);
        }

        if (buttonDown != null) {
            buttonDown.render(graphics);
        }

        // Related to constructing entities
        if (buildList != null) {
            buildList.render(graphics);
        }

    }

    @Override
    public void leftClicked() {
        if (buildList != null) {
            buildList.leftClicked();
        }

        if (buttonDown != null) {
            buttonDown.leftClicked();
        }

        if (buttonUp != null) {
            buttonUp.leftClicked();
        }
    }

    @Override
    public void rightClicked() {
        if (buildList != null) {
            buildList.rightClicked();
        }
    }

    @Override
    public void draggedToCoordinates(Vector2D coordinates) {
        // nothing to do here
    }

    @Override
    public void movedTo(Vector2D coordinates) {
        if (buildList != null) {
            if (buildList.isVectorWithin(coordinates)) {
                buildList.getsFocus();
            } else {
                buildList.lostFocus();
            }
            buildList.movedTo(coordinates);
        }

        if (buttonUp != null) {
            if (buttonUp.isVectorWithin(coordinates)) {
                buttonUp.getsFocus();
            } else {
                buttonUp.lostFocus();
            }
        }

        if (buttonDown != null) {
            if (buttonDown.isVectorWithin(coordinates)) {
                buttonDown.getsFocus();
            } else {
                buttonDown.lostFocus();
            }
        }
    }

    @Override
    public void leftButtonReleased() {
        if (buildList != null) {
            buildList.leftButtonReleased();
        }
    }

    @Override
    public void update(float deltaInSeconds) {
        if (buildList != null) {
            buildList.update(deltaInSeconds);
        }
    }

    @Override
    public void lostFocus() {
        super.lostFocus();
        if (buildList != null) {
            buildList.lostFocus();
        }
    }

    @Override
    public String toString() {
        return "Sidebar";
    }

    /**
     * playing around still, I suppose this triggers a certain kind of 'gui element' to be drawn
     * which needs an Entity reference to show progress of and also base its offerings?
     * @param entity
     */
    public void showEntityInfoFor(Entity entity) {
        this.selectedEntity = entity;

        // nothing to build, so get rid of any references to buildlist related gui elements
        if (!entity.isEntityBuilder()) {
            this.buttonUp = null;
            this.buttonDown = null;
            this.buildList = null;
            return;
        }

        setupGuiElementsForBuildList((EntityBuilder) entity);
    }

    public void setupGuiElementsForBuildList(EntityBuilder entity) {
        int buttonHeight = 32;

        // buildList is 4 icons high + 4 pixels between them, so:
        int buildIconMargin = 4;
        int amountOfIconsInColumn = 4;
        int heightOfBuildList = (RenderableBuildableEntity.HEIGHT + buildIconMargin) * amountOfIconsInColumn;

        int margin = 5;
        int buildListTopX = getTopLeftX() + margin;
        int buildListTopY = getBottomRightY() - margin - heightOfBuildList - buttonHeight;
        int buildListWidth = getWidth() - (margin * 2); // compensate for marging to the left (with startX) + margin to the right, so times 2
        int buildListHeight = heightOfBuildList + buildIconMargin;


        this.buildList =
                new BuildList(
                        buildListTopX,
                        buildListTopY,
                        buildListWidth,
                        buildListHeight,
                        getPlayer(),
                        entity,
                        guiComposite
                );

        this.buttonUp = new BuildListButton(
                getTopLeftX() + margin,
                getBottomRightY() - buttonHeight,
                RenderableBuildableEntity.WIDTH + 8,
                buttonHeight - margin,
                buildList,
                "  -  "
        ) {
            @Override
            void moveList() {
                buildList.moveListUp();
            }
        };

        this.buttonDown = new BuildListButton(
                getTopLeftX() + margin + RenderableBuildableEntity.WIDTH + margin + 8,
                getBottomRightY() - buttonHeight,
                RenderableBuildableEntity.WIDTH + 8,
                buttonHeight - margin,
                buildList,
                "  +  ") {
            @Override
            void moveList() {
                buildList.moveListDown();
            }
        };
    }

    /**
     * Entity is placed on map.
     *
     * @param entity
     */
    public void entityPlacedOnMap(Entity entity) {
        if (buildList != null) {
            buildList.entityPlacedOnMap(entity);
        }
    }

    public void hideEntityBuilderGui() {
        buildList = null;
        buttonDown = null;
        buttonUp = null;
    }
}
