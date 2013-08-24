package de.dekstop.andrey.play;

import java.util.List;
import java.util.concurrent.locks.LockSupport;

public class PlaybackThread extends Thread {
	
	float ticksPerSecond;
	List<Voice> voices;
	
  long lastTimeNanos = -1;
  long nowInTicks; // Current time in ticks, a steadily incremented cursor.
  
  boolean stopped = false;
	
	public PlaybackThread(float ticksPerSecond, List<Voice> voices) {
		this.ticksPerSecond = ticksPerSecond;
		this.voices = voices;
	}
	
	public void stopPlayback() {
		stopped = true;
	}

	@Override
	public void run () {
		long cycleDuration = Math.round((1f / ticksPerSecond) * 1000*1000*1000f);
		long last = System.nanoTime();
		while (!stopped) {
			play();
			long now = System.nanoTime();
			long delayNanos = cycleDuration - (now-last);
			if (delayNanos > 0) LockSupport.parkNanos(delayNanos);
			last = now;
		}
	}
	
	void play() {
  	// Update cursor
    long timeNanos = System.nanoTime(); //System.currentTimeMillis();
    if (lastTimeNanos == -1) {
      lastTimeNanos = timeNanos;
    }
    int elapsedTimeInTicks = getElapsedTimeInTicks(lastTimeNanos, timeNanos);
    nowInTicks += elapsedTimeInTicks;

    // Playback
    for (Voice voice : voices) {
    	voice.play(nowInTicks);
    }
    
    // Done.
    lastTimeNanos = timeNanos;
  }

	int getElapsedTimeInTicks(long lastTimeNanos, long timeNanos) {
    // Calculate ticks delta
    float elapsedTimeInSeconds = ((timeNanos - lastTimeNanos) / (1000*1000*1000f));
	  return Math.round(elapsedTimeInSeconds * ticksPerSecond);
  }
  
}
