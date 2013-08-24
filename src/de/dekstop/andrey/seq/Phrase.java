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
package de.dekstop.andrey.seq;

import java.util.Arrays;

import de.dekstop.andrey.util.ArrayUtils;

/**
 * A sequence of notes and pauses.
 *
 */
public class Phrase implements ScoreElement {
	
	Note[] notes;
	int duration = 0;
	
	public Phrase(Note[] notes) {
	  this.notes = notes;
	  for (Note note : notes) {
	  	this.duration += note.duration;
	  }
  }
	
	/**
	 * Select a block of notes within the specified time range [fromTicks, toTicks[.
	 * The range can wrap around, so that toTicks < fromTicks.
	 * 
	 * fromTicks and toTicks are relative cursors, starting at 0 (the beginning of the
	 * phrase.) Selections that start beyond the end of the phrase yield an empty list 
	 * of notes. 
	 * 
	 * @param fromTicks start of selection window in number of ticks
	 * @param toTicks start of selection window in number of ticks
	 * @return an empty array, or a sequence of one or more notes
	 */
	public Note[] selectNotes(int fromTicks, int toTicks) {

		// Zero-length window?
		if (fromTicks == toTicks) return new Note[]{};

		// Window wraps around phrase ending?
		if (toTicks < fromTicks) {
			return ArrayUtils.concat(selectNotes(fromTicks, duration), selectNotes(0, toTicks));
		}
		
		// Window is non-zero and within range.
		int fromIdx = -1;
		int toIdx = -1;
		int curNoteStartTicks = 0;
		for (int idx=0; idx<notes.length; idx++) {
			Note note = notes[idx];

			// Determine selection start index
			if (fromIdx==-1) {
				if (fromTicks<=curNoteStartTicks) {
					fromIdx = idx; // Mark the first note
				}
			}

			// Determine selection end index
			if (toTicks <= curNoteStartTicks + note.duration) {
				// Range within a single note?
				if (fromTicks>curNoteStartTicks) {
					return new Note[]{}; // Empty selection
				}
				toIdx = idx; // Extend range to include the current note
			}
			
			// Advance cursor
			curNoteStartTicks += note.duration;
			if (curNoteStartTicks>toTicks) { // At end of selection range?
				break;
			}
		}
		if (fromIdx==-1 || toIdx==-1) return new Note[]{}; // Empty selection at end of phrase
		return Arrays.copyOfRange(notes, fromIdx, toIdx + 1);
	}
	
	@Override
  public int getDuration() {
	  return duration;
  }

	@Override
  public int hashCode() {
	  final int prime = 31;
	  int result = 1;
	  result = prime * result + Arrays.hashCode(notes);
	  return result;
  }

	@Override
  public boolean equals(Object obj) {
	  if (this == obj)
		  return true;
	  if (obj == null)
		  return false;
	  if (getClass() != obj.getClass())
		  return false;
	  Phrase other = (Phrase) obj;
	  if (!Arrays.equals(notes, other.notes))
		  return false;
	  return true;
  }
}
