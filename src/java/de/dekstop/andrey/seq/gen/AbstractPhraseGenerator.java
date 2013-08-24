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
import java.util.Arrays;
import java.util.List;

import de.dekstop.andrey.seq.Generator;
import de.dekstop.andrey.seq.Note;
import de.dekstop.andrey.seq.Phrase;

/**
 * Base class for {@link Generator} implementations that operate with sequences (Phrases). 
 * Emits Notes and Pauses from a chain of consecutive Phrases. 
 */
public abstract class AbstractPhraseGenerator implements Generator {

	long startTicks = 0; // Absolute time in ticks (starts at an arbitrary count)
	long lastCursorTicks = 0; // Relative time in ticks (starts at 0, can be reset)
	
	Phrase currentPhrase = null;
	long phraseStartInTicks = 0;

	@Override
	public Note[] getNotes(long nowInTicks) {
		List<Note> notes = new ArrayList<Note>();

		// Relative cursor
		if (startTicks==0) startTicks = nowInTicks;
		long cursorTicks = nowInTicks - startTicks;
		
		// Initial phrase
		if (currentPhrase==null) {
			currentPhrase = getNextPhrase();
			phraseStartInTicks = cursorTicks;
		}
		
		// Fill note buffer from phrase sequence
		long selectionStartInTicks = lastCursorTicks;
		while (selectionStartInTicks < cursorTicks) {
			int fromPhraseTicks = (int)(selectionStartInTicks - phraseStartInTicks);
			int toPhraseTicks = (int)(cursorTicks - phraseStartInTicks);
//			System.out.println(String.format("%d -> %d / %d %d", selectionStartInTicks, cursorTicks, fromPhraseTicks, toPhraseTicks));
			
			notes.addAll(Arrays.asList(currentPhrase.selectNotes(fromPhraseTicks, toPhraseTicks)));
			selectionStartInTicks += Math.min(currentPhrase.getDuration(), toPhraseTicks) - fromPhraseTicks;

//			System.out.println((selectionStartInTicks<cursorTicks) + " " + (toPhraseTicks >= currentPhrase.getDuration()));
			if (selectionStartInTicks<cursorTicks && toPhraseTicks >= currentPhrase.getDuration()) { // Reached end of phrase?
				currentPhrase = getNextPhrase(); // Get next phrase
//				System.out.println("next phrase");
				phraseStartInTicks = selectionStartInTicks;
			}
		};
		
		lastCursorTicks = cursorTicks;
		return notes.toArray(new Note[notes.size()]);
	}

	@Override
	public void reset() {
		startTicks = 0;
		lastCursorTicks = 0;
		currentPhrase = null; // TODO: send note off; e.g. copy phrase/current note and shorten duration
		phraseStartInTicks = 0;
	}
	
	/**
	 * To be implemented by subclasses.
	 * @return the next Phrase in the sequence.
	 */
	protected abstract Phrase getNextPhrase();
}
