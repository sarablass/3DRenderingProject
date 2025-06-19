package primitives;

import java.util.List;
import geometries.Intersectable.Intersection;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * The Ray class represents a ray in 3D space, defined by a starting point (head) and a direction vector.
 */
public class Ray {
    private final Point head;
    private final Vector direction;
    private static final double DELTA = 0.1; // A small delta value to avoid self-intersection issues

    /**
     * Constructs a Ray with a given head point and direction vector.
     * The direction vector is normalized before being stored.
     *
     * @param head The starting point of the ray.
     * @param direction The direction vector of the ray.
     * @throws IllegalArgumentException If the direction vector is a zero vector.
     */
    public Ray(Point head, Vector direction) {
        this.head = head;
        this.direction = direction.normalize();
    }

    /**
     * Constructs a new {@code Ray} with a given starting point, direction vector, and normal vector.
     * <p>
     * This constructor adjusts the starting point slightly along the normal vector to avoid
     * self-intersection issues in geometric calculations.
     *
     * @param head      the starting point of the ray
     * @param direction the direction vector of the ray (will be normalized)
     * @param normal    the normal vector used to adjust the starting point
     */
    public Ray(Point head, Vector direction, Vector normal) {
        double dot = alignZero(direction.dotProduct(normal));
        double scaleFactor = (dot > 0 ? DELTA : -DELTA);
        this.head = head.add(normal.scale(scaleFactor));
        this.direction = direction.normalize();
    }

    /**
     * Returns the head (starting point) of the ray.
     *
     * @return The head point of the ray.
     */
    public Point getHead() {
        return head;
    }

    /**
     * Returns the direction vector of the ray.
     *
     * @return The normalized direction vector of the ray.
     */
    public Vector getDirection() {
        return direction;
    }



    /**
     * Returns a point on the ray at a distance t from the head.
     *
     * @param t The distance from the head of the ray.
     * @return The point on the ray at the specified distance from the head.
     */
    public Point getPoint(double t) {
        if (isZero(t))
            return head;
        return head.add(direction.scale(t));
    }
    /**
     * Checks if this ray is equal to another object.
     * Two rays are considered equal if they have the same head point and direction vector.
     *
     * @param obj The object to compare with.
     * @return true if the given object is a Ray with the same head and direction, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Ray other = (Ray) obj;
        return head.equals(other.head) && direction.equals(other.direction);
    }

    /**
     * Returns a string representation of the ray.
     *
     * @return A string containing the head point and direction vector.
     */
    @Override
    public String toString() {
        return "Ray{head=" + head + ", direction=" + direction + "}";
    }

    /**
     * Finds the closest point to the head of the ray from a list of points.
     *
     * @param intersections The list of points to search.
     * @return The closest point to the head of the ray.
     */
    public Intersection findClosestIntersection(List<Intersection> intersections) {
        Intersection closest = null;
        double minDistance = Double.POSITIVE_INFINITY;
        for (Intersection p : intersections) {
            double distance = head.distance(p.point);
            if (distance < minDistance) {
                minDistance = distance;
                closest = p;
            }
        }
        return closest;
    }

    /**
     * Finds the closest point to the head of the ray from a list of points.
     *
     * @param points The list of points to check.
     * @return The closest point to the head of the ray.
     */
    public Point findClosestPoint(List<Point> points) {
        return points == null || points.isEmpty() ? null
                : findClosestIntersection(points.stream().map(p -> new Intersection(null, p)).toList()).point;
    }
}
