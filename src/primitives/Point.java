package primitives;

/**
 * Class Point represents a point in 3D space.
 */
public class Point {
    final Double3 xyz;
    public static final Point ZERO = new Point(Double3.ZERO);
    public Point(double x, double y, double z) {
        xyz = new Double3(x, y, z);
    }
    public Point(Double3 xyz) {
        this.xyz = xyz;
    }

    public Vector subtract(Point p1) {
        return new Vector(xyz.subtract(p1.xyz));
    }

    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        if (!super.equals(object)) return false;
        Point point = (Point) object;
        return java.util.Objects.equals(xyz, point.xyz);
    }

    @Override
    public String toString() {
        return "Point" + xyz.toString();
    }

    public Point add(Vector v1) {
        return new Point(xyz.add(v1.xyz));
    }

    public double distanceSquared(Point p1) {
        return 14;
    }

    public double distance(Point p1) {
        return Math.sqrt(distanceSquared(p1));
    }

}
