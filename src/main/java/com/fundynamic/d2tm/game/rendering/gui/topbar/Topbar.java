package com.fundynamic.d2tm.game.rendering.gui.topbar;


import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.rendering.gui.GuiElement;
import com.fundynamic.d2tm.math.Vector2D;
import com.fundynamic.d2tm.utils.SlickUtils;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/**
 * Topbar
 */
public class Topbar extends GuiElement {

    private final Player player;

    public Topbar(int x, int y, int width, int height, Player player) {
        super(x, y, width, height);
        this.player = player;
    }

    @Override
    public void render(Graphics graphics) {
        Vector2D topLeft = getTopLeft();
        if (hasFocus) {
            graphics.setColor(Color.blue);
        } else {
            graphics.setColor(Color.gray);
        }
        graphics.fillRect(topLeft.getXAsInt(), topLeft.getYAsInt(), getWidthAsInt(), getHeightAsInt());

        SlickUtils.drawText(graphics, Color.white, "Moneybar", topLeft.getXAsInt(), topLeft.getYAsInt() + (getHeightAsInt() / 2) - 8);

        String creditsString = String.format("$ %d", player.getCredits());
        SlickUtils.drawShadowedText(graphics, Color.white, creditsString, (topLeft.getXAsInt() + getWidthAsInt()) - 100, topLeft.getYAsInt() + (getHeightAsInt() / 2) - 8);
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
        // do nothing because Dummy element
    }

    @Override
    public void leftButtonReleased() {
        // do nothing because Dummy element
    }

    @Override
    public void update(float deltaInSeconds) {
        // do nothing because Dummy element
    }
}
