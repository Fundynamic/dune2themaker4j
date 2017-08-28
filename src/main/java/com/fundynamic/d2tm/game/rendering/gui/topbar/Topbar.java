package com.fundynamic.d2tm.game.rendering.gui.topbar;


import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.rendering.gui.GuiElement;
import com.fundynamic.d2tm.math.Vector2D;
import com.fundynamic.d2tm.utils.Colors;
import com.fundynamic.d2tm.utils.SlickUtils;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

/**
 * Topbar
 */
public class Topbar extends GuiElement {

    private final Player player;
    private final Image lightningImage;

    public Topbar(int x, int y, int width, int height, Player player, Image lightningImage) {
        super(x, y, width, height);
        this.player = player;
        this.lightningImage = lightningImage;
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

        SlickUtils.drawText(graphics, Color.white, "Resources", topLeft.getXAsInt(), topLeft.getYAsInt() + (getHeightAsInt() / 2) - 8);

        String creditsString = String.format("$ %d", player.getAnimatedCredits());
        SlickUtils.drawShadowedText(graphics, Color.white, creditsString, (topLeft.getXAsInt() + getWidthAsInt()) - 100, topLeft.getYAsInt() + (getHeightAsInt() / 2) - 8);

        int producing = 350;
        int consumption = 351;
        String powerString = String.format("%d > %d", consumption, producing);
        Color statusColor = Color.white;
        if (consumption > (producing - 25)) statusColor = Color.yellow;
        if (consumption > producing) statusColor = Colors.RED_BRIGHT;

        int startX = (topLeft.getXAsInt() + getWidthAsInt()) - 250;
        lightningImage.draw(startX - 40, topLeft.getYAsInt() + (getHeightAsInt() / 2) - 14);
        SlickUtils.drawShadowedText(graphics, statusColor, powerString, startX, topLeft.getYAsInt() + (getHeightAsInt() / 2) - 8);
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
