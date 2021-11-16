package atlas.model.rules;

import atlas.model.Body;

import java.util.List;

/**
 * This class represents a N-Body algorithm, which is used to update the
 * simulation.
 */
public abstract class Algorithm implements java.io.Serializable {

    private static final long serialVersionUID = -766146245161256993L;

    protected CollisionStrategy collisionStrategy = new CollisionStrategyFragments();

    /**
     * It updates the simulation according to a specific n-body algorithm
     * implementation.
     *
     * @param bodies input bodies to be updated
     * @param sec    time step of the update
     * @return the updated bodies
     */
    public abstract List<Body> exceuteUpdate(List<Body> bodies, double sec);

    /**
     * @return the current algorithm's collision system
     */
    public CollisionStrategy getCollisionStrategy() {
        return this.collisionStrategy;
    }

    /**
     * Sets a new collision strategy, changes the collision rules.
     *
     * @param strategy the new strategy
     */
    public void setCollisionStrategy(CollisionStrategy strategy) {
        this.collisionStrategy = strategy;
    }

}
