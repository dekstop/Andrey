package de.dekstop.andrey.seq.gen;

import static org.junit.Assert.*;

import org.junit.Test;

import de.dekstop.andrey.seq.Note;
import de.dekstop.andrey.util.BiasedRng;
import de.dekstop.andrey.util.Rng;

public class MCNoteGeneratorTest {
	
	Note[] ascendingSequence = new Note[]{
			new Note(1, 100, 100),
			new Note(2, 100, 100),
			new Note(3, 100, 100),
			new Note(4, 100, 100),
	};

	@Test
	public void testMCNoteGenerator() {
		Rng rng = new BiasedRng();
		MCNoteGenerator mc1 = new MCNoteGenerator(ascendingSequence, rng);
		assertEquals("firstNote", ascendingSequence[0], mc1.getNextNote());
	}
	
	@Test
	public void testReset() {
		Rng rng = new BiasedRng();
		MCNoteGenerator mc1 = new MCNoteGenerator(ascendingSequence, rng);
		assertEquals("first note", ascendingSequence[0], mc1.getNextNote());
		assertNotEquals("second note", ascendingSequence[0], mc1.getNextNote());
		mc1.reset();
		assertEquals("first note after reset", ascendingSequence[0], mc1.getNextNote());
	}
}
