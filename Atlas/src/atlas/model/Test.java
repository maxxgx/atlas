package atlas.model;

import static atlas.model.Body.Properties.celsiusToKelvin;
import static atlas.model.BodyType.AU;
import static atlas.model.BodyType.EARTH_DAY;
import static atlas.model.BodyType.EARTH_MASS;
import static atlas.model.BodyType.PLANET;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Assert;

import atlas.model.rules.AlgorithmBruteForce;
import atlas.model.rules.CollisionStrategyFragments;
import atlas.utils.Pair;

public class Test {

	/**
	 * Test fragments collision.
	 */
	@org.junit.Test
	public void testCollision() {

		Model m = new ModelImpl();

		m.setAlgorithm(new AlgorithmBruteForce());
		m.setCollsion(new CollisionStrategyFragments());

		Body one = EpochJ2000.SUN.getBody();
		one.setPosX(0);
		one.setPosY(0);

		Body two = EpochJ2000.VENUS.getBody();
		two.setPosX(100 * 1000 * 1000);
		two.setPosY(0);

		two.setVelocity(new Pair<>(5000.0, 0.0));

		m.addBody(one);
		m.addBody(two);
		for (int i = 0; i < 20; i++) {
			m.updateSim(5000);
		}

		assertTrue(m.getBodiesToRender().size() > 2);
	}

	/**
	 * Test builer
	 */
	@org.junit.Test
	public void testBuilder() {
		try {
			new BodyImpl.Builder().name("test").build();
		} catch (IllegalStateException e) {
		} catch (Exception ex) {
			Assert.fail("Builder must throw IllegalStateException if mass or type is not initialized");
		}
		Body b = new BodyImpl.Builder().name("Earth").type(PLANET).imagePath(Body.IMAGE_FOLDER + "earth.png")
				.mass(EARTH_MASS).posX(-1.756637922977121E-01 * AU).posY(9.659912850526894E-01 * AU)
				.velX((-1.722857156974861E-02 * AU) / EARTH_DAY).velY((-3.015071224668472E-03 * AU) / EARTH_DAY)
				.properties(new Body.Properties(6371 * 1000, EARTH_DAY, null, null, celsiusToKelvin(14.00))).build();

		assertEquals("Earth", b.getName());
		assertTrue(b.getProperties() != null);
		/*Distance +- AU*/
		assertTrue(b.distanceTo(EpochJ2000.SUN.getBody()) < AU * 1.1);
		assertTrue(b.distanceTo(EpochJ2000.SUN.getBody()) > AU * 0.95);
		
		b.setMass(100.0);
		assertTrue(b.getMass() == 100.0);
	}

	/**
	 * Test gravity forces.
	 */
	@org.junit.Test
	public void testSim() {
		Body one = EpochJ2000.SUN.getBody();
		one.setPosX(0);
		one.setPosY(0);

		Body two = EpochJ2000.VENUS.getBody();
		
		double initialPosX = 100 * 1000 * 1000;
		two.setPosX(initialPosX);
		two.setPosY(0);
		two.setVelocity(new Pair<>(0.0, 0.0));
		
		two.resetForce();
		
		two.addForce(one);
		
		two.updatePos(5000);
		
		assertTrue(two.getPosX() < initialPosX);
	}
}