package de.dekstop.andrey.seq.gen;

import static org.junit.Assert.*;

import org.junit.Test;

import de.dekstop.andrey.seq.Note;

public class NoteLoopGeneratorTest {

	Note[] ascendingSequence = new Note[]{
			new Note(1, 100, 100),
			new Note(2, 100, 100),
			new Note(3, 100, 100),
			new Note(4, 100, 100),
	};

	@Test
	public void testPhraseLoopGenerator() {
		NoteLoopGenerator gen1 = new NoteLoopGenerator(ascendingSequence);
		assertEquals("first note", ascendingSequence[0], gen1.getNextNote());
		assertEquals("second note", ascendingSequence[1], gen1.getNextNote());
		assertEquals("third note", ascendingSequence[2], gen1.getNextNote());
		assertEquals("fourth note", ascendingSequence[3], gen1.getNextNote());
		assertEquals("fifth note", ascendingSequence[0], gen1.getNextNote());
	}

	@Test
	public void testReset() {
		NoteLoopGenerator gen1 = new NoteLoopGenerator(ascendingSequence);
		assertEquals("first phrase", ascendingSequence[0], gen1.getNextNote());
		assertEquals("second phrase", ascendingSequence[1], gen1.getNextNote());
		gen1.reset();
		assertEquals("first phrase after reset", ascendingSequence[0], gen1.getNextNote());
	}
}
