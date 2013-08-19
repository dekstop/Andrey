package de.dekstop.andrey.seq.gen;

import de.dekstop.andrey.seq.Phrase;

public class LoopGenerator extends AbstractPhraseGenerator {
	
	Phrase[] sequence;
	int idx = -1;
	
	public LoopGenerator(Phrase[] sequence) {
		this.sequence = sequence;
	}

	@Override
	protected Phrase getNextPhrase() {
		idx = (idx+1) % sequence.length;
		return sequence[idx];
	}

	@Override
  public void reset() {
	  super.reset();
	  idx = -1;
  }
}
