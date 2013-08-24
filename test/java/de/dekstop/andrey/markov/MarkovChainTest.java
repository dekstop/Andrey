package de.dekstop.andrey.markov;

import static org.junit.Assert.*;

import org.junit.Test;

import de.dekstop.andrey.util.BiasedRng;
import de.dekstop.andrey.util.Rng;

public class MarkovChainTest {

	@Test
	public void testSimpleMarkovChain() {
		MarkovChain<Integer> mc1 = new MarkovChain<Integer>(new Integer[]{1, 2, 3, 4});
    // {1={2=1.0}, 2={3=1.0}, 3={4=1.0}, 4={1=1.0}}		

		// Basic properties
		assertEquals("probability matrix column size", 4, mc1.chain.size());
		for (Integer colKey : mc1.chain.keySet()) {
			assertEquals("probability matrix row size", 1, mc1.chain.get(colKey).size());

			Integer rowKey = mc1.chain.get(colKey).keySet().iterator().next();
			assertEquals("<k,v> probability", 
					1.0f, mc1.chain.get(colKey).get(rowKey).floatValue(),
					0.0001);
		}
		
		// Sequences
		Rng rng = new BiasedRng();
		assertEquals("generated sequence", 2, mc1.nextValue(1, rng).intValue());
		assertEquals("generated sequence", 3, mc1.nextValue(2, rng).intValue());
		assertEquals("generated sequence", 4, mc1.nextValue(3, rng).intValue());
		assertEquals("generated sequence", 1, mc1.nextValue(4, rng).intValue());
//		assertArrayEquals("probability matrix", new Float[]{}, mc1.p[0]);
	}

	@Test
	public void testMarkovChain() {
		MarkovChain<Integer> mc1 = new MarkovChain<Integer>(new Integer[]{1, 2, 1});
    // {1={1=0.5, 2=0.5}, 2={1=1.0}}

		// Basic properties
		assertEquals("probability matrix column size", 2, mc1.chain.size());
		assertEquals("<k,v> probability", 
				.5f, mc1.chain.get(1).get(1).floatValue(),
				0.0001);
		assertEquals("<k,v> probability", 
				.5f, mc1.chain.get(1).get(2).floatValue(),
				0.0001);
		assertEquals("<k,v> probability", 
				1.0f, mc1.chain.get(2).get(1).floatValue(),
				0.0001);

		// Sequences
		Rng rng = new BiasedRng();
		int val = mc1.nextValue(1, rng).intValue();
		assertTrue("generated sequence", val==1 || val==2);
		val = mc1.nextValue(1, rng).intValue();
		assertTrue("generated sequence", val==1 || val==2);
		assertEquals("generated sequence", 1, mc1.nextValue(2, rng).intValue());
	}
}
