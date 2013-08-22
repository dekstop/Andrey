package de.dekstop.andrey.seq.gen;

import java.util.ArrayList;
import java.util.List;

import de.dekstop.andrey.seq.Generator;
import de.dekstop.andrey.seq.Note;

/**
 * Base class for {@link Generator} implementations that yield streams of notes. 
 */
public abstract class AbstractGenerator implements Generator {

	long startTicks = 0; // Absolute time in ticks (starts at an arbitrary count)
	
	Note currentNote = null;
	long noteStartInTicks = 0;

	@Override
	public Note[] getNotes(long nowInTicks) {
		List<Note> notes = new ArrayList<Note>();
		
		// Relative cursor
		if (startTicks==0) startTicks = nowInTicks;
		long cursorTicks = nowInTicks - startTicks;
		
		// Initial note
		if (currentNote==null) {
			currentNote = getNextNote();
			noteStartInTicks = cursorTicks;
//			System.out.println(noteStartInTicks + " " + currentNote.getDuration());
			return new Note[] { currentNote };
		}
		
		// Fill note buffer from phrase sequence
		long selectionStartInTicks = noteStartInTicks + currentNote.getDuration();
		while (selectionStartInTicks <= cursorTicks) {
//			System.out.println(selectionStartInTicks + " " + currentNote.getDuration());
			currentNote = getNextNote();
			notes.add(currentNote);
			noteStartInTicks = selectionStartInTicks;
			selectionStartInTicks += currentNote.getDuration();
		};
		
		return notes.toArray(new Note[notes.size()]);
	}

	@Override
	public void reset() {
		startTicks = 0;
		currentNote = null;
		noteStartInTicks = 0;
	}
	
	/**
	 * To be implemented by subclasses.
	 * @return the next Note in the sequence.
	 */
	protected abstract Note getNextNote();
}
