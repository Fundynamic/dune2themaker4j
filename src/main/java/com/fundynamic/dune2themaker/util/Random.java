package com.fundynamic.dune2themaker.util;

public class Random {

	public static int getRandomInt(int min, int max) {
		int result = min + (int)(Math.random() * (max - min));
		return result;
	}
	
	/**
	 * 50/50 percent chance it will return first, or second.
	 * 
	 * @param first
	 * @param second
	 * @return
	 */
	public static int getFiftyFifty(int first, int second) {
		int chance = (int)(Math.random() * 10);
		if (chance < 5) return first;
		return second;
	}
}
