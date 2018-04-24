package com.fundynamic.d2tm.game.rendering.gui;

import com.fundynamic.d2tm.TestFont;
import com.fundynamic.d2tm.math.Vector2D;
import org.junit.Test;
import org.mockito.Mockito;
import org.newdawn.slick.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class TextBoxTest {

	@Test
	public void usingTextExpandsItsDimensions() {
		Graphics graphics = Mockito.mock(Graphics.class);
		when(graphics.getFont()).thenReturn(new TestFont(24, 24));

	    TextBox textBox = new TextBox(graphics, Vector2D.zero());

	    textBox.setText("Hello");

	    assertEquals(Vector2D.create(82, 50), textBox.getDimensions());
	}
}