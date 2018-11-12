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
	private boolean patternChanges;

	/*
	 * Constructor
	 * @param inputPatternSize The size of the input pattern
	 * @param outputPatternSize The size of the output pattern
	 */
	public Pattern(int patternSize){
		pattern = new int[patternSize];
		width = 0;
		height = 0;
		patternChanges = false;
	}

	public Pattern(String patternString, int width, int height){
		pattern = new int[patternString.length()];
		this.width = width;
		this.height = height;
		patternChanges = false;
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
	
	public int[] getPattern() {
		return pattern;
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

	public int[] getRow(int row){
		int [] returnRow = new int[width];
		int index = 0;
		for(int i = row*width; i < row*width + width; i++){
			returnRow[index] = pattern[i];
			index++;
		}
		return returnRow;
	}

	public String toString(){
		String patternstr = "";
		for(int i = 0; i < pattern.length; i++){
			if( i % width == 0 ){patternstr += "\n";}
			patternstr += pattern[i];
		}

		return patternstr;
	}

	public void updatePatternAtIndex(int index,int newValue){
		if(pattern[index] != newValue)
			patternChanges = true;
		pattern[index] = newValue;
	}
	
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	public boolean changed() {
		return patternChanges;
	}

}

