package seq.gen;

import java.util.HashSet;
import java.util.Set;

import seq.*;
import util.BiasedRng;

/**
 * First-order Markov Chain Generator.
 */
public class MarkovChain implements Generator {

  BiasedRng rng;
  Set<Integer> allValues = new HashSet<Integer>();
  int w;
  float[][] p = new float[128][128];
  int lastValue = -1;

  public MarkovChain(BiasedRng rng, int[] trainingData) {
    
    this.rng = rng;

    // Count successions
    int[][] c = new int[128][128];
    for (int idx = 0; idx<trainingData.length; idx++) {
      int prevIdx = idx - 1;
      if (prevIdx < 0) { 
        prevIdx += trainingData.length;
      }
      allValues.add(trainingData[idx]);
      c[trainingData[prevIdx]][trainingData[idx]] +=  1;
    }

    // Probability map
    for (int v1=0; v1<128; v1++) {
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

  protected int randomValue() {
    return (Integer)allValues.toArray()[(int)Math.floor(rng.value(allValues.size()))];
  }

  public int nextValue() {
    int value;
    if (lastValue == -1) {
      value = randomValue();
    } 
    else {
      float r = rng.value(1f);
      value = -1;
      float sum = 0;
      do {
        value += 1;
        sum += p[lastValue][value];
      } 
      while (sum < r);
    }
    lastValue = value;
    return value;
  }

  public void reset() {
    lastValue = -1;
  }
}
