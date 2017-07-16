
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

/**
 * 
 * @author Xu Wang
 * 
 *         07/15/2017
 * 
 *         A 2d-tree representation of points on a plane, from which optimized
 *         implementations of range() and nearest() are benefited.
 */

public class KdTree {

	/*
	 * a flag indicating how to compare 2 points
	 * 
	 * VERTICAL: compare x-coordinates, smaller nodes go to lb subtree,
	 * otherwise, go to rt subtree
	 * 
	 * !VERTICAL: compare y-coordinates, smaller nodes go to lb subtree,
	 * otherwise, go to rt subtree
	 */
	private static final boolean VERTICAL = true;
	private Node root; // a reference to root of Kdtree
	private int count; // number of nodes in Kdtree

	/* define Node of Kdtree */
	private static class Node {
		private Point2D pt; // point associated with a Node
		private RectHV rect; // rectangle associated with a Node
		private Node lb; // reference to left/bottom subtree
		private Node rt; // reference to right/top subtree

		/**
		 * construct a Node using given fields
		 */
		public Node(Point2D pt, RectHV rect, Node lb, Node rt) {
			this.pt = pt;
			this.rect = rect;
			this.lb = lb;
			this.rt = rt;
		}

	}

	/**
	 * construct an empty Kdtree
	 */
	public KdTree() {
		root = null;
		count = 0;
	}

	/**
	 * check if the Kdtree is empty or not
	 * 
	 * @return true if Kdtree is empty; else, false
	 */
	public boolean isEmpty() {
		return count == 0;
	}

	/**
	 * get the size of Kdtree
	 * 
	 * @return number of nodes in Kdtree
	 */
	public int size() {
		return count;
	}

	/**
	 * add point to Kdtree (if it is not already there)
	 * 
	 * @param p point to be inserted
	 */
	public void insert(Point2D p) {
		if (p == null)
			throw new java.lang.IllegalArgumentException();
		RectHV baseRect = new RectHV(0, 0, 1, 1);
		root = insert(root, p, baseRect, VERTICAL); // compare x coordinate 1st
		/* count++; if p already exists, no need to update count */
	}

	/*
	 * a helper function
	 * 
	 * approach:
	 * 
	 * traverse the tree, alternatively comparing x and y coordinate to
	 * determine the next subtree to go, until a null node was found, then
	 * insert the node containing point and rectangle.
	 * 
	 * A parent rect is always tracked by argument passing from last level of
	 * recursion; the rect is split into two rects owned by two children at
	 * current level.
	 * 
	 * The position of the child rect can be calculated given the positions
	 * parent rect and pt.
	 */
	private Node insert(Node root, Point2D p, RectHV rect, boolean orientation) {
		/* base case: create a Node with p and a rect */
		if (root == null) {
			count++; // update count
			return new Node(p, rect, null, null);
		}
		/* if the point already exists, just return */
		else if (root.pt.x() == p.x() && root.pt.y() == p.y())
			return root;

		/*
		 * always check orientation of node to enter the proper if statement; at
		 * last, flip orientation to search the subtree
		 */
		if (orientation) {
			/* vertical: use x as key */
			double compX = p.x() - root.pt.x();
			if (compX < 0) {
				RectHV leftRect = new RectHV(root.rect.xmin(), root.rect.ymin(), root.pt.x(), root.rect.ymax());
				root.lb = insert(root.lb, p, leftRect, !orientation);
			} else {
				// point with same x as parent but different y, goes to right
				RectHV rightRect = new RectHV(root.pt.x(), root.rect.ymin(), root.rect.xmax(), root.rect.ymax());
				root.rt = insert(root.rt, p, rightRect, !orientation);
			}
		} else {
			/* horizontal: use y as key */
			double compY = p.y() - root.pt.y();
			if (compY < 0) {
				RectHV bottomRect = new RectHV(root.rect.xmin(), root.rect.ymin(), root.rect.xmax(), root.pt.y());
				root.lb = insert(root.lb, p, bottomRect, !orientation);
			} else {
				// point with same y as parent but different x, goes to top
				RectHV topRect = new RectHV(root.rect.xmin(), root.pt.y(), root.rect.xmax(), root.rect.ymax());
				root.rt = insert(root.rt, p, topRect, !orientation);
			}
		}

		return root; // A new tree is built from bottom to top.

	}

	/**
	 * check if point is in Kdtree
	 * 
	 * @param p target point
	 * @return true if point is Kdtree, else, false
	 */
	public boolean contains(Point2D p) {
		if (p == null)
			throw new java.lang.IllegalArgumentException();

		/* search beginning from comparing x */
		return get(root, p, VERTICAL) != null;
	}

	/*
	 * a helper function
	 * 
	 * approach:
	 * 
	 * To traverse the tree, alternatively comparing x and y coordinate to
	 * determine the next subtree to go, comparing with the target point, until
	 * that point was found, return it; or not found, return null.
	 * 
	 */
	private Point2D get(Node root, Point2D p, boolean orientation) {
		if (p == null)
			throw new java.lang.IllegalArgumentException();

		/* base case 1: indicating point is not in the tree */
		if (root == null)
			return null;

		/* base case 2: found directly, return the point in the node */
		if (root.pt.x() == p.x() && root.pt.y() == p.y())
			return root.pt;

		if (orientation) {
			double compX = p.x() - root.pt.x();
			if (compX < 0)
				return get(root.lb, p, !orientation);
			else
				// if have the same x, go to rt; consistent with insert()
				return get(root.rt, p, !orientation);
		} else {
			double compY = p.y() - root.pt.y();
			if (compY < 0)
				return get(root.lb, p, !orientation);
			else
				// if have the same y, go to rt; consistent with insert()
				return get(root.rt, p, !orientation);
		}
	}

	/**
	 * draw all points and splitting lines to standard draw
	 */
	public void draw() {
		draw(root, VERTICAL); // draw vertical line passing root first
	}

	/*
	 * a helper function
	 * 
	 * approach:
	 * 
	 * Draw the point in root first in black, then a vertical line in red that
	 * splits its rect passing its pt.
	 * 
	 * Two ends of the line can be calculated given the rect and pt.
	 * 
	 * Then recursively call the method on its two subtress, drawing horizontal
	 * lines in blue instead, and so forth.
	 * 
	 */
	private void draw(Node root, boolean orientation) {
		/* base case: draw nothing if the node has no child */
		if (root == null)
			return;

		/* draw point */
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.setPenRadius(0.01);
		root.pt.draw();

		/* draw line */
		if (orientation) {
			StdDraw.setPenColor(StdDraw.RED);
			StdDraw.setPenRadius();
			/* the line goes through point from bottom to top of the rect. */
			StdDraw.line(root.pt.x(), root.rect.ymin(), root.pt.x(), root.rect.ymax());
		} else {
			StdDraw.setPenColor(StdDraw.BLUE);
			StdDraw.setPenRadius();
			/* the line goes through point from left to right of the rect. */
			StdDraw.line(root.rect.xmin(), root.pt.y(), root.rect.xmax(), root.pt.y());
		}

		/* draw subtrees */
		draw(root.lb, !orientation);
		draw(root.rt, !orientation);
	}

	/**
	 * look for a set of points within query rectangle
	 * 
	 * @param rect query rectangle
	 * @return an Iterable set of all points that are inside rectangle
	 */
	public Iterable<Point2D> range(RectHV rect) {
		if (rect == null)
			throw new java.lang.IllegalArgumentException();

		Queue<Point2D> queue = new Queue<Point2D>();
		range(root, rect, queue);
		return queue;
	}

	/*
	 * a helper function
	 * 
	 * approach:
	 * 
	 * check if root's rect intersects with the query rect:
	 * 
	 * if so, the point in the node may fall within the query rect, and go to
	 * have a check: whenever that point is found, enqueue it to the queue;
	 * 
	 * otherwise, there is no need to check it and its subtrees.
	 * 
	 */
	private void range(Node root, RectHV rect, Queue<Point2D> queue) {
		/** base case 1: no points are found in rect */
		if (root == null)
			return;

		/**
		 * base case 2: check if the rect could contain some candidate points;
		 * if so, go to have a look and dequeue found point; else, just ignore.
		 */
		if (root.rect.intersects(rect)) {
			if (rect.contains(root.pt))
				queue.enqueue(root.pt);
		} else
			return;

		/* check subtrees */
		range(root.lb, rect, queue);
		range(root.rt, rect, queue);
	}

	/**
	 * 
	 * look for the nearest neighbor in the set to point p; null if the set is
	 * empty
	 * 
	 * @param p target point
	 * @return point that has the shortest distance to target point
	 */
	public Point2D nearest(Point2D p) {
		if (p == null)
			throw new java.lang.IllegalArgumentException();

		if (root == null)
			return null;
		return nearest(root, p, root.pt, VERTICAL);
	}

	/*
	 * a helper function
	 * 
	 * approach:
	 * 
	 * Starting from pt in the root as the closest point, distance between the
	 * pt and target p is measured, compared with the closest distance obtained
	 * so far, updating closest point if necessary; recursively check subtrees
	 * that may have candidate points.
	 */
	private Point2D nearest(Node root, Point2D p, Point2D nearestSoFar, boolean orientation) {
		Point2D closest = nearestSoFar;

		/*
		 * base case: if no more nodes left, return the closest obtained so far
		 */
		if (root == null)
			return closest;

		/* distanceSquaredTo() is more efficient than distanceTo() */
		if (root.pt.distanceSquaredTo(p) < closest.distanceSquaredTo(p))
			closest = root.pt; // update closest point

		/*
		 * If closest distance obtained so far is smaller than the distance
		 * between the query point to the rect of node, there is no need to
		 * check the node and its subtrees (the one near to p first);
		 * 
		 * else, need to check both subtrees but always check the subtree whose
		 * rectangle is on the same side of the target point first because the
		 * closest point found while exploring the first subtree may enable
		 * pruning of the second subtree.
		 */

		if (root.rect.distanceSquaredTo(p) < closest.distanceSquaredTo(p)) {
			Node near, far;
			if ((orientation && (p.x() < root.pt.x())) || (!orientation && (p.y() < root.pt.y()))) {
				near = root.lb;
				far = root.rt;
			} else {
				near = root.lb;
				far = root.rt;
			}

			/* Check subtree on the same side as p */
			closest = nearest(near, p, closest, !orientation);
			/* Check subtree on the opposite side as p */
			closest = nearest(far, p, closest, !orientation);
		}

		return closest;

	}

	public static void main(String[] args) {
		KdTree set = new KdTree();
		Point2D p1 = new Point2D(0.7, 0.2);
		Point2D p2 = new Point2D(0.5, 0.4);
		Point2D p3 = new Point2D(0.2, 0.3);
		Point2D p4 = new Point2D(0.4, 0.7);
		Point2D p5 = new Point2D(0.9, 0.6);
		Point2D p6 = new Point2D(0.15, 0.25);
		set.insert(p1);
		set.insert(p2);
		set.insert(p3);
		set.insert(p4);
		set.insert(p5);
		System.out.println("size: " + set.size());
		System.out.println("contains P3 " + set.contains(p3));
		System.out.println("contains P5 " + set.contains(p5));
		System.out.println("contains P6 " + set.contains(p6));

		System.out.println("root of tree should be (0.7, 0.2): " + set.root.pt);
		System.out.println("left of root should be (0.5, 0.4): " + set.root.lb.pt);
		System.out.println("left of left of root should be (0.2, 0.3): " + set.root.lb.lb.pt);
		System.out.println("right of left of root should be (0.4, 0.7): " + set.root.lb.rt.pt);
		System.out.println("right of root should be (0.9, 0.6): " + set.root.rt.pt);

		System.out.println("Now testing rects: ");
		System.out.println("(0.7, 0.2) -> (0.0, 0.0), (1.0, 1.0) vs. " + set.root.rect);
		System.out.println("(0.5, 0.4) -> (0.0, 0.0), (0.7, 1.0) vs. " + set.root.lb.rect);
		System.out.println("(0.9, 0.6) -> (0.7, 0.0), (1.0, 1.0) vs. " + set.root.rt.rect);
		System.out.println("(0.2, 0.3) -> (0.0, 0.0), (0.7, 0.4) vs. " + set.root.lb.lb.rect);
		System.out.println("(0.4, 0.7) -> (0.0, 0.4), (0.7, 1.0) vs. " + set.root.lb.rt.rect);

		System.out.println("Now insert a new point (0.8, 0.8)");
		Point2D p7 = new Point2D(0.8, 0.8);
		set.insert(p7);
		System.out.println("(0.8, 0.8): " + set.root.rt.rt.pt);
		System.out.println("(0.7, 0.6), (1.0, 1.0) vs. " + set.root.rt.rt.rect);

		System.out.println("Now insert a new point (0.75, 0.75)");
		Point2D p8 = new Point2D(0.75, 0.75);
		set.insert(p8);
		System.out.println("(0.75, 0.75): " + set.root.rt.rt.lb.pt);
		System.out.println("(0.7, 0.6), (0.8, 1.0) vs. " + set.root.rt.rt.lb.rect);

		System.out.println("Now insert a new point (0.72, 0.72)");
		Point2D p9 = new Point2D(0.72, 0.72);
		set.insert(p9);
		System.out.println("(0.72, 0.72): " + set.root.rt.rt.lb.lb.pt);
		System.out.println("(0.7, 0.6), (0.8, 0.75) vs. " + set.root.rt.rt.lb.lb.rect);

		set.draw();

		System.out.println("......................................");
		System.out.println("rang() testing...");

		RectHV query1 = new RectHV(0, 0, 1, 1);
		Iterable<Point2D> pointsInRange1 = set.range(query1);
		System.out.println("Result of query 1: ");
		for (Point2D p : pointsInRange1)
			System.out.print(p + ", ");

		System.out.println();
		RectHV query2 = new RectHV(0, 0, 0.1, 0.1);
		Iterable<Point2D> pointsInRange2 = set.range(query2);
		System.out.println("Result of query 2: ");
		for (Point2D p : pointsInRange2)
			System.out.print(p + ", ");

		System.out.println();
		RectHV query3 = new RectHV(0.7, 0, 1, 1);
		Iterable<Point2D> pointsInRange3 = set.range(query3);
		System.out.println("Result of query 2: ");
		for (Point2D p : pointsInRange3)
			System.out.print(p + ", ");

		System.out.println();
		System.out.println("......................................");
		System.out.println("nearest() testing...");
		Point2D start = new Point2D(1, 1);
		System.out.println(set.nearest(start));

	}

}
