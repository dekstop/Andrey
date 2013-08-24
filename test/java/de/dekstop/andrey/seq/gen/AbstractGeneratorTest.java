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
		
		// Single consecutive notes
		assertArrayEquals(title, 
				Arrays.copyOfRange(ascendingSequence, 1, 2), 
				gen1.getNotes(offset + 120));

		// Multiple consecutive notes
		assertArrayEquals(title, 
				Arrays.copyOfRange(ascendingSequence, 2, 4), 
				gen1.getNotes(offset + 320));
	
		// Gap 
  }
	
//// select first note
//assertEquals("phrase note sequence", 1, phrase.selectNotes(0, 1)[0].getPitch());
//assertEquals("phrase note sequence", 1, phrase.selectNotes(0, 99)[0].getPitch());
//assertEquals("phrase note sequence", 1, phrase.selectNotes(0, 100)[0].getPitch());
//
//// select second note
//assertEquals("phrase note sequence", 2, phrase.selectNotes(99, 101)[0].getPitch());
//assertEquals("phrase note sequence", 2, phrase.selectNotes(100, 100)[0].getPitch());
//assertEquals("phrase note sequence", 2, phrase.selectNotes(100, 200)[0].getPitch());
//
//// select note 2 and 3
//Note[] note23 = Arrays.copyOfRange(ascendingSequence, 1, 3);
//assertArrayEquals("phrase note sequence", note23, phrase.selectNotes(99, 200));
//assertArrayEquals("phrase note sequence", note23, phrase.selectNotes(100, 210));
//assertArrayEquals("phrase note sequence", note23, phrase.selectNotes(100, 299));
//
//// select gap between notes
//assertEquals("phrase note sequence", 0, phrase.selectNotes(1, 2).length);
//assertEquals("phrase note sequence", 0, phrase.selectNotes(1, 99).length);
//assertEquals("phrase note sequence", 0, phrase.selectNotes(101, 199).length);
//
//// select gap after phrase
//assertEquals("phrase note sequence", 0, phrase.selectNotes(401, 500).length);

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
