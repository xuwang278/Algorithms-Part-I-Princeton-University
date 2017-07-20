package assign2;

import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/**
 * 
 * @author Xu Wang
 * 
 *         04/01/2017
 * 
 *         A representation of a queue that the item removed is chosen uniformly
 *         at random from items in the data structure.
 *
 * @param <Item> a generic data type stored in RandomizedQueue
 */
public class RandomizedQueue<Item> implements Iterable<Item> {
	private Item[] queue;
	private int size;

	/**
	 * construct an empty RandomizedQueue
	 */
	public RandomizedQueue() {
		queue = (Item[]) new Object[2];
		size = 0;
	}

	/**
	 * check if RandomizedQueue is empty or not
	 * 
	 * @return true if the queue is empty, else false
	 */
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * tell the number of items on the queue
	 * 
	 * @return the number of items on the queue
	 */
	public int size() {
		return size;
	}

	/**
	 * add an item to the queue
	 * 
	 * @param item item to be inserted
	 */
	public void enqueue(Item item) {
		if (item == null) {
			throw new java.lang.NullPointerException("adding a null item");
		}
		if (size == queue.length)
			resize(2 * queue.length);
		queue[size++] = item;
	}

	/*
	 * a helper method to resize to size of array when necessary
	 */
	private void resize(int capacity) {
		Item[] temp = (Item[]) new Object[capacity];
		for (int i = 0; i < size; i++) {
			temp[i] = queue[i];
		}
		queue = temp; // no holes in the new array
	}

	/**
	 * remove a random selected item and return it
	 * 
	 * @return a random selected item used to be in the queue
	 */
	public Item dequeue() {
		if (isEmpty()) {
			throw new java.util.NoSuchElementException("empty deque");
		}
		int randomIndex = StdRandom.uniform(size);
		Item chosenItem = queue[randomIndex];
		Item lastItem = queue[size - 1];
		// replace with the last item to maintain contiguous memory
		queue[randomIndex] = lastItem;
		queue[size - 1] = null; // avoid of loitering
		size--;
		if (size > 0 && size == queue.length / 4) // shrink when necessary
			resize(queue.length / 2);
		return chosenItem;
	}

	/**
	 * @return a random selected item
	 */
	public Item sample() {
		if (isEmpty()) {
			throw new java.util.NoSuchElementException("empty deque");
		}
		int randomIndex = StdRandom.uniform(size);
		Item chosenItem = queue[randomIndex];
		return chosenItem;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<Item> iterator() {
		return new RandomizedQueueIterator();
	}

	/*
	 * a helper method defining iterator of RandomizedQueue that traverses the
	 * queue in a random order
	 * 
	 * shuffle a array containing indices, based on which go over items in the
	 * array containing items
	 */
	private class RandomizedQueueIterator implements Iterator<Item> {
		private int[] randomIndex; // contains the randomly arranged index
		private int counter; // cursor in randomIndex
		private Item current; // contains the returned item

		/**
		 * constructs an iterator
		 */
		public RandomizedQueueIterator() {
			randomIndex = new int[size];
			for (int i = 0; i < size; i++) {
				randomIndex[i] = i;
			}
			// each entry contains a randomly arranged index
			StdRandom.shuffle(randomIndex);
			counter = 0;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Iterator#hasNext()
		 */
		@Override
		public boolean hasNext() {
			return counter < size;
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
			current = queue[randomIndex[counter++]];
			return current;
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

	// used for unit testing in main
	private int getCapacity() {
		return queue.length;
	}

	// unit testing (optional)
	public static void main(String[] args) {
		StdOut.println("Creating... ");
		RandomizedQueue<String> queue = new RandomizedQueue<String>();
		StdOut.println("Empty? " + queue.isEmpty());
		StdOut.println("size = " + queue.size());
		StdOut.println("Capacity = " + queue.getCapacity());

		StdOut.println();
		StdOut.println("Adding 5 items... ");
		queue.enqueue("a");
		queue.enqueue("b");
		queue.enqueue("c");
		queue.enqueue("d");
		queue.enqueue("e");
		StdOut.println("Empty? " + queue.isEmpty());
		StdOut.println("size = " + queue.size());
		StdOut.println("Capacity = " + queue.getCapacity());

		StdOut.println();
		StdOut.println("Choosing samples... ");
		StdOut.println("Sample 1: " + queue.sample());
		StdOut.println("size = " + queue.size());
		StdOut.println("Capacity = " + queue.getCapacity());

		StdOut.println("Sample 2: " + queue.sample());
		StdOut.println("size = " + queue.size());
		StdOut.println("Capacity = " + queue.getCapacity());

		StdOut.println();
		StdOut.println("RandomlyRemoving items...");
		StdOut.println("Randomly remove 1st item: ");
		StdOut.println(queue.dequeue());
		StdOut.println("size = " + queue.size());
		StdOut.println("Capacity = " + queue.getCapacity());

		StdOut.println("Randomly remove 2nd item: ");
		StdOut.println(queue.dequeue());
		StdOut.println("size = " + queue.size());
		StdOut.println("Capacity = " + queue.getCapacity());

		StdOut.println("Randomly remove 3rd item: ");
		StdOut.println(queue.dequeue());
		StdOut.println("size = " + queue.size());
		StdOut.println("Capacity = " + queue.getCapacity());

		StdOut.println("Randomly remove 4th item: ");
		StdOut.println(queue.dequeue());
		StdOut.println("size = " + queue.size());
		StdOut.println("Capacity = " + queue.getCapacity());

		StdOut.println("Randomly remove 5th item: ");
		StdOut.println(queue.dequeue());
		StdOut.println("size = " + queue.size());
		StdOut.println("Capacity = " + queue.getCapacity());

		StdOut.println();
		StdOut.println("Adding 5 items again... ");
		queue.enqueue("a");
		queue.enqueue("b");
		queue.enqueue("c");
		queue.enqueue("d");
		queue.enqueue("e");

		StdOut.println("Creating interator...");
		Iterator<String> iterator = queue.iterator();

		for (String s1 : queue) {
			for (String s2 : queue) {
				StdOut.println(s1 + "  " + s2);
			}
		}
	}

}
