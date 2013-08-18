package de.dekstop.andrey.seq.gen;

import java.util.HashSet;
import java.util.Set;

import de.dekstop.andrey.seq.*;
import de.dekstop.andrey.util.BiasedRng;


/**
 * Second-order Markov Chain Generator.
 * Limited to values between [0..127]
 */
public class MarkovChain2 implements Generator {

  BiasedRng rng = new BiasedRng();
  Integer[] allPredecessors; // Set of all possible two-value sequences (encoded as 14-bit int)
  int w;
  float[][] p = new float[128*128][128];
  // In a perfect world the Java standard library would have a sparse multi-dimensional matrix implementation.
  int lastValues = -1;

  public MarkovChain2(BiasedRng rng, int[] trainingData) {
    
    this.rng = rng;

    // Count successions
    Set<Integer> allPredecessors = new HashSet<Integer>();
    int[][] c = new int[128*128][128];
    int prev = trainingData[trainingData.length-2];
    for (int idx = 0; idx<trainingData.length; idx++) {
      int prevIdx = idx - 1;
      if (prevIdx < 0) { 
        prevIdx += trainingData.length;
      }
      // Concatenate two previous values as 14-bit key:
      prev = ((prev << 7) + trainingData[prevIdx]) & 0x3F7F;
      allPredecessors.add(prev);
      c[prev][trainingData[idx]] +=  1;
    }

    this.allPredecessors = allPredecessors.toArray(new Integer[]{});

    // Probability map
    for (int v1=0; v1<128*128; v1++) {
      int sum = 0;
      for (int v2=0; v2<128; v2++) {
        sum += c[v1][v2];
      }
      if (sum > 0) {
        for (int v2=0; v2<128; v2++) {
          p[v1][v2] = c[v1][v2] / (float)sum;
        }
      }
    }
  }

  protected int randomPredecessorPair() {
    return allPredecessors[(int)Math.floor(rng.value(allPredecessors.length))];
  }

  protected int randomSuccessor(int lastValues) {
    float r = rng.value(1f);
    int value = -1;
    float sum = 0;
    do {
      value += 1;
      sum += p[lastValues][value];
    } 
    while (sum < r);
    return value;
  }

  public int nextValue() {
    if (lastValues == -1) {
      lastValues = randomPredecessorPair();
      //      println("Randomly selecting previous values: " + (lastValues>>7) + ", " + (lastValues&127));
    }

    int value = randomSuccessor(lastValues);
    lastValues = ((lastValues << 7) + value) & 0x3F7F; // 14 bits
    return value;
  }

  public void reset() {
    lastValues = -1;
  }

	@Override
  public Note[] getNotes(long nowInTicks) {
	  // TODO Auto-generated method stub
	  return null;
  }
}
