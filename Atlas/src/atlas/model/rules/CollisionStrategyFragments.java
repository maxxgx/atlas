package atlas.model.rules;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import atlas.model.Body;
import atlas.model.BodyImpl;
import atlas.model.BodyType;

/**
 * A specific collision strategy in which the smaller body explodes in many
 * fragments that can orbit around the bigger body or collide with it as well.
 *
 */
public class CollisionStrategyFragments extends CollisionStrategy {

	private static final long serialVersionUID = -1811886471163319254L;

	private final static double ATTRACTING_FRAGMENTS_PERCENTAGE = 1;// 1 is 100%
	// number of times to shrink mass
	private final static int MAX_REDUCTION = 5000; // smallest fragment
	private final static int MIN_REDUCTION = 10; // largest fragment

	private final static double MAX_ANGLE = 120;
	private final static double ANG_OFFSET = 5;
	private final static int MAX_FRAGMENTS = (int) (MAX_ANGLE / ANG_OFFSET);

	private int numFragments;

	/**
	 * Default constructor, it spawns the maximum amount of fragments within the
	 * parameters.
	 */
	public CollisionStrategyFragments() {
		this.numFragments = MAX_FRAGMENTS;
	}

	/**
	 * Construct the class with the specified number of fragments.
	 * 
	 * @param fragments
	 *            the number of fragments to spawn on collision, if it exceeds
	 *            the limit, it is set to that limit
	 */
	public CollisionStrategyFragments(int fragments) {
		this.numFragments = fragments > MAX_FRAGMENTS ? MAX_FRAGMENTS : fragments;
	}

	@Override
	public List<Body> manageCollision(List<Body> sim, Body a, Body b) {
		if (this.detectCollision(a, b)) {
			// target = the smaller one | targeted is the bigger one
			Body target = a.getMass() < b.getMass() ? a : b;
			Body targeted = target.equals(a) ? b : a;
			switch (target.getType()) {
			case FRAGMENT:
				break;
			/* Black holes takes priority */
			case BLACKHOLE:
				if (targeted.getType() != BodyType.BLACKHOLE) {
					targeted = target;
					target = targeted.equals(a) ? b : a;
				}
				break;
			case STAR:
			case PLANET:
			case DWARF_PLANET:
			case SATELLITE:
			default:
				sim.addAll(this.spawnFragments(target, targeted));
				break;
			}

			// The larger body gains about 90% of the smaller body
			double massGain = 0.9 * target.getMass() + 1;
			// Radius increases according to the masses ratio
			double radiusGain = target.getProperties().getRadius() * (massGain / targeted.getMass());
			targeted.getProperties().setRadius(radiusGain * 2 + targeted.getProperties().getRadius());
			targeted.setMass(massGain + targeted.getMass());
			// The smaller body is removed
			sim.remove(target);
		}
		return sim;
	}

	/* It spawns numFragments, using the parameters */
	private List<Body> spawnFragments(Body body, Body parent) {
		List<Body> fragments = new ArrayList<>();

		int numAttract = (int) (numFragments * ATTRACTING_FRAGMENTS_PERCENTAGE);
		Random rand = new Random();

		for (int i = 0; i < numFragments; i++, numAttract--) {
			// reduction = how big is the fragment
			double reduction = rand.nextInt(MAX_REDUCTION - MIN_REDUCTION + 1) + MIN_REDUCTION;

			// the angle of the fragment's position vector
			double rotAngle = Math.toRadians(i * ANG_OFFSET);
			rotAngle = i % 2 == 0 ? rotAngle : -rotAngle;
			double x = body.getPosX() + body.getProperties().getRadius() * Math.cos(rotAngle);
			double y = body.getPosY() + body.getProperties().getRadius() * Math.sin(rotAngle);
			double vx = body.getVelX() * Math.cos(rotAngle);
			double vy = body.getVelX() * Math.sin(rotAngle);
			double length = Math.sqrt(x * x + y * y);
			// off set half of the body's radius from the center
			double multiplier = (length + body.getProperties().getRadius() / 2) / length;
			x = multiplier * x;
			y = multiplier * y;

			/* Creating the fragment */
			Double temp = null; // setting temperature
			if (body.getProperties().getTemperature().isPresent()
					&& parent.getProperties().getTemperature().isPresent()) {
				temp = body.getProperties().getTemperature().get()
						+ parent.getProperties().getTemperature().get() * rand.nextDouble();
			}
			Long orbitalPeriod = null;
			Body fragment = new BodyImpl.Builder().name(body.getName().concat("_FRAG").concat("" + i))
					.type(BodyType.FRAGMENT).imagePath(Body.IMAGE_FOLDER + "fragment.png")
					.mass(body.getMass() / reduction).posX(x).posY(y)
					.velX(vx).velY(vy)
					.properties(new Body.Properties(body.getProperties().getRadius() / numFragments,
							body.getProperties().getRotationPeriod() * (long) rand.nextDouble(), orbitalPeriod, parent,
							temp))
					.build();
			fragment.setAttracting(numAttract > 0);
			numAttract = numAttract > 0 ? numAttract : 0;

			fragments.add(fragment);
		}
		return fragments;
	}
}