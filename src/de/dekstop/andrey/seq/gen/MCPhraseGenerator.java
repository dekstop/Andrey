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
import de.dekstop.andrey.seq.Phrase;
import de.dekstop.andrey.util.Rng;

public class MCPhraseGenerator extends AbstractPhraseGenerator {
	
	MarkovChain<Phrase> mc = null;
	Rng rng = null;
	Phrase firstPhrase = null;
	Phrase currentPhrase = null;
	
	public MCPhraseGenerator(Phrase[] trainingData, Rng rng) {
		this.mc = new MarkovChain<Phrase>(trainingData);
		this.rng = rng;
		firstPhrase = trainingData[0];
		currentPhrase = firstPhrase; // TODO: select a random phrase instead
	}

	@Override
	protected Phrase getNextPhrase() {
		currentPhrase = mc.nextValue(currentPhrase, rng);
		return currentPhrase;
	}

	@Override
  public void reset() {
	  super.reset();
	  currentPhrase = firstPhrase; // TODO: select a random phrase instead
  }
}
