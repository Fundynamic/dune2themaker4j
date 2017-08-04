package com.fundynamic.d2tm.game.rendering.gui.sidebar;


import com.fundynamic.d2tm.game.behaviors.EntityBuilder;
import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.entitybuilders.AbstractBuildableEntity;
import com.fundynamic.d2tm.game.entities.sidebar.RenderableBuildableEntity;
import com.fundynamic.d2tm.game.rendering.gui.GuiComposite;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import java.util.ArrayList;
import java.util.List;

/**
 * A fancy name for a 'build icon'
 */
public class SidebarSelectBuildableEntityGuiElement extends BattlefieldInteractableGuiElement {

    // the entity that is building other entities
    private EntityBuilder entityBuilder;

    // requires communication with BattleField so that we can instantiate the 'place structure mouse' at the battlefield
    // once construction is completed.
    private GuiComposite guiComposite;

    private RenderableBuildableEntity focussedRenderableBuildableEntity;

    private List<RenderableBuildableEntity> renderableBuildableEntities;

    public SidebarSelectBuildableEntityGuiElement(int x, int y, int width, int height, EntityBuilder entityBuilder, GuiComposite guiComposite) {
        super(x, y, width, height);
        this.entityBuilder = entityBuilder;
        this.guiComposite = guiComposite;

        List<AbstractBuildableEntity> buildList = entityBuilder.getBuildList(); // determine build list
        this.renderableBuildableEntities = new ArrayList<>(buildList.size());

        // make renderable versions for the GUI
        int drawY = getTopLeftYAsInt() + 4; // arbitrary amount down to start
        for (AbstractBuildableEntity placementBuildableEntity : buildList) {

            renderableBuildableEntities.add(
                    new RenderableBuildableEntity(
                            placementBuildableEntity,
                            getTopLeftXAsInt() + 10, // arbitrary amount to the right
                            drawY)
            );
            drawY += 51; // height of icon + 3 extra pixels
        }

    }

    @Override
    public void render(Graphics graphics) {
        Vector2D topLeft = getTopLeft();

        graphics.setColor(Color.gray);
        graphics.fillRect(topLeft.getXAsInt(), topLeft.getYAsInt(), getWidthAsInt(), getHeightAsInt());
        graphics.setColor(Color.white);

        for (RenderableBuildableEntity renderableBuildableEntity : renderableBuildableEntities) {
            renderableBuildableEntity.render(graphics);
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
        // clear focus
        focussedRenderableBuildableEntity = null;

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
        for (RenderableBuildableEntity renderableBuildableEntity : renderableBuildableEntities) {
            renderableBuildableEntity.update(deltaInSeconds);
        }
    }

    @Override
    public void entityPlacedOnMap(Entity entity) {
        if (!entityBuilder.isBuildingAnEntity()) {
            // not building anything, that is weird?
            throw new IllegalStateException("Did not expect this");
        }

        // if nothing was awaiting placement, throw an exception as well
        if (!entityBuilder.isAwaitingPlacement(entity.getEntityData())) {
            throw new IllegalStateException("Would expect to have an entityBuilder selected with the entity that just has been placed.");
        }

        entityBuilder.entityIsDelivered(entity);
    }
}
