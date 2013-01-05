import java.util.HashSet;
import java.util.Set;

import themidibus.*;

MidiBus myBus;

int channel = 9; // 9 = GM percussion

float bpm = 134f;
long lastTimeMillis = -1;
float cursor = 0f;

MarkovChain mcn, mcv;
int lastNote, lastVelocity;

void setup() {
  size(400,400);
  background(0);

  MidiBus.list();
  myBus = new MidiBus(null, -1, "Java Sound Synthesizer");
  
  int[] notes = {
    // http://en.wikipedia.org/wiki/General_MIDI#Percussion
//    35, 42, 38, 42, 35, 42, 38, 42,   
    35, 42, 42, 42, 38, 42, 42, 42, 35, 42, 42, 42, 38, 42, 42, 42,  
  };
  mcn = new MarkovChain(notes);
  int[] velocities = {
    99, 50, 70, 50, 99, 50, 70, 50, 99, 50, 70, 50, 99, 50, 70, 50,   
  };
  mcv = new MarkovChain(velocities);
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

    int note = mcn.nextValue();
    int velocity = mcv.nextValue();

    // Note off
    if (lastNote != 0) {
      myBus.sendNoteOff(channel, lastNote, lastVelocity);
//      println("Note off: " + lastNote);
    }

    // Note on
    if (note > 0) {
      myBus.sendNoteOn(channel, note, velocity);
      println("Note on: " + note + " at velocity " + velocity);
    }

    lastNote = note;
    lastVelocity = velocity;
    lastTimeMillis = now;
  }
}


/**
 * Markov Chain Generator.
 * Limited to values between [0..127] (In a perfect world the Java standard library 
 * would have a sparse multi-dimensional matrix implementation.)
 */
class MarkovChain {
  
  Integer[] allPredecessors; // Set of all possible two-value sequences (encoded as 14-bit int)
  int w;
  float[][] p = new float[128*128][128];
  int lastValues = -1;
  
  public MarkovChain(int[] trainingData) {

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
        print((v1>>7) + ", " + (v1&127) + ": ");
        for (int v2=0; v2<128; v2++) {
          p[v1][v2] = c[v1][v2] / (float)sum;
          print(p[v1][v2] + " ");
        }
        println();
      }
    }
  }
  
  protected int randomPredecessorPair() {
    return allPredecessors[floor(random(allPredecessors.length))];
  }
  
  protected int randomSuccessor(int lastValues) {
    float r = random(1f);
//    println(r);
    int value = -1;
    float sum = 0;
    do {
      value += 1;
//      println(lastValues + " / " + value + " -> " + sum);
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

