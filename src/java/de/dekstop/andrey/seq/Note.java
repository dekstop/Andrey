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

public class Note implements ScoreElement {
	
	protected int pitch; // MIDI pitch, [0..127], 0 == pause
	protected int velocity; // MIDI velocity, [0..127]
	protected int duration; // In ticks
	
	public Note(int pitch, int velocity, int duration) {
	  this.pitch = pitch;
	  this.velocity = velocity;
	  this.duration = duration;
  }
	
	public int getPitch() {
		return pitch;
	}

	public void setPitch(int pitch) {
		this.pitch = pitch;
	}

	public int getVelocity() {
		return velocity;
	}

	public void setVelocity(int velocity) {
		this.velocity = velocity;
	}

	@Override
	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	@Override
  public int hashCode() {
	  final int prime = 31;
	  int result = 1;
	  result = prime * result + duration;
	  result = prime * result + pitch;
	  result = prime * result + velocity;
	  return result;
  }

	@Override
  public boolean equals(Object obj) {
	  if (this == obj)
		  return true;
	  if (obj == null)
		  return false;
	  if (getClass() != obj.getClass())
		  return false;
	  Note other = (Note) obj;
	  if (duration != other.duration)
		  return false;
	  if (pitch != other.pitch)
		  return false;
	  if (velocity != other.velocity)
		  return false;
	  return true;
  }

	@Override
  public String toString() {
	  return "Note [pitch=" + pitch + ", velocity=" + velocity + ", duration="
	      + duration + "]";
  }
}
