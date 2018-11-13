import java.io.*;
import java.util.*;
import java.lang.*;

/*
 * PatternSet
 * 
 * Defines a pattern set and functionality to create and read pattern sets. A pattern set contains many patterns which it manages.
 * 
 * @author Michael Dana, Om Kanwar
 */

public class PatternSet {

	protected Pattern[] patternSet;
	protected int patternSize, patternWidth, patternHeight, numberOfPatterns;
	protected boolean setInitialized;
	protected int[][] weights;

	/*
	 * Constructor
	 * 
	 * @param file The file to initialize the pattern set from
	 * 
	 * @param ps The perceptron settings file the perceptron net was initialized
	 * from
	 */
	public PatternSet() {
		this.patternSize = 0;
		this.patternWidth = 0;
		this.patternHeight = 0;
		this.numberOfPatterns = 0;
		this.patternSet = null;
		this.weights = null;

	}

	/*
	 * loadSetFromFile - Loads a set from file
	 * 
	 * @return Returns a boolean indicating successful loading or not
	 */
	public boolean loadSetFromFile(String file) {
		BufferedReader reader = null;
		String line = "";
		String currentPattern = "";
		int patternIndex = 0;

		// Parse set
		try {
			reader = new BufferedReader(new FileReader(file));
			int firstThreeLines = 0;

			// Read first two lines
			String lineOne = reader.readLine().trim();
			String lineTwo = reader.readLine().trim();

			// Tokinze first two lines
			StringTokenizer strtok = new StringTokenizer(lineOne, " ");
			if (strtok.hasMoreTokens())
				patternSize = Integer.parseInt(strtok.nextToken());
			strtok = new StringTokenizer(lineTwo, " ");
			if (strtok.hasMoreTokens())
				numberOfPatterns = Integer.parseInt(strtok.nextToken());

			// Setup set
			patternSet = new Pattern[numberOfPatterns];
			int currentPatternSize = 0;

			// Begin reading samples
			while ((line = reader.readLine()) != null) {
				if (!line.isEmpty() && currentPatternSize != patternSize && Pattern.containsPattern(line)) {
					if (patternWidth == 0) {
						patternWidth = line.length();
						patternHeight = patternSize / patternWidth;
					}
					// Create pattern
					currentPattern += createEqualLine(line);
					currentPatternSize += patternWidth;
				} else {
					// Finished reading a pattern
					if (!currentPattern.isEmpty()) {
						
						currentPattern = fillPattern(currentPattern);

						// Create pattern
						patternSet[patternIndex] = new Pattern(currentPattern, patternWidth, patternHeight);
						currentPattern = "";
						patternIndex++;
						currentPatternSize = 0;
					}
				}
			}
		} catch (FileNotFoundException f) {
			System.out.println("Could not create pattern set for file:" + file);
			return false;
		} catch (Exception e) {
			System.out.println("Error parsing file. " + e);
			return false;
		}
		if (!currentPattern.isEmpty())
			patternSet[patternIndex] = new Pattern(currentPattern, patternWidth, patternHeight);

		System.out.println("--- Patterns ---");
		for (int i = 0; i < patternSet.length; i++) {
			System.out.println(patternSet[i].toString());
		}
		System.out.println();
		initializeWeights();
		return true;
	}

	/*
	 * initializeWeights - Initializes the weights from net settings
	 */
	private void initializeWeights() {

		weights = new int[patternSize][patternSize];
		boolean successfulWeightInitialization = true;

		for (int i = 0; i < patternSize; i++) {
			for (int j = 0; j < patternSize; j++) {
				weights[i][j] = 0;
			}
		}

	}

	/*
	 * setWeights - Sets weights of the net with an array
	 * 
	 * @param newWeights New weights for the net
	 */
	public void setWeights(int[][] newWeights) {
		this.weights = newWeights;
	}

	/*
	 * getPatternSize
	 * 
	 * @return Returns the size of the pattern
	 */
	public int getPatternSize() {
		return patternSize;
	}

	/*
	 * getPattern - Returns the pattern at the index
	 * 
	 * @param patternIndex Index of the pattern to return
	 */
	public Pattern getPattern(int patternIndex) {
		return patternSet[patternIndex];
	}

	/*
	 * setWeightsFromFile - Initializes the weights from file
	 * 
	 * @param weightsFile The file to initialize the weights from
	 * 
	 * @return Returns a boolean indicating successful loading of weights or
	 * unsuccessful loading
	 */
	public boolean setWeightsFromFile(String weightsFile) {
		// Initialize weights from file
		BufferedReader reader = null;
		String line = "";
		try {
			reader = new BufferedReader(new FileReader(weightsFile));
			int patternSize = Integer.parseInt(reader.readLine().trim());
			int patternWidth = Integer.parseInt(reader.readLine().trim());
			int patternHeight = Integer.parseInt(reader.readLine().trim());

			if (patternSize != this.patternSize) {
				System.out.println("Mismatch pattern sizes.");
				return false;
			}
			if (weights == null) {
				weights = new int[patternSize][patternSize];
			}

			// Begin reading weights
			int rowIndex = 0;
			int columnIndex = 0;
			while ((line = reader.readLine()) != null) {
				// Parse line of weights
				StringTokenizer st = new StringTokenizer(line, " ");
				while (st.hasMoreTokens()) {
					weights[columnIndex][rowIndex] = Integer.parseInt(st.nextToken());
					columnIndex++;
				}
				columnIndex = 0;
				rowIndex++;
			}
		} catch (Exception e) {
			System.out.println("Error reading weights from file. " + e);
			return false;
		}

		return true;
	}

	/*
	 * weightsToString - Returns the weights as a string
	 * 
	 * @return Returns the weights as a string
	 */
	public String weightsToString() {
		String strweights = "";
		strweights += patternSize + "\n" + patternWidth + "\n" + patternHeight + "\n";
		for (int i = 0; i < patternSize; i++) {
			for (int j = 0; j < patternSize; j++) {
				strweights += String.format("%8s", weights[j][i] + " ");
			}
			strweights += "\n";
		}

		return strweights;
	}

	/*
	 * weightsToFile - writes the weights to file
	 * 
	 * @param filename Name of the file to write to
	 */
	public void weightsToFile(String filename) {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter((filename)));
			writer.write(weightsToString());
			writer.close();

		} catch (Exception e) {
			System.out.println("Error printing weights to file. " + e);
		}
	}

	/*
	 * patternsToFile - writes the patterns to file
	 * 
	 * @param filename The name of the file to write to
	 */
	public void patternsToFile(String filename) {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(filename));
			for (int pattern = 0; pattern < numberOfPatterns; pattern++) {
				writer.write(patternSet[pattern].toString());
				writer.newLine();
			}
			writer.close();

			System.out.println("\nSaved patterns from set to: " + filename + "\n");

		} catch (Exception e) {
			System.out.println("Error printing patterns to file. " + e);
		}
	}

	/*
	 * getWeights - Returns the weights as an array
	 * 
	 * @return A 2d array of the weights
	 */
	public int[][] getWeights() {
		return weights;
	}

	/*
	 * getNumberOfPatterns - Returns the number of patterns in the net
	 * 
	 * @return Int representing the number of patterns
	 */
	public int getNumberOfPatterns() {
		return numberOfPatterns;
	}
	
	private String createEqualLine(String line) {
		if(line.length() < patternWidth) {
			while(line.length() < patternSize){
				line += " ";
			}
			return line;
		} else if(line.length() > patternWidth) {
			return line.substring(0, patternWidth);
		} else {
			return line;
		}
	}
	
	private String fillPattern(String pattern) {
		if(pattern.length() < patternSize) {
			while(pattern.length() < patternSize) {
				pattern += " ";
			}
			return pattern;
		} else if(pattern.length() > patternSize) {
			return pattern.substring(0, patternSize);
		} else {
			return pattern;
		}
	}
}
