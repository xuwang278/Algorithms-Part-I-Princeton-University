import java.util.TreeSet;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

/**
 * @author Xu Wang 07/13/2017
 * 
 *         A representation of a set of points on a plane.
 */
public class PointSET {
	/* a red-black tree to organize and store points */
	private TreeSet<Point2D> rbTree;
	private int count; // number of points in the set

	/**
	 * construct an empty set of points
	 */
	public PointSET() {
		rbTree = new TreeSet<Point2D>();
		count = 0;
	}

	/**
	 * check if the set is empty or not
	 * 
	 * @return true if set is empty; else, false
	 */
	public boolean isEmpty() {
		return count == 0;
	}

	/**
	 * get the size of set
	 * 
	 * @return number of nodes in Kdtree
	 */
	public int size() {
		return count;
	}

	/**
	 * add a point to set (if it is not already there)
	 * 
	 * @param p point to be inserted in set
	 */
	public void insert(Point2D p) {
		if (p == null)
			throw new java.lang.IllegalArgumentException();
		if (!contains(p)) {
			rbTree.add(p);
			count++;
		}
	}

	/**
	 * check if a point is in set
	 * 
	 * @param p target point
	 * @return true if the point is in set, else, false
	 */
	public boolean contains(Point2D p) {
		if (p == null)
			throw new java.lang.IllegalArgumentException();
		if (rbTree.contains(p))
			return true;
		return false;
	}

	/**
	 * draw all points to standard draw
	 */
	public void draw() {
		for (Point2D p : rbTree)
			StdDraw.point(p.x(), p.y());
	}

	/**
	 * 
	 * @param rect query rectangle
	 * @return an iterable set of all points that are inside the rectangle
	 * 
	 *         brute-force: O(n)
	 */
	public Iterable<Point2D> range(RectHV rect) {
		if (rect == null)
			throw new java.lang.IllegalArgumentException();
		Queue<Point2D> queue = new Queue<Point2D>();
		for (Point2D p : rbTree) {
			if (rect.contains(p))
				queue.enqueue(p);
		}
		return queue;
	}

	/**
	 * 
	 * @param p target point
	 * @return a nearest neighbor in the set to point p; null if set is empty
	 * 
	 *         brute-force: O(n)
	 */
	public Point2D nearest(Point2D p) {
		if (p == null)
			throw new java.lang.IllegalArgumentException();
		if (isEmpty())
			return null;

		Point2D nearest = null;
		double nearestDistance = Double.POSITIVE_INFINITY;

		for (Point2D pt : rbTree) {
			double distance = pt.distanceTo(p);
			if (distance < nearestDistance) {
				nearestDistance = distance;
				nearest = pt;
			}
		}

		return nearest;
	}

	public static void main(String[] args) {
		Point2D p1 = new Point2D(0.5, 0.5);
		Point2D p2 = new Point2D(0.25, 0.25);
		Point2D p3 = new Point2D(0.7, 0.9);
		PointSET set = new PointSET();

		System.out.println("Empty? " + set.isEmpty());
		System.out.println("size: " + set.size());

		set.insert(p1);
		set.insert(p2);

		System.out.println("Empty? " + set.isEmpty());
		System.out.println("size: " + set.size());
		System.out.println("contains p2? " + set.contains(p2));
		System.out.println("contains p3? " + set.contains(p3));

		StdDraw.clear();
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.setPenRadius(0.01);
		set.draw();

		RectHV rect1 = new RectHV(0, 0, 1, 1);
		for (Point2D p : set.range(rect1))
			System.out.print(p + ", ");
		System.out.println();

		System.out.println("-------------");
		RectHV rect2 = new RectHV(0, 0, 0.1, 0.1);
		System.out.println("points in a very small rect");
		for (Point2D p : set.range(rect2))
			System.out.println(p);

		System.out.println("-------------");
		PointSET emptySet = new PointSET();
		System.out.println("nearest point to an empty set? " + emptySet.nearest(p2));

		System.out.println("-------------");
		System.out.println("Is set empty? " + set.isEmpty());
		for (Point2D p : set.rbTree)
			System.out.println(p);
		System.out.println("nearest point to p2(0.25, 0.25) in SET: " + set.nearest(p2));
		System.out.println("nearest point to p3(0.7, 0.9) in SET: " + set.nearest(p3));

	}

}
