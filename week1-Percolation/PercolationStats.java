import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/**
 * 
 * @author Yuanfang Ying
 * 
 *         03/16/2017
 * 
 *         a client that conducts percolation measurements and gets statistic
 *         data
 *
 */
public class PercolationStats {
	private double results[];
	private int totalGridSize;
	private int trials;

	/**
	 * starts to perform trials independent experiments on an n-by-n grid
	 * 
	 * @param gridSize length of grid
	 * @param trials times of trials for measurements
	 * @throws IllegalArgumentException
	 */
	public PercolationStats(int gridSize, int trials) {
		if (gridSize <= 0 || trials <= 0) {
			throw new IllegalArgumentException();
		} else {
			results = new double[trials];
			totalGridSize = gridSize * gridSize;
			this.trials = trials;
			for (int i = 0; i < trials; i++) {
				Percolation percolation = new Percolation(gridSize);
				do {
					int row = StdRandom.uniform(1, gridSize + 1);
					int col = StdRandom.uniform(1, gridSize + 1);
					percolation.open(row, col);
				} while (!percolation.percolates());
				results[i] = (double) percolation.numberOfOpenSites() / totalGridSize;
			}
		}
	}

	/**
	 * @return sample mean of percolation threshold
	 */
	public double mean() {
		return StdStats.mean(results);
	}

	/**
	 * @return sample standard deviation of percolation threshold
	 */
	public double stddev() {
		return StdStats.stddev(results);
	}

	/**
	 * @return low endpoint of 95% confidence interval
	 */
	public double confidenceLo() {
		return mean() - (1.96 * stddev()) / Math.sqrt(trials);
	}

	/**
	 * @return high endpoint of 95% confidence interval
	 */
	public double confidenceHi() {
		return mean() + (1.96 * stddev()) / Math.sqrt(trials);
	}

	public static void main(String[] args) {
		int gridSize = Integer.parseInt(args[0]);
		int trials = Integer.parseInt(args[1]);
		PercolationStats tests = new PercolationStats(gridSize, trials);
		System.out.printf("%-25s %s %s \n", "mean", "=", tests.mean());
		System.out.printf("%-25s %s %s \n", "stddev", "=", tests.stddev());
		System.out.printf("%-25s %s", "95% confidence interval", "= ");
		System.out.printf("%s%s, %s%s", "[", tests.confidenceLo(), tests.confidenceHi(), "]");
	}
}
