import java.util.TreeSet;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class PointSET {
	private TreeSet<Point2D> rbTree;
	private int count;

	// construct an empty set of points
	public PointSET() {
		rbTree = new TreeSet<Point2D>();
		count = 0;
	}

	// is the set empty?
	public boolean isEmpty() {
		return count == 0;
	}

	// number of points in the set
	public int size() {
		return count;
	}

	// add the point to the set (if it is not already in the set)
	// O(lgN)
	public void insert(Point2D p) {
		if (p == null)
			throw new java.lang.IllegalArgumentException();
		if (!contains(p)) {
			rbTree.add(p);
			count++;
		}
	}

	// does the set contain point p?
	// O(lgN)
	public boolean contains(Point2D p) {
		if (p == null)
			throw new java.lang.IllegalArgumentException();
		if (rbTree.contains(p))
			return true;
		return false;
	}

	// draw all points to standard draw
	public void draw() {
		for (Point2D p : rbTree) {
			StdDraw.point(p.x(), p.y());
		}
	}

	// all points that are inside the rectangle
	// O(N)
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

	// a nearest neighbor in the set to point p; null if the set is empty
	// O(N)
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
