package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link geometries.Triangle} class.
 */
class TriangleTests {
    /**
     * Delta value for accuracy when comparing the numbers of type 'double' in
     * assertEquals
     */
    private static final double DELTA = 0.000001;
    /**
     * Test method for {@link geometries.Triangle#getNormal(primitives.Point)}.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Simple triangle in 3D space
        Point p1 = new Point(0, 0, 0);
        Point p2 = new Point(1, 0, 0);
        Point p3 = new Point(0, 1, 0);

        Triangle triangle = new Triangle(p1, p2, p3);

        // Pick a point on the triangle's surface
        Point testPoint = new Point(0.25, 0.25, 0);

        // Act
        Vector normal = triangle.getNormal(testPoint);

        // Assert that the normal is a unit vector
        assertEquals(1, normal.length(), DELTA, "Normal vector is not a unit vector");

        // The normal vector should be orthogonal to two triangle edges
        Vector v1 = p2.subtract(p1);
        Vector v2 = p3.subtract(p1);

        assertEquals(0, normal.dotProduct(v1), DELTA, "Normal is not orthogonal to edge v1");
        assertEquals(0, normal.dotProduct(v2), DELTA, "Normal is not orthogonal to edge v2");
    }
}