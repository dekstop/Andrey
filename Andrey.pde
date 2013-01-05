import themidibus.*;

MidiBus myBus;

float bpm = 134f;

int channel = 9; // 9 = GM percussion
int velocity = 100;

long lastTimeMillis = -1;
float cursor = 0f;

int[] notes = {
  35, 0, 0, 0, 38, 0, 0, 0, 35, 0, 0, 0, 38, 0, 0, 0,  
};
int lastNoteIdx = -1;

void setup() {
  size(400,400);
  background(0);

  MidiBus.list();
  myBus = new MidiBus(null, -1, "Java Sound Synthesizer");
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
  while (cursor > beatDuration * notes.length) {
    cursor -= beatDuration * notes.length;
  }

  int noteIdx = floor(cursor / beatDuration);
  
  // Play
  if (lastNoteIdx != noteIdx) { // Next beat?
    println("Pos: " + cursor);
    println("Note index: " + noteIdx);

    // Note off
    if (lastNoteIdx != -1) {
      myBus.sendNoteOff(channel, notes[lastNoteIdx], velocity);
      println("Note off: " + notes[lastNoteIdx]);
    }

    // Note on
    if (notes[noteIdx] > 0) {
      myBus.sendNoteOn(channel, notes[noteIdx], velocity);
      println("Note on: " + notes[noteIdx]);
    }

    lastNoteIdx = noteIdx;
    lastTimeMillis = now;
  }
}

