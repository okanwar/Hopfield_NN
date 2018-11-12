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
     int numInputs;
     float[][] weights;
     float[] tempStorage;
     int[] inputCells;
     int trainingDataSize;
     PatternSet trainingData;
     PatternSet testingData;
     String trainData;
     String testData;

    public Hopfield(int numInputs) {
        //pass in numInputs here//
        inputCells = null;
        tempStorage = new float[numInputs];
        weights = null;
    }

    public void train() {
        //Get training file
        Scanner input = new Scanner(System.in);
        System.out.print("Enter the training file:");
        String trainingFile = input.next();
        System.out.println();
        trainingData = new PatternSet(trainingFile);
        weights = trainData.getWeights();


        for (int j = 1; j < numInputs; j++) {
            for (int i = 0; i < j; i++) {
                for (int n = 0; n < trainingData.getPatternSize(); n++) {
                    int[] data = trainingData.getPattern(j).getRow(n);
                    float temp1 = adjustInput((float)data[i]) * adjustInput((float)data[j]);
                    float temp = truncate(temp1 + weights[j][i]);
                    weights[i][j] = weights[j][i] = temp;
                }
            }
            for (int k = 0; k < numInputs; k++) {
                tempStorage[k] = 0.0f;
                for (int n = 0; n < k; n++) {
                    tempStorage[n] += weights[n][k];
                }
            }
        }

        trainingData.setWeights(weights);
        //Get weights file
        System.out.print("Enter the weights file:");
        String weightsFile = input.next();
        System.out.println();
        trainingData.weightsToFile(weightsFile);
    }
    private float adjustInput (float x) {
        if (x < 0.0f) return -1.0f;
        return 1.0f;
    }

    private float truncate(float x) {
        int i = (int) x;
        return (float) i;
    }
    public float compute(int index) {
        float temp = 0.0f;
        for (int j = 0; j < numInputs; j++) {
            temp += weights[index][j] * inputCells[j];
        }
        return 2.0f * temp - tempStorage[index];
    }

    public float[] deploy(float[] pattern, int numIterations) {
        //Get training file
        Scanner input = new Scanner(System.in);
        System.out.print("Enter the deployment file:");
        String deploymentFile = input.next();
        System.out.println();
        testingData = new PatternSet(deploymentFile);
        System.out.println("enter the weights file:");
        String weightsFile = input.next();
        System.out.println();
        testingData.setWeightsFromFile(weightsFile);
        weights = testingData.getWeights();


        for (int i = 0; i < numInputs; i++) inputCells[i] = testingData.getPattern(i);
            for (int ii = 0; ii < numIterations; ii++) {
                for (int i = 0; i < numInputs; i++) {
                    if(compute(i) > 0.0f) {
                        inputCells[i] = 1.0f;
                    } else if (inputCells[i] == 0) {
                        return inputCells;
                    } else {inputCells[i] = -1.0f;}
            }
        }
        return inputCells;
    }
}

