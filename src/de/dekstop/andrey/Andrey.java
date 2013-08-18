package de.dekstop.andrey;

/**
 * Andrey -- a MIDI note sequence generator based on Markov chains.
 * 
 * Martin Dittus 2013.
 * https://github.com/dekstop/Andrey
 */

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import themidibus.MidiBus;
import de.dekstop.andrey.play.Voice;
import de.dekstop.andrey.seq.Note;
import de.dekstop.andrey.seq.Phrase;
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
  static final int CTRL_RNG_BIAS = 1;
  static final int CTRL_RNG_RANGE = 5;
  
  // =========
  // = State =
  // =========
  
  float bpm = 75;

  BiasedRng rng = new BiasedRng();
  MidiBus midiBus;
  
  List<Voice> voices = new ArrayList<Voice>();  
  
  long lastTimeNanos = -1;
  long nowInTicks; // Current time in ticks, a steadily incremented cursor.
  
  // ========
  // = Main =
  // ========
  
  public void setup() {
    size(400, 400);
    background(0);
  
    MidiBus.list();
  //  midiBus = new MidiBus(this, -1, "IAC Bus 2");
  //  midiBus = new MidiBus(this, -1, "TAL-U-No-62-AU");
  //  midiBus = new MidiBus(this, "LPD8", "Java Sound Synthesizer");
    midiBus = new MidiBus(this, -1, "Java Sound Synthesizer");
    
//    voices = new Song04(rng, midiBus).getVoices();
    voices.add(new Voice(midiBus, 1, new Phrase(new Note[]{
    		new Note(45, 100, 1 * TICKS_PER_BEAT),
    		new Note( 0,   0, 1 * TICKS_PER_BEAT),
    		new Note(47,  70, 1 * TICKS_PER_BEAT),
    		new Note( 0,   0, 1 * TICKS_PER_BEAT),
    		new Note(48,  70, 1 * TICKS_PER_BEAT),
    		new Note( 0,   0, 1 * TICKS_PER_BEAT),
    		new Note(50,  70, 1 * TICKS_PER_BEAT),
    		new Note( 0,   0, 1 * TICKS_PER_BEAT),
    })));
  }
//  45, 0, 47, 0, 48, 0, 50, 0, 51, 0,  
//  47, 0, 48, 0, 50, 0, 51, 0, 
//  47, 0, 48, 0, 50, 0, 51, 0, 0,

//  100, 70, 70, 70,   

  public void draw() {
  	// Update cursor
    long timeNanos = System.nanoTime(); //System.currentTimeMillis();
    if (lastTimeNanos == -1) {
      lastTimeNanos = timeNanos;
    }
    nowInTicks += getElapsedTimeInTicks(lastTimeNanos, timeNanos);

    // Playback
    for (Voice voice : voices) {
    	voice.play(nowInTicks);
    }
    
    // Done.
    lastTimeNanos = timeNanos;
  }

	private int getElapsedTimeInTicks(long lastTimeNanos, long timeNanos) {
	  // Update tempo parameters
    float secondsPerBeat = 60f / bpm;
    float ticksPerSecond = secondsPerBeat / TICKS_PER_BEAT;

    // Calculate ticks delta
    float elapsedTimeInSeconds = ((timeNanos - lastTimeNanos) / (1000*1000*1000f));
	  return Math.round(elapsedTimeInSeconds / ticksPerSecond);
  }
  
	// =============
  // = Callbacks =
  // =============
  
  public void keyPressed() {
    if (key == ESC) {
      for (Voice voice : voices) {
        voice.stop();
      }
      midiBus.stop();
      exit();
    } 
    else if (key==' ') {
      for (Voice voice : voices) {
        println("Reset");
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