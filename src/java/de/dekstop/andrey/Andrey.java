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
package de.dekstop.andrey;

/**
 * Andrey -- a MIDI note sequence generator based on Markov chains.
 * 
 * Martin Dittus 2013.
 * https://github.com/dekstop/Andrey
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.LockSupport;

import processing.core.PApplet;
import themidibus.MidiBus;
import de.dekstop.andrey.play.PlaybackThread;
import de.dekstop.andrey.play.Voice;
import de.dekstop.andrey.seq.Note;
import de.dekstop.andrey.seq.Pause;
import de.dekstop.andrey.seq.Phrase;
import de.dekstop.andrey.seq.gen.NoteLoopGenerator;
import de.dekstop.andrey.seq.gen.PhraseLoopGenerator;
import de.dekstop.andrey.seq.gen.MCNoteGenerator;
import de.dekstop.andrey.seq.gen.MCPhraseGenerator;
import de.dekstop.andrey.util.BiasedRng;

public class Andrey extends PApplet {

  private static final long serialVersionUID = 2574723086754423775L;

  // ============
  // = Settings =
  // ============
  
  static final int TICKS_PER_BEAT = 96;
  
  // Channel IDs
  // static final int CH_CTRL = 0; // Receiving controller values
  static final int CH_DRUMS = 9; // 9 = GM percussion
  
  // Controller IDs
  static final int CTRL_PAN = 10;
  
  static final int CTRL_RNG_BIAS = 1;
  static final int CTRL_RNG_RANGE = 5;
  
  // =========
  // = State =
  // =========
  
  float bpm = 80;
  float secondsPerBeat = 60f / bpm;
  float ticksPerSecond = TICKS_PER_BEAT / secondsPerBeat;

  BiasedRng rng = new BiasedRng();
  MidiBus midiBus;
  
  PlaybackThread playbackThread;
  List<Voice> voices = new ArrayList<Voice>();  
  
  // ========
  // = Main =
  // ========
  
  public void setup() {
    size(400, 400);
    background(0);
//    frameRate(ticksPerSecond);
  
    MidiBus.list();
    midiBus = new MidiBus(this, -1, "IAC Bus 2");
//    midiBus = new MidiBus(this, -1, "TAL-U-No-62-AU");
//    midiBus = new MidiBus(this, "LPD8", "Java Sound Synthesizer");
//    midiBus = new MidiBus(this, -1, "Java Sound Synthesizer");

//  	midiBus.sendControllerChange(1, CTRL_PAN, 20); // Panning: mid left
//  	midiBus.sendControllerChange(2, CTRL_PAN, 100); // Panning: mid right
    
//    loadInstrument01();
    loadInstrument02();
    
    // Start playback thread
    playbackThread = new PlaybackThread(ticksPerSecond, voices);
    playbackThread.start();
    Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
    	public void run() {
    		playbackThread.stopPlayback();
    	}
    }));
  }

	public boolean stopped = false;

	void loadInstrument01() {
  	Note[] noteSequence = new Note[]{
    		new Note(45 + 12, 100, TICKS_PER_BEAT / 2),
    		new Pause(        TICKS_PER_BEAT / 4),
    		new Note(47 + 12,  70, TICKS_PER_BEAT / 4),
    		new Pause(        TICKS_PER_BEAT / 4),
    		new Note(48 + 12,  70, TICKS_PER_BEAT / 4),
    		new Pause(        TICKS_PER_BEAT / 4),
    		new Note(50 + 12,  70, TICKS_PER_BEAT / 4),
    		new Pause(        TICKS_PER_BEAT / 4),
    		new Note(45 + 24 + 12, 40, TICKS_PER_BEAT / 4),
    };
  	Phrase[] phraseSequence = new Phrase[]{
  		// bar 1
  			new Phrase(new Note[]{
  					new Note(45, 100, TICKS_PER_BEAT / 2),
  					new Pause(        TICKS_PER_BEAT / 4),
  			}),
  			new Phrase(new Note[]{
  					new Note(48,  70, TICKS_PER_BEAT / 4),
  					new Pause(        TICKS_PER_BEAT / 4),
  			}),
  			new Phrase(new Note[]{
  					new Note(47,  70, TICKS_PER_BEAT / 4),
  					new Pause(        TICKS_PER_BEAT / 4),
  			}),
  			new Phrase(new Note[]{
  					new Note(50,  70, TICKS_PER_BEAT / 4),
  					new Pause(        TICKS_PER_BEAT / 4),
  			}),
  			new Phrase(new Note[]{
  					new Note(45 + 0, 40, TICKS_PER_BEAT / 4),
  			}),
  		// bar 2
  			new Phrase(new Note[]{
  					new Note(45, 100, TICKS_PER_BEAT / 2),
  					new Pause(        TICKS_PER_BEAT / 4),
  			}),
  			new Phrase(new Note[]{
  					new Note(47,  70, TICKS_PER_BEAT / 4),
  					new Pause(        TICKS_PER_BEAT / 4),
  			}),
  			new Phrase(new Note[]{
  					new Note(48,  70, TICKS_PER_BEAT / 4),
  					new Pause(        TICKS_PER_BEAT / 4),
  			}),
  			new Phrase(new Note[]{
  					new Note(50,  70, TICKS_PER_BEAT / 4),
  					new Pause(        TICKS_PER_BEAT / 4),
  			}),
  			new Phrase(new Note[]{
  					new Note(45 + 36, 40, TICKS_PER_BEAT / 4),
  			}),
    };
  	voices.add(new Voice(midiBus, 1, new NoteLoopGenerator(noteSequence)));
//    voices.add(new Voice(midiBus, 1, new MCNoteGenerator(noteSequence, rng)));
//    voices.add(new Voice(midiBus, 1, new PhraseLoopGenerator(new Phrase[]{new Phrase(noteSequence)})));
  	voices.add(new Voice(midiBus, 2, new MCPhraseGenerator(phraseSequence, rng)));
	}
	
	void loadInstrument02() {
		Note kick_2 = new Note(45, 100, TICKS_PER_BEAT / 2);
		Note kick_4 = new Note(45, 90, TICKS_PER_BEAT / 4);
		Note kick_8 = new Note(45, 80, TICKS_PER_BEAT / 8);
		Note pause_2 = new Pause(TICKS_PER_BEAT / 2);
		Note pause_4 = new Pause(TICKS_PER_BEAT / 4);
		Note pause_8 = new Pause(TICKS_PER_BEAT / 8);

		Note[] sequence = new Note[]{
				kick_2, kick_2, pause_4, kick_4, kick_2, 
				kick_2, kick_8, pause_8, kick_8, kick_8, pause_4, kick_4, kick_8, kick_8, pause_4, 
		};

//  	voices.add(new Voice(midiBus, 1, new NoteLoopGenerator(sequence)));
  voices.add(new Voice(midiBus, 1, new MCNoteGenerator(sequence, rng)));
//  	voices.add(new Voice(midiBus, 1, new MCPhraseGenerator(phraseSequence, rng)));
	}
	
  void loadSongOld() {
//  	Phrase phrase1 = new Phrase(new Note[]{
//    		new Note(45, 100, TICKS_PER_BEAT / 4),
//    		new Pause(        TICKS_PER_BEAT / 4),
//    		new Note(47,  70, TICKS_PER_BEAT / 4),
//    		new Pause(        TICKS_PER_BEAT / 4),
//    		new Note(48,  70, TICKS_PER_BEAT / 4),
//    		new Pause(        TICKS_PER_BEAT / 4),
//    		new Note(50,  70, TICKS_PER_BEAT / 4),
//    		new Pause(        TICKS_PER_BEAT / 4),
//    });
//  	Phrase phrase2 = new Phrase(new Note[]{
//    		new Note(47, 100, TICKS_PER_BEAT / 4),
//    		new Pause(        TICKS_PER_BEAT / 4),
//    		new Note(48,  70, TICKS_PER_BEAT / 4),
//    		new Pause(        TICKS_PER_BEAT / 4),
//    		new Note(50,  70, TICKS_PER_BEAT / 4),
//    		new Pause(        TICKS_PER_BEAT / 4),
//    		new Note(51,  70, TICKS_PER_BEAT / 4),
//    		new Pause(        TICKS_PER_BEAT / 4),
//    });
//  	Phrase phrase3 = new Phrase(new Note[]{
//    		new Note(47, 100, TICKS_PER_BEAT / 4),
//    		new Pause(        TICKS_PER_BEAT / 4),
//    		new Note(48,  70, TICKS_PER_BEAT / 4),
//    		new Pause(        TICKS_PER_BEAT / 4),
//    		new Note(50,  70, TICKS_PER_BEAT / 4),
//    		new Pause(        TICKS_PER_BEAT / 2),
//    });
//  	
//		Phrase phrase4 = new Phrase(new Note[] {
//    		new Note(47, 100, TICKS_PER_BEAT / 4),
//    		new Pause(        TICKS_PER_BEAT / 4),
//    		new Note(48,  70, TICKS_PER_BEAT / 4),
//    		new Pause(        TICKS_PER_BEAT / 4),
//		});
//
//		Phrase pause2 = new Phrase(new Note[] {
//    		new Pause(        TICKS_PER_BEAT / 2),
//		});
//
//		Phrase[] sequence1 = new Phrase[] {
//  			phrase1, phrase2, phrase1, phrase2, 
//  			phrase1, phrase2, phrase1, phrase2, 
//  			phrase1, phrase2, phrase1, phrase3 
//  	};
//    voices.add(new Voice(midiBus, 2, new MCPhraseGenerator(sequence1, rng)));
//    midiBus.sendControllerChange(2, CTRL_PAN, 20); // Panning: mid left
//
//    Phrase[] sequence2 = new Phrase[] {
//    		pause2,
//    		phrase1, phrase4, phrase1, phrase4, 
//  			phrase1, phrase2, phrase1, phrase3,
//  	};
//    voices.add(new Voice(midiBus, 2, new LoopGenerator(sequence2)));
//    midiBus.sendControllerChange(2, CTRL_PAN, 100); // Panning: mid right
  } 

  public void draw() {
  	
  }
  
  public void stop() {
  	for (Voice voice : voices) {
    	voice.stopCurrentNote();
    }
  }
  
	// =============
  // = Callbacks =
  // =============
  
  public void keyPressed() {
    if (key == ESC) {
      for (Voice voice : voices) {
        voice.stopCurrentNote();
      }
      midiBus.stop();
      exit();
    } 
    else if (key==' ') {
      println("Reset");
      for (Voice voice : voices) {
        voice.reset();
      }
    }
  }
  
  public void noteOn(int channel, int pitch, int velocity) {
  }
  public void noteOff(int channel, int pitch, int velocity) {
  }
  
  public void controllerChange(int channel, int number, int value) {
    // Receive a controllerChange
    println();
    println("Controller Change:");
    println("--------");
    println("Channel:"+channel);
    println("Number:"+number);
    println("Value:"+value);
  
    if (number==CTRL_RNG_BIAS) {
      rng.setBias(value / 127f * 2 - 1);
    } 
    else if (number==CTRL_RNG_RANGE) {
      rng.setRange(value / 127f);
    }
  }
  
  // ============
  // = Launcher =
  // ============
  
  public static void main(String args[]) {
    PApplet.main(new String[] { "de.dekstop.andrey.Andrey" });
//    PApplet.main(new String[] { "--present", "de.dekstop.andrey.Andrey" });
  }
}