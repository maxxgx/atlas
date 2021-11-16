package atlas.model;

import atlas.utils.Pair;

import java.util.Collection;
import java.util.Optional;

/**
 * A generic celestial body interface.
 */
public interface Body {

    public static final String IMAGE_FOLDER = "/bodies_images/";

    /**
     * @return the body's type
     */
    public BodyType getType();

    /**
     * Changes body type.
     *
     * @param type the new type
     */
    public void setType(BodyType type);

    /**
     * Gets the unique id of the body, it cannot be changed.
     *
     * @return the unique id of the body
     */
    public long getId();

    /**
     * Gets the body's image path, it's always available
     *
     * @return the path to the body's image
     */
    public String getImagePath();

    /**
     * @return the body's name
     */
    public String getName();

    /**
     * Assigns the given name to the body.
     *
     * @param name the new name
     */
    public void setName(String name);

    /**
     * @return the body's mass in kilograms
     */
    public double getMass();

    /**
     * Sets the body's mass.
     *
     * @param mass the new mass
     */
    public void setMass(double mass);

    /**
     * Returns the combined body with b, center of mass between this and b.
     *
     * @param b body to combine
     */
    public Body add(Body b);

    /**
     * Adds the force relative to an another body using Newton's law of gravity.
     *
     * @param b the body taken into consideration
     */
    public void addForce(Body b);

    /**
     * Resets the total force applied to this body.
     */
    public void resetForce();

    /**
     * Updates the position of the body in the simulation.
     *
     * @param dt time-stamp, determines both accuracy and speed, smaller dt
     *           gives more accuracy but slower speed.
     */
    public void updatePos(double dt);

    /**
     * Calculates the distance from an another body.
     *
     * @param b the target body
     * @return the distance between this body and a body b
     */
    public double distanceTo(Body b);

    /**
     * Returns the x coordinates from the origin.
     *
     * @return the x coordinates in meters
     */
    public double getPosX();

    /**
     * Sets the horizontal position.
     *
     * @param x the x coordinate to set
     */
    public void setPosX(double x);

    /**
     * Returns the y coordinates from the origin.
     *
     * @return the y coordinates in meters
     */
    public double getPosY();

    /**
     * Sets the vertical position.
     *
     * @param y the y coordinate to set
     */
    public void setPosY(double y);

    /**
     * Returns the velocity in the x-axis
     *
     * @return the horizontal velocity x
     */
    public double getVelX();

    /**
     * Returns the velocity in the y-axis
     *
     * @return the vertical velocity y
     */
    public double getVelY();

    /**
     * Sets the velocity with a pair, this methods also changes the direction of
     * the body.
     *
     * @param velocity a pair that represents the velocity, X and Y.
     */
    public void setVelocity(Pair<Double, Double> velocity);

    /**
     * Gets the total velocity of the body, regardless of the direction.
     *
     * @return total velocity of the body
     */
    public double getTotalVelocity();

    /**
     * Sets the total velocity of the body, maintaining the same direction.
     *
     * @param vt total velocity
     */
    public void setTotalVelocity(double vt);

    /**
     * @return the body's properties.
     */
    public Properties getProperties();

    /**
     * This method returns the current trail produced by the movement of the
     * body.
     *
     * @return the collection of the trail points
     */
    public Collection<Pair<Double, Double>> getTrail();

    /**
     * Checks if the body attracts other bodies.
     *
     * @return whether this body attracts others
     */
    public boolean isAttracting();

    /**
     * Set whether the body attracts other bodies.
     */
    public void setAttracting(boolean attract);

    /**
     * Updates the current body with another body's information, it does not
     * make a copy of said body, so it maintains the id.
     *
     * @param b the body to get the info from
     */
    public void updateInfo(Body b);

    /**
     * Checks if the body is in a certain quadrant.
     *
     * @param q quadrant
     */
    public boolean in(Quad q);

    /**
     * Unable/disable adding of trail points.
     */
    public void toggleTrail();

    /**
     * Nested class used to store a celestial body's secondary properties.
     */
    public static class Properties implements java.io.Serializable {

        private static final long serialVersionUID = -689182940096161486L;
        private double radius;
        // Rotazione sul proprio asse
        private long rotationPeriod;
        private double rotationAngle = 0;

        /* Optional properties */
        private Long orbitalPeriod = null;
        private Body parent = null;
        private Double temperature = null;

        /**
         * Converts celsius to kelvin.
         *
         * @param c temperature in celsius
         * @return temperature in kelvin
         */
        public static double celsiusToKelvin(double c) {
            return c + 273.15;
        }

        /**
         * Converts kelvin to celsius.
         *
         * @param c temperature in kelvin
         * @return temperature in celsius
         */
        public static double KelvinToCelsius(double c) {
            return c - 273.15;
        }

        /**
         * The complete constructor.
         *
         * @param radius         radius of the body in meters
         * @param rotationPeriod the time it takes for the body make to rotate 360 deg on
         *                       its axis.
         * @param rotationAngle  the current angle of the rotation
         * @param orbitalPeriod  the time it takes to orbit around its parent
         * @param parent         the parent (body) of this body
         * @param temperature    temperature of the body in kelvin
         */
        public Properties(double radius, long rotationPeriod, double rotationAngle, Long orbitalPeriod, Body parent,
                          Double temperature) {
            this(radius, rotationPeriod, orbitalPeriod, parent, temperature);
            this.rotationAngle = rotationAngle;
        }

        /**
         * Additional constructor.
         *
         * @param radius         radius of the body in meters
         * @param rotationPeriod the time it takes for the body make to rotate 360 deg on
         *                       its axis.
         * @param orbitalPeriod  the time it takes to orbit around its parent
         * @param parent         the parent (body) of this body
         * @param temperature    temperature of the body in kelvin
         */
        public Properties(double radius, long rotationPeriod, Long orbitalPeriod, Body parent, Double temperature) {
            this(radius, rotationPeriod);
            this.orbitalPeriod = orbitalPeriod;
            this.parent = parent;
            this.temperature = temperature;
        }

        /**
         * Minimal constructor.
         *
         * @param radius         radius of the body in meters
         * @param rotationPeriod the time it takes for the body make to rotate 360 deg on
         *                       its axis.
         */
        public Properties(double radius, long rotationPeriod) {
            this.radius = radius;
            this.rotationPeriod = rotationPeriod;
        }

        public double getRadius() {
            return radius;
        }

        public void setRadius(double radius) {
            this.radius = radius;
        }

        public long getRotationPeriod() {
            return rotationPeriod;
        }

        public void setRotationPeriod(long rotationPeriod) {
            this.rotationPeriod = rotationPeriod;
        }

        /**
         * Returns the current angle by which the body is rotated.
         *
         * @return the current angle in degrees
         */
        public double getRotationAngle() {
            return rotationAngle;
        }

        public void setRotationAngle(double rotationAngle) {
            this.rotationAngle = rotationAngle;
        }

        /**
         * Updates the rotation of the body, which is calculated using a
         * time-stamp (dt) and the time it takes to complete a 360 degrees
         * rotation.
         *
         * @param dt time-stamp, in seconds
         */
        public void updateRotation(double dt) {
            if (this.rotationPeriod != 0) {
                double currentAngle = this.rotationAngle + dt / this.rotationPeriod * 360;
                this.rotationAngle = currentAngle >= 360 ? currentAngle - 360 : currentAngle;
            }
        }

        public Optional<Long> getOrbitalPeriod() {
            return Optional.ofNullable(this.orbitalPeriod);
        }

        public void setOrbitalPeriod(Long time) {
            this.orbitalPeriod = time;
        }

        public Optional<Body> getParent() {
            return Optional.ofNullable(parent);
        }

        public void setParent(Body parent) {
            this.parent = parent;
        }

        public Optional<Double> getTemperature() {
            return Optional.ofNullable(temperature);
        }

        public void setTemperature(double temperature) {
            this.temperature = temperature;
        }

        public void updateInfo(Properties p) {
            this.radius = p.getRadius();
            this.rotationPeriod = p.getRotationPeriod();
            this.rotationAngle = p.getRotationAngle();
            this.orbitalPeriod = p.getOrbitalPeriod().isPresent() ? p.getOrbitalPeriod().get() : this.orbitalPeriod;
            this.parent = p.getParent().isPresent() ? p.getParent().get() : this.parent;
            this.temperature = p.getTemperature().isPresent() ? p.getTemperature().get() : this.temperature;
        }

        @Override
        public String toString() {
            return "Properties [radius=" + radius + ", rotation period=" + rotationPeriod + ", rotation angle="
                    + rotationAngle + ", parent="
                    + (this.getParent().isPresent() ? this.getParent().get().getName() : "none") + ", temperature="
                    + temperature + "]";
        }
    }

}
