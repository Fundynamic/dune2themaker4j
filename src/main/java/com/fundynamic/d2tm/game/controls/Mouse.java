package com.fundynamic.d2tm.game.controls;

import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.rendering.gui.GuiComposite;
import com.fundynamic.d2tm.graphics.ImageRepository;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import java.util.HashMap;
import java.util.Map;

/**
 * <h1>General</h1>
 * <p>
 *     This class represents the mouse and functions as an abstraction between hardware inputs and rendering
 *     logic. It propagates all mouse events to its related {@link GuiComposite}.
 * </p>
 * <h2>Rendering</h2>
 * <p>
 *     This class does not implement the Renderable interface because its rendering
 *     is done outside the rendering logic by Slick.
 * </p>
 * <h2>Mouse images</h2>
 * <p>
 *     The image of the mouse to render is set by {@link #setMouseImage(Image, int, int)} (and other)
 *     methods.
 * </p>
 * <h2>Propagating {@link MouseBehavior} events</h2>
 * <p>
 *     This class listens to mouse behavior events, which are passed down by the {@link com.fundynamic.d2tm.game.event.MouseListener}.
 * </p>
 * <p>
 *     This class propagates the mouse behavior events to its related {@link GuiComposite}.
 * </p>
 */
public class Mouse implements MouseBehavior {

    // RENDERING
    public enum MouseImages {
        NORMAL,
        HOVER_OVER_SELECTABLE_ENTITY,
        MOVE,
        ATTACK,
        // and other images we can think of (ie, they are not GUI area specific, these are ALL definitions)
        CUSTOM
    }
    private MouseImages currentImage;
    private Map<MouseImages, Image> mouseImages = new HashMap<>();

    // STATE
    private final Player controllingPlayer;

    private final GameContainer gameContainer;

    // GUI IT INTERACTS WITH
    private MouseBehavior guiComposite;

    Mouse(Player controllingPlayer, GameContainer gameContainer, GuiComposite guiComposite) {
        this.controllingPlayer = controllingPlayer;
        this.gameContainer = gameContainer;
        this.guiComposite = guiComposite;
    }

    public static Mouse create(Player player, GameContainer gameContainer, ImageRepository imageRepository, GuiComposite guiComposite) throws SlickException {
        Mouse mouse = new Mouse(player, gameContainer, guiComposite);
        mouse.setupMouseImage(Mouse.MouseImages.NORMAL, imageRepository.loadAndCache("mouse/mouse_normal.png"));
        mouse.setupMouseImage(Mouse.MouseImages.HOVER_OVER_SELECTABLE_ENTITY, imageRepository.loadAndCache("mouse/mouse_pick.png"));
        mouse.setupMouseImage(Mouse.MouseImages.MOVE, imageRepository.loadAndCache("mouse/mouse_move.png"));
        mouse.setupMouseImage(Mouse.MouseImages.ATTACK, imageRepository.loadAndCache("mouse/mouse_attack.png"));
        return mouse;
    }

    public void init() {
        this.guiComposite = new GuiComposite();
    }

    public Player getControllingPlayer() {
        return controllingPlayer;
    }

    public void setMouseImage(Image image, int hotSpotX, int hotSpotY) {
        if (image == null) throw new IllegalArgumentException("Image to set for mouse cursor may not be null!");
        if (!mouseImages.containsValue(image)) {
            this.currentImage = MouseImages.CUSTOM;
        }
        try {
            gameContainer.setMouseCursor(image, hotSpotX, hotSpotY);
        } catch (SlickException e) {
            throw new CannotSetMouseCursorException(e);
        }
    }

    public void setMouseImageHotSpotCentered(Image image) {
        if (image == null) throw new IllegalArgumentException("Image to set for mouse cursor may not be null!");
        if (!mouseImages.containsValue(image)) {
            this.currentImage = MouseImages.CUSTOM;
        }
        try {
            gameContainer.setMouseCursor(image, image.getWidth() / 2, image.getHeight() / 2);
        } catch (SlickException e) {
            throw new CannotSetMouseCursorException(e);
        }
    }

    public void setMouseImage(MouseImages key, int hotSpotX, int hotSpotY) {
        if (key.equals(this.currentImage)) return;
        this.currentImage = key;
        setMouseImage(mouseImages.get(key), hotSpotX, hotSpotY);
    }

    public void setupMouseImage(MouseImages key, Image image) {
        if (image == null) throw new IllegalArgumentException("Image for MouseImages [" + key + "] may not be null!");
        this.mouseImages.put(key, image);
    }

    @Override
    public void draggedToCoordinates(Vector2D coordinates) {
        guiComposite.draggedToCoordinates(coordinates);
    }

    /**
     * When a left click (== press and release) has been detected, this method is called.
     */
    public void leftClicked() {
        guiComposite.leftClicked();
    }

    /**
     * When a right click (== press and release) has been detected, this method is called.
     */
    public void rightClicked() {
        guiComposite.rightClicked();
    }

    public void movedTo(Vector2D screenPosition) {
        guiComposite.movedTo(screenPosition);
    }

    @Override
    public void leftButtonReleased() {
        guiComposite.leftButtonReleased();
    }

}
