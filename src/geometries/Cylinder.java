package geometries;

import primitives.Point;
import primitives.Vector;
import primitives.Ray;

/**
 * Represents a cylinder in 3D space.
 * A cylinder is defined by a radius, a central axis (ray), and a height.
 */
public class Cylinder extends Tube {
    /**
     * The height of the cylinder.
     */
    private final double height;

    /**
     * Constructs a cylinder with the given radius, central axis, and height.
     *
     * @param radius the radius of the cylinder
     * @param exis   the central axis of the cylinder
     * @param height the height of the cylinder
     */
    public Cylinder(double radius, Ray exis, double height) {
        super(radius, exis);
        this.height = height;
    }

    /**
     * Returns the normal vector at a given point on the cylinder's surface.
     *
     * @param point the point on the surface of the cylinder
     * @return the normal vector at the given point
     */
    @Override
    public Vector getNormal(Point point) {
        return null;
    }
}
