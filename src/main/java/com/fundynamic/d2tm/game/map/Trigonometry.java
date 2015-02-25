package com.fundynamic.d2tm.game.map;

/**
 * Contains a lookup table for several trigonometry function based on degrees.
 */
public class Trigonometry {

  public static final double[] radians;
  public static final double[] sin;
  public static final double[] cos;

  static {
    radians = getRadiansLookup();
    sin = getSinLookup();
    cos = getCosLookup();
  }

  public static double[] getRadiansLookup() {
    double[] lookup = new double[360];
    for (int degree = 0; degree < 360; degree++) {
      lookup[degree] = Math.toRadians(degree);
    }
    return lookup;
  }

  private static double[] getSinLookup() {
    double[] lookup = new double[360];
    for (int degree = 0; degree < 360; degree++) {
      lookup[degree] = Math.sin(radians[degree]);
    }
    return lookup;
  }

  private static double[] getCosLookup() {
    double[] lookup = new double[360];
    for (int degree = 0; degree < 360; degree++) {
      lookup[degree] = Math.cos(radians[degree]);
    }
    return lookup;
  }
}
