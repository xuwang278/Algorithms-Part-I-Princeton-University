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
 *         A representation of an optimized strategy determining whether 4 or
 *         more points are collinear.
 * 
 *         Mechanism:
 * 
 *         1. Sort the array of points by y-coordinate and from lowest point to
 *         highest point
 * 
 *         2. Traverse the array to seek for collinear points from testing the
 *         lowest point to the highest point as starting point one by one:
 * 
 *         (1)copy the sub array[starting point, end] to an auxiliary array,
 *         sort the array by the slope made relative to the starting point;
 * 
 *         (2)traverse the sub array to seek for collinear points from the
 *         smaller slope to the largest slope (sweeping counter-clockwise);
 * 
 *         (3)if found, add to list of lines
 * 
 *         (4)a longer line is always found before its sub lines are, so sub
 *         lines can be avoided by checking if a new found line's starting point
 *         is already on a previous line.
 * 
 *         3. Construct segments from the list of lines.
 * 
 */
public class FastCollinearPoints {
	private ArrayList<LineSegment> segments;
	private ArrayList<line> lines;

	// a inner class representing a line with starting point, end point its
	// slope
	private class line {
		private Point end;
		private Point start;
		private double slope;

		/**
		 * construct a line
		 * 
		 * @param end end point
		 * @param start starting point
		 * @param slope slope of line
		 */
		public line(Point end, Point start, double slope) {
			this.end = end;
			this.start = start;
			this.slope = slope;
		}

	}

	/**
	 * starts to seek for collinear lines given a set of points
	 * 
	 * @param points a set of points on a plane
	 */
	public FastCollinearPoints(Point[] points) {
		check(points);
		segments = new ArrayList<LineSegment>();
		lines = new ArrayList<line>();
		Point[] ptsClone = points.clone();
		Arrays.sort(ptsClone); // sort all points by y-coordinate

		for (int i = 0; i < ptsClone.length - 3; i++) {

			Point[] pts = Arrays.copyOfRange(ptsClone, i, ptsClone.length);
			Point startPoint = pts[0];

			// sort by slope relative to start point
			Arrays.sort(pts, startPoint.slopeOrder());

			int mid1 = 1, mid2 = mid1 + 1;
			while (mid2 < pts.length) {
				// from smallest to largest slope, sweeping counter-clockwise
				double slope = startPoint.slopeTo(pts[mid1]);

				while (mid2 < pts.length && Double.compare(startPoint.slopeTo(pts[mid2]), slope) == 0)
					mid2++;

				if (mid2 - mid1 > 2 && !isEndPointCounted(startPoint, slope))
					lines.add(new line(startPoint, pts[mid2 - 1], slope));

				mid1 = mid2; // check the second smallest slope
				mid2 = mid1 + 1;

			}

		}

		for (line p : lines) {
			// construct a segment from the starting point to the very far end
			segments.add(new LineSegment(p.start, p.end));
		}

	}

	/*
	 * a helper function used to check if the starting point of a new found line
	 * is already on a previous line
	 * 
	 * For example, for a set of points (0,0), (1,1), (2,2), (3,3), (4,4),
	 * (5,5), (6,6), line (0,0) -> (6,6) is found first; then (1,1) -> (6,6) is
	 * found, however, it is just a sub line of the previous line, which doesn't
	 * count.
	 */
	private boolean isEndPointCounted(Point startPoint, double slope) {
		for (line l : lines)
			/*
			 * there already exists a line whose slope is the same as the new
			 * found line, also the slope can be defined by the two starting
			 * points of the two lines, which means the new found line is a sub
			 * line of the previous line
			 */
			if (l.slope == slope && l.start.slopeTo(startPoint) == slope)
				return true;
		return false;
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
	 * 
	 * @return number of segments
	 */
	public int numberOfSegments() {
		return segments.size();
	}

	/**
	 * 
	 * @return a array of segments that resulted from collinear points
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
		StdDraw.setPenRadius(0.004);

		// print and draw the line segments
		FastCollinearPoints collinear = new FastCollinearPoints(points);
		StdOut.println("number of segments: " + collinear.numberOfSegments());
		for (LineSegment segment : collinear.segments()) {
			StdOut.println(segment);
			segment.draw();
		}
		StdDraw.show();
	}
}
