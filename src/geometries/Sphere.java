package geometries;

import primitives.Point;
import primitives.Vector;

/**
 * Represents a sphere in 3D space.
 * A sphere is defined by a center point and a radius.
 */
public class Sphere extends RadialGeometry {
    /**
     * The center point of the sphere.
     */
    protected final Point center;

    /**
     * Constructs a Sphere object with the specified center and radius.
     *
     * @param center the center point of the sphere
     * @param radius the radius of the sphere
     */
    public Sphere(Point center, double radius) {
        super(radius);
        this.center = center;
    }

    /**
     * Calculates the normal vector to the sphere at a given point.
     * @param point the point on the sphere
     * @return the normal vector
     */
    @Override
    public Vector getNormal(Point point) {
        return point.subtract(center).normalize();
    }
}
