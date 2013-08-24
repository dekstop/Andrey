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
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A sequence of notes and pauses.
 *
 */
public class Phrase implements ScoreElement, Iterable<Note> {
	
	/**
	 * 
	 */
	class PhraseIterator implements Iterator<Note> {
		
		Phrase phrase;
		int idx = -1;
		
		public PhraseIterator(Phrase phrase) {
			this.phrase = phrase;
		}

		@Override
    public boolean hasNext() {
	    return phrase.notes.length > 0 && idx < (phrase.notes.length-1);
    }

		@Override
    public Note next() {
			if (++idx >= phrase.notes.length) throw new NoSuchElementException();
	    return phrase.notes[idx];
    }

		@Override
    public void remove() {
	    throw new UnsupportedOperationException("Not implemented");
    }
		
	}
	
	Note[] notes;
	int duration = 0;
	
	public Phrase(Note[] notes) {
	  this.notes = notes;
	  for (Note note : notes) {
	  	this.duration += note.duration;
	  }
  }
	
	public int getLength() {
		return notes.length;
	}
	
	public Note getNote(int idx) {
		return notes[idx];
	}
	
	public Note[] getNotes() {
		return notes;
	}
	
	@Override
  public int getDuration() {
	  return duration;
  }

	@Override
  public Iterator<Note> iterator() {
	  return new PhraseIterator(this);
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

	@Override
  public String toString() {
	  return "Phrase [notes=" + Arrays.toString(notes) + ", duration=" + duration
	      + "]";
  }
}
