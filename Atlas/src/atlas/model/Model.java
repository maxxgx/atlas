package atlas.model;

import java.util.List;

import atlas.model.rules.Algorithm;
import atlas.model.rules.CollisionStrategy;

/**
 * Interface for a generic model implementation.
 *
 */
public interface Model {

	/**
	 * Method called used by the controller to get all the bodies to be rendered
	 * by the view.
	 * 
	 * @return List of bodies to be rendered
	 */
	public List<Body> getBodiesToRender();

	/**
	 * Used to advance in the simulation by a delta time.
	 * 
	 * @param sec
	 *            seconds to progress in the simulation
	 */
	public void updateSim(double sec);

	/**
	 * Adds a body to the solar system in circural orbit.
	 * 
	 * @param b
	 *            the body to be added
	 */
	public void addBody(Body b);

	/**
	 * Time of the simulation, either a date (i.e. 01/01/2001) or simply a time
	 * counter (i.e. 3.25 years).
	 * 
	 * @return time of the simulation
	 */
	public String getTime();

	/**
	 * Allows to change the n-body simulation algorithm.
	 * 
	 * @param algorithm
	 *            the new algorithm
	 */
	public void setAlgorithm(Algorithm algorithm);

	/**
	 * Changes the model's collision rules.
	 * 
	 * @param collision
	 *            the new collision rules
	 */
	public void setCollsion(CollisionStrategy collision);

	/**
	 * 
	 * @return The clock of the simulation
	 */
	public SimClock getClock();

	/**
	 * Sets the simulation's clock.
	 * 
	 * @param clock
	 *            the new clock
	 */
	public void setClock(SimClock clock);
}
