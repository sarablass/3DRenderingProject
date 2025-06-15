package geometries;

import primitives.*;

import java.util.List;
import lighting.LightSource;

/**
 * Abstract class representing an object in the scene that can be intersected by a ray.
 * Provides a framework for calculating intersection points between rays and geometries.
 */
public abstract class Intersectable {
    /**
     * This method is used to find the intersection points of a ray with the geometry.
     *
     * @param ray The ray to check for intersections.
     * @return A list of intersection points.
     */
    public final List<Point> findIntersections(Ray ray) {
        var list = calculateIntersections(ray);
        return list == null ? null : list.stream().map(intersection -> intersection.point).toList();
    }
    /**
     * Represents an intersection between a ray and a geometry,
     * including additional data used for shading calculations.
     */
    public static class Intersection {
        public final Geometry geometry;
        public final Point point;
        public final Material material;
        public Vector normal;
        public Vector v;
        public double vNormal;
        public LightSource light;
        public Vector l;
        public double lNormal;

        /**
         * Constructs an `Intersection` object with the specified geometry and point.
         *
         * @param geometry The geometry that was intersected.
         * @param point    The intersection point on the geometry.
         */
        public Intersection(Geometry geometry, Point point) {
            this.geometry = geometry;
            this.point = point;
            if (geometry != null) {
                this.material = geometry.getMaterial();
            } else {
                this.material = null;
            }
        }

        /**
         * Checks if this intersection is equal to another object.
         * <p>
         * Two intersections are considered equal if they have the same geometry
         * and the same intersection point.
         *
         * @param o The object to compare with this intersection.
         * @return True if the objects are equal, false otherwise.
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Intersection)) return false;
            return ((Intersection) o).geometry == geometry &&
                    ((Intersection) o).point.equals(point);
        }

        /**
         * Returns a string representation of the intersection.
         *
         * @return A string describing the intersection.
         */
        @Override
        public String toString() {
            return "Intersection{" +
                    "geometry=" + geometry +
                    ", point=" + point +
                    '}';
        }
    }

    /**
     * Abstract method to be implemented by concrete geometries.
     * Computes detailed intersection data between the given ray and the geometry.
     *
     * @param ray The ray to test for intersection.
     * @return A list of detailed intersection objects, or null if no intersection occurs.
     */
    protected abstract List<Intersection> calculateIntersectionsHelper(Ray ray);

    /**
     * Calls the internal helper method to compute intersection data.
     *
     * @param ray The ray to test for intersection.
     * @return A list of detailed intersection objects, or null if no intersection occurs.
     */
    public final List<Intersection> calculateIntersections(Ray ray){
        return calculateIntersectionsHelper(ray);
    }
}
