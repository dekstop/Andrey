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
package de.dekstop.andrey.play;

import de.dekstop.andrey.seq.*;

import themidibus.*;

/**
 * Plays MIDI notes produced by a generator.
 * 
 * Note that this relies on frequent method calls to play(long):
 * * For stable timing, since a note will only ever played during a method call 
 *   (never before or after).
 * * To avoid skipped notes, since we're only ever playing the last note of the 
 *   currently selected sequence.
 */
public class Voice {

  MidiBus midiBus;
  int channel;
  Generator generator;
  Note curNote;
  long curNoteEndInTicks = -1;

  public Voice(MidiBus midiBus, int channel, Generator generator) {
    this.midiBus = midiBus;
    this.channel = channel;
    this.generator = generator;
  }

  public void play(long nowInTicks) {

  	// Note off
  	if (curNoteEndInTicks!=-1 && curNoteEndInTicks<=nowInTicks) {
  		stopCurrentNote();
  	}

    // Note on
    Note[] notes = generator.getNotes(nowInTicks);
    if (notes.length == 0) return;
    Note note = notes[notes.length-1]; // Select last note of current sequence, skip the others.
    
    if (note.getPitch() > 0) {
      midiBus.sendNoteOn(channel, note.getPitch(), note.getVelocity());
      System.out.println("Note on: " + note.getPitch() + " at velocity " + note.getVelocity());
    }

    curNote = note;
  	curNoteEndInTicks = nowInTicks + note.getDuration();
  }

  public void reset() {
  	stopCurrentNote();
    generator.reset();
  }

  public void stopCurrentNote() {
    if (curNote!=null && curNote.getPitch() != 0) {
      midiBus.sendNoteOff(channel, curNote.getPitch(), curNote.getVelocity());
  	  curNote = null;
    	curNoteEndInTicks = -1;
    }
  }
}
