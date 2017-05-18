package atlas.model;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;

import atlas.utils.Pair;

/**
 * This class represents the trail that a body leaves behind when moving. It
 * functions as a fixed size queue, so that when it reaches maximum capacity it
 * removes the oldest element to make room for the new one.
 *
 */
public class Trail {

	private static final int TRAILSIZE = 500;
	/* Determines the storing frequency */
	private static final int STORE_PER_UPDATE = 50;

	private Deque<Pair<Double, Double>> points;
	private int length;
	private int timesCalled = 0;

	/**
	 * Default constructor witch default size.
	 */
	public Trail() {
		this(TRAILSIZE);
	}

	/**
	 * Construct a trail made of n points.
	 */
	public Trail(int length) {
		this.points = new ArrayDeque<>();
		this.length = length;
	}

	/**
	 * Adds a point to the trail.
	 * 
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 */
	public void addPoint(double x, double y) {
		if (this.shouldAdd()) {
			this.points.addFirst(new Pair<Double, Double>(x, y));
			if (this.points.size() - 1 == length) {
				this.points.removeLast();
			}
		}
	}

	/* Adding policy according to STORE parameter */
	private boolean shouldAdd() {
		if (this.timesCalled >= this.length) {
			this.timesCalled = 0;
		}
		return (timesCalled++ % Trail.STORE_PER_UPDATE) == 0;
	}

	/**
	 * Get trail in a series of points in space.
	 * 
	 * @return a collection of the trail points
	 */
	public Collection<Pair<Double, Double>> getPoints() {
		return new ArrayDeque<>(this.points); // defensive copy
	}

	/**
	 * 
	 * @return the length of the trail
	 */
	public long getLength() {
		return this.length;
	}

	/**
	 * Resets the trail by deleting all points.
	 */
	public void reset() {
		this.points = new ArrayDeque<>();
	}
}