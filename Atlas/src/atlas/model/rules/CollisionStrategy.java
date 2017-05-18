package atlas.model.rules;

import java.util.List;

import atlas.model.Body;

/**
 * A generic collision strategy, it decides what happen in case of a collision
 * between two bodies.
 *
 */
public abstract class CollisionStrategy implements java.io.Serializable {

	private static final long serialVersionUID = 2351707986649495199L;

	/**
	 * Detects a collision between two bodies.
	 * 
	 * @param a
	 *            the first body
	 * @param b
	 *            the second body
	 * @return true if there's a collision, false otherwise.
	 */
	protected final boolean detectCollision(Body a, Body b) {
		return a.distanceTo(b) <= a.getProperties().getRadius() + b.getProperties().getRadius();
	}

	/**
	 * Manages the collision through the specified collision rules.
	 * 
	 * @param sim
	 *            the bodies of the simulation
	 * @param a
	 *            the first body to consider
	 * @param b
	 *            the second body to consider
	 * @return the updated body list with collisions.
	 */
	public abstract List<Body> manageCollision(List<Body> sim, Body a, Body b);
}