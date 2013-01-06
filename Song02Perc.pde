import java.util.List;

import themidibus.*;

class Song02Perc {
  
  public void load(MidiBus midiBus, List<Voice> voices) {
    int[] velocities = {
      99, 50, 70, 50, 99, 50, 70, 50, 99, 50, 70, 50, 99, 50, 70, 50,   
    };
  
    // http://en.wikipedia.org/wiki/General_MIDI#Percussion
  
    // HH
    int[] hhNotes = {
      70, 70, 70, 70, 
      70,  0, 70, 70, 
      70, 70, 70, 70, 
      70, 70, 70, 69,
    }; 
    voices.add(new Voice(midiBus, drumChannel, new MarkovChain2(hhNotes), new Sequence(new int[]{50, 20, 30, 20})));
  
    // Low wood block + claves
    int[] percNotes = {
      77,  0,  0,  0, 
      77,  0,  0,  0, 
      77,  0,  0,  0, 
      77, 75, 75, 75, 
      77,  0,  0,  0, 
      77, 75,  0,  0, 
      77,  0,  0,  0, 
      77, 75, 75, 75, 
    };
    voices.add(new Voice(midiBus, drumChannel, new MarkovChain2(percNotes), new Sequence(velocities)));
  }
}

