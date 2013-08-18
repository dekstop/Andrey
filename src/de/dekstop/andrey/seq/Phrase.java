package de.dekstop.andrey.seq;

import java.util.Arrays;

import de.dekstop.andrey.util.ArrayUtils;

/**
 * A sequence of notes and pauses.
 * 
 * @author martind
 *
 */
public class Phrase implements Generator {
	
	Note[] notes;
	int duration = 0;
	long startTicks = 0; // Absolute time in ticks (starts at an arbitrary count)
	long lastCursorTicks = 0; // Relative time in ticks (starts at 0, can be reset)
	
	public Phrase(Note[] notes) {
	  this.notes = notes;
	  for (Note note : notes) {
	  	this.duration += note.duration;
	  }
  }

	@Override
	public Note[] getNotes(long nowInTicks) {
		if (startTicks==0) startTicks = nowInTicks;
		long cursorTicks = nowInTicks - startTicks;
		
		int fromTicksOffset = (int)(lastCursorTicks % this.duration);
		int toTicksOffset = (int)(cursorTicks % this.duration);
		Note[] curNotes = selectNotes(fromTicksOffset, toTicksOffset);
		
		lastCursorTicks = cursorTicks;
		return curNotes;
	}
	
	/**
	 * Select a block of notes within the specified time range [fromTicks, toTicks[.
	 * The range can wrap around, so that toTicks < fromTicks.
	 * 
	 * @param fromTicks start of selection window in number of ticks, must be <= phrase duration
	 * @param toTicks start of selection window in number of ticks, must be <= phrase duration
	 * @return an empty array, or a sequence of one or more notes
	 */
	Note[] selectNotes(int fromTicks, int toTicks) {

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
		if (fromIdx==-1) throw new IllegalStateException(String.format(
				"Could not determine fromIdx! fromTicks (%d) may be longer than phrase duration (%d).",
				fromTicks, duration));
		if (toIdx==-1) throw new IllegalStateException(String.format(
				"Could not determine toIdx! toTicks (%d) may be longer than phrase duration (%d).",
				toTicks, duration));
		return Arrays.copyOfRange(notes, fromIdx, toIdx + 1);
	}
	
	/**
	 * 
	 * @param v
	 * @param a
	 * @param b
	 * @return true if v is in the range [a, b[, false otherwise
	 */
	boolean between(long v, long a, long b) {
		return a<=v && v<b;
	}

	@Override
	public void reset() {
		startTicks = 0;
		lastCursorTicks = 0;
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
