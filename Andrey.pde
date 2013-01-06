
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import themidibus.*;

MidiBus midiBus;

int drumChannel = 9; // 9 = GM percussion

float bpm = 75;
long lastTimeMillis = -1;
float cursor = 0f;

List<Voice> voices = new ArrayList<Voice>();

void setup() {
  size(400,400);
  background(0);

  MidiBus.list();
  midiBus = new MidiBus(null, -1, "IAC Bus 2");
//  midiBus = new MidiBus(null, -1, "Java Sound Synthesizer");
  
//  new Song01().load(midiBus, voices);
//  new Song02().load(midiBus, voices);
  new Song02Perc().load(midiBus, voices);
}

void draw() {
  // Initialise timing parameters
  float beatDuration = 60f / bpm;

  // Update counter
  long now = System.currentTimeMillis();
  if (lastTimeMillis == -1) {
    lastTimeMillis = now;
  }
  
  float elapsedTimeInSeconds = ((now - lastTimeMillis) / 1000f);
  cursor += elapsedTimeInSeconds;

  // Play
  if (cursor > beatDuration) { // Next beat?
    while (cursor > beatDuration) cursor -= beatDuration; // Avoid overflow

    for (Voice voice : voices) {
      voice.step();
    }
    
    lastTimeMillis = now;
  }
}

void keyPressed() {
  if (key == ESC) {
    for (Voice voice : voices) {
      voice.stop();
    }
    midiBus.stop();
    exit();
  }
}

/**
 * Encapsulates a note & velocity generator. Triggers MIDI notes.
 */
class Voice {
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
      println("Note on: " + note + " at velocity " + velocity);
    }

    lastNote = note;
    lastVelocity = velocity;
  }
  
  public void stop() {
    if (lastNote != 0) {
      midiBus.sendNoteOff(channel, lastNote, lastVelocity);
    }
  }
}

/**
 * Produces sequences of ints.
 */
interface Generator { 
  public int nextValue();
}

/**
 * Repeats a fixed sequence.
 */
class Sequence implements Generator {
  
  int[] seq;
  int curIdx = -1;
  
  public Sequence(int[] values) {
    seq = values;
  }
  
  public int nextValue() {
    curIdx++;
    if (curIdx>=seq.length) curIdx = 0;
    return seq[curIdx];
  }
}

/**
 * First-order Markov Chain Generator.
 */
class MarkovChain implements Generator {
  
  Set<Integer> allValues = new HashSet<Integer>();
  int w;
  float[][] p = new float[128][128];
  int lastValue = -1;
  
  public MarkovChain(int[] trainingData) {

    // Count successions
    int[][] c = new int[128][128];
    for (int idx = 0; idx<trainingData.length; idx++) {
      int prevIdx = idx - 1;
      if (prevIdx < 0) { prevIdx += trainingData.length; }
      allValues.add(trainingData[idx]);
      c[trainingData[prevIdx]][trainingData[idx]] +=  1;
    }
    
    // Probability map
    for (int v1=0; v1<128; v1++) {
      int sum = 0;
      for (int v2=0; v2<128; v2++) {
        sum += c[v1][v2];
      }
      if (sum > 0) {
        for (int v2=0; v2<128; v2++) {
          p[v1][v2] = c[v1][v2] / (float)sum;
        }
      }
    }
  }
  
  public int nextValue() {
    int value;
    if (lastValue == -1) {
      value = (Integer)allValues.toArray()[floor(random(allValues.size()))];
    } else {
      float r = random(1f);
      value = -1;
      float sum = 0;
      do {
        value += 1;
        sum += p[lastValue][value];
      } while (sum < r);
    }
    lastValue = value;
    return value;
  }
}

/**
 * Second-order Markov Chain Generator.
 * Limited to values between [0..127]
 */
class MarkovChain2 implements Generator {
  
  Integer[] allPredecessors; // Set of all possible two-value sequences (encoded as 14-bit int)
  int w;
  float[][] p = new float[128*128][128];
    // In a perfect world the Java standard library would have a sparse multi-dimensional matrix implementation.
  int lastValues = -1;
  
  public MarkovChain2(int[] trainingData) {

    // Count successions
    Set<Integer> allPredecessors = new HashSet<Integer>();
    int[][] c = new int[128*128][128];
    int prev = trainingData[trainingData.length-2];
    for (int idx = 0; idx<trainingData.length; idx++) {
      int prevIdx = idx - 1;
      if (prevIdx < 0) { prevIdx += trainingData.length; }
      // Concatenate two previous values as 14-bit key:
      prev = ((prev << 7) + trainingData[prevIdx]) & 0x3F7F;
      allPredecessors.add(prev);
      c[prev][trainingData[idx]] +=  1;
    }
    
    this.allPredecessors = allPredecessors.toArray(new Integer[]{});
    
    // Probability map
    for (int v1=0; v1<128*128; v1++) {
      int sum = 0;
      for (int v2=0; v2<128; v2++) {
        sum += c[v1][v2];
      }
      if (sum > 0) {
        for (int v2=0; v2<128; v2++) {
          p[v1][v2] = c[v1][v2] / (float)sum;
        }
      }
    }
  }
  
  protected int randomPredecessorPair() {
    return allPredecessors[floor(random(allPredecessors.length))];
  }
  
  protected int randomSuccessor(int lastValues) {
    float r = random(1f);
    int value = -1;
    float sum = 0;
    do {
      value += 1;
      sum += p[lastValues][value];
    } while (sum < r);
    return value;
  }
  
  public int nextValue() {
    if (lastValues == -1) {
      lastValues = randomPredecessorPair();
      println("Randomly selecting previous values: " + (lastValues>>7) + ", " + (lastValues&127));
    }

    int value = randomSuccessor(lastValues);
    lastValues = ((lastValues << 7) + value) & 0x3F7F; // 14 bits
    return value;
  }
}

