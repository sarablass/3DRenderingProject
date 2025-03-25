package primitives;

/**
 * The Ray class represents a ray in 3D space, defined by a starting point (head) and a direction vector.
 */
public class Ray {
    private final Point head;
    private final Vector direction;

    /**
     * Constructs a Ray with a given head point and direction vector.
     * The direction vector is normalized before being stored.
     *
     * @param head The starting point of the ray.
     * @param direction The direction vector of the ray.
     * @throws IllegalArgumentException If the direction vector is a zero vector.
     */
    public Ray(Point head, Vector direction) {
        if (direction.equals(Vector.ZERO)) {
            throw new IllegalArgumentException("The direction vector cannot be a zero vector.");
        }
        this.head = head;
        this.direction = direction.normalize();
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
}
