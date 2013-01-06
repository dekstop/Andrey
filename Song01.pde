import java.util.List;

import themidibus.*;

class Song01 {
  
  public void load(MidiBus midiBus, List<Voice> voices) {
    int[] velocities = {
      99, 50, 70, 50, 99, 50, 70, 50, 99, 50, 70, 50, 99, 50, 70, 50,   
    };
  
    int[] notes = { // 36: C1
    // F A C Eb = 41 45 36 39, or 29 33 24 27
      24,  0, 24, 24,
      33,  0,  0,  0,
      36,  0, 36,  0,
      27, 27, 39,  0,
  
       0, 24, 24, 24,
       0,  0,  0,  0,
      36,  0, 36, 27,
      27,  0,  0, 39,
    };
    voices.add(new Voice(midiBus, 10, new MarkovChain2(notes), new MarkovChain2(velocities)));
  //  midiBus.sendControllerChange(10, 10, 60); // Panning: mid
  
    voices.add(new Voice(midiBus, 12, new MarkovChain2(notes), new MarkovChain2(velocities)));
  //  midiBus.sendControllerChange(12, 10, 20); // Panning: mid-left
  
  //  voices.add(new Voice(midiBus, 13, new MarkovChain2(notes), new MarkovChain2(velocities)));
  //  midiBus.sendControllerChange(13, 10, 100); // Panning: mid-right
  
  //  if (1==1) return;
  
    // http://en.wikipedia.org/wiki/General_MIDI#Percussion
  
    // Kick 1, 2
    int[] kickNotes = {
      35,  0, 35,  0, 
      35,  0, 35, 35, 
      35,  0,  0, 35, 
      35, 36, 36, 36, 
  
      35,  0,  0,  0, 
      35,  0,  0,  0, 
      35,  0,  0,  0, 
      35,  0,  0,  0, 
    };
    voices.add(new Voice(midiBus, drumChannel, new MarkovChain2(kickNotes), new Sequence(velocities)));
    
    // Snare 1, Clap
    int[] snareNotes = {
       0,  0,  0,  0,
      38,  0,  0,  0,
       0,  0,  0,  0,
      39,  0,  0, 39,
  
       0,  0,  0,  0,
       0,  0,  0,  0,
       0,  0,  0,  0,
      39,  0,  0, 39,
    };
    voices.add(new Voice(midiBus, drumChannel, new Sequence(snareNotes), new Sequence(velocities)));
  
    // HH
    int[] hhNotes = {
      42, 42, 42, 42, 
      42,  0, 42, 42, 
      42, 42, 42, 42, 
      42, 42, 42, 46,
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

