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
}
