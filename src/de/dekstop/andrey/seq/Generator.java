package de.dekstop.andrey.seq;

/**
 * Produces sequences of Notes.
 */
public interface Generator { 

	public Note[] getNotes(long nowInTicks);
  public void reset();
}
