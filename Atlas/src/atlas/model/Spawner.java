package atlas.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static atlas.model.BodyType.AU;

/**
 * Helper class used to spawn many bodies given a template or list of template bodies to copy.
 * Bodies are spawned uniformly in space and frequency.
 */
public class Spawner {
    public Spawner() {
    }

    public List<Body> spawn(int n, Body template, double originX, double originY, double radius) {
        List<Body> generatedBodies = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            generatedBodies.add(spawn(template, originX, originY, radius));
        }
        return generatedBodies;
    }

    public List<Body> spawn(int n, List<Body> templates, double originX, double originY, double radius) {
        List<Body> generatedBodies = new ArrayList<>();
        Random ran = new Random();
        for (int i = 0; i < n; i++) {
            int ti = ran.nextInt(templates.size());
            generatedBodies.add(spawn(templates.get(ti), originX, originY, radius));
        }
        return generatedBodies;
    }

    private Body spawn(Body template, double originX, double originY, double radius) {
        double theta = Math.random() * 2 * Math.PI;
        double x = originX + radius * Math.cos(theta);
        double y = originY + radius * Math.sin(theta);
        Body generated = new BodyImpl(template);
        generated.setName(template.getName() + "_clone");
        generated.setPosX(x);
        generated.setPosY(y);
        return generated;
    }

    public static void main(String[] args) {
        Spawner spwn = new Spawner();
        System.out.println("Spawn 3 Earths: ");
        spwn.spawn(3,
                EpochJ2000.EARTH.getBody(),
                EpochJ2000.SUN.getBody().getPosX(),
                EpochJ2000.SUN.getBody().getPosY(),
                AU).stream().forEach(System.out::println);

        System.out.println("Spawn 5 Mars/Jupiter: ");
        spwn.spawn(5,
                Arrays.stream(EpochJ2000.values()).map(ep -> ep.getBody()).collect(Collectors.toList()),
                EpochJ2000.SUN.getBody().getPosX(),
                EpochJ2000.SUN.getBody().getPosY(),
                AU).stream().forEach(System.out::println);
    }
}
