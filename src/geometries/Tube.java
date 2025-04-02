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
    protected final Ray axis;

    /**
     * Constructs a tube with the given radius and central axis.
     *
     * @param radius the radius of the tube
     * @param axis   the central axis of the tube
     */
    public Tube(double radius, Ray axis) {
        super(radius);
        this.axis = axis;
    }

    /**
     * Returns the normal vector to the tube at a given point.
     * The normal is calculated as:
     * N = (P - (O + tV)) / |P - (O + tV)|
     * Where:
     * P - the given point
     * O - the head of the axis ray
     * V - the direction of the axis ray
     * t - projection scalar of (P - O) onto V
     *
     * @param point the point on the tube's surface
     * @return the normal vector to the tube at the given point
     */
    @Override
    public Vector getNormal(Point point) {
        double t = axis.getDirection().dotProduct(point.subtract(axis.getHead()));
        Point oPlusTv = axis.getHead().add(axis.getDirection().scale(t));
        return point.subtract(oPlusTv).normalize();
    }
}
