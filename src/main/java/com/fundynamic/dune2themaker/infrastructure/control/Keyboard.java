package com.fundynamic.dune2themaker.infrastructure.control;

import org.newdawn.slick.Input;

public class Keyboard {

	private final Input input;

	public Keyboard(Input input) {
		this.input = input;
	}

	public boolean isEscPressed() {
		// can also use isKeyDown!!!
		return this.input.isKeyDown(Input.KEY_ESCAPE);
	}

	public boolean isKeyUpPressed() {
		return this.input.isKeyDown(Input.KEY_UP);
	}

	public boolean isKeyDownPressed() {
		return this.input.isKeyDown(Input.KEY_DOWN);
	}

	public boolean isKeyLeftPressed() {
		return this.input.isKeyDown(Input.KEY_LEFT);
	}

	public boolean isKeyRightPressed() {
		return this.input.isKeyDown(Input.KEY_RIGHT);
	}
}
