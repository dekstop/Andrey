import java.util.HashSet;
import java.util.Set;

import themidibus.*;

MidiBus myBus;

int channel = 9; // 9 = GM percussion
int velocity = 100;

float bpm = 134f;
long lastTimeMillis = -1;
float cursor = 0f;

MarkovChain mc;
int lastNote;

void setup() {
  size(400,400);
  background(0);

  MidiBus.list();
  myBus = new MidiBus(null, -1, "Java Sound Synthesizer");
  
  int[] notes = {
    // http://en.wikipedia.org/wiki/General_MIDI#Percussion
    35, 42, 42, 42, 38, 42, 42, 42, 35, 42, 42, 42, 38, 42, 42, 42,  
  };
  mc = new MarkovChain(notes);
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
    while (cursor > beatDuration) cursor -= beatDuration;
    println("Pos: " + cursor);

    int note = mc.nextValue();

    // Note off
    if (lastNote != 0) {
      myBus.sendNoteOff(channel, lastNote, velocity);
      println("Note off: " + lastNote);
    }

    // Note on
    if (note > 0) {
      myBus.sendNoteOn(channel, note, velocity);
      println("Note on: " + note);
    }

    lastNote = note;
    lastTimeMillis = now;
  }
}


/**
 * Markov Chain Generator.
 * Limited to values between [0..127]
 */
class MarkovChain {
  
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
      println("Selecting random value: " + value);
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

