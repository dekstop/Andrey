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

	@Override
  public String toString() {
	  return "Pause [pitch=" + pitch + ", velocity=" + velocity + ", duration="
	      + duration + "]";
  }
}
