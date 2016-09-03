package com.fundynamic.d2tm.game.rendering.gui.sidebar;


import com.fundynamic.d2tm.game.behaviors.EntityBuilder;
import com.fundynamic.d2tm.game.entities.sidebar.BuildableEntity;
import com.fundynamic.d2tm.game.entities.sidebar.RenderableBuildableEntity;
import com.fundynamic.d2tm.game.rendering.gui.GuiElement;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import java.util.ArrayList;
import java.util.List;


public class SidebarSelectBuildableEntityGuiElement extends GuiElement {

    private EntityBuilder entityBuilder;
    private List<RenderableBuildableEntity> renderableBuildableEntities;

    public SidebarSelectBuildableEntityGuiElement(int x, int y, int width, int height, EntityBuilder entityBuilder) {
        super(x, y, width, height);
        this.entityBuilder = entityBuilder;

        List<BuildableEntity> buildList = entityBuilder.getBuildList(); // determine build list
        this.renderableBuildableEntities = new ArrayList<>(buildList.size());
        // make renderable versions for the GUI
        int drawY = getTopLeftYAsInt();
        for (BuildableEntity buildableEntity : buildList) {
            renderableBuildableEntities.add(new RenderableBuildableEntity(buildableEntity, getTopLeftXAsInt() + 5, drawY));
            drawY += 50;
        }

    }

    @Override
    public void render(Graphics graphics) {
        Vector2D topLeft = getTopLeft();
//        if (hasFocus) {
//            graphics.setColor(Color.blue);
//        } else {
//            graphics.setColor(Color.gray);
//        }
        graphics.setColor(Color.gray);
        graphics.fillRect(topLeft.getXAsInt(), topLeft.getYAsInt(), getWidthAsInt(), getHeightAsInt());
        graphics.setColor(Color.white);
        for (RenderableBuildableEntity renderableBuildableEntity : renderableBuildableEntities) {
            renderableBuildableEntity.render(graphics);
        }
    }

    @Override
    public void leftClicked() {
        // do nothing because Dummy element
    }

    @Override
    public void rightClicked() {
        // do nothing because Dummy element
    }

    @Override
    public void draggedToCoordinates(Vector2D coordinates) {
        // do nothing because Dummy element
    }

    @Override
    public void movedTo(Vector2D coordinates) {
        for (RenderableBuildableEntity renderableBuildableEntity : renderableBuildableEntities) {
            renderableBuildableEntity.movedTo(coordinates);
        }
    }

    @Override
    public void leftButtonReleased() {
        // do nothing because Dummy element
    }

    @Override
    public void update(float deltaInSeconds) {
        for (RenderableBuildableEntity renderableBuildableEntity : renderableBuildableEntities) {
            renderableBuildableEntity.update(deltaInSeconds);
        }
    }
}
