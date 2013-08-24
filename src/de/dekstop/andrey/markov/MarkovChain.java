/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package de.dekstop.andrey.markov;

import java.util.HashMap;
import java.util.Map;

import de.dekstop.andrey.util.Rng;

/**
 * First-order Markov Chain.
 */
public class MarkovChain<T> {

//  T[] trainingData;
  Map<T, Map<T, Float>> chain = new HashMap<T, Map<T,Float>>();
  float[][] p = new float[128][128];

  /**
   * 
   * @param trainingData
   */
  public MarkovChain(T[] trainingData) {
//  	this.trainingData = trainingData.clone();
    
    // Count successions
  	Map<T, Map<T, Integer>> counters = new HashMap<T, Map<T,Integer>>(); 
    for (int idx = 0; idx<trainingData.length; idx++) {
    	int nextIdx = (idx+1) % trainingData.length;
      T key1 = trainingData[idx];
      T key2 = trainingData[nextIdx];
      put(counters, key1, key2, get(counters, key1, key2, 0) + 1);
    }

    // Probability map
    for (T key1 : counters.keySet()) {
    	int sum = 0;
    	for (T key2 : counters.get(key1).keySet()) {
    		sum += get(counters, key1, key2, 0);
    	}
    	if (sum > 0) {
    		for (T key2 : counters.get(key1).keySet()) {
      		put(chain, key1, key2, get(counters, key1, key2, 0) / (float)sum);
      	}
    	}
    }
  }

//  public T randomValue(Rng rng) {
//    return trainingData[(int)Math.floor(rng.value(trainingData.length))];
//  }

  public T nextValue(T currentValue, Rng rng) {
  	if (!chain.containsKey(currentValue)) 
  		throw new IllegalArgumentException("The item is not a valid key for this Markov chain: " + 
  				currentValue);
    float r = rng.value(1f);
    float sum = 0;
    for (T nextValue : chain.get(currentValue).keySet()) {
      sum += get(chain, currentValue, nextValue, 0f);
      if (sum >= r) {
      	return nextValue;
      }
    } 
		throw new IllegalStateException("Could not find a successor for this value... there's a bug in the probability matrix.");
  }
  
  protected <U> void put(Map<T, Map<T, U>> muliKeyMap, T key1, T key2, U value) {
  	if (!muliKeyMap.containsKey(key1)) muliKeyMap.put(key1, new HashMap<T, U>());
  	muliKeyMap.get(key1).put(key2, value);
  }

  protected <U> U get(Map<T, Map<T, U>> muliKeyMap, T key1, T key2, U defaultValue) {
  	if (!muliKeyMap.containsKey(key1)) return defaultValue;
  	if (!muliKeyMap.get(key1).containsKey(key2)) return defaultValue;
  	return muliKeyMap.get(key1).get(key2);
  }
}
