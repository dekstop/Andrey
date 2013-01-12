import java.util.List;

import themidibus.*;

class Song03Perc {
  
  public void load(MidiBus midiBus, List<Voice> voices) {
    int[] velocities = {
      100, 60, 40, 20,   
    };
  
    // http://en.wikipedia.org/wiki/General_MIDI#Percussion
  
    // HH
    int[] hhNotes = {
      60, 60, 60, 60, 
      60,  0, 60, 60, 
      60, 60, 60, 60, 
      60, 60, 60, 61,
    }; 
    voices.add(new Voice(midiBus, 1, 
      new MarkovChain2(hhNotes), 
      new Sequence(new int[]{50, 20, 30, 20})));
  
    // Kick 1/2
    int[] kickNotes = {
      48,  0,  0,  0, 
      48,  0,  0,  0, 
      48,  0,  0,  0, 
      48, 50, 50, 50, 
      48,  0,  0,  0, 
      48, 50,  0,  0, 
      48,  0,  0,  0, 
      48, 50, 50, 50, 
    };
    voices.add(new Voice(midiBus, 1, 
      new MarkovChain2(kickNotes), 
      new Sequence(velocities)));

    // Snare 1/2
    int[] snNotes = {
      0,  0,  52,  0,
      0,  0,   0,  0,   
      0,  0,  53,  0,  
      0,  0,   0,  0,   
    };
    voices.add(new Voice(midiBus, 1, 
      new MarkovChain2(snNotes), 
      new Sequence(velocities)));
  }
}

