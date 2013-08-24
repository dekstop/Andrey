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

import java.util.Iterator;

import de.dekstop.andrey.seq.Generator;
import de.dekstop.andrey.seq.Note;
import de.dekstop.andrey.seq.Phrase;

/**
 * Base class for {@link Generator} implementations that operate with sequences (Phrases). 
 * Emits Notes and Pauses from a chain of consecutive Phrases. 
 */
public abstract class AbstractPhraseGenerator extends AbstractGenerator {
	
	Iterator<Note> phraseIterator = null;

	@Override
	protected Note getNextNote() {
		while (phraseIterator==null || !phraseIterator.hasNext()) {
			phraseIterator = getNextPhrase().iterator();
		}
		return phraseIterator.next();
	}

	/**
	 * To be implemented by subclasses.
	 * @return the next Phrase in the sequence.
	 */
	protected abstract Phrase getNextPhrase();
	
}
