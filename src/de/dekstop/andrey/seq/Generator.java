package de.dekstop.andrey.seq;

/**
 * Produces sequences of ints.
 */
public interface Generator { 
  public int nextValue();
  public void reset();
}
