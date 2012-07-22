package com.fundynamic.dune2themaker.infrastructure.control;

import org.newdawn.slick.Input;

/**
 * @author shendriks
 * @since 22-7-12 22:10
 */
public class InputHandler {

	private final Input input;

	public InputHandler(Input input) {
		this.input = input;
	}

	public void generateEvents() {
		if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
//			new Event();
		}
		// return list of events?
	}
}
