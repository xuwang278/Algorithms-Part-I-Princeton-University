import java.util.ArrayList;
import java.util.Arrays;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

/**
 * 
 * @author Xu Wang
 * 
 *         04/26/2017
 * 
 *         A representation of a brute-force strategy determining whether 4
 *         points (p1, p2, p3 and p4) are collinear by checking if the 3 slopes
 *         produced by p1&p2, p1&p3 and p1&p4 are same. (assuming no 5 or more
 *         points are collinear)
 *
 */
public class BruteCollinearPoints {
	private ArrayList<LineSegment> segments;

	public BruteCollinearPoints(Point[] points) {
		// checking validity of argument
		check(points);

		/*
		 * a list of segments created by 4 collinear points; an ArrayList is
		 * adopted without worrying the size
		 */
		segments = new ArrayList<LineSegment>();

		// in case of not changing argument
		Point[] pointsClone = points.clone();

		Arrays.sort(pointsClone); // sort points by y-coordinate
		int N = pointsClone.length;
		double slope1, slope2, slope3;

		for (int i = 0; i < N; i++) {
			for (int j = i + 1; j < N; j++) {
				for (int k = j + 1; k < N; k++) {
					for (int p = k + 1; p < N; p++) {
						slope1 = pointsClone[i].slopeTo(pointsClone[j]);
						slope2 = pointsClone[i].slopeTo(pointsClone[k]);
						slope3 = pointsClone[i].slopeTo(pointsClone[p]);
						if (Double.compare(slope1, slope2) == 0 && Double.compare(slope2, slope3) == 0) {
							// add the line created to list
							segments.add(new LineSegment(pointsClone[i], pointsClone[p]));
						}
					}
				}
			}
		}
	}

	// a helper method checking validity of points
	private void check(Point[] points) {
		if (points == null)
			throw new java.lang.IllegalArgumentException();

		for (int i = 0; i < points.length; i++)
			if (points[i] == null)
				throw new java.lang.IllegalArgumentException();

		for (int i = 0; i < points.length - 1; i++) {
			for (int j = i + 1; j < points.length; j++)
				if (points[i].compareTo(points[j]) == 0)
					throw new java.lang.IllegalArgumentException();
		}
	}

	/**
	 * @return number of segments in list
	 */
	public int numberOfSegments() {
		return segments.size();
	}

	/**
	 * convert the arrayList containing resulting segments to an array
	 * 
	 * @return an array of segments
	 */
	public LineSegment[] segments() {
		return segments.toArray(new LineSegment[segments.size()]);
	}

	public static void main(String[] args) {
		// read the n points from a file
		In in = new In(args[0]);
		int n = in.readInt();
		Point[] points = new Point[n];
		for (int i = 0; i < n; i++) {
			int x = in.readInt();
			int y = in.readInt();
			points[i] = new Point(x, y);
		}

		// draw the points
		StdDraw.enableDoubleBuffering();
		StdDraw.setXscale(0, 32768);
		StdDraw.setYscale(0, 32768);
		for (Point p : points) {
			p.draw();
		}
		StdDraw.show();

		// print and draw the line segments
		BruteCollinearPoints collinear = new BruteCollinearPoints(points);
		StdOut.println("number of segments: " + collinear.numberOfSegments());

		for (LineSegment segment : collinear.segments()) {
			StdOut.println(segment);
			segment.draw();
		}

		StdDraw.show();
	}

}
