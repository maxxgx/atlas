package atlas.model;

import atlas.model.rules.AlgorithmBarnesHut;
import atlas.model.rules.AlgorithmBruteForce;
import atlas.model.rules.CollisionStrategyFragments;
import atlas.utils.Pair;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static atlas.model.Body.Properties.celsiusToKelvin;
import static atlas.model.BodyType.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

    @org.junit.Test
    public void testBurnesHut() {
        Model modelBH = new ModelImpl();
        modelBH.setAlgorithm(new AlgorithmBarnesHut());
        Model modelBruteForce = new ModelImpl();
        modelBruteForce.setAlgorithm(new AlgorithmBruteForce());
        List<Body> bodies = new ArrayList<>();
        new Spawner().spawn(100,
                Arrays.stream(EpochJ2000.values()).map(ep -> ep.getBody()).collect(Collectors.toList()),
                EpochJ2000.SUN.getBody().getPosX(),
                EpochJ2000.SUN.getBody().getPosY(),
                AU * 100).stream().forEach(b -> bodies.add(b));
        for (Body b : bodies) {
            modelBH.addBody(new BodyImpl(b));
            modelBruteForce.addBody(new BodyImpl(b));
        }
        // update iterations
        for (int i = 1; i <= 10000; i++) {
            modelBH.updateSim(1);
            modelBruteForce.updateSim(1);
            if (i % 1000 == 0) {
                List<Body> bhu = modelBH.getBodiesToRender();
                List<Body> bbf = modelBruteForce.getBodiesToRender();
                assertEquals(bhu.size(), bbf.size());
                // check if positions are similar
                for (int bi = 0; bi < bhu.size(); bi++) {
                    Body a = bhu.get(bi);
                    Body b = bbf.get(bi);
                    assertEquals(a.getPosX(), b.getPosX(), 1e8);
                    assertEquals(a.getPosY(), b.getPosY(), 1e8);
                }
            }
        }
    }

    private void setSim(Model m, int numBodies) {
        new Spawner().spawn(numBodies,
                Arrays.stream(EpochJ2000.values()).map(ep -> ep.getBody()).collect(Collectors.toList()),
                EpochJ2000.SUN.getBody().getPosX(),
                EpochJ2000.SUN.getBody().getPosY(),
                AU * 100).stream().forEach(b -> m.addBody(b));
    }

    public double runModelTimings(String name, Model model, int size, int iters) {
        setSim(model, size);

        long tik = System.currentTimeMillis();
        for (int i = 1; i <= iters; i++) {
            model.updateSim(1);
        }
        long took = System.currentTimeMillis() - tik;
        double speed = (double) iters / (double) took * 1000;
        System.out.println("Model " + name + " took " + took + "ms" +
                " overall speed = " + speed + " update/sec");
        return speed;
    }

    @org.junit.Test
    public void testBurnesHutTimings1000() {
        Model model = new ModelImpl();
        model.setAlgorithm(new AlgorithmBarnesHut());
        assertTrue(runModelTimings("Burnes-Hut 1000", model, 1000, 100) > 10);
    }

    @org.junit.Test
    public void testBurnesHutTimings500() {
        Model model = new ModelImpl();
        model.setAlgorithm(new AlgorithmBarnesHut());
        assertTrue(runModelTimings("Burnes-Hut 500", model, 500, 10000) > 1000);
    }

    @org.junit.Test
    public void testBurnesHutTimings100() {
        Model model = new ModelImpl();
        model.setAlgorithm(new AlgorithmBarnesHut());
        assertTrue(runModelTimings("Burnes-Hut 100", model, 100, 10000) > 1000);
    }

    @org.junit.Test
    public void testBruteForceTimings500() {
        Model model = new ModelImpl();
        model.setAlgorithm(new AlgorithmBruteForce());
        assertTrue(runModelTimings("Brute force 500", model, 500, 10) > 1);
    }

    @org.junit.Test
    public void testBruteForceTimings100() {
        Model model = new ModelImpl();
        model.setAlgorithm(new AlgorithmBruteForce());
        assertTrue(runModelTimings("Brute force 100", model, 100, 1000) > 1);
    }
}
