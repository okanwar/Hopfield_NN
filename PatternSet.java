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

	protected String dataFile;
	protected Pattern[] patternSet;
	protected int patternSize, patternWidth, patternHeight, numberOfPatterns;
	protected boolean setInitialized;
	protected float[][] weights;
	protected boolean setLoaded, weightsInitialized;
	
	/*
	 * Constructor
	 * @param file The file to initialize the pattern set from
	 * @param ps The perceptron settings file the perceptron net was initialized from
	 */
	public PatternSet(String file) {
		this.dataFile = file;
		this.patternSize = 0;
		this.patternWidth = 0;
		this.patternHeight = 0;
		this.numberOfPatterns = 0;
		this.patternSet = null;
		this.weights = null;
		this.setLoaded = false;
		this.weightsInitialized = true;

		//Create pattern set
		if (file != null)
			this.setLoaded = loadSetFromFile(dataFile);
		initializeWeights();
		this.setInitialized = this.setLoaded && this.weightsInitialized;

	}

	/*
	 * loadSetFromFile - Loads a set from file
	 * @return Returns a boolean indicating successful loading or not
	 */
	private boolean loadSetFromFile(String file) {
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
			
			//Tokinze first two lines
			StringTokenizer strtok = new StringTokenizer(lineOne, " ");
			if(strtok.hasMoreTokens()) patternSize = Integer.parseInt(strtok.nextToken());
			strtok = new StringTokenizer(lineTwo, " ");
			if(strtok.hasMoreTokens()) numberOfPatterns = Integer.parseInt(strtok.nextToken());


			// Setup set
			patternSet = new Pattern[numberOfPatterns];
			
			int countingPatternHeight = 0;
			int currentPatternSize = 0;


			// Begin reading samples
			while((line = reader.readLine()) != null){
				if(!line.isEmpty() && currentPatternSize != patternSize){
					if(patternWidth == 0){
						patternWidth = line.length();
					} 
					//Create pattern
					currentPattern += line.substring(0, patternWidth);
					currentPatternSize += patternWidth;
					countingPatternHeight++;
				} else {
					//Finished reading a pattern
					if(!currentPattern.isEmpty()){
						//Check pattern height
						if(patternHeight == 0){
							patternHeight = countingPatternHeight;
						} else {
							//Verify all patterns have the same height
							if(countingPatternHeight != patternHeight){
								System.out.println("Error pattern has a height of:" + countingPatternHeight+
								" but patterns have a height of " + patternHeight);
							}
						}
						countingPatternHeight = 0;

						//Create pattern
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
		patternSet[patternIndex] = new Pattern(currentPattern, patternWidth, patternHeight);

		for(int i = 0; i < patternSet.length; i++){
			System.out.println(patternSet[i].toString());
		}
		return true;
	}

	/*
	 * initializeWeights - Initializes the weights from net settings
	 */
	private void initializeWeights() {

		weights = new float[patternSize][patternSize];
		boolean successfulWeightInitialization = true;

		for(int i = 0; i < patternSize; i++){
			for(int j = 0; j < patternSize; j++){
				weights[i][j] = 0;
			}
		}

	}

	public void setWeights(float [][] newWeights){
		this.weights = newWeights;
	}

	public int getPatternSize(){
		return patternSize;
	}

	public Pattern getPattern(int patternIndex){
		return patternSet[patternIndex];
	}

	/*
	 * setWeightsFromFile - Initializes the weights from file
	 * @param weightsFile The file to initialize the weights from
	 * @return Returns a boolean indicating successful loading of weights or unsuccessful loading
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

			if(weights == null){
				weights = new float[patternSize][patternSize];
			}
			
			//Begin reading weights
			int rowIndex = 0;
			int columnIndex = 0;
			while ((line = reader.readLine()) != null) {
				//Parse line of weights
				StringTokenizer st = new StringTokenizer(line, " ");
				while(st.hasMoreTokens()){
					weights[columnIndex][rowIndex] = Float.parseFloat(st.nextToken());
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

	public String weightsToString(){
		String strweights = "";
		strweights += patternSize + "\n" + patternWidth + "\n" + patternHeight + "\n";
		for(int i = 0; i < patternSize; i++){
			for(int j = 0; j < patternSize; j++){
				strweights += String.format("%8s", weights[j][i] + " ");
			}
			strweights += "\n";
		}

		return strweights;
	}

	public void weightsToFile(String filename){
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(filename));
			writer.write(weightsToString());
			writer.close();
			
			System.out.println("\nSaved weights from training to: " + filename + "\n");
			
		} catch (Exception e) {
			System.out.println("Error printing weights to file. " + e);
		}
	}
	
	public void patternsToFile(String filename) {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(filename));
			for(int pattern = 0; pattern < numberOfPatterns; pattern++) {
				writer.write(patternSet[pattern].toString());
				writer.newLine();
			}
			writer.close();
			
			System.out.println("\nSaved patterns from set to: " + filename + "\n");
			
		} catch (Exception e) {
			System.out.println("Error printing patterns to file. " + e);
		}
	}

	public float[][] getWeights(){
		return weights;
	}

	public int getNumberOfPatterns(){
		return numberOfPatterns;
	}
}
