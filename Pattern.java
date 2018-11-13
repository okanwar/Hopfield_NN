import java.util.*;

/*
 * Pattern
 * 
 * This object defines a pattern and gives access to the patterns data
 * 
 * @author Michael Dana, Om Kanwar
 */

public class Pattern {
	private int[] pattern;
	private int width, height;
	private boolean patternChanges;

	/*
	 * Constructor
	 * 
	 * @param inputPatternSize The size of the input pattern
	 * 
	 * @param outputPatternSize The size of the output pattern
	 */
	public Pattern(int patternSize) {
		pattern = new int[patternSize];
		width = 0;
		height = 0;
		patternChanges = false;
	}

	/*
	 * Constructor
	 * 
	 * @param patternString The pattern as a string
	 * 
	 * @param width The width of the pattern
	 * 
	 * @param height The height of the pattern
	 */
	public Pattern(String patternString, int width, int height) {
		pattern = new int[patternString.length()];
		this.width = width;
		this.height = height;
		patternChanges = false;
		setPattern(patternString);
	}

	/*
	 * setPattern - sets the patter
	 * 
	 * @param newPattern The pattern as an array
	 */
	public void setPattern(int[] newPattern) {
		this.pattern = newPattern;
	}

	/*
	 * setPattern - sets the pattern
	 * 
	 * @param newPattern The pattern as a string
	 */
	public void setPattern(String newPattern) {
		int length = newPattern.length();
		if (pattern != null && newPattern.length() == pattern.length) {
			for (int i = 0; i < pattern.length; i++) {
				if (newPattern.charAt(i) == 'o' || newPattern.charAt(i) == 'O' || newPattern.charAt(i) == '1') {
					pattern[i] = 1;
				} else {
					pattern[i] = -1;
				}
			}
		}
	}

	/*
	 * getPattern - Returns the pattern
	 * 
	 * @return The pattern as a string
	 */
	public int[] getPattern() {
		return pattern;
	}

	/*
	 * inputAt - Returns the input of the pattern at a given index
	 * 
	 * @param index The index of the requested input value
	 * 
	 * @return Returns the input value of the pattern at the requested index
	 */
	public int valueAt(int index) {
		return pattern[index];
	}

	/*
	 * getInputSize - returns the size of the input
	 * 
	 * @return Returns the size of the input
	 */
	public int getSize() {
		return pattern.length;
	}

	/*
	 * toString - returns the pattern as a string
	 * 
	 * @return A string that is the pattern
	 */
	public String toString() {
		String patternstr = "";
		for (int i = 0; i < pattern.length; i++) {
			if (i % width == 0) {
				patternstr += "\n";
			}
			if (pattern[i] == 1) {
				patternstr += 'o';
			} else {
				patternstr += ' ';
			}
		}

		return patternstr;
	}

	/*
	 * updatePatternAtIndex - updates the pattern at the index
	 * 
	 * @param index The index to update
	 * 
	 * @param newValue The value to update to
	 */
	public void updatePatternAtIndex(int index, int newValue) {
		try {
			if (pattern[index] != newValue)
				patternChanges = true;
			pattern[index] = newValue;
		} catch (Exception e) {
			System.out.println();
		}
	}

	/*
	 * getWidth - Gets the width
	 */
	public int getWidth() {
		return width;
	}

	/*
	 * setWidth - sets the width
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/*
	 * getHeight - gets the height
	 */
	public int getHeight() {
		return height;
	}

	/*
	 * setHeight - sets the height
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/*
	 * changed - returns a boolean whether the pattern has changed values
	 * 
	 * @return boolean indicating the pattern has changed
	 */
	public boolean changed() {
		return patternChanges;
	}

	public static boolean containsPattern(String str) {
		if (str.contains("O") || str.contains("o") || str.contains("1"))
			return true;
		else
			return false;
	}

}
