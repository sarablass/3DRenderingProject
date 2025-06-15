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
     * @param axis   the central axis of the cylinder
     * @param height the height of the cylinder
     */
    public Cylinder(double radius, Ray axis, double height) {
        super(radius, axis);
        this.height = height;
    }

    public Vector getNormal(Point p){
        Point p0 = axis.getHead();
        Vector dir = axis.getDirection();

        // Vector from the base point to the given point
        if(p.equals(p0))
            return dir.scale(-1).normalize();

        Vector vectorFromP0 = p.subtract(p0);

        // Project the point onto the cylinder's axis
        double t = dir.dotProduct(vectorFromP0);

        // Check if the point is on the bottom base
        if (t <= 0) {
            return dir.scale(-1).normalize(); // normal is the opposite direction of the cylinder's axis direction
        }

        // Check if the point is on the top base
        if (t >= height) {
            return dir.normalize(); // normal is the direction of the cylinder's axis direction
        }

        // The point is on the lateral surface
        Point o = p0.add(dir.scale(t));
        return p.subtract(o).normalize();
    }

}
