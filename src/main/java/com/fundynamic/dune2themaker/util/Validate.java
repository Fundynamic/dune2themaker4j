package com.fundynamic.dune2themaker.util;

public class Validate {

	public static void notNull(Object object, String errorMessage) {
		if (object == null) {
			throw new IllegalArgumentException(errorMessage);
		}
	}

	public static void notNull(Object object) {
		notNull("Argument may not be null");
	}

}
