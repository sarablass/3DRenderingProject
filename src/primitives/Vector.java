package primitives;
import static primitives.Util.alignZero;


public class Vector extends Point {

//    // Static unit vectors for the axes
   public static final Vector AXIS_X = new Vector(1, 0, 0);
   public static final Vector AXIS_Y = new Vector(0, 1, 0);
   public static final Vector AXIS_Z = new Vector(0, 0, 1);

    // Constructor accepting three double values for coordinates
    public Vector(double x, double y, double z) {
        super(x, y, z);
        if (xyz.equals(Double3.ZERO)) {
            throw new IllegalArgumentException("Cannot create a zero vector");
        }
    }

    // Constructor accepting a Double3 object for coordinates
    public Vector(Double3 xyz) {
        super(xyz);
        if (this.xyz.equals(Double3.ZERO)) {
            throw new IllegalArgumentException("Cannot create a zero vector");
        }
    }

    /**
     * Adds a vector to this vector, returning a new vector.
     *
     * @param v The vector to add
     * @return A new vector resulting from adding the vectors
     */
    public Vector add(Vector v) {
        return new Vector(xyz.add(v.xyz));
    }

    /**
     * Scales this vector by a scalar and returns a new vector.
     *
     * @param scalar The scalar to scale the vector by
     * @return A new scaled vector
     */
    public Vector scale(double scalar) {
        return new Vector(xyz.scale(scalar));
    }

    /**
     * Computes the dot product of this vector and another vector.
     *
     * @param v The vector to compute the dot product with
     * @return The dot product of the vectors
     */
    public double dotProduct(Vector v) {
        return xyz.d1() * v.xyz.d1() + xyz.d2() * v.xyz.d2() + xyz.d3() * v.xyz.d3();
    }

    /**
     * Computes the cross product of this vector and another vector, returning a new vector.
     *
     * @param v The vector to compute the cross product with
     * @return The cross product vector
     */
    public Vector crossProduct(Vector v) {
        double x = xyz.d2() * v.xyz.d3() - xyz.d3() * v.xyz.d2();
        double y = xyz.d3() * v.xyz.d1() - xyz.d1() * v.xyz.d3();
        double z = xyz.d1() * v.xyz.d2() - xyz.d2() * v.xyz.d1();
        return new Vector(x, y, z);
    }

    /**
     * Computes the square of the length of this vector.
     *
     * @return The square of the length of this vector.
     */
    public double lengthSquared() {
        return ((xyz.d1() * xyz.d1()) + (xyz.d2() * xyz.d2()) + (xyz.d3() * xyz.d3()));
    }

    /**
     * Computes the length (magnitude) of the vector.
     *
     * @return The length of the vector
     */
    public double length() {
        return Math.sqrt(lengthSquared());
    }

    /**
     * Normalizes this vector, returning a new vector with length 1.
     *
     * @return A new normalized vector
     */
    public Vector normalize() {
        double length = alignZero(length());
        if (length == 0)
            throw new ArithmeticException("Cannot normalize Vector(0,0,0)");
//        return new Vector(xyz.d1() / length, xyz.d2() / length, xyz.d3() / length);
        return new Vector(xyz.scale(1 / length));

    }

    @Override
    public String toString() {
        return "->" + super.toString();
    }
    
}
