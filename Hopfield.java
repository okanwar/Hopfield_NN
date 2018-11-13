import java.lang.*;
import java.util.*;
import java.io.*;

/*
 * Hopfield 
 *
 * Defines a hopfield and the functions the hopfield uses to train and deploy on pattern set
 *
 *
 * @author Michael Dana, Om Kanwar
 *
 */

public class Hopfield {
	private PatternSet trainingData;
	private PatternSet testingData;

	public Hopfield() {
		trainingData = null;
		testingData = null;
	}

	/*
	 * train - Trains the net
	 */
	public void train() {
		// Get training file
		Scanner input = new Scanner(System.in);
		boolean failedtoload = true;
		while (failedtoload) {
			System.out.print("Enter the training file:");
			String trainingFile = input.next();
			System.out.println();
			trainingData = new PatternSet();
			failedtoload = !trainingData.loadSetFromFile(trainingFile);
		}

		// Create weight matrix by outerproduct of all patterns
		for (int j = 0; j < trainingData.getNumberOfPatterns(); j++) {
			// Compute outerproduct per pattern and add to weights
			int[][] outer = outerproduct(trainingData.getPattern(j));
			add(trainingData.getWeights(), outer);
		}

		// Get weights file
		System.out.print("Enter the weights file:");
		String weightsFile = input.next();
		System.out.println();
		trainingData.weightsToFile(weightsFile);
	}

	/*
	 * outerproduct - computes the outerproduct of the pattern
	 * 
	 * @param pattern The pattern the outerproduct is computed for
	 * 
	 * @return Returns a two 2d array representing the outerproduct
	 */
	private int[][] outerproduct(Pattern p) {
		int[][] outerproduct = new int[p.getSize()][p.getSize()];
		for (int i = 0; i < p.getSize(); i++) {
			for (int j = 0; j < p.getSize(); j++) {
				outerproduct[i][j] = p.valueAt(i) * p.valueAt(j);
			}
		}
		return outerproduct;
	}

	/*
	 * add - adds two matrices / 2d arrays, adds to mat1
	 * 
	 * @param mat1 The first matrix to add, added to
	 * 
	 * @param mat2 The second matrix to add
	 */
	private void add(int[][] mat1, int[][] mat2) {
		int i, j;
		i = j = 0;
		try {
			for (i = 0; i < mat1.length; i++) {
				for (j = 0; j < mat1[0].length; j++) {
					mat1[i][j] += mat2[i][j];
				}
			}
		} catch (Exception e) {
			System.out.println("Error accessing " + i + " " + j);
		}
	}

	/*
	 * compute - computes the activation and yin_i of a pattern values i
	 * 
	 * @param pattern The pattern the computation is done on
	 * 
	 * @param index The index of i
	 */
	public int compute(Pattern pattern, int index) {
		int y = 0;
		for (int j = 0; j < testingData.getNumberOfPatterns(); j++) {
			y += testingData.getWeights()[index][j] * pattern.valueAt(j);
		}

		if (y > 0.0f) {
			return 1;
		} else if (y == 0) {
			return pattern.valueAt(index);
		} else {
			return -1;
		}
	}

	/*
	 * deploy - deploys the net
	 */
	public void deploy() {
		// Get training file
		Scanner input = new Scanner(System.in);
		boolean failedtoload = true;
		while (failedtoload) {
			System.out.print("Enter the deployment file:");
			String deploymentFile = input.next();
			System.out.println();
			testingData = new PatternSet();
			failedtoload = !testingData.loadSetFromFile(deploymentFile);
		}
		
		//Get weights file
		failedtoload = true;
		while (failedtoload) {
		System.out.println("enter the weights file:");
		String weightsFile = input.next();
		System.out.println();
		failedtoload = !testingData.setWeightsFromFile(weightsFile);
		if (failedtoload)
			System.out.println("Failed to load file:" + weightsFile);
		}
		
		System.out.print("Enter the max amount of iterations:");
		int maxIterations = input.nextInt();
		System.out.println();

		int[] sequence = generateRandomSequence(testingData.getPatternSize());
		boolean converged = false;

		int iterationNumber = 0;
		for (int i = 0; i < testingData.getNumberOfPatterns(); i++) {
			while (!converged) {
				for (int j = 0; j < testingData.getPatternSize(); j++) {
					int calculatedVal = compute(testingData.getPattern(i), sequence[j]);
					testingData.getPattern(i).updatePatternAtIndex(sequence[j], calculatedVal);
				}
				// Check for convergence
				if (!testingData.getPattern(i).changed() || iterationNumber == maxIterations) {
					converged = true;
				}
				iterationNumber++;
			}
			iterationNumber = 0;
		}

		System.out.print("Enter the results file:");
		String resultsFile = input.next();
		System.out.println();
		testingData.patternsToFile(resultsFile);
	}

	/*
	 * generateRandomSequence - generates a random sequence given size, includes
	 * every number from 0 -> size-1
	 * 
	 * @param size The maximum range
	 * 
	 * @return returns and int array representing the sequence
	 */
	private int[] generateRandomSequence(int size) {
		Random rand = new Random();
		int[] randomSequence = new int[size];
		boolean[] setAlready = new boolean[size];
		int nextSequence = -1;

		for (int i = 0; i < size; i++) {
			nextSequence = rand.nextInt(size);
			while (setAlready[nextSequence] == true) {
				nextSequence = rand.nextInt(size);
			}
			setAlready[nextSequence] = true;
			randomSequence[i] = nextSequence;
		}
		return randomSequence;
	}
}
