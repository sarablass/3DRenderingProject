package primitives;

/**
 * Class Point represents a point in 3D space.
 * This class is immutable.
 *
 */
public class Point {
    /** The 3D coordinates of the point */
    protected final Double3 xyz;

    /** The zero point (0,0,0) */
    public static final Point ZERO = new Point(Double3.ZERO);

    /**
     * Constructor for creating a point with three coordinates.
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     */
    public Point(double x, double y, double z) {
        xyz = new Double3(x, y, z);
    }

    /**
     * Constructor for creating a point from an existing Double3 object.
     *
     * @param xyz A Double3 object representing the coordinates
     */
    public Point(Double3 xyz) {
        this.xyz = xyz;
    }

    /**
     * Subtracts another point from this point, returning a vector.
     *
     * @param p The point to subtract
     * @return A vector from the given point to this point
     */
    public Vector subtract(Point p) {
        return new Vector(xyz.subtract(p.xyz));
    }

    /**
     * Adds a vector to this point, returning a new point.
     *
     * @param v The vector to add
     * @return A new point resulting from adding the vector to this point
     */
    public Point add(Vector v) {
        return new Point(xyz.add(v.xyz));
    }

    /**
     * Computes the squared distance between this point and another point.
     *
     * @param p The other point
     * @return The squared distance between the points
     */
    public double distanceSquared(Point p) {
        double dx = xyz.d1() - p.xyz.d1();
        double dy = xyz.d2() - p.xyz.d2();
        double dz = xyz.d3() - p.xyz.d3();
        return dx * dx + dy * dy + dz * dz;
    }

    /**
     * Computes the Euclidean distance between this point and another point.
     *
     * @param p The other point
     * @return The distance between the points
     */
    public double distance(Point p) {
        return Math.sqrt(distanceSquared(p));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Point other) && xyz.equals(other.xyz);
    }

    @Override
    public String toString() {
        return "Point" + xyz;
    }
}
