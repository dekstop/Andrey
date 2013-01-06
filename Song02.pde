import java.util.List;

import themidibus.*;

class Song02 {
  
  public void load(MidiBus midiBus, List<Voice> voices) {
    
    // Chords
    // F#3 A3  C4  E4
    // F3  A3  C4  D#4
    // 66 69 72 76
    // 65 69 72 75
    
    int[] velocities = {
      99, 50, 70, 50, 99, 50, 70, 50, 99, 50, 70, 50, 99, 50, 70, 50,   
    };
  
    int[] baseNotes = {
      69,  0, 72,  0,
      69,  0, 72,  0,
       0, 69,  0,  72,
      69,  0,  0,  72,
    };
    voices.add(new Voice(midiBus, 1, 
      new MarkovChain(baseNotes), 
      new Sequence(velocities)));

    int[] voice1Notes = {
      66, 66,  0,
      66,  0,  0,
       0,  0,  0,
       0,  0, 66,
      65, 65, 65,
      65,  0,  0,
      65,  0,  0,
       0,  0,  0,
    };
    voices.add(new Voice(midiBus, 1, 
      new Sequence(voice1Notes), 
      new MarkovChain(new int[]{80, 90, 0, 0, 10, 90, 10, 80, 0, })));
    
    int[] voice2Notes = {
      75, 75,  0, 75,
       0,  0,  0, 0,
       0,  0,  0, 0,
       0,  0,  0, 0,
//       0,  0,  0, 0,
//       0,  0,  0, 0,
//       0,  0,  0, 0,
////      76, 76, 76, 76,
//       0,  0,  0, 0,
//       0,  0,  0, 0,
//       0,  0,  0, 0,
//       0,  0,  0, 0,
    };
    voices.add(new Voice(midiBus, 1, 
      new MarkovChain2(voice2Notes), 
      new MarkovChain2(velocities)));
  }
}

