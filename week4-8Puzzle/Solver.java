package assign4_8Puzzle;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

/**
 * 
 * @author Xu Wang
 * 
 *         07/04/2017
 * 
 *         a solver of 8 puzzle game
 * 
 *         Mechanism:
 * 
 *         A priority queue is used to organize all possible boards moving from
 *         initial towards goal board, from which the board with smallest
 *         priority is dequeued (whose derived neighboring boards are enqueued),
 *         and is compared to goal board: if not match, put it to a stack and
 *         continue to check next board with the 2nd smallest priority; if
 *         match, game is done.
 * 
 *         The stack containing all the boards dequeued from the queue is the
 *         solution to the game, if solvable: always choosing the best next step
 *         given the current situation makes the best solution overall.
 * 
 *         Either a board or its twin is solvable, i.e., a solvable twin
 *         indicates an unsolvable board.
 *
 */
public class Solver {
	private MinPQ<SearchNode> minPQ; // a priority queue for game board
	private MinPQ<SearchNode> minPQtwin; // a priority queue for twin board
	private boolean solvable; // if the game is solvable
	private SearchNode solutionNode; // used to track back all solution boards
	private Board goalBoard; // board with goal pattern, used for comparison

	// a stack containing all boards towards goal step by step
	private Stack<Board> solutionBoards;

	// define a inner SearchNode class
	private class SearchNode implements Comparable<SearchNode> {
		private Board board; // a game board
		private int moves, distance; // moves made so far, Manhattan distance
		private SearchNode previous; // reference to its previous node

		/**
		 * constructs a SearchNode using given fields
		 * 
		 * @param board a game board
		 * @param moves moves made so far
		 * @param previous reference to its previous node
		 */
		public SearchNode(Board board, int moves, SearchNode previous) {
			this.board = board;
			this.moves = moves;
			distance = board.manhattan();
			this.previous = previous;
		}

		/**
		 * set up role on comparing SearchNodes, i.e. priority of SearchNodes
		 * 
		 * @return 1 if the priority of this node is larger than that of that;
		 *         -1 if the priority of this node is smaller than that of that;
		 *         0 if the priority of this node is the same as that of that
		 */
		@Override
		public int compareTo(SearchNode that) {
			int thisPriority, thatPriority;
			thisPriority = this.moves + this.distance;
			thatPriority = that.moves + that.distance;
			if (thisPriority > thatPriority)
				return 1;
			if (thisPriority < thatPriority)
				return -1;
			else
				return 0;
		}
	}

	/**
	 * constructs a Solver using given game board, starting to find solutions
	 * 
	 * @param game board containing the initial block pattern
	 */
	public Solver(Board initial) {
		if (initial == null)
			throw new IllegalArgumentException();

		minPQ = new MinPQ<SearchNode>();
		minPQtwin = new MinPQ<SearchNode>();
		solvable = false;
		solutionNode = null;
		goalBoard = new Board(goalBoard(initial));
		solutionBoards = null;

		// enqueue the initial board and its twin
		minPQ.insert(new SearchNode(initial, 0, null));
		minPQtwin.insert(new SearchNode(initial.twin(), 0, null));

		while (!minPQ.isEmpty() && !minPQtwin.isEmpty()) {
			SearchNode min1 = minPQ.delMin();
			SearchNode min2 = minPQtwin.delMin();

			if (min1.board.equals(goalBoard)) {
				solvable = true;
				solutionNode = min1;
				break;
			}
			if (min2.board.equals(goalBoard)) {
				solvable = false;
				break;
			} else {
				// store all neighboring boards
				Iterable<Board> neighbors1 = min1.board.neighbors();
				Iterable<Board> neighbors2 = min2.board.neighbors();

				/*
				 * check if neighboring boards of this board are same as its
				 * previous board: if not, enqueue the SearchNode with the
				 * neighboring board, increased moves, and using this node as
				 * previous node; if yes, just jgnore.
				 */
				for (Board b : neighbors1)
					// trick: run 1st time, min1.previous == null
					if (min1.previous == null || !b.equals(min1.previous.board))
						minPQ.insert(new SearchNode(b, min1.moves + 1, min1));

				for (Board b : neighbors2)
					if (min2.previous == null || !b.equals(min2.previous.board))
						minPQtwin.insert(new SearchNode(b, min2.moves + 1, min2));
			}
		}

		if (solvable) {
			solutionBoards = new Stack<Board>();

			/*
			 * By tracking back starting from the last node dequeued, a sequence
			 * of solution nodes was obtained
			 */
			SearchNode current = solutionNode;
			while (current != null) {
				solutionBoards.push(current.board);
				current = current.previous;
			}
		}
	}

	/*
	 * @return the goal board corresponding to its dimension
	 */
	private int[][] goalBoard(Board initial) {
		int n = initial.dimension(), k = 1;
		int[][] goal = new int[n][n];
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				goal[i][j] = k++;
		goal[n - 1][n - 1] = 0;
		return goal;
	}

	/**
	 * @return true if board is solvable, else false
	 */
	public boolean isSolvable() {
		return solvable;
	}

	// min number of moves to solve initial board; -1 if unsolvable
	public int moves() {
		if (isSolvable()) {
			return solutionBoards.size() - 1;
		} else
			return -1;
	}

	/**
	 * @return sequence of boards in a shortest solution; null if unsolvable
	 */
	public Iterable<Board> solution() {
		if (isSolvable())
			return solutionBoards;
		return null;
	}

	// solve a slider puzzle (given below)
	public static void main(String[] args) {
		// create initial board from file
		In in = new In(args[0]);
		int n = in.readInt();
		int[][] blocks = new int[n][n];
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				blocks[i][j] = in.readInt();
		Board initial = new Board(blocks);
		System.out.println("initial Manhattan: " + initial.manhattan());
		// solve the puzzle
		Solver solver = new Solver(initial);

		// print solution to standard output
		if (!solver.isSolvable())
			StdOut.println("No solution possible");
		else {
			StdOut.println("Minimum number of moves = " + solver.moves());
			for (Board board : solver.solution()) // queue: 1st in, 1st out
				StdOut.println(board);
		}

	}
}
