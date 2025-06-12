package geometries;

import primitives.*;

import java.util.List;
import lighting.LightSource;

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
    protected abstract List<Intersection> calculateIntersectionsHelper(Ray ray);
    public final List<Intersection> calculateIntersections(Ray ray){
        return calculateIntersectionsHelper(ray);
    }
}
