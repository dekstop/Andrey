package de.dekstop.andrey.seq.gen;

import static org.junit.Assert.*;

import org.junit.Test;

import de.dekstop.andrey.seq.Note;
import de.dekstop.andrey.seq.Phrase;

public class PhraseLoopGeneratorTest {

	Phrase[] ascendingSequence = new Phrase[]{
			new Phrase(new Note[]{ new Note(1, 100, 100) }),
			new Phrase(new Note[]{ new Note(2, 100, 100) }),
			new Phrase(new Note[]{ new Note(3, 100, 100) }),
			new Phrase(new Note[]{ new Note(4, 100, 100) }),
	};

	@Test
	public void testPhraseLoopGenerator() {
		PhraseLoopGenerator gen1 = new PhraseLoopGenerator(ascendingSequence);
		assertEquals("first phrase", ascendingSequence[0], gen1.getNextPhrase());
		assertEquals("second phrase", ascendingSequence[1], gen1.getNextPhrase());
		assertEquals("third phrase", ascendingSequence[2], gen1.getNextPhrase());
		assertEquals("fourth phrase", ascendingSequence[3], gen1.getNextPhrase());
		assertEquals("fifth phrase", ascendingSequence[0], gen1.getNextPhrase());
	}

	@Test
	public void testReset() {
		PhraseLoopGenerator gen1 = new PhraseLoopGenerator(ascendingSequence);
		assertEquals("first phrase", ascendingSequence[0], gen1.getNextPhrase());
		assertEquals("second phrase", ascendingSequence[1], gen1.getNextPhrase());
		gen1.reset();
		assertEquals("first phrase after reset", ascendingSequence[0], gen1.getNextPhrase());
	}
}
