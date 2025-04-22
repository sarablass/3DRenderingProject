package geometries;

import primitives.Point;
import primitives.Vector;

/**
 * The Geometry interface represents a geometric shape in 3D space.
 */
public interface Geometry extends Intersectable {

    /**
     * Computes and returns the normal vector to the geometry at a given point.
     * @param p The point at which to compute the normal vector.
     * @return The normal vector to the geometry at the given point.
     */
    public Vector getNormal(Point p);
}