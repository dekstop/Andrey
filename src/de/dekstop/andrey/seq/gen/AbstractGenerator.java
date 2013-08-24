/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
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
	long currentNoteEndInTicks = 0;

	@Override
	public Note[] getNotes(long nowInTicks) {
		List<Note> notes = new ArrayList<Note>();
		
		// Relative cursor
		if (startTicks==0) startTicks = nowInTicks;
		long cursorTicks = nowInTicks - startTicks;
		
		// Initial note
		if (currentNote==null) {
			currentNote = getNextNote();
			currentNoteEndInTicks = cursorTicks + currentNote.getDuration();
//			System.out.println(noteStartInTicks + " " + currentNote.getDuration());
			return new Note[] { currentNote };
		}
		
		// Fill note buffer from phrase sequence
		long selectionStartInTicks = currentNoteEndInTicks;
		while (selectionStartInTicks <= cursorTicks) {
//			System.out.println(selectionStartInTicks + " " + currentNote.getDuration());
			currentNote = getNextNote();
			notes.add(currentNote);
			selectionStartInTicks += currentNote.getDuration();
			currentNoteEndInTicks = selectionStartInTicks;
		};
		
		return notes.toArray(new Note[notes.size()]);
	}

	@Override
	public void reset() {
		startTicks = 0;
		currentNote = null; // TODO: send note off; e.g. copy note and shorten duration
		currentNoteEndInTicks = 0;
	}
	
	/**
	 * To be implemented by subclasses.
	 * @return the next Note in the sequence.
	 */
	protected abstract Note getNextNote();
}
