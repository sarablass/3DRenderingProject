package geometries;

/**
 * Represents a geometric object with a radius.
 * This class serves as a base class for radial geometries.
 */
public abstract class RadialGeometry implements Geometry {
    /**
     * The radius of the geometric object.
     */
    protected final double radius;

    /**
     * Constructs a RadialGeometry object with the specified radius.
     *
     * @param radius the radius of the geometry
     */
    protected RadialGeometry(double radius) {
        this.radius = radius;
    }
}

