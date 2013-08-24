package de.dekstop.andrey.seq;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PhraseTest {

	Note[] ascendingSequence = new Note[]{
			new Note(1, 100, 100),
			new Note(2, 100, 100),
			new Note(3, 100, 100),
			new Note(4, 100, 100),
	};

	@Test
	public void testSelectNotes() {
		Phrase phrase = new Phrase(ascendingSequence);

		assertEquals("phrase length", 4, phrase.getLength());
		assertEquals("phrase note sequence", ascendingSequence[0], phrase.getNote(0));
		assertEquals("phrase note sequence", ascendingSequence[1], phrase.getNote(1));
		assertEquals("phrase note sequence", ascendingSequence[2], phrase.getNote(2));
		assertEquals("phrase note sequence", ascendingSequence[3], phrase.getNote(3));
	}

	@Test
	public void testGetDuration() {
		Phrase phrase = new Phrase(ascendingSequence);
		assertEquals("phrase duration", 400, phrase.getDuration());
	}
}
