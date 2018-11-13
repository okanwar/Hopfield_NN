import java.lang.Integer;
import java.util.*;
import java.io.*;

/*
 *  Main
 *  
 *  Runs the training and classification of the perceptron net
 *  
 *  @author Michael Dana, Om Kanwar
 */
public class Main {

	public static void main(String[] args) {

		boolean quit = false;

		while (!quit) {
			// Run by prompts
			String mainPrompt = "---- Welcome to the Hopfield Nnet ----\n" + "(1) Train a new Hopeifld Net\n"
					+ "(2) Load net from file\n";

			// Get mode
			Scanner input = new Scanner(System.in);
			System.out.println(mainPrompt);
			int mode = input.nextInt();

			Hopfield hopfieldObject = new Hopfield();

			if (mode == 1) {
				// New net from scratch
				hopfieldObject.train();
				hopfieldObject.deploy();
			} else {
				// Load from file
				hopfieldObject.deploy();
			}

			// Run again?
			System.out.print("Enter 1 to quit or 0 to continue ");
			int userIn = input.nextInt();
			System.out.println();
			if (userIn == 1) {
				quit = true;
			}

		}
	}
}
