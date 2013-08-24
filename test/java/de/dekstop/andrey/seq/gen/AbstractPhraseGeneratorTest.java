package de.dekstop.andrey.seq.gen;

import static org.junit.Assert.assertArrayEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import de.dekstop.andrey.seq.Note;
import de.dekstop.andrey.seq.Phrase;

public class AbstractPhraseGeneratorTest {

	class TestPhraseGenerator extends AbstractPhraseGenerator {
		Phrase[] sequence;
		int idx = -1;
		
		public TestPhraseGenerator(Phrase[] sequence) {
			this.sequence = sequence;
		}
		
		@Override
    public void reset() {
	    super.reset();
	    idx = -1;
    }

		@Override
    protected Phrase getNextPhrase() {
			idx = (idx+1) % sequence.length;
	    return sequence[idx];
    }
	};

	Phrase[] ascendingSequence = new Phrase[]{
			new Phrase(new Note[]{ new Note(1, 100, 100) }),
			new Phrase(new Note[]{ new Note(2, 100, 100) }),
			new Phrase(new Note[]{ new Note(3, 100, 100) }),
			new Phrase(new Note[]{ new Note(4, 100, 100) }),
	};
	
	@Test
	public void testGetNotes() {
		
		doTestGetNotesWithOffset(0);
		doTestGetNotesWithOffset(111);
		doTestGetNotesWithOffset(-123);
	}

	private void doTestGetNotesWithOffset(int offset){
		TestPhraseGenerator gen1 = new TestPhraseGenerator(ascendingSequence);
		String title = "sequence with cursor offset " + offset;

		// First note
		assertArrayEquals(title, 
				extractNotes(Arrays.copyOfRange(ascendingSequence, 0, 1)), 
				gen1.getNotes(offset + 0));
		
		// Gap after first note
		assertArrayEquals(title, 
				new Note[] {}, 
				gen1.getNotes(offset + 1));
		assertArrayEquals(title, 
				new Note[] {}, 
				gen1.getNotes(offset + 99));
		
		// Consecutive notes
		assertArrayEquals(title, 
				extractNotes(Arrays.copyOfRange(ascendingSequence, 1, 2)), 
				gen1.getNotes(offset + 120));
		assertArrayEquals(title, 
				extractNotes(Arrays.copyOfRange(ascendingSequence, 2, 4)), 
				gen1.getNotes(offset + 320));
  }

	@Test
	public void testGetNotesEmptyRange() {
		TestPhraseGenerator gen1 = new TestPhraseGenerator(ascendingSequence);
		gen1.getNotes(0);
		assertArrayEquals("empty range", new Note[]{}, gen1.getNotes(0));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testGetNotesInvalidRange() {
		TestPhraseGenerator gen1 = new TestPhraseGenerator(ascendingSequence);
		gen1.getNotes(10);
		gen1.getNotes(0); // Backtracking
	}

	@Test
	public void testReset() {
		TestPhraseGenerator gen1 = new TestPhraseGenerator(ascendingSequence);
		
		// First pass
		assertArrayEquals("before reset", 
				extractNotes(Arrays.copyOfRange(ascendingSequence, 0, 1)), 
				gen1.getNotes(0));
		assertArrayEquals("before reset", 
				extractNotes(Arrays.copyOfRange(ascendingSequence, 1, 2)), 
				gen1.getNotes(100));
		
		gen1.reset();
		System.out.println("reset");

		// Second pass
		assertArrayEquals("after reset", 
				extractNotes(Arrays.copyOfRange(ascendingSequence, 0, 1)), 
				gen1.getNotes(200));
	}
	
	Note[] extractNotes(Phrase[] phrases) {
		List<Note> notes = new ArrayList<Note>();
		for (Phrase phrase : phrases) {
			notes.addAll(Arrays.asList(phrase.getNotes()));
		}
		return notes.toArray(new Note[notes.size()]);
	}
}
