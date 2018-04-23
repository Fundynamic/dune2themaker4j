package com.fundynamic.d2tm;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;

/**
 *
 */
public class TestFont implements Font {

	private int charWidth;
	private int lineHeight;

	public TestFont(int charWidth, int lineHeight) {
		this.charWidth = charWidth;
		this.lineHeight = lineHeight;
	}

	@Override
	public int getWidth(String str) {
		return str.length() * 16;
	}

	@Override
	public int getHeight(String str) {
		return ((str.split("\n").length) + 1) * getLineHeight();
	}

	@Override
	public int getLineHeight() {
		return lineHeight;
	}

	@Override
	public void drawString(float x, float y, String text) {

	}

	@Override
	public void drawString(float x, float y, String text, Color col) {

	}

	@Override
	public void drawString(float x, float y, String text, Color col, int startIndex, int endIndex) {

	}
}
