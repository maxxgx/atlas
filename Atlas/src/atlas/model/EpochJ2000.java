package atlas.model;

import static atlas.model.Body.Properties.celsiusToKelvin;
import static atlas.model.BodyType.AU;
import static atlas.model.BodyType.DWARF_PLANET;
import static atlas.model.BodyType.EARTH_DAY;
import static atlas.model.BodyType.EARTH_MASS;
import static atlas.model.BodyType.MOON_DAY;
import static atlas.model.BodyType.MOON_MASS;
import static atlas.model.BodyType.JUPITER_DAY;
import static atlas.model.BodyType.JUPITER_MASS;
import static atlas.model.BodyType.MARS_DAY;
import static atlas.model.BodyType.MARS_MASS;
import static atlas.model.BodyType.MERCURY_DAY;
import static atlas.model.BodyType.MERCURY_MASS;
import static atlas.model.BodyType.NEPTUNE_DAY;
import static atlas.model.BodyType.NEPTUNE_MASS;
import static atlas.model.BodyType.PLANET;
import static atlas.model.BodyType.PLUTO_DAY;
import static atlas.model.BodyType.PLUTO_MASS;
import static atlas.model.BodyType.SATELLITE;
import static atlas.model.BodyType.SATURN_DAY;
import static atlas.model.BodyType.SATURN_MASS;
import static atlas.model.BodyType.SOLAR_MASS;
import static atlas.model.BodyType.STAR;
import static atlas.model.BodyType.URANUS_DAY;
import static atlas.model.BodyType.URANUS_MASS;
import static atlas.model.BodyType.VENUS_DAY;
import static atlas.model.BodyType.VENUS_MASS;

import java.util.Calendar;

/**
 * Represents the astronomical epoch of time : 01/01/2000, with the relative coordinates.
 *
 */
public enum EpochJ2000 {
    

    SUN(new BodyImpl.Builder().name("Sun")
                                .type(STAR)
                                .imagePath(Body.IMAGE_FOLDER + "sun.png")
                                .mass(SOLAR_MASS)
                                .posX(0)
                                .posY(0)
                                .velX((5.374260940168565E-06 * AU) / EARTH_DAY)
                                .velY((-7.410965396701423E-06 * AU) / EARTH_DAY)
                                .properties(new Body.Properties(702020*1000, 26 * EARTH_DAY, null, null, 5775.00 ))
                                .build()),
    
    MERCURY(new BodyImpl.Builder().name("Mercury")
            .type(PLANET)
            .imagePath(Body.IMAGE_FOLDER + "mercury.png")
            .mass(MERCURY_MASS)
            .posX(-1.478672233442572E-01 * AU)
            .posY(-4.466929775364947E-01 * AU)
            .velX(( 2.117424563261189E-02 * AU) / EARTH_DAY)
            .velY((-7.105386404267509E-03 * AU) / EARTH_DAY)
            .properties(new Body.Properties(2440*1000, MERCURY_DAY, null, SUN.getBody(), 440.00 ))
            .build()),
    
    VENUS(new BodyImpl.Builder().name("Venus")
    		.type(PLANET)
    		.imagePath(Body.IMAGE_FOLDER + "venus.png")
    		.mass(VENUS_MASS)
    		.posX(-7.257693602841776E-01 * AU)
    		.posY(-2.529582082587794E-02 * AU)
    		.velX(( 5.189070188671264E-04 * AU) / EARTH_DAY)
    		.velY((-2.031355258779472E-02 * AU) / EARTH_DAY)
    		.properties(new Body.Properties(6051.8*1000, VENUS_DAY, null, SUN.getBody(), 735.00 ))
    		.build()),
    
    EARTH(new BodyImpl.Builder().name("Earth")
                                .type(PLANET)
                                .imagePath(Body.IMAGE_FOLDER + "earth.png")
                                .mass(EARTH_MASS)
                                .posX(-1.756637922977121E-01 * AU)
                                .posY(9.659912850526894E-01 * AU)
                                .velX((-1.722857156974861E-02 * AU) / EARTH_DAY)
                                .velY((-3.015071224668472E-03 * AU) / EARTH_DAY)
                                .properties(new Body.Properties(6371*1000, EARTH_DAY, null, SUN.getBody(), celsiusToKelvin(14.00) ))
                                .build()),
    
    MOON(new BodyImpl.Builder().name("Moon")
            .type(SATELLITE)
            .imagePath(Body.IMAGE_FOLDER + "moon.png")
            .mass(MOON_MASS)
            .posX(-1.777871530146587E-01 * AU)
            .posY(9.643743958957228E-01 * AU)
            .velX((-1.690468993933486E-02 * AU) / EARTH_DAY)
            .velY((-3.477070764654480E-03 * AU) / EARTH_DAY)
            .properties(new Body.Properties(1737.4*1000, MOON_DAY, null, EARTH.getBody(), 250.00 ))
            .build()),
    
    MARS(new BodyImpl.Builder().name("Mars")
            .type(PLANET)
            .imagePath(Body.IMAGE_FOLDER + "mars.png")
            .mass(MARS_MASS)
            .posX(1.383221922520998E+00 * AU)
            .posY(-2.380174081741852E-02 * AU)
            .velX((7.533013850513374E-04 * AU) / EARTH_DAY)
            .velY((1.517888771209419E-02 * AU) / EARTH_DAY)
            .properties(new Body.Properties(3389.9*1000, MARS_DAY, null, SUN.getBody(), 210.00 ))
            .build()),
    
    JUPITER(new BodyImpl.Builder().name("Jupiter")
            .type(PLANET)
            .imagePath(Body.IMAGE_FOLDER + "jupiter.png")
            .mass(JUPITER_MASS)
            .posX(3.996321311604079E+00 * AU)
            .posY(2.932561211517849E+00 * AU)
            .velX((-4.558376589062066E-03 * AU) / EARTH_DAY)
            .velY((6.439863212743646E-03 * AU) / EARTH_DAY)
            .properties(new Body.Properties(71492*1000, JUPITER_DAY, null, SUN.getBody(), 165.00 ))
            .build()),
    
    SATURN(new BodyImpl.Builder().name("Saturn")
            .type(PLANET)
            .imagePath(Body.IMAGE_FOLDER + "saturn.png")
            .mass(SATURN_MASS)
            .posX(6.401416890663500E+00 * AU)
            .posY(6.565250734685103E+00 * AU)
            .velX((-4.285166238539475E-03 * AU) / EARTH_DAY)
            .velY((3.884579926659154E-03 * AU) / EARTH_DAY)
            .properties(new Body.Properties(60268*1000, SATURN_DAY, null, SUN.getBody(), 134.00 ))
            .build()),
    
    URANUS(new BodyImpl.Builder().name("Uranus")
            .type(PLANET)
            .imagePath(Body.IMAGE_FOLDER + "uranus.png")
            .mass(URANUS_MASS)
            .posX(1.442337843936191E+01 * AU)
            .posY(-1.373845030140273E+01 * AU)
            .velX((2.683840344076701E-03 * AU) / EARTH_DAY)
            .velY((2.665016541217002E-03 * AU) / EARTH_DAY)
            .properties(new Body.Properties(25559*1000, URANUS_DAY, null, SUN.getBody(), 76.00 ))
            .build()),
    
    NEPTUNE(new BodyImpl.Builder().name("Neptune")
            .type(PLANET)
            .imagePath(Body.IMAGE_FOLDER + "neptune.png")
            .mass(NEPTUNE_MASS)
            .posX(1.680361764335729E+01 * AU)
            .posY(-2.499544328458694E+01 * AU)
            .velX((2.584589572083709E-03 * AU) / EARTH_DAY)
            .velY((1.768943546348827E-03 * AU) / EARTH_DAY)
            .properties(new Body.Properties(24766*1000, NEPTUNE_DAY, null, SUN.getBody(), 72.00 ))
            .build()),
    
    PLUTO(new BodyImpl.Builder().name("Pluto")
            .type(DWARF_PLANET)
            .imagePath(Body.IMAGE_FOLDER + "pluto.png")
            .mass(PLUTO_MASS)
            .posX(-9.884006595855094E+00 * AU)
            .posY(-2.796081320603301E+01 * AU)
            .velX((3.044390641302692E-03 * AU) / EARTH_DAY)
            .velY((-1.537290075737863E-03 * AU) / EARTH_DAY)
            .properties(new Body.Properties(1195*1000, PLUTO_DAY, null, SUN.getBody(), 40.00 ))
            .build()),
    ;
    
    public static final long TIME_MILLS = new Calendar.Builder().setDate(2000, Calendar.JANUARY, 01)
                                                              .setTimeOfDay(00, 00, 00, 00)
                                                              .build().getTimeInMillis();
    
    private Body body;
    
    private EpochJ2000(final Body b){
        this.body = b;
    }
    
    public Body getBody(){
        return this.body;
    }
}
