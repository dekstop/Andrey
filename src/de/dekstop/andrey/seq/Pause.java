package de.dekstop.andrey.seq;

/**
 * A pause for a duration. Can be used in place of a note.
 * 
 */
public class Pause extends Note {

	public static final int PAUSE_PITCH = 0;

	public Pause(int duration) {
	  super(PAUSE_PITCH, 0, duration);
  }
}
