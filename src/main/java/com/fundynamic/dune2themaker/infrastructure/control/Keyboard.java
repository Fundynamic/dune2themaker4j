package com.fundynamic.dune2themaker.infrastructure.control;

import org.newdawn.slick.Input;

public class Keyboard {

	private final Input input;

	public Keyboard(Input input) {
		this.input = input;
	}

	public boolean isEscPressed() {
		return this.input.isKeyPressed(Input.KEY_ESCAPE);
	}

}
