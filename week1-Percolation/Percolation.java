import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * 
 * @author Xu Wang
 * 
 *         03/16/2017
 * 
 *         a representation of model of percolation system
 * 
 *         Notes:
 * 
 *         Open sites that connect with top are called full open sites;
 *         otherwise, empty open sites.
 * 
 *         When a system percolates, its virtual top (sites in the first row) is
 *         connected with virtual bottom (sites in the last row) through a set
 *         of open sites in the middle. In that scenario, if there are some
 *         empty open sites only connected with the bottom, they are actually
 *         "connected" with the top still via the media of bottom, and therefore
 *         those sites are regarded as "full" open sites.
 * 
 *         So, two grids are adopted and maintained respectively: one (with a
 *         virtual top and bottom) is used to check percolation, the other(with
 *         only a virtual top) for full open sites.
 * 
 */
public class Percolation {
	private WeightedQuickUnionUF grid; // used to check percolation
	private WeightedQuickUnionUF grid_isFull; // used to check full open sites
	private boolean[][] openSite; // mark if a site is open or closed
	private int top; // index of virtual top
	private int bottom; // index of virtual bottom
	private int gridSize; // length of square grid
	private int openCount; // number of open sites

	/**
	 * create n-by-n grid, with all sites blocked
	 * 
	 * @param n dimension of grid
	 */
	public Percolation(int n) {
		if (n <= 0)
			throw new IllegalArgumentException();
		else {
			grid = new WeightedQuickUnionUF(n * n + 2); // plus a top and bottom
			grid_isFull = new WeightedQuickUnionUF(n * n + 1); // plus a top
			openSite = new boolean[n][n];
			top = 0; // 1st index in grid and grid_isFull
			bottom = n * n + 1; // last index grid
			gridSize = n; // length of grid
			openCount = 0; // number of open sites
		}
	}

	/**
	 * open the site at (row, col) if it is not open already
	 * 
	 * @param row row the site on
	 * @param col column the site on
	 */
	public void open(int row, int col) {
		validSiteChecker(row, col);

		// only open it if it was blocked
		if (!isOpen(row, col)) {
			openSite[row - 1][col - 1] = true;
			openCount++;
		}

		/*
		 * Whenever a new site is open, union its adjacent open sites. The union
		 * information is organized using WeightedQuickUnionUF data structure
		 */

		// convert 2D (row, col) to a 1D index in grid
		int site = xyTo1D(row, col);

		// union with virtual top and bottom
		if (row == 1) {
			grid.union(site, top);
			grid_isFull.union(site, top);
		}
		if (row == gridSize) {
			grid.union(site, bottom);
		}

		// union neighbors if any are open
		if (row > 1 && isOpen(row - 1, col)) { // neighbor on top
			grid.union(site, xyTo1D(row - 1, col));
			grid_isFull.union(site, xyTo1D(row - 1, col));
		}

		if (row < gridSize && isOpen(row + 1, col)) { // neighbor on bottom
			grid.union(site, xyTo1D(row + 1, col));
			grid_isFull.union(site, xyTo1D(row + 1, col));
		}

		if (col > 1 && isOpen(row, col - 1)) { // neighbor on left
			grid.union(site, xyTo1D(row, col - 1));
			grid_isFull.union(site, xyTo1D(row, col - 1));
		}

		if (col < gridSize && isOpen(row, col + 1)) { // neighbor on right
			grid.union(site, xyTo1D(row, col + 1));
			grid_isFull.union(site, xyTo1D(row, col + 1));
		}
	}

	/**
	 * check if a site is open or not
	 * 
	 * @param row row the site is on
	 * @param col column the site is on
	 * @return true if the site is open, else false
	 */
	public boolean isOpen(int row, int col) {
		// check if the row and col are valid
		validSiteChecker(row, col);
		return openSite[row - 1][col - 1];
	}

	/**
	 * check if a site is full, i.e., connecting to the top
	 * 
	 * @param row row the site is on
	 * @param col column the site is on
	 * @return true if the site is full, else false
	 */
	public boolean isFull(int row, int col) {
		validSiteChecker(row, col);
		int site = xyTo1D(row, col);
		return grid_isFull.connected(site, top); // no virtual bottom
	}

	/**
	 * 
	 * @return number of open sites
	 */
	public int numberOfOpenSites() {
		return openCount;
	}

	/**
	 * check if a grid percolates, i.e., its top and bottom are connected
	 * 
	 * @return true if the site percolates, else false
	 */
	public boolean percolates() {
		return grid.connected(top, bottom);
	}

	// test driver
	public static void main(String[] args) {
		Percolation percolation = new Percolation(20);
		do {
			int row = StdRandom.uniform(1, 21);
			int col = StdRandom.uniform(1, 21);
			percolation.open(row, col);
		} while (!percolation.percolates());

		System.out.println("open sites = " + percolation.numberOfOpenSites());
		System.out.println("P* = " + (double) percolation.numberOfOpenSites() / 400);
	}

	private int xyTo1D(int row, int col) {
		validSiteChecker(row, col);
		return gridSize * (row - 1) + col;
	}

	private void validSiteChecker(int row, int col) throws IndexOutOfBoundsException {
		if (row < 1 || col < 1 || row > gridSize || col > gridSize) {
			throw new IndexOutOfBoundsException();
		}
	}
}
