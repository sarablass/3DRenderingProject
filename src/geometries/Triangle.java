package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.isZero;

/**
 * Represents a triangle in 3D space.
 * A triangle is a special case of a polygon with exactly three vertices.
 */
public class Triangle extends Polygon {
    /**
     * Constructs a triangle with three given vertices.
     *
     * @param p1 The first vertex of the triangle.
     * @param p2 The second vertex of the triangle.
     * @param p3 The third vertex of the triangle.
     */
    public Triangle(Point p1, Point p2, Point p3) {
        super(p1, p2, p3); // Calls the constructor of Polygon
    }

    @Override
    public List<Intersection> calculateIntersectionsHelper(Ray ray) {

        // Find the intersection point with the plane of the triangle
        Point p1 = vertices.get(0);
        Point p2 = vertices.get(1);
        Point p3 = vertices.get(2);
        // Calculate the normal to the plane
        Vector ab = p2.subtract(p1);
        Vector ac = p3.subtract(p1);
        Vector n = ab.crossProduct(ac);

        double nd = n.dotProduct(ray.getDirection());
        if (isZero(nd)) {
            return null; // The ray is parallel to the plane of the triangle
        }

        double t = n.dotProduct(p1.subtract(ray.getPoint(0))) / nd;
        if (t < 0) {
            return null; // The intersection is behind the ray's origin
        }

        Point intersectionPoint = ray.getPoint(t);

        // Check if the intersection point is a vertex of the triangle
        if (intersectionPoint.equals(p1) ||
                intersectionPoint.equals(p2) ||
                intersectionPoint.equals(p3)) {
            return null;
        }
        // Check if the intersection point is inside the triangle
        if (isPointInTriangle(intersectionPoint, p1, p2, p3)) {
            return List.of(new Intersection(this,intersectionPoint));
        }

        return null;
    }
    /**
     * Checks if a point is inside a triangle using barycentric coordinates.
     *
     * @param p  The point to check.
     * @param p1 The first vertex of the triangle.
     * @param p2 The second vertex of the triangle.
     * @param p3 The third vertex of the triangle.
     * @return true if the point is inside the triangle, false otherwise.
     */
    private boolean isPointInTriangle(Point p, Point p1, Point p2, Point p3) {
        // Calculate vectors
        Vector v0 = p2.subtract(p1);
        Vector v1 = p3.subtract(p1);
        Vector v2 = p.subtract(p1);

        // Compute dot products in order to calculate barycentric coordinates
        double dot00 = v0.dotProduct(v0);
        double dot01 = v0.dotProduct(v1);
        double dot02 = v0.dotProduct(v2);
        double dot11 = v1.dotProduct(v1);
        double dot12 = v1.dotProduct(v2);

        // Compute barycentric coordinates
        // u, v, and w are the barycentric coordinates of the point- we can present every point as a linear combination of the vertices
        //invDenom is the inverse of the matrix determinant that we use to calculate the barycentric coordinates by its formula
        double invDenom = 1 / (dot00 * dot11 - dot01 * dot01);
        double u = (dot11 * dot02 - dot01 * dot12) * invDenom;
        double v = (dot00 * dot12 - dot01 * dot02) * invDenom;
        double w = 1.0 - u - v;

        // Check if point is in triangle- if the barycentric coordinates are positive and their sum is less than or equal to 1
        if (u > 0 && v > 0 && w > 0 && u + v + w <= 1) {
            return true;
        }
        return false;
    }
}