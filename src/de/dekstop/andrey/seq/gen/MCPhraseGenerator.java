package de.dekstop.andrey.seq.gen;

import de.dekstop.andrey.markov.MarkovChain;
import de.dekstop.andrey.seq.Phrase;
import de.dekstop.andrey.util.Rng;

public class MCPhraseGenerator extends AbstractPhraseGenerator {
	
	MarkovChain<Phrase> mc = null;
	Rng rng = null;
	Phrase firstPhrase = null;
	Phrase currentPhrase = null;
	
	public MCPhraseGenerator(Phrase[] trainingData, Rng rng) {
		this.mc = new MarkovChain<Phrase>(trainingData);
		this.rng = rng;
		firstPhrase = trainingData[0];
		currentPhrase = firstPhrase; // TODO: select a random phrase instead
	}

	@Override
	protected Phrase getNextPhrase() {
		currentPhrase = mc.nextValue(currentPhrase, rng);
		return currentPhrase;
	}

	@Override
  public void reset() {
	  super.reset();
	  currentPhrase = firstPhrase; // TODO: select a random phrase instead
  }
}
