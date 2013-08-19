package de.dekstop.andrey.util;

/**
 * Random number generator with variable bias (an offset of [-n/2 .. n2]) and range (a multiplier of [0 .. 1])
 */
public class BiasedRng implements Rng {
  float bias = 0.9f; // [-1 .. 1]
  float range = 0; // [0 .. 1]

  public void setBias(float bias) {
    this.bias = Math.max(-1f, Math.min(1f, bias));
    System.out.println("bias: " + this.bias);
  }

  public void setRange(float range) {
    this.range = Math.max(0f, Math.min(1f, range));
    System.out.println("range: " + this.range);
  }

  @Override
  public float value() {
    return value(1f);
  }

  @Override
  public float value(float max) {
    return (float)Math.random() * (Math.max(0f, Math.min(max, max/2 + bias * max/2 + max/2 * range)));
  }
}
