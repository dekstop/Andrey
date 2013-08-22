package de.dekstop.andrey.seq.gen;

import de.dekstop.andrey.markov.MarkovChain;
import de.dekstop.andrey.seq.Note;
import de.dekstop.andrey.util.Rng;

public class MCNoteGenerator extends AbstractGenerator {
	
	MarkovChain<Note> mc = null;
	Rng rng = null;
	Note firstNote = null;
	Note currentNote = null;
	
	public MCNoteGenerator(Note[] trainingData, Rng rng) {
		this.mc = new MarkovChain<Note>(trainingData);
		this.rng = rng;
		firstNote = trainingData[0];
		currentNote = firstNote; // TODO: select a random phrase instead
	}

	@Override
	protected Note getNextNote() {
		currentNote = mc.nextValue(currentNote, rng);
		return currentNote;
	}

	@Override
  public void reset() {
	  super.reset();
	  currentNote = firstNote; // TODO: select a random phrase instead
  }
}
