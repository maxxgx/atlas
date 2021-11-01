package atlas.model.rules;

import atlas.model.Body;

import java.util.ArrayList;
import java.util.List;

/**
 * This algorithm computes, for each body, the net force from all other bodies.
 * Complexity is N^2. Pros: accurate results, simple. Cons: impossible to use
 * when there are a lot of elements.
 */
public class AlgorithmBruteForce extends Algorithm {

    private static final long serialVersionUID = -766146245161256993L;

    /**
     * Default empty constructor.
     */
    public AlgorithmBruteForce() {
        super();
        super.collisionStrategy = null;
    }

    /**
     * Construct the algorithm with the specified collision algorithm.
     *
     * @param collisionStrategy
     */
    public AlgorithmBruteForce(CollisionStrategy collisionStrategy) {
        super();
        super.collisionStrategy = collisionStrategy;
    }

    @Override
    public List<Body> exceuteUpdate(List<Body> bodies, double sec) {
        // 2 loops --> N^2 complexity
        ArrayList<Body> copy = new ArrayList<>(bodies);
        for (Body b : copy) {
            if (b.isAttracting()) {
                b.resetForce();
                for (Body c : copy) {
                    if (!b.equals(c)) {
                        if (bodies.contains(b) && bodies.contains(c) && super.collisionStrategy != null) {
                            super.collisionStrategy.manageCollision(bodies, b, c);
                        }
                        b.addForce(c);
                    }
                }
                b.updatePos(sec);
            }
        }
        return bodies;
    }
}
