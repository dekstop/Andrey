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

	Long lastNowInTicks = null; // Absolute time in ticks (starts at an arbitrary count)
	long cursorTicks = 0; // Relative time in ticks (starts at 0) 
	
	Note currentNote = null;
	long currentNoteEndInTicks = 0;

	@Override
	public Note[] getNotes(long nowInTicks) {
		List<Note> notes = new ArrayList<Note>();
		
		// First call?
		if (lastNowInTicks == null) {
			lastNowInTicks = nowInTicks;
		}

		// Cursor range check
		if (nowInTicks < lastNowInTicks) {
			throw new IllegalArgumentException(String.format(
					"Selection range backtracking: last cursor position %d, current position %d",
					lastNowInTicks, nowInTicks));
		}

		// Relative cursor
		cursorTicks += nowInTicks - lastNowInTicks;

		if (currentNote==null) {
			// Initial note
			currentNote = getNextNote();
			currentNoteEndInTicks = cursorTicks + currentNote.getDuration();
//			System.out.println(noteStartInTicks + " " + currentNote.getDuration());
			notes.add(currentNote);
		} else {
			// Fill note buffer from phrase sequence
			long selectionStartInTicks = currentNoteEndInTicks;
			while (selectionStartInTicks <= cursorTicks) {
//				System.out.println(selectionStartInTicks + " " + currentNote.getDuration());
				currentNote = getNextNote();
				notes.add(currentNote);
				selectionStartInTicks += currentNote.getDuration();
				currentNoteEndInTicks = selectionStartInTicks;
			};
		}
		
		lastNowInTicks = nowInTicks;
		return notes.toArray(new Note[notes.size()]);
	}

	@Override
	public void reset() {
		lastNowInTicks = null;
		cursorTicks = 0;
		currentNote = null; // TODO: send note off; e.g. copy note and shorten duration
		currentNoteEndInTicks = 0;
	}
	
	/**
	 * To be implemented by subclasses.
	 * @return the next Note in the sequence.
	 */
	protected abstract Note getNextNote();
}
