package de.dekstop.andrey.seq;

/**
 * Repeats a fixed sequence.
 */
public class Sequence implements Generator {

  int[] seq;
  int curIdx = -1;

  public Sequence(int[] values) {
    seq = values;
  }

  public int nextValue() {
    curIdx++;
    if (curIdx>=seq.length) curIdx = 0;
    return seq[curIdx];
  }

  public void reset() {
    curIdx = -1;
  }
}
