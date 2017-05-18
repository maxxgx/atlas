package atlas.model;

import atlas.utils.Units;

/**
 * It contains all Possible body types and physical values.
 *
 */
public enum BodyType {
	BLACKHOLE("Blackhole"),
	STAR("Star"),
	PLANET("Planet"),
	DWARF_PLANET("Dwarf planet"),
	SATELLITE("Satellite"),
	COMET("Comet"),
	ASTEROID("Asteroid"),
	FRAGMENT("Fragment"),
	OBJECT("Object");
    
    private String nameType;
    
    private BodyType(final String name){
        this.nameType = name;
    }
    
    public String toString(){
        return this.nameType;
    }
    
    /**
     * G constant from CODATA 2010
     */
    public static final double G = 6.67384e-11; // gravitational constant
    
    /**
     * Astronomical unit in meters
     */
    public static final double AU = 149597870.700 * 1000;
    
    
    /**
     * Data from NASA Jet Propulsion Laboratory (link = http://ssd.jpl.nasa.gov)
     */
    public static final double SOLAR_MASS = 1.988544e30;
    public static final double MERCURY_MASS = 0.330104e24;
    public static final double VENUS_MASS = 4.86732e24;
    public static final double EARTH_MASS = 5.97219e24;
    public static final double MOON_MASS = 734.9e20;
    public static final double MARS_MASS = 0.641693e24;
    public static final double JUPITER_MASS = 1898.13e24;
    public static final double SATURN_MASS = 568.319e24;
    public static final double URANUS_MASS = 86.8103e24; 
    public static final double NEPTUNE_MASS = 102.410e24;
    public static final double PLUTO_MASS = 0.01309e24;
    
    /**
     * Rotation period
     */
    public static final long SUN_DAY = (long)(27 * Units.DAY_SEC.getValue());
    public static final long MERCURY_DAY = (long)(58.6462 * Units.DAY_SEC.getValue());
    public static final long VENUS_DAY = (long)(-243.0185 * Units.DAY_SEC.getValue());
    public static final long EARTH_DAY = (long)(1.002738 * Units.DAY_SEC.getValue());
    public static final long MOON_DAY = (long)(27.321582 * Units.DAY_SEC.getValue());
    public static final long MARS_DAY = (long)(24.622962 * Units.HOUR_SEC.getValue());
    public static final long JUPITER_DAY = (long)(0.413538021 * Units.DAY_SEC.getValue());
    public static final long SATURN_DAY = (long)(0.449375 * Units.DAY_SEC.getValue());
    public static final long URANUS_DAY = (long)(0.71833 * Units.DAY_SEC.getValue());
    public static final long NEPTUNE_DAY = (long)(16.11 * Units.HOUR_SEC.getValue());
    public static final long PLUTO_DAY = (long)(6.387230 * Units.DAY_SEC.getValue());
    
}
