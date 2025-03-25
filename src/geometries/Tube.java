package geometries;

import primitives.Point;
import primitives.Vector;
import primitives.Ray;

/**
 * Represents a tube in 3D space.
 * A tube is defined by a radius and a central axis represented by a ray.
 */
public class Tube extends RadialGeometry {
    /**
     * The central axis of the tube.
     */
    protected final Ray exis;

    /**
     * Constructs a tube with the given radius and central axis.
     *
     * @param radius the radius of the tube
     * @param exis   the central axis of the tube
     */
    public Tube(double radius, Ray exis) {
        super(radius);
        this.exis = exis;
    }

    /**
     * Returns the normal vector at a given point on the tube's surface.
     *
     * @param point the point on the surface of the tube
     * @return the normal vector at the given point
     */
    @Override
    public Vector getNormal(Point point) {
        return null;
    }
}
