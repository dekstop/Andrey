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

import de.dekstop.andrey.seq.Phrase;

public class PhraseLoopGenerator extends AbstractPhraseGenerator {
	
	Phrase[] sequence;
	int idx = -1;
	
	public PhraseLoopGenerator(Phrase[] sequence) {
		this.sequence = sequence;
	}

	@Override
	protected Phrase getNextPhrase() {
		idx = (idx+1) % sequence.length;
		return sequence[idx];
	}

	@Override
  public void reset() {
	  super.reset();
	  idx = -1;
  }
}
