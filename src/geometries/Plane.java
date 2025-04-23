package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * Represents a plane in three-dimensional space.
 * The class is immutable.
 */
public class Plane implements Geometry {
    /**
     * A reference point on the plane.
     */
    private final Point q;

    /**
     * The normal vector of the plane.
     */
    private final Vector normal;

    /**
     * Constructs a plane using three points in space.
     * The normal is initially set to null.
     *
     * @param v0 the first point
     * @param v1 the second point
     * @param v2 the third point
     */
    public Plane(Point v0, Point v1, Point v2) {
        this.q = v0;
        this.normal = null; // The normal will be calculated in the next stage
    }

    /**
     * Constructs a plane using a reference point and a normal vector.
     * The normal vector is normalized before storing.
     *
     * @param q    a reference point on the plane
     * @param normal the normal vector of the plane (may not be normalized, but will be stored normalized)
     */
    public Plane(Point q, Vector normal) {
        this.q = q;
        this.normal = normal.normalize(); // Ensure the normal is stored as a normalized vector
    }

    /**
     * Finds the intersection points of a given ray with the plane.
     *
     * @param ray the ray to intersect with the plane
     * @return a list of intersection points, or null if there are no intersections
     */
    @Override
    public List<Point> findIntersections(Ray ray) {

        if (q.equals(ray.getHead())) { // if the ray starts from the plane it doesn't cut the plane at all
            return null;
        }
        double nv = normal.dotProduct(ray.getDirection());
        if (isZero(nv)) {
            return null;
        }
        double t = alignZero(normal.dotProduct(q.subtract(ray.getHead())) / nv);
        if (t > 0) {
            return List.of(ray.getPoint(t));
        } else {
            return null;

        }
    }

    /**
     * Returns the normal vector of the plane.
     *
     * @param point a point on the plane (not used in this implementation as the normal is the same for all points)
     * @return the normal vector of the plane
     */
    @Override
    public Vector getNormal(Point point) {
        return normal;
    }
}
