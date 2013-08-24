package de.dekstop.andrey.seq.gen;

import static org.junit.Assert.assertArrayEquals;

import java.util.Arrays;

import org.junit.Test;

import de.dekstop.andrey.seq.Note;

public class AbstractGeneratorTest {

	class TestNoteGenerator extends AbstractGenerator {
		Note[] sequence;
		int idx = -1;
		
		public TestNoteGenerator(Note[] sequence) {
			this.sequence = sequence;
		}
		
		@Override
    public void reset() {
	    super.reset();
	    idx = -1;
    }

		@Override
    protected Note getNextNote() {
			idx = (idx+1) % sequence.length;
	    return sequence[idx];
    }
	};

	Note[] ascendingSequence = new Note[]{
			new Note(1, 100, 100),
			new Note(2, 100, 100),
			new Note(3, 100, 100),
			new Note(4, 100, 100),
	};
	
	@Test
	public void testGetNotes() {
		
		doTestGetNotesWithOffset(0);
		doTestGetNotesWithOffset(111);
		doTestGetNotesWithOffset(-123);
	}

	private void doTestGetNotesWithOffset(int offset){
		TestNoteGenerator gen1 = new TestNoteGenerator(ascendingSequence);
		String title = "sequence with cursor offset " + offset;

		// First note
		assertArrayEquals(title, 
				Arrays.copyOfRange(ascendingSequence, 0, 1), 
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
				Arrays.copyOfRange(ascendingSequence, 1, 2), 
				gen1.getNotes(offset + 120));
		assertArrayEquals(title, 
				Arrays.copyOfRange(ascendingSequence, 2, 4), 
				gen1.getNotes(offset + 320));
  }

	@Test
	public void testGetNotesEmptyRange() {
		TestNoteGenerator gen1 = new TestNoteGenerator(ascendingSequence);
		gen1.getNotes(0);
		assertArrayEquals("empty range", new Note[]{}, gen1.getNotes(0));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testGetNotesInvalidRange() {
		TestNoteGenerator gen1 = new TestNoteGenerator(ascendingSequence);
		gen1.getNotes(10);
		gen1.getNotes(0); // Backtracking
	}

	@Test
	public void testReset() {
		TestNoteGenerator gen1 = new TestNoteGenerator(ascendingSequence);
		
		// First pass
		assertArrayEquals("before reset", 
				Arrays.copyOfRange(ascendingSequence, 0, 1), 
				gen1.getNotes(0));
		assertArrayEquals("before reset", 
				Arrays.copyOfRange(ascendingSequence, 1, 2), 
				gen1.getNotes(100));
		
		gen1.reset();

		// Second pass
		assertArrayEquals("after reset", 
				Arrays.copyOfRange(ascendingSequence, 0, 1), 
				gen1.getNotes(200));
	}
}
