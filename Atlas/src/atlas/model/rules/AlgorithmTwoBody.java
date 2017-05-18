package atlas.model.rules;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import atlas.model.Body;

/**
 * This algorithm computes, for each body, the net force from a few selected
 * bodies (the parent of the body and a small number of the largest bodies in
 * the simulation). Complexity is N. Pros: fastest. Cons: not very precise over
 * time.
 * 
 */
public class AlgorithmTwoBody extends Algorithm {

	private static final long serialVersionUID = -7785821713075379790L;

	private static final int BODIES_TO_CONSIDER = 5;

	@Override
	public List<Body> exceuteUpdate(List<Body> bodies, double sec) {
		// select largest body of the first one
		List<Body> ordered = bodies.stream().sorted((i, j) -> (int) (j.getMass() - i.getMass()))
				.collect(Collectors.toList());

		List<Body> targets = new LinkedList<>();
		for (int i = 0; i < BODIES_TO_CONSIDER && i < ordered.size(); i++) {
			targets.add(ordered.get(i));
		}

		for (Body b : bodies) {
			if (b.isAttracting()) {
				b.resetForce();

				/* Add the force from the parent */
				b.getProperties().getParent().ifPresent(i -> {
					if (ordered.contains(i)) {
						b.addForce(i);
					}
				});

				/* Add the force from a few selected bodies */
				targets.forEach(j -> {
					if (!b.equals(j) && !j.equals(b.getProperties().getParent().orElse(b))) {
						b.addForce(j);
					}
				});

				b.updatePos(sec);
			}
		}
		return bodies;
	}
}
