package geometries;

import primitives.Point;
import primitives.Vector;
import primitives.Ray;

import java.util.List;

import static primitives.Util.isZero;

/**
 * Represents a tube in 3D space.
 * A tube is defined by a radius and a central axis represented by a ray.
 */
public class Tube extends RadialGeometry {
    /**
     * The central axis of the tube.
     */
    protected final Ray axis;

    /**
     * Constructs a tube with the given radius and central axis.
     *
     * @param radius the radius of the tube
     * @param axis   the central axis of the tube
     */
    public Tube(double radius, Ray axis) {
        super(radius);
        this.axis = axis;
    }

    @Override
    public List<Intersection> calculateIntersectionsHelper(Ray ray) {
        return null;
    }

    public Vector getNormal(Point p) {

        // Calculate the vector from the base point of the axis to the given point
        Vector vectorFromAxisStart = p.subtract(axis.getHead());

        // Project the above vector on the axis direction to find the projection point on the axis
        double t = axis.getDirection().dotProduct(vectorFromAxisStart);
        Point o;
        if(isZero(t))
            o=axis.getHead();
        else
            o = axis.getPoint(t);

        // Calculate the normal vector by subtracting the projection point from the given point
        Vector normal = p.subtract(o).normalize();

        return normal;


    }
}
