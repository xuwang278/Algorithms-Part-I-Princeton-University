package assign2;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * 
 * @author Xu Wang
 * 
 *         04/01/2017
 * 
 *         A client that takes a command-line integer k; reads in a sequence of
 *         strings from standard input using StdIn.readString(); and prints
 *         exactly k of them, uniformly at random. Print each item from the
 *         sequence at most once.
 *
 */
public class Permutation {

	public static void main(String[] args) {
		int sizeOfOutput = Integer.parseInt(args[0]);
		RandomizedQueue<String> randomQueue = new RandomizedQueue<String>();

		String input;
		while (!StdIn.isEmpty()) {
			input = StdIn.readString();
			randomQueue.enqueue(input);
		}

		String output;
		for (int i = 0; i < sizeOfOutput; i++) {
			output = randomQueue.dequeue();
			StdOut.println(output);
		}

	}

}
