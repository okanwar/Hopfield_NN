import java.lang.*;
import java.util.*
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
     float[] inputCells;
     int trainingDataSize;
     PatternSet trainingData;
     PatternSet testingData;
     String trainData;
     String testData;

    public Hopfield(int numInputs) {


    }

    public void train() {
        for (int j = 1; j < numInputs; j++) {
            for (int i = 0; i < j; i++) {
                for (int n = 0; n < trainingDataSize.getPatternSize(); n++) {
                    float[] data = (float[]) trainingData.elementAt(n);
                    float temp1 = adjustInput(data[i]) * adjustInput(data[j]);
                    float temp = truncate(temp1 + weights[j][i]);
                    weights[i][j] = weights[j][i] = temp;
                }
            }
            for (int i = 0; i < numInputs; i++) {
                tempStorage[i] = 0.0f;
                for (int j = 0; j < i; j++) {
                    tempStorage[i] += weights[i][j];
                }
            }
        }
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
        for (int i = 0; i < numInputs; i++) inputCells[i] = pattern[i];
        for (int ii = 0; ii < numIterations; ii++) {
            for (int i = 0; i < numInputs; i++) {
                if(compute(i) > 0.0f) {
                    inputCells[i] = 1.0f;
                } else if (inputCells[i] = 0) {
                    return inputCells;
                } else {inputCells[i] = -1.0f;}
            }
        }
        return inputCells;
    }
}
