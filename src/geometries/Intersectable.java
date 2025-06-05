package geometries;

import primitives.*;

import java.util.List;

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
        public Intersection(Geometry geometry, Point point) {
            this.geometry = geometry;
            this.point = point;
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Intersection intersection)) return false;
            return point.equals(intersection.point) && geometry.equals(intersection.geometry);
        }
        @Override
        public String toString() {
            return "Intersectable{" +
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
