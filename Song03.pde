import java.util.List;

import themidibus.*;

class Song03 {
  
  public void load(MidiBus midiBus, List<Voice> voices) {
    int[] velocities = {
      100, 70, 70, 70,   
    };
  
//  48,49,51,52,54,55,56,58,60,61,63,64,66,67,68,70,72,73,75,76,78,79,80,82,84,85,87,88,90,91,92,94 // Blues
//  51,53,55,56,57,58,59,60,62,63,65,67,68,69,70,71,72,74,75,77,79,80,81,82,83,84,86,87,89,91,92,93 // Minor
//  45,47,48,50,51,53,55,57,59,60,62,63,65,67,69,71,72,74,75,77,79,81,83,84,86,87,89,91,93,95,96,98 // JazzMinor

    int[] v1Notes = {
      45, 0, 47, 0, 48, 0, 50, 0, 51, 0,  
      47, 0, 48, 0, 50, 0, 51, 0, 
      47, 0, 48, 0, 50, 0, 51, 0, 
    }; 
    voices.add(new Voice(midiBus, 12, new MarkovChain2(v1Notes), new Sequence(velocities)));
    midiBus.sendControllerChange(12, 10, 20); // Panning: mid-left

    int[] v2Notes = {
      0, 89, 0, 91, 0, 93, 
      0, 89, 0, 91, 0,  0,
      0,  0, 0, 91, 0, 93,
      0, 93, 0, 91, 0, 89, 
    }; 
    voices.add(new Voice(midiBus, 13, new MarkovChain2(v2Notes), new Sequence(velocities)));
    midiBus.sendControllerChange(13, 10, 100); // Panning: mid-right

    int[] v3Notes = {
      69, 0, 65, 65, 0, 67, 67, 0, 69,  
    }; 
    voices.add(new Voice(midiBus, 11, new Sequence(v3Notes), new Sequence(new int[]{30, 0, 50})));
  }
}

