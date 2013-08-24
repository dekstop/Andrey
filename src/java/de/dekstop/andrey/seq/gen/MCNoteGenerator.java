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

import de.dekstop.andrey.markov.MarkovChain;
import de.dekstop.andrey.seq.Note;
import de.dekstop.andrey.util.Rng;

public class MCNoteGenerator extends AbstractGenerator {
	
	MarkovChain<Note> mc = null;
	Rng rng = null;
	Note firstNote = null;
	Note currentNote = null;
	
	public MCNoteGenerator(Note[] trainingData, Rng rng) {
		this.mc = new MarkovChain<Note>(trainingData);
		this.rng = rng;
		firstNote = trainingData[0];
	}

	@Override
	protected Note getNextNote() {
		if (currentNote==null) {
			currentNote = firstNote; // TODO: select a random phrase instead
		} else {
			currentNote = mc.nextValue(currentNote, rng);
		}
		return currentNote;
	}

	@Override
  public void reset() {
	  super.reset();
	  currentNote = null; // TODO: select a random phrase instead
  }
}
