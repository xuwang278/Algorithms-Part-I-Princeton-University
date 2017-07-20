package assign2;

import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdOut;

/**
 * 
 * @author Xu Wang
 * 
 *         04/01/2017
 * 
 *         A representation of a double-ended queue that supports adding and
 *         removing items from either the front or the back of the data
 *         structure.
 * 
 *         A sentinel node is an auxiliary node helping maintain the
 *         double-linked queue, and that is not counted in.
 * 
 *         In an empty deque, both prev and next of sentinel point to itself;
 * 
 *         in a non-empty deque, the prev of sentinel points to 1st node, next
 *         of sentinel points to the last node; pre of the 1st node and next of
 *         the last node point to sentinel.
 * 
 * @param <Item> a generic data type stored in deque
 * 
 */
public class Deque<Item> implements Iterable<Item> {
	private Node sentinel; // a special node that can represent the whole deque
	private int size; // number of nodes in deque, not including sentinel

	private class Node {
		private Item item;
		private Node prev;
		private Node next;
	}

	/**
	 * construct an empty deque, initialize sentinel node
	 */
	public Deque() {
		sentinel = new Node();
		sentinel.prev = sentinel.next = sentinel;
		size = 0;
	}

	/**
	 * tell if deque is empty or not
	 * 
	 * @return true if deque is empty, else false
	 */
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * tell the size of deque
	 * 
	 * @return number of items in deque
	 */
	public int size() {
		return size;
	}

	/**
	 * add an item to the front of deque
	 * 
	 * @param item item to be inserted
	 */
	public void addFirst(Item item) {
		if (item == null) {
			throw new java.lang.NullPointerException("adding a null item");
		}
		Node newFirstNode = new Node(); // create an empty node
		newFirstNode.item = item;

		// maintain data structure
		Node oldFirstNode = sentinel.next;
		sentinel.next = newFirstNode;
		oldFirstNode.prev = newFirstNode;
		newFirstNode.prev = sentinel;
		newFirstNode.next = oldFirstNode;

		size++; // update the size
	}

	/**
	 * add an item to the end of deque
	 * 
	 * @param item item to be inserted
	 */
	public void addLast(Item item) {
		if (item == null) {
			throw new java.lang.NullPointerException("adding a null item");
		}
		Node newLastNode = new Node();
		newLastNode.item = item;

		Node oldLastNode = sentinel.prev;
		sentinel.prev = newLastNode;
		oldLastNode.next = newLastNode;
		newLastNode.prev = oldLastNode;
		newLastNode.next = sentinel;

		size++;
	}

	/**
	 * remove and return the item from the front
	 * 
	 * @return the item used to be the front of deque
	 */
	public Item removeFirst() {
		if (isEmpty()) {
			throw new java.util.NoSuchElementException("removing from an empty deque");
		}
		Node oldFirstNode = sentinel.next;
		sentinel.next = sentinel.next.next;
		sentinel.next.prev = sentinel;
		size--;
		return oldFirstNode.item;
	}

	/**
	 * remove and return the item from the end
	 * 
	 * @return the item used to be the end of deque
	 */
	public Item removeLast() {
		if (isEmpty()) {
			throw new java.util.NoSuchElementException("removing from an empty deque");
		}
		Node oldLastNode = sentinel.prev;
		sentinel.prev = sentinel.prev.prev;
		sentinel.prev.next = sentinel;
		size--;
		return oldLastNode.item;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<Item> iterator() {
		return new DequeIterator();
	}

	/*
	 * an innter class that defines the iterator of deque
	 */
	private class DequeIterator implements Iterator<Item> {
		private Node current; // a cursor traversing through deque

		/**
		 * construct an iterator of deque
		 */
		public DequeIterator() {
			current = sentinel.next; // sentinel points to first node
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Iterator#hasNext()
		 */
		@Override
		public boolean hasNext() {
			return current != sentinel; // the last node points to sentinel
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Iterator#next()
		 */
		@Override
		public Item next() {
			if (!hasNext())
				throw new NoSuchElementException();
			Item item = current.item;
			current = current.next;
			return item;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Iterator#remove()
		 */
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

	// helper method used for testing
	private void showList() {
		Node current = sentinel.next;
		while (current != sentinel) {
			System.out.print(current.item + ", ");
			current = current.next;
		}
		System.out.println();
	}

	// unit testing (optional)
	public static void main(String[] args) {
		Deque<String> deque = new Deque<String>();
		System.out.println("Is the deque empty? " + deque.isEmpty());
		System.out.println("size = " + deque.size());

		System.out.println("Now adding items...");
		deque.addFirst("a"); // a
		deque.addFirst("b"); // ba
		deque.addFirst("c"); // cba
		deque.addLast("d"); // cbad
		deque.addLast("e"); // cbade

		System.out.println("After adding 5 items, the list contains: ");
		deque.showList(); // cbade

		System.out.println("Is the deque empty? " + deque.isEmpty());
		System.out.println("size = " + deque.size());

		System.out.println("Now removing items...");
		deque.removeFirst(); // bade
		deque.removeLast(); // bad

		System.out.println("After removing 2 items, the list contains: ");
		deque.showList(); // bad

		System.out.println("Is the deque empty? " + deque.isEmpty());
		System.out.println("size = " + deque.size());

		System.out.println("Creating the an iterator...");
		Iterator<String> iterator = deque.iterator();

		System.out.println("Using the iterator to print out the list");
		while (iterator.hasNext()) {
			System.out.print(iterator.next() + " ");
		}
		System.out.println();

		System.out.println("Using the foreach loop to print out the list");
		for (String s : deque) {
			StdOut.print(s + " ");
		}

		StdOut.println("Testing multiple iterators...");
		// b a d
		for (String s1 : deque) {
			for (String s2 : deque) {
				StdOut.println(s1 + " " + s2);
			}
		}

	}

}
