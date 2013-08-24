/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
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
