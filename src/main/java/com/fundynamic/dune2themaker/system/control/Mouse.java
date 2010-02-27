package com.fundynamic.dune2themaker.system.control;

import org.newdawn.slick.Input;

public class Mouse {
	
	private Input input; // requires input signals
	
	public Mouse(Input input) {
		if (input == null) throw new IllegalArgumentException("Input may not be null");
		this.input = input;
	}
	
	public boolean isLeftMouseButtonPressed() {
		return input.isMousePressed(Input.MOUSE_LEFT_BUTTON);
	}
	
	public boolean isRightMouseButtonPressed() {
		return input.isMousePressed(Input.MOUSE_RIGHT_BUTTON);
	}
	
	public int getXCoordinate() {
		return input.getMouseX();
	}
	
	public int getYCoordinate() {
		return input.getMouseY();
	}

	/**
	 * Constructs a <code>String</code> with all attributes
	 * in name = value format.
	 *
	 * @return a <code>String</code> representation 
	 * of this object.
	 */
	public String toString()
	{
	    final String TAB = "    ";
	    
	    String retValue = "";
	    
	    retValue = "Mouse ( "
	    	+ "isLeftMouseButtonPressed = [" + isLeftMouseButtonPressed() + "] " 
	    	+ "isRightMouseButtonPressed = [" + isRightMouseButtonPressed() + "] " 
	    	+ "isLeftMouseButtonPressed = [" + getXCoordinate() + "] " 
	    	+ "isLeftMouseButtonPressed = [" + getYCoordinate() + "] " 
	        + " )";
	
	    return retValue;
	}
	
}
