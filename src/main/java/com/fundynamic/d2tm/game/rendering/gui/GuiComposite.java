package com.fundynamic.d2tm.game.rendering.gui;

import com.fundynamic.d2tm.Game;
import com.fundynamic.d2tm.game.behaviors.EntityBuilder;
import com.fundynamic.d2tm.game.behaviors.Renderable;
import com.fundynamic.d2tm.game.behaviors.Updateable;
import com.fundynamic.d2tm.game.controls.MouseBehavior;
import com.fundynamic.d2tm.game.controls.battlefield.NormalMouse;
import com.fundynamic.d2tm.game.controls.battlefield.PlacingStructureMouse;
import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.entitybuilders.AbstractBuildableEntity;
import com.fundynamic.d2tm.game.rendering.gui.battlefield.BattleField;
import com.fundynamic.d2tm.game.rendering.gui.sidebar.Sidebar;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.Graphics;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1>General</h1>
 * <p>
 *     This class represents all GUI elements in the game.
 * </p>
 * <h2>Rendering</h2>
 * <p>
 *     This renders all gui elements in the order they have been added (first in, first drawn)
 * </p>
 * <h2>Updating</h2>
 * <p>
 *     This updates all gui elements in the order they have been added (first in, first updated)
 * </p>
 * <b>{@link MouseBehavior} events</b>
 * <p>
 *     Mouse events are propagated from the {@link com.fundynamic.d2tm.game.controls.Mouse} class.
 * </p>
 * <p>
 *     This class listens to mouse events. Based on {@link #movedTo(Vector2D)} it will determine
 *     which guiElement is focussed on, stored in {@link #activeGuiElement}. All other mouse events
 *     are propagated to {@link #activeGuiElement}.
 * </p>
 */
public class GuiComposite implements Renderable, Updateable, MouseBehavior, BattleFieldInteractable {

    public static final int PIXELS_NEAR_BORDER = 2;

    // Gui elements references to interact with
    private List<GuiElement> guiElements = new ArrayList<>();

    /**
     * A quick reference to the object that the mouse interacts with, determined by
     * {@link #movedTo(Vector2D)}.
     **/
    private GuiElement activeGuiElement = NullGuiElement.getInstance();

    private BattleField battleField;
    private Sidebar sidebar;

    @Override
    public void render(Graphics graphics) {
        for (GuiElement guiElement : guiElements) {
            guiElement.render(graphics);
        }
    }

    @Override
    public void update(float deltaInSeconds) {
        for (GuiElement guiElement : guiElements) {
            guiElement.update(deltaInSeconds);
        }
    }

    public void movedTo(Vector2D screenPosition) {
        // determine wich guiElement is active by determining if the position is within the vector of a GUI element.
        activeGuiElement.lostFocus();
        activeGuiElement = findGuiElementWhereVectorLiesWithin(screenPosition);
        activeGuiElement.getsFocus();

        // propagate the moved to event
        activeGuiElement.movedTo(screenPosition);

        // Scroll the battlefield by hitting the borders of the screen
        if (battleField != null) {
            int newx = screenPosition.getXAsInt();
            int newy = screenPosition.getYAsInt();

            if (newx <= PIXELS_NEAR_BORDER) {
                battleField.moveLeft();
            } else if (newx >= Game.getResolution().getXAsInt() - PIXELS_NEAR_BORDER) {
                battleField.moveRight();
            } else {
                battleField.stopMovingHorizontally();
            }

            if (newy <= PIXELS_NEAR_BORDER) {
                battleField.moveUp();
            } else if (newy >= Game.getResolution().getYAsInt() - PIXELS_NEAR_BORDER) {
                battleField.moveDown();
            } else {
                battleField.stopMovingVertically();
            }
        }
    }

    public GuiElement findGuiElementWhereVectorLiesWithin(Vector2D screenPosition) {
        for (GuiElement guiElement : guiElements) {
            if (guiElement.isVectorWithin(screenPosition)) {
                return guiElement;
            }
        }
        return NullGuiElement.getInstance();
    }

    public void leftClicked() {
        activeGuiElement.leftClicked();
    }

    public void rightClicked() {
        activeGuiElement.rightClicked();
    }

    @Override
    public void draggedToCoordinates(Vector2D coordinates) {
        activeGuiElement.draggedToCoordinates(coordinates);
    }

    public void leftButtonReleased() {
        activeGuiElement.leftButtonReleased();
    }

    public void addGuiElement(GuiElement guiElement) {
        assignBattlefieldPropertyIfApplicable(guiElement);
        assignSidebarPropertyIfApplicable(guiElement);

        guiElement.setGuiComposite(this);
        guiElements.add(guiElement);
    }

    public void assignSidebarPropertyIfApplicable(GuiElement guiElement) {
        if (guiElement instanceof Sidebar) {
            if (sidebar != null) {
                throw new IllegalArgumentException("There cannot be more than one sidebar gui element");
            }
            sidebar = (Sidebar) guiElement;
        }
    }

    public void assignBattlefieldPropertyIfApplicable(GuiElement guiElement) {
        if (guiElement instanceof BattleField) {
            if (battleField != null) {
                throw new IllegalArgumentException("There cannot be more than one battlefield gui element");
            }
            battleField = (BattleField) guiElement;
        }
    }

    /**
     * Event: An entity builder is selected
     * @param entityBuilder
     */
    public void entityBuilderSelected(Entity entityBuilder) {
        if (!entityBuilder.isEntityBuilder()) {
            throw new IllegalArgumentException("Can only select entities which implement entity builder from here on");
        }
        if (entityBuilder.isEntityTypeStructure()) {
            sidebar.showEntityBuilderGuiFor((EntityBuilder) entityBuilder);
        }
    }

    /**
     * Event: a buildable Entity which should be placed is selected.
     * @param abstractBuildableEntity
     */
    public void wantsToPlaceBuildableEntityOnBattlefield(AbstractBuildableEntity abstractBuildableEntity) {
        battleField.setMouseBehavior(new PlacingStructureMouse(battleField, abstractBuildableEntity.getEntityData()));
    }

    /**
     * Event: an entity is placed on the map
     * @param entity
     */
    public void entityPlacedOnMap(Entity entity) {
        sidebar.entityPlacedOnMap(entity);
        battleField.setMouseBehavior(new NormalMouse(battleField));
    }

    public void allEntityBuildersDeSelected() {
        sidebar.hideEntityBuilderGui();
    }
}
