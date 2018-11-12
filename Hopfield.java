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

	public void train() {
		// Get training file
		Scanner input = new Scanner(System.in);
		System.out.print("Enter the training file:");
		String trainingFile = input.next();
		System.out.println();
		trainingData = new PatternSet(trainingFile);

		// Create weight matrix by outerproduct of all patterns
		for (int j = 0; j < trainingData.getNumberOfPatterns(); j++) {
			// Compute outerproduct per pattern and add to weights
			add(trainingData.getWeights(), outerproduct(trainingData.getPattern(j)));
		}

		// Get weights file
		System.out.print("Enter the weights file:");
		String weightsFile = input.next();
		System.out.println();
		trainingData.weightsToFile(weightsFile);
	}

	private int[][] outerproduct(Pattern p) {
		int[][] outerproduct = new int[p.getSize()][p.getSize()];
		for (int i = 0; i < p.getSize(); i++) {
			for (int j = 0; j < p.getSize(); j++) {
				outerproduct[i][j] = p.valueAt(i) * p.valueAt(j);
			}
		}
		return outerproduct;
	}

	private void add(float[][] mat1, int[][] mat2) {
		for (int i = 0; i < mat1.length; i++) {
			for (int j = 0; j < mat1[0].length; j++) {
				mat1[i][j] += mat2[i][j];
			}
		}
	}

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

	public void deploy() {
		// Get training file
		Scanner input = new Scanner(System.in);
		System.out.print("Enter the deployment file:");
		String deploymentFile = input.next();
		System.out.println();
		testingData = new PatternSet(deploymentFile);
		System.out.println("enter the weights file:");
		String weightsFile = input.next();
		System.out.println();
		testingData.setWeightsFromFile(weightsFile);

		int[] sequence = generateRandomSequence(testingData.getPatternSize());
		boolean converged = false;

		for (int i = 0; i < testingData.getNumberOfPatterns(); i++) {
			while (!converged) {
				for (int j = 0; j < testingData.getPatternSize(); j++) {
					int calculatedVal = compute(testingData.getPattern(i), sequence[j]);
					testingData.getPattern(i).updatePatternAtIndex(sequence[j], calculatedVal);
				}
				//Check for convergence
				if(!testingData.getPattern(i).changed()) {
					converged = true;
				}
			}
		}

		System.out.print("Enter the results file:");
		String resultsFile = input.next();
		System.out.println();
		testingData.patternsToFile(resultsFile);
	}

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
