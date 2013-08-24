package de.dekstop.andrey.seq;

import static org.junit.Assert.*;

import java.util.Arrays;

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
		// select first note
		assertEquals("phrase note sequence", 1, phrase.selectNotes(0, 0)[0].getPitch());
		assertEquals("phrase note sequence", 1, phrase.selectNotes(0, 1)[0].getPitch());
		assertEquals("phrase note sequence", 1, phrase.selectNotes(0, 99)[0].getPitch());
		assertEquals("phrase note sequence", 1, phrase.selectNotes(0, 100)[0].getPitch());

		// select second note
		assertEquals("phrase note sequence", 2, phrase.selectNotes(99, 101)[0].getPitch());
		assertEquals("phrase note sequence", 2, phrase.selectNotes(100, 100)[0].getPitch());
		assertEquals("phrase note sequence", 2, phrase.selectNotes(100, 200)[0].getPitch());

		// select note 2 and 3
		Note[] note23 = Arrays.copyOfRange(ascendingSequence, 1, 3);
		assertArrayEquals("phrase note sequence", note23, phrase.selectNotes(99, 200));
		assertArrayEquals("phrase note sequence", note23, phrase.selectNotes(100, 210));
		assertArrayEquals("phrase note sequence", note23, phrase.selectNotes(100, 299));

		// select gap between notes
		assertEquals("phrase note sequence", 0, phrase.selectNotes(1, 2).length);
		assertEquals("phrase note sequence", 0, phrase.selectNotes(1, 99).length);
		assertEquals("phrase note sequence", 0, phrase.selectNotes(101, 199).length);

		// select gap after phrase
		assertEquals("phrase note sequence", 0, phrase.selectNotes(401, 500).length);
	}

	@Test
	public void testGetDuration() {
		Phrase phrase = new Phrase(ascendingSequence);
		assertEquals("phrase duration", 400, phrase.getDuration());
	}
}
