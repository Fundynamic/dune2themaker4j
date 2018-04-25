package com.fundynamic.d2tm.game.rendering.gui.sidebar;


import com.fundynamic.d2tm.game.behaviors.EntityBuilder;
import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.entities.entitybuilders.AbstractBuildableEntity;
import com.fundynamic.d2tm.game.entities.sidebar.RenderableBuildableEntity;
import com.fundynamic.d2tm.game.rendering.gui.GuiComposite;
import com.fundynamic.d2tm.game.rendering.gui.TextBox;
import com.fundynamic.d2tm.game.types.EntityData;
import com.fundynamic.d2tm.math.Rectangle;
import com.fundynamic.d2tm.math.Vector2D;
import com.fundynamic.d2tm.utils.Colors;
import com.fundynamic.d2tm.utils.SlickUtils;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import java.util.ArrayList;
import java.util.List;

public class BuildList extends BattlefieldInteractableGuiElement {

    private final Player player;
    private Vector2D mouseCoordinates;

    // The entity that is building other entities, ie a Structure
    private EntityBuilder entityBuilder;

    // Requires communication with BattleField so that we can instantiate the 'place structure mouse' at the battlefield
    // once construction is completed.
    private GuiComposite guiComposite;

    private RenderableBuildableEntity focussedRenderableBuildableEntity;

    private List<RenderableBuildableEntity> renderableBuildableEntities;

    private int amountOfIconsAbleToRenderHorizontally = 5;
    private Rectangle drawingArea;

    public BuildList(int x, int y, int width, int height, Player player, EntityBuilder entityBuilder, GuiComposite guiComposite) {
        super(x, y, width, height);
        this.player = player;
        this.entityBuilder = entityBuilder;
        this.guiComposite = guiComposite;

        int heightOfIcon = RenderableBuildableEntity.HEIGHT + 4;
        int widthOfIcon = RenderableBuildableEntity.WIDTH + 6;
        this.amountOfIconsAbleToRenderHorizontally = (int) Math.floor(width / widthOfIcon);

        List<AbstractBuildableEntity> buildList = entityBuilder.getBuildList(); // determine build list
        this.renderableBuildableEntities = new ArrayList<>(buildList.size());

        drawingArea = new Rectangle(x + 4, y + 4, Vector2D.create(width - 8, height - 8));

        // make renderable versions for the GUI
        int drawY = getTopLeftY() + 4; // arbitrary amount down to start

        int drawXLeftIcon = getTopLeftX() + 8;
        int drawXRightIcon = drawXLeftIcon + widthOfIcon;

        // start with left icon
        int drawX = drawXLeftIcon;

        int icon = 0;
        for (AbstractBuildableEntity placementBuildableEntity : buildList) {
            renderableBuildableEntities.add(
                    new RenderableBuildableEntity(
                            placementBuildableEntity,
                            this.player,
                            drawX,
                            drawY)
            );

            icon++;

            if (icon == amountOfIconsAbleToRenderHorizontally) {
                drawY += heightOfIcon;  // new row
                drawX = drawXLeftIcon; // reset X coordinate
                icon = 0;
            } else {
                drawX += widthOfIcon;
            }
        }

    }

    @Override
    public void render(Graphics graphics) {
        graphics.setColor(Colors.SIDEBAR_YELLOW_BAR_DARK);
        graphics.fillRect(getTopLeftX(), getTopLeftY(), getWidth(), getHeight());

        graphics.setColor(Colors.SIDEBAR_DARK_RIGHT_DOWN_SIDE);
        graphics.drawLine(getTopLeftX(), getTopLeftY(), getTopLeftX(), getBottomRightY() - 1); // left side (up / down)
        graphics.drawLine(getTopLeftX(), getTopLeftY(), getTopLeftX() + getWidth(), getTopLeftY()); // top side

        graphics.setColor(Colors.SIDEBAR_BRIGHT_LEFT_UP_SIDE);
        graphics.drawLine(getTopLeftX() + getWidth(), getTopLeftY() + 1, getTopLeftX() + getWidth(), getBottomRightY() - 1); // right side (up / down)
        graphics.drawLine(getTopLeftX() + 1, getBottomRightY() - 1, getTopLeftX() + getWidth() - 1, getBottomRightY() - 1); // bottom side


        graphics.setColor(Color.white);

        SlickUtils.setClip(graphics, drawingArea);

        for (RenderableBuildableEntity renderableBuildableEntity : renderableBuildableEntities) {
            renderableBuildableEntity.render(graphics);
        }

        graphics.clearClip();

        if (focussedRenderableBuildableEntity != null) {
            graphics.setColor(Color.red);
            EntityData entityData = focussedRenderableBuildableEntity.getAbstractBuildableEntity().getEntityData();

            TextBox textBox = new TextBox(graphics, mouseCoordinates.min(Vector2D.create(32, 0)));
            String text = "" + entityData.name + "\n" +
                          "\nCost: $" + entityData.buildCost +
                          "\nTime: S" + entityData.buildTimeInSeconds +
                          "\nPower -/+: " + entityData.powerConsumption + "/" + entityData.powerProduction;

            textBox.setText(text);
            textBox.flipPositionHorizontally();
            textBox.render(graphics);
        }
    }

    @Override
    public void leftClicked() {
        if (focussedRenderableBuildableEntity == null) return;
        AbstractBuildableEntity abstractBuildableEntity = focussedRenderableBuildableEntity.getAbstractBuildableEntity();

        // not constructing anything, so start building it.
        if (!entityBuilder.isBuildingAnEntity()) {
            // construct it:
            // 1. tell it to construct
            entityBuilder.buildEntity(abstractBuildableEntity);
        } else {
            // it is building anything, so lets check the progress:

            // does it await placement?
            if (entityBuilder.isAwaitingPlacement(abstractBuildableEntity)) {
                // TODO move to BattlefieldInteractable interface?!

                guiComposite.wantsToPlaceBuildableEntityOnBattlefield(abstractBuildableEntity);
            }
        }

        // update state of this entity
        focussedRenderableBuildableEntity.leftClicked();
    }

    @Override
    public void rightClicked() {
        for (RenderableBuildableEntity renderableBuildableEntity : renderableBuildableEntities) {
            renderableBuildableEntity.rightClicked();
        }
    }

    @Override
    public void draggedToCoordinates(Vector2D coordinates) {
        // do nothing here
    }

    @Override
    public void movedTo(Vector2D coordinates) {
        mouseCoordinates = coordinates;

        // clear focus
        if (focussedRenderableBuildableEntity != null) {
            focussedRenderableBuildableEntity.lostFocus();
        }
        focussedRenderableBuildableEntity = null;

//        if (coordinates.getYAsInt() > getTopLeftY() + maxWidthOfListInPixels) {
//            return;
//        }

        // tell all entities that mouse has moved, let entity decide etc
        // and based on focus state remember that the mouse is moving over a specific icon, which is needed for
        // constructing later (see {@link #leftClicked()})
        for (RenderableBuildableEntity renderableBuildableEntity : renderableBuildableEntities) {
            renderableBuildableEntity.movedTo(coordinates);
            if (renderableBuildableEntity.hasFocus()) {
                focussedRenderableBuildableEntity = renderableBuildableEntity;
            }
        }
    }

    @Override
    public void leftButtonReleased() {
        // nothing to do here
    }

    @Override
    public void update(float deltaInSeconds) {
        // in case the mouse moves too fast from an icon to another gui element, it remains 'selected/hovered'
        // so in those cases explicitly make sure we clear out the focused entity when lost focus.
        if (!hasFocus) {
            if (focussedRenderableBuildableEntity != null) {
                focussedRenderableBuildableEntity.lostFocus();
            }
            focussedRenderableBuildableEntity = null;
        }

        for (RenderableBuildableEntity renderableBuildableEntity : renderableBuildableEntities) {
            renderableBuildableEntity.update(deltaInSeconds);
        }
    }

    @Override
    public void entityPlacedOnMap(Entity entity) {
        // if we where not building anything, throw an exception
        if (!entityBuilder.isBuildingAnEntity()) {
            throw new IllegalStateException("Did not expect this");
        }

        // if nothing was awaiting placement, throw an exception as well
        if (!entityBuilder.isAwaitingPlacement(entity.getEntityData())) {
            throw new IllegalStateException("Would expect to have an entityBuilder selected with the entity that just has been placed.");
        }

        entityBuilder.entityIsDelivered(entity);
    }

    @Override
    public String toString() {
        return "BuildList";
    }
}
