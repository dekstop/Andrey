package de.dekstop.andrey.seq.gen;

import static org.junit.Assert.*;

import org.junit.Test;

import de.dekstop.andrey.seq.Note;
import de.dekstop.andrey.seq.Phrase;
import de.dekstop.andrey.util.BiasedRng;
import de.dekstop.andrey.util.Rng;

public class MCPhraseGeneratorTest {
	
	Phrase[] ascendingSequence = new Phrase[]{
			new Phrase(new Note[]{ new Note(1, 100, 100) }),
			new Phrase(new Note[]{ new Note(2, 100, 100) }),
			new Phrase(new Note[]{ new Note(3, 100, 100) }),
			new Phrase(new Note[]{ new Note(4, 100, 100) }),
	};

	@Test
	public void testMCNoteGenerator() {
		Rng rng = new BiasedRng();
		MCPhraseGenerator mc1 = new MCPhraseGenerator(ascendingSequence, rng);
		assertEquals("firstPhrase", ascendingSequence[0], mc1.getNextPhrase());
	}
	
	@Test
	public void testReset() {
		Rng rng = new BiasedRng();
		MCPhraseGenerator mc1 = new MCPhraseGenerator(ascendingSequence, rng);
		assertEquals("first phrase", ascendingSequence[0], mc1.getNextPhrase());
		assertEquals("second phrase", ascendingSequence[1], mc1.getNextPhrase());
		mc1.reset();
		assertEquals("first phrase after reset", ascendingSequence[0], mc1.getNextPhrase());
	}
}
