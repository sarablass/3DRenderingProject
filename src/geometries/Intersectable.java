package geometries;

import primitives.*;

import java.util.List;

public interface Intersectable {
    /**
     * This method is used to find the intersection points of a ray with the geometry.
     *
     * @param ray The ray to check for intersections.
     * @return A list of intersection points.
     */
    List<Point> findIntersections(Ray ray);

    /**
     * This method is used to get the normal vector at a given point on the geometry.
     *
     * @param point The point on the geometry.
     * @return The normal vector at the given point.
     */
    Vector getNormal(Point point);
}
