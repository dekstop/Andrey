package de.dekstop.andrey.play;

import de.dekstop.andrey.seq.*;

import themidibus.*;

/**
 * Encapsulates a note & velocity generator. Triggers MIDI notes.
 */
public class Voice {

  MidiBus midiBus;
  int channel;
  Generator noteGen;
  Generator velocityGen;
  int lastNote, lastVelocity;

  public Voice(MidiBus midiBus, int channel, Generator noteGen, Generator velocityGen) {
    this.midiBus = midiBus;
    this.channel = channel;
    this.noteGen = noteGen;
    this.velocityGen = velocityGen;
  }

  public void step() {
    int note = noteGen.nextValue();
    int velocity = velocityGen.nextValue();

    // Note off
    stop();

    // Note on
    if (note > 0) {
      midiBus.sendNoteOn(channel, note, velocity);
      //      println("Note on: " + note + " at velocity " + velocity);
    }

    lastNote = note;
    lastVelocity = velocity;
  }

  public void stop() {
    if (lastNote != 0) {
      midiBus.sendNoteOff(channel, lastNote, lastVelocity);
    }
  }

  public void reset() {
    noteGen.reset();
    velocityGen.reset();
  }
}
