import java.util.*;

/*
 * Pattern
 * 
 * This object defines a pattern and gives access to the patterns data
 * 
 * @author Michael Dana, Om Kanwar
 */

public class Pattern{

	private int[] pattern;
	private int width, height;

	/*
	 * Constructor
	 * @param inputPatternSize The size of the input pattern
	 * @param outputPatternSize The size of the output pattern
	 */
	public Pattern(int patternSize){
		pattern = new int[patternSize];
	}

	public Pattern(String patternString, int width, int height){
		pattern = new int[patternString.length()];
		this.width = width;
		this.height = height;
		setPattern(patternString);
	}

	public void setPattern(int [] newPattern){
		this.pattern = newPattern;
	}

	public void setPattern(String newPattern){
		int length = newPattern.length();
		if(pattern != null && newPattern.length() == pattern.length){
			for(int i = 0; i < pattern.length; i++){
				if(newPattern.charAt(i) == ' '){
					pattern[i] = -1;
				} else {
					pattern[i] = 1;
				}
			}
		}
	}
	
	/*
	 * inputAt - Returns the input of the pattern at a given index
	 * @param index The index of the requested input value
	 * @return Returns the input value of the pattern at the requested index
	 */
	public int valueAt(int index){
		return pattern[index];
	}

	/*
	 * getInputSize - returns the size of the input
	 * @return Returns the size of the input
	 */
	public int getSize() {
		return pattern.length;
	}

	public String toString(){
		String patternstr = "";
		for(int i = 0; i < pattern.length; i++){
			if( i % width == 0 ){patternstr += "\n";}
			patternstr += pattern[i];
		}

		return patternstr;
	}
}
