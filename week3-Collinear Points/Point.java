
/******************************************************************************
 *  Compilation:  javac Point.java
 *  Execution:    java Point
 *  Dependencies: none
 *  
 *  An immutable data type for points in the plane.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/

import java.util.Arrays;
import java.util.Comparator;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class Point implements Comparable<Point> {

	private final int x; // x-coordinate of this point
	private final int y; // y-coordinate of this point

	/**
	 * Initializes a new point.
	 *
	 * @param x the <em>x</em>-coordinate of the point
	 * @param y the <em>y</em>-coordinate of the point
	 */
	public Point(int x, int y) {
		/* DO NOT MODIFY */
		this.x = x;
		this.y = y;
	}

	/**
	 * Draws this point to standard draw.
	 */
	public void draw() {
		/* DO NOT MODIFY */
		StdDraw.point(x, y);
	}

	/**
	 * Draws the line segment between this point and the specified point to
	 * standard draw.
	 *
	 * @param that the other point
	 */
	public void drawTo(Point that) {
		/* DO NOT MODIFY */
		StdDraw.line(this.x, this.y, that.x, that.y);
	}

	/**
	 * @author Xu Wang
	 * 
	 *         04/26/2017
	 * 
	 *         Returns the slope between this point and the specified point.
	 *         Formally, if the two points are (x0, y0) and (x1, y1), then the
	 *         slope is (y1 - y0) / (x1 - x0). For completeness, the slope is
	 *         defined to be +0.0 if the line segment connecting the two points
	 *         is horizontal; Double.POSITIVE_INFINITY if the line segment is
	 *         vertical; and Double.NEGATIVE_INFINITY if (x0, y0) and (x1, y1)
	 *         are equal.
	 *
	 * @param that the other point
	 * @return the slope between this point and the specified point
	 */
	public double slopeTo(Point that) {
		/* YOUR CODE HERE */
		if (that.x != x) {
			if (that.y != y)
				return (double) (that.y - y) / (that.x - x);
			else
				return +0.0;
		} else {
			if (that.y != y)
				return Double.POSITIVE_INFINITY;
			else
				return Double.NEGATIVE_INFINITY;
		}
	}

	/**
	 * 
	 * @author Xu Wang
	 * 
	 *         04/26/2017
	 * 
	 *         Compares two points by y-coordinate, breaking ties by
	 *         x-coordinate. Formally, the invoking point (x0, y0) is less than
	 *         the argument point (x1, y1) if and only if either y0 < y1 or if
	 *         y0 = y1 and x0 < x1.
	 *
	 * @param that the other point
	 * @return the value <tt>0</tt> if this point is equal to the argument point
	 *         (x0 = x1 and y0 = y1); a negative integer if this point is less
	 *         than the argument point; and a positive integer if this point is
	 *         greater than the argument point
	 */
	public int compareTo(Point that) {
		/* YOUR CODE HERE */
		if (y < that.y)
			return -1;
		if (y == that.y) {
			if (x < that.x)
				return -1;
			if (x == that.x)
				return 0;
			else
				return 1;
		} else
			return 1;
	}

	/**
	 * 
	 * @author Xu Wang
	 * 
	 *         04/26/2017
	 * 
	 *         Compares two points by the slope they make with this point. The
	 *         slope is defined as in the slopeTo() method.
	 *
	 * @return the Comparator that defines this ordering on points
	 */
	public Comparator<Point> slopeOrder() {
		/*
		 * when calling the method, points[0].slopeOrder(), the method is
		 * associated with a particular point object, so the method can not be
		 * static.
		 */
		return new BySlopeOrder(this);
	}

	// not associated with any Point object, so it could be static
	// define within the class to emphasize the comparator belongs to Point
	// class

	/*
	 * @author Xu Wang
	 * 
	 * 04/26/2017
	 * 
	 * A inner class, implementing Comparator<Point>, defines a rule on how to
	 * compare two points by the slopes they make with a specified point.
	 * 
	 */
	private static class BySlopeOrder implements Comparator<Point> {
		private Point point;
		private double slope1;
		private double slope2;

		public BySlopeOrder(Point point) {
			this.point = point;
		}

		/**
		 * compares two points by their slope with a specified point
		 * 
		 * @return the value of 1 if the slope of point o1 and the specified
		 *         point > that of o2; value of -1 if that of point o1 < that of
		 *         o2; value of 0 if their slopes are the same
		 */
		@Override
		public int compare(Point o1, Point o2) {
			slope1 = point.slopeTo(o1);
			slope2 = point.slopeTo(o2);
			int result = Double.compare(slope1, slope2);
			if (result < 0)
				return -1;
			if (result == 0)
				return 0;
			else
				return 1;
		}
	}

	/**
	 * Returns a string representation of this point. This method is provide for
	 * debugging; your program should not rely on the format of the string
	 * representation.
	 *
	 * @return a string representation of this point
	 */
	public String toString() {
		/* DO NOT MODIFY */
		return "(" + x + ", " + y + ")";
	}

	/**
	 * Unit tests the Point data type.
	 */
	public static void main(String[] args) {
		/* YOUR CODE HERE */
		Point p00 = new Point(0, 0);
		Point p = new Point(0, 0);
		System.out.println("same? " + p00.compareTo(p));
		Point p10 = new Point(1, 0);
		Point p01 = new Point(0, 1);
		Point p11 = new Point(1, 1);

		StdOut.println(p00.slopeTo(p11));
		StdOut.println(p00.slopeTo(p01));
		StdOut.println(p00.slopeTo(p00));
		StdOut.println(p00.slopeTo(p10));

		Point[] points = new Point[4];
		points[0] = p00;
		points[1] = p11;
		points[2] = p10;
		points[3] = p01;

		StdDraw.setXscale(0, 5);
		StdDraw.setYscale(0, 5);
		points[0].drawTo(points[1]);
		points[2].drawTo(points[3]);

		StdOut.println("Before: ");
		for (Point p1 : points) {
			StdOut.println(p1);
		}

		// Arrays.sort(points);
		Arrays.sort(points, points[2].slopeOrder());
		StdOut.println("AFter: ");
		for (Point p1 : points) {
			StdOut.println(p1);
		}
	}
}
