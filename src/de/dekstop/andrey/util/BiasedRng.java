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
package de.dekstop.andrey.util;

/**
 * Random number generator with variable bias (an offset of [-n/2 .. n2]) and range (a multiplier of [0 .. 1])
 */
public class BiasedRng implements Rng {
  float bias = 0.9f; // [-1 .. 1]
  float range = 0; // [0 .. 1]

  public void setBias(float bias) {
    this.bias = Math.max(-1f, Math.min(1f, bias));
    System.out.println("bias: " + this.bias);
  }

  public void setRange(float range) {
    this.range = Math.max(0f, Math.min(1f, range));
    System.out.println("range: " + this.range);
  }

  @Override
  public float value() {
    return value(1f);
  }

  @Override
  public float value(float max) {
    return (float)Math.random() * (Math.max(0f, Math.min(max, max/2 + bias * max/2 + max/2 * range)));
  }
}
