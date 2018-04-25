package com.fundynamic.d2tm.game.rendering.gui;

import com.fundynamic.d2tm.math.Vector2D;
import com.fundynamic.d2tm.utils.Colors;
import com.fundynamic.d2tm.utils.SlickUtils;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class TextBox {

	private Graphics graphics;

	private Vector2D position;

	private Vector2D dimensions;

	private String text;
	private Color backGroundColor;

	public TextBox(Graphics graphics, Vector2D position) {
		this.graphics = graphics;
		this.position = position;

		dimensions = Vector2D.zero();
	}

	public void setText(String text) {
		this.text = text;
		int height = graphics.getFont().getHeight(text) + 2;
		int width = graphics.getFont().getWidth(text) + 2;
		dimensions = Vector2D.create(width, height);
	}

	public Vector2D getDimensions() {
		return dimensions;
	}

	public void render(Graphics graphics) {
		graphics.setColor(Colors.BLACK_ALPHA_128);
		SlickUtils.fillRect(graphics, position.add(Vector2D.create(7,7)), dimensions);
		graphics.setColor(Colors.SIDEBAR_BACKGROUND);
		SlickUtils.fillRect(graphics, position, dimensions);
		graphics.setColor(Colors.SIDEBAR_BRIGHT_LEFT_UP_SIDE);
		SlickUtils.drawRect(graphics, position, dimensions);
		SlickUtils.drawShadowedText(graphics, Color.white, text, position);
	}

	public void flipPositionHorizontally() {
		this.position = this.position.min(Vector2D.create(dimensions.getXAsInt(), 0));
	}

	public void setBackGroundColor(Color backGroundColor) {
		this.backGroundColor = backGroundColor;
	}

}
