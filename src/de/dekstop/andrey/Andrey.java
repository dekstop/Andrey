package de.dekstop.andrey;

/**
 * Andrey -- a MIDI note sequence generator based on Markov chains.
 * 
 * Martin Dittus 2013.
 * https://github.com/dekstop/Andrey
 */

import java.util.ArrayList;
import java.util.List;

import de.dekstop.andrey.play.*;
import de.dekstop.andrey.songs.Song04;
import de.dekstop.andrey.util.*;


import processing.core.PApplet;
import themidibus.*;

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

  long lastTimeNanos = -1;
  float cursor = 0f;
  
  BiasedRng rng = new BiasedRng();
  
  List<Voice> voices = new ArrayList<Voice>();
  
  MidiBus midiBus;
  
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
  
    //  new Song01().load(midiBus, voices);
    //  new Song02().load(midiBus, voices);
    //  new Song02Perc().load(midiBus, voices);
    //  new Song03().load(midiBus, voices);
    //  new Song03Perc().load(midiBus, voices);
    new Song04(rng).load(midiBus, voices);
    //  new Song04Perc().load(midiBus, voices);
  }
  
  public void draw() {
    // Initialise timing parameters
    float beatDuration = 60f / bpm;
  
    // Update counter
    long now = System.nanoTime(); //System.currentTimeMillis();
    if (lastTimeNanos == -1) {
      lastTimeNanos = now;
    }
  
    float elapsedTimeInSeconds = ((now - lastTimeNanos) / (1000*1000*1000f));
    cursor += elapsedTimeInSeconds;
  
    // Play
    if (cursor > beatDuration) { // Next beat?
      while (cursor > beatDuration) cursor -= beatDuration; // Avoid overflow
  
      for (Voice voice : voices) {
        voice.step();
      }
  
      lastTimeNanos = now;
    }
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
    PApplet.main(new String[] { "Andrey" });
//    PApplet.main(new String[] { "--present", "Andrey" });
  }
}