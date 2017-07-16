package assign4_8Puzzle;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;

/**
 * @author Xu Wang
 * 
 *         07/04/2017
 * 
 *         a representation of 8 puzzle game board
 */
public class Board {
	private int n; // dimension of game board
	/*
	 * A 2d array representation of blocks on board, where blocks[i][j]
	 * indicates the number on block in row i, column j.
	 */
	private int[][] nodes;

	/**
	 * construct a board from an n-by-n array of blocks
	 * 
	 * @param blocks initial block pattern on board
	 */
	public Board(int[][] blocks) {
		if (blocks == null)
			throw new IllegalArgumentException();
		n = blocks.length;
		nodes = new int[n][n];
		// copy from blocks
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				nodes[i][j] = blocks[i][j];
	}

	/**
	 * tell the dimension of game board, e.g., dimension of a n-by-n board is n.
	 * 
	 * @return dimension of game board
	 * 
	 */
	public int dimension() {
		return n;
	}

	/**
	 * indicate Hamming distance of a Board: number of blocks in the wrong
	 * position
	 * 
	 * @return Hamming distance of Board
	 */
	public int hamming() {
		int count = 0;
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				if (nodes[i][j] != 0 && nodes[i][j] != goal(i, j))
					count++;
		return count;
	}

	/*
	 * helper function:
	 * 
	 * @return number that is supposed to be on the goal board
	 * 
	 * For a 3-by-3 board, board[0][0] = 1, board[0][1] = 2, board[0][2] = 3,
	 * board[1][0] = 4 ... board[2][1] = 8, board[2][2] = 0 (a blank square)
	 */
	private int goal(int i, int j) {
		if (i == n - 1 && j == n - 1)
			return 0;
		return i * n + j + 1;
	}

	/**
	 * indicate Manhattan distance of a Board: sum of the vertical and
	 * horizontal distance from the blocks to their goal positions
	 * 
	 * @return Manhattan distance of a Board
	 */
	public int manhattan() {
		int count = 0;
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				if (nodes[i][j] != 0 && nodes[i][j] != goal(i, j)) {
					// calculate which row a block should be in goal board
					int row;
					if (nodes[i][j] % n != 0) // special case
						row = nodes[i][j] / n;
					else
						row = nodes[i][j] / n - 1;

					// calculate which column a block should be in goal board
					int column;
					if (nodes[i][j] % n != 0) // special case
						column = nodes[i][j] % n - 1;
					else
						column = n - 1;

					int distance = Math.abs(row - i) + Math.abs(column - j);
					count += distance;
				}
		// just distance instead of priority (distance + moves made so far)
		return count;

	}

	/**
	 * check if this board is the goal board
	 * 
	 * @return true if the block pattern of this board is same as that of goal
	 *         board, else false
	 */
	public boolean isGoal() {
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				if (nodes[i][j] != goal(i, j))
					return false;
		return true;
	}

	/**
	 * create a twin board by exchanging the first pair of blocks that are not
	 * blank on this board
	 * 
	 * @return twin board to this board
	 */
	public Board twin() {
		/*
		 * As Board is immutable, it's not recommended to create twin by copying
		 * this board, then modify its nodes field.
		 * 
		 * Here, a twin nodes is created, by which the twin board is
		 * initialized.
		 */

		int[][] nodesTwin = new int[n][n];
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				nodesTwin[i][j] = nodes[i][j];

		int x = 0, y = 0, p = 0, q = 0; // exchange nodes[x][y] with nodes[p][q]
		int value = 0; // record the value of 1st block

		// to find 1st block position
		outerLoop: for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++)
				if (nodesTwin[i][j] != 0) {
					value = nodesTwin[i][j];
					// record down the position of the block
					x = i;
					y = j;
					break outerLoop; // jump out of loop as long as it is found
				}
		}

		// find next block position
		outerLoop: for (int i = x; i < n; i++) {
			for (int j = y; j < n; j++)
				if (nodesTwin[i][j] != 0 && nodesTwin[i][j] != value) {
					p = i;
					q = j;
					break outerLoop;
				}
		}

		/*
		 * exchange the two blocks
		 * 
		 * trick: As int is premitive values not a reference, so method exch(int
		 * a, int b) has no side effect on nodesTwin.
		 * 
		 */
		exch(nodesTwin, x, y, p, q);
		return new Board(nodesTwin); // create twin board using twin board
	}

	// exchange two blocks on a board
	private void exch(int[][] nodes, int x, int y, int p, int q) {
		int temp = nodes[x][y];
		nodes[x][y] = nodes[p][q];
		nodes[p][q] = temp;
	}

	/**
	 * predict if Object y is the same thing as this board by comparing their
	 * their nodes content.
	 * 
	 * @return true if Object y is the same thing as this board, else false
	 */
	public boolean equals(Object y) {
		if (y == null)
			return false;
		if (y == this)
			return true;
		if (y.getClass() != this.getClass())
			return false;
		Board that = (Board) y;
		if (this.dimension() != that.dimension())
			return false;
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				if (this.nodes[i][j] != that.nodes[i][j])
					return false;
		return true;
	}

	/**
	 * a set of all neighboring boards that are 1 step away from this board
	 * 
	 * @return an Iterable set of all neighboring boards
	 */
	public Iterable<Board> neighbors() {
		Stack<Board> neighbors = new Stack<Board>();
		int x = 0, y = 0; // used to record position of hole

		// find hole position
		outerLoop: for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++) {
				if (nodes[i][j] == 0) {
					x = i;
					y = j;
					break outerLoop;
				}
			}

		// move the block on the right of hole to fulfill the hole
		if (y + 1 < n) {
			Board right = new Board(nodes);
			right.nodes[x][y] = right.nodes[x][y + 1];
			right.nodes[x][y + 1] = 0; // reset the hole position
			neighbors.push(right); // put into stack for future use
		}

		// move the block on the left of hole to fulfill the hole
		if (y - 1 >= 0) {
			Board left = new Board(nodes);
			left.nodes[x][y] = left.nodes[x][y - 1];
			left.nodes[x][y - 1] = 0;
			neighbors.push(left);
		}

		// move the block below the hole to fulfill the hole
		if (x + 1 < n) {
			Board down = new Board(nodes);
			down.nodes[x][y] = down.nodes[x + 1][y];
			down.nodes[x + 1][y] = 0;
			neighbors.push(down);
		}

		// move the block above the hole to fulfill the hole
		if (x - 1 >= 0) {
			Board up = new Board(nodes);
			up.nodes[x][y] = up.nodes[x - 1][y];
			up.nodes[x - 1][y] = 0;
			neighbors.push(up);
		}

		return neighbors;
	}

	/**
	 * @return string representation of this board
	 */
	public String toString() {
		StringBuilder s = new StringBuilder();
		int n = nodes.length;
		s.append(n + "\n");
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				s.append(String.format("%2d ", nodes[i][j]));
			}
			s.append("\n");
		}
		return s.toString();
	}

	public static void main(String[] args) {
		In in = new In(args[0]);
		int n = in.readInt();
		int[][] blocks = new int[n][n];
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				blocks[i][j] = in.readInt();

		Board initial = new Board(blocks); // read from file
		System.out.println("initial: ");
		System.out.println(initial);
		System.out.println("initial Hamming: " + initial.hamming());
		System.out.println("initial Manhattan: " + initial.manhattan());

		System.out.println("----------------------");

		Board twin = initial.twin();
		System.out.println("twin of initial: \n" + twin);
		System.out.println("Manhattan of twin: " + twin.manhattan());

		System.out.println("----------------------");

		Iterable<Board> stack = initial.neighbors();
		System.out.println("neighbors of initial: ");
		for (Board b : stack) {
			System.out.println("neighour: ");
			System.out.println(b);
		}

		System.out.println("Testing Manhattan........");
		int[][] test1 = new int[2][2];
		test1[0][0] = 2;
		test1[0][1] = 1;
		test1[1][0] = 0;
		test1[1][1] = 3;
		Board testBoard1 = new Board(test1);
		System.out.println(testBoard1);
		System.out.println("Manhattan: " + testBoard1.manhattan());

		int[][] test2 = new int[3][3];
		test2[0][0] = 4;
		test2[0][1] = 6;
		test2[0][2] = 0;
		test2[1][0] = 1;
		test2[1][1] = 5;
		test2[1][2] = 7;
		test2[2][0] = 8;
		test2[2][1] = 3;
		test2[2][2] = 2;
		Board testBoard2 = new Board(test2);
		System.out.println(testBoard2);
		System.out.println("Manhattan: " + testBoard2.manhattan());

		int[][] test3 = new int[3][3];
		test3[0][0] = 1;
		test3[0][1] = 2;
		test3[0][2] = 3;
		test3[1][0] = 0;
		test3[1][1] = 7;
		test3[1][2] = 6;
		test3[2][0] = 5;
		test3[2][1] = 4;
		test3[2][2] = 8;
		Board testBoard3 = new Board(test3);
		System.out.println(testBoard3);
		System.out.println("Manhattan: " + testBoard3.manhattan());

		int[][] test4 = new int[3][3];
		test4[0][0] = 5;
		test4[0][1] = 1;
		test4[0][2] = 8;
		test4[1][0] = 2;
		test4[1][1] = 7;
		test4[1][2] = 3;
		test4[2][0] = 4;
		test4[2][1] = 0;
		test4[2][2] = 6;
		Board testBoard4 = new Board(test4);
		System.out.println(testBoard4);
		System.out.println("Manhattan: " + testBoard4.manhattan());
	}
}
