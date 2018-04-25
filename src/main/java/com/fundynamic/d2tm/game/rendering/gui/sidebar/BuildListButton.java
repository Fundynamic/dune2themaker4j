package com.fundynamic.d2tm.game.rendering.gui.sidebar;

import com.fundynamic.d2tm.game.rendering.gui.GuiElement;
import com.fundynamic.d2tm.math.Vector2D;
import com.fundynamic.d2tm.utils.SlickUtils;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public abstract class BuildListButton extends GuiElement {

	private BuildList buildList;
	private String text;
	private int translation;

	public BuildListButton(int x, int y, int width, int height, BuildList buildList, String text) {
		super(x, y, width, height);
		this.buildList = buildList;
		this.text = text;
	}

	@Override
	public String toString() {
		return null;
	}

	@Override
	public void render(Graphics graphics) {
		if (hasFocus) {
			graphics.setColor(Color.darkGray);
		} else {
			graphics.setColor(Color.black);
		}
		SlickUtils.fillRect(graphics, getTopLeft(), getDimensions());
		graphics.setColor(Color.white);
		SlickUtils.drawRect(graphics, getTopLeft(), getDimensions());
		SlickUtils.drawText(graphics, Color.white, text, getTopLeftX(),  getTopLeftY());
	}

	@Override
	public void update(float deltaInSeconds) {

	}

	@Override
	public void leftClicked() {
		if (!hasFocus) return;
		System.out.println("LeftClicked! " + text);
		moveList();
	}

	@Override
	public void rightClicked() {

	}

	@Override
	public void draggedToCoordinates(Vector2D coordinates) {
		// does nothing
	}

	@Override
	public void movedTo(Vector2D coordinates) {
		if (isVectorWithin(coordinates)) {
			getsFocus();
		}
	}

	@Override
	public void leftButtonReleased() {

	}

	abstract void moveList();
}
