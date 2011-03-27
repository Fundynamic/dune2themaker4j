package com.fundynamic.dune2themaker.util;

public class Random {

	public static int getRandomInt(int min, int max) {
		int result = min + (int)(Math.random() * (max - min));
		return result;
	}
	
	public static int getFiftyFifty(int first, int second) {
		int chance = (int)(Math.random() * 2);
		if (chance < 1) {
			return first;
		}
		return second;
	}
}
