package de.dekstop.andrey.util;

import java.util.Arrays;

public class ArrayUtils {

	/**
	 * http://stackoverflow.com/questions/80476/how-to-concatenate-two-arrays-in-java
	 * @param first
	 * @param second
	 * @return
	 */
	public static <T> T[] concat(T[] first, T[] second) {
	  T[] result = Arrays.copyOf(first, first.length + second.length);
	  System.arraycopy(second, 0, result, first.length, second.length);
	  return result;
	}
}
