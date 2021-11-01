package atlas.model;

import atlas.utils.Pair;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Random;

/**
 * Implementation of Body interface.
 */
public class BodyImpl implements Body, java.io.Serializable {

    private static final long serialVersionUID = 3305253121341825047L;
    private static final Random RAND = new Random();

    private BodyType type;
    private transient Long id;
    private String imagePath = null;
    private String name = null;
    private double posx, posy;
    private double velx, vely;
    private double fx, fy;
    private double mass;
    private transient Trail trail = new Trail();
    private Properties properties;
    private boolean attracting = true;
    private boolean trailDisabled = false;

    /**
     * Complete Constructor.
     *
     * @param type
     * @param id
     * @param imagePath
     * @param name
     * @param posx
     * @param posy
     * @param velx
     * @param vely
     * @param mass
     * @param properties
     */
    public BodyImpl(BodyType type, long id, String imagePath, String name, double posx, double posy, double velx,
                    double vely, double mass, Properties properties) {
        this.type = type;
        this.id = (id != 0 ? id : generateId());
        this.imagePath = imagePath;
        this.name = name;
        this.posx = posx;
        this.posy = posy;
        this.velx = velx;
        this.vely = vely;
        this.mass = mass;
        this.properties = properties;
    }

    public BodyImpl(Body b) {
        this(b.getType(), generateId(), b.getImagePath(), b.getName(), b.getPosX(), b.getPosY(), b.getVelX(),
                b.getVelY(), b.getMass(), b.getProperties());
    }

    private static long generateId() {
        return System.currentTimeMillis() + RAND.nextLong();
    }

    @Override
    public BodyType getType() {
        return this.type;
    }

    @Override
    public void setType(BodyType type) {
        this.type = type;
    }

    @Override
    public long getId() {
        this.id = id == null ? generateId() : id;
        return this.id;
    }

    @Override
    public String getImagePath() {
        if (this.imagePath == null || !this.imagePath.startsWith(IMAGE_FOLDER)) {
            this.imagePath = chooseImage();
        }
        return this.imagePath;
    }

    private String chooseImage() {
        return IMAGE_FOLDER + "jupiter.png";
    }

    @Override
    public String getName() {
        return this.name == null ? "Unknown" : name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public double getMass() {
        return this.mass;
    }

    @Override
    public void setMass(double mass) {
        this.mass = mass;
    }

    @Override
    public Body add(Body b) {
        double sumMass = this.mass + b.getMass();
        double centerMassX = (this.posx * this.mass + b.getPosX() * b.getMass()) / (sumMass);
        double centerMassY = (this.posy * this.mass + b.getPosY() * b.getMass()) / (sumMass);
        Body combined = new BodyImpl(this);
        combined.setMass(sumMass);
        combined.setPosX(centerMassX);
        combined.setPosY(centerMassY);
        return combined;
    }

    @Override
    public void addForce(Body b) {
        double EPS = 1; // softening parameter (just to avoid infinities)
        double dx = b.getPosX() - this.posx;
        double dy = b.getPosY() - this.posy;
        double dist = Math.sqrt(dx * dx + dy * dy);
        double F = (BodyType.G * this.getMass() * b.getMass()) / (dist * dist + EPS);
        this.fx += F * dx / dist;
        this.fy += F * dy / dist;
    }

    @Override
    public void resetForce() {
        this.fx = 0.0;
        this.fy = 0.0;
    }

    @Override
    public void updatePos(double dt) {
        this.velx += dt * fx / mass;
        this.vely += dt * fy / mass;
        this.posx += dt * velx;
        this.posy += dt * vely;
        // updates the rotation angle
        this.properties.updateRotation(dt);
        this.checkTrail();
        if (!trailDisabled) this.trail.addPoint(this.posx, this.posy);
        // updates orbital period with the 3rd keplar law
        if (this.properties.getParent().isPresent()) {
            Body parent = this.properties.getParent().get();
            this.properties.setOrbitalPeriod((long) ((2 * Math.PI) / Math.sqrt((BodyType.G * parent.getMass()))
                    * Math.pow(distanceTo(parent), 1.5)));
        }
    }

    @Override
    public double distanceTo(Body b) {
        double dx = this.posx - b.getPosX();
        double dy = this.posy - b.getPosY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public double getPosX() {
        return this.posx;
    }

    @Override
    public void setPosX(double x) {
        this.posx = x;
    }

    @Override
    public double getPosY() {
        return this.posy;
    }

    @Override
    public void setPosY(double y) {
        this.posy = y;
    }

    @Override
    public double getVelX() {
        return this.velx;
    }

    @Override
    public double getVelY() {
        return this.vely;
    }

    @Override
    public void setVelocity(Pair<Double, Double> vel) {
        this.velx = vel.getX();
        this.vely = vel.getY();
    }

    @Override
    public double getTotalVelocity() {
        return Math.sqrt(velx * velx + vely * vely);
    }

    @Override
    public void setTotalVelocity(double vt) {
        if (vt < 0) {
            throw new IllegalStateException();
        }
        double velocity = this.getTotalVelocity();
        double change = vt / velocity;
        this.velx = change * this.velx;
        this.vely = change * this.vely;
    }

    @Override
    public Properties getProperties() {
        /*
         * Returns a defensive copy of the body's properties?? I dont think so
         */
        return this.properties;
    }

    @Override
    public Collection<Pair<Double, Double>> getTrail() {
        this.checkTrail();
        return new ArrayDeque<>(this.trail.getPoints());// defensive copy
    }

    /**
     * Creates a new trail if there isn't one already.
     */
    private void checkTrail() {
        if (trailDisabled) return;
        if (this.trail == null) {
            this.trail = new Trail();
        }
    }

    @Override
    public boolean isAttracting() {
        return this.attracting;
    }

    @Override
    public void setAttracting(boolean attracting) {
        this.attracting = attracting;
    }

    @Override
    public void updateInfo(Body b) {
        this.name = b.getName();
        this.type = b.getType();
        this.setTotalVelocity(b.getTotalVelocity());
        this.mass = b.getMass();
        this.imagePath = b.getImagePath();
        this.properties.updateInfo(b.getProperties());
    }

    @Override
    public boolean in(Quad q) {
        return q.contains(this.getPosX(), this.getPosY());
    }

    @Override
    public void toggleTrail() {
        this.trailDisabled = !this.trailDisabled;
        this.trail = new Trail();
    }

    @Override
    public String toString() {
        NumberFormat formatter = new DecimalFormat("0.### ### ### ### ###E0");
        double AU = 149597870.700 * 1000;

        return "[Name: " + this.getName() + ", posX/AU = " + formatter.format(this.posx / AU) + ", posY/AU = "
                + formatter.format(this.posy / AU) + "]" + "\n"
                /*
                 * + "| forceX =" + formatter.format(this.fx) + " forceY =" +
                 * formatter.format(this.fy)
                 */
                + "|| velX =" + formatter.format(this.velx / AU * 86400) + " velY ="
                + formatter.format(this.vely / AU * 86400);
    }

    /**
     * This inner class is used to implement the Builder design pattern.
     */
    public static class Builder {
        private BodyType type;
        private long id;
        private String imagePath;
        private String name = null;
        private double posx, posy;
        private double velx, vely;
        private double mass;
        private Properties properties;

        public Builder type(BodyType type) {
            this.type = type;
            return this;
        }

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder imagePath(String path) {
            this.imagePath = path;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder posX(double x) {
            this.posx = x;
            return this;
        }

        public Builder posY(double y) {
            this.posy = y;
            return this;
        }

        public Builder velX(double vx) {
            this.velx = vx;
            return this;
        }

        public Builder velY(double vy) {
            this.vely = vy;
            return this;
        }

        public Builder mass(double mass) {
            this.mass = mass;
            return this;
        }

        public Builder properties(BodyImpl.Properties properties) {
            this.properties = properties;
            return this;
        }

        /**
         * Construct the body. If type or mass is not initialized, it throws an
         * IllegalStateException.
         *
         * @return the constructed body
         */
        public BodyImpl build() {
            if (this.type == null || this.mass == 0) {
                throw new IllegalStateException();
            }
            BodyImpl b = new BodyImpl(this.type, this.id, this.imagePath, this.name, this.posx, this.posy, this.velx,
                    this.vely, this.mass, this.properties);
            return b;
        }
    }

}
