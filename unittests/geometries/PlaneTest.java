package geometries;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;
import primitives.Util;

class PlaneTest {

    /**
     * Delta value for accuracy when comparing the numbers of type 'double' in
     * assertEquals
     */
    private static final double DELTA = 0.000001;

    /**
     * Test method for {@link geometries.Plane#Plane(Point, Point, Point)}.
     */
    @Test
    void testConstructor() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Creating a plane with three non-collinear points - normal should be correct and unit length
        Point p1 = new Point(1, 1, 1);
        Point p2 = new Point(3, 2, 1);
        Point p3 = new Point(2, 5, 3);

        Plane plane = new Plane(p1, p2, p3);
        Vector normal = plane.getNormal(p1);

        // Check that the normal is perpendicular to both vectors formed by the points
        Vector v1 = p2.subtract(p1);
        Vector v2 = p3.subtract(p1);

        assertTrue(Util.isZero(normal.dotProduct(v1)), "TC01: Normal is not perpendicular to v1");
        assertTrue(Util.isZero(normal.dotProduct(v2)), "TC01: Normal is not perpendicular to v2");

        // Check that the normal is a unit vector
        assertEquals(1, normal.length(), DELTA, "TC01: Normal is not a unit vector");

        // =============== Boundary Values Tests ==================

        // TC02: First and second points are identical → should throw exception
        assertThrows(IllegalArgumentException.class,
                () -> new Plane(new Point(1, 2, 3), new Point(1, 2, 3), new Point(4, 5, 6)),
                "TC02: Constructor should throw exception when first and second points are identical");

        // TC03: First and third points are identical → should throw exception
        assertThrows(IllegalArgumentException.class,
                () -> new Plane(new Point(1, 2, 3), new Point(4, 5, 6), new Point(1, 2, 3)),
                "TC03: Constructor should throw exception when first and third points are identical");

        // TC04: Second and third points are identical → should throw exception
        assertThrows(IllegalArgumentException.class,
                () -> new Plane(new Point(4, 5, 6), new Point(1, 2, 3), new Point(1, 2, 3)),
                "TC04: Constructor should throw exception when second and third points are identical");

        // TC05: All three points are identical → should throw exception
        assertThrows(IllegalArgumentException.class,
                () -> new Plane(new Point(1, 1, 1), new Point(1, 1, 1), new Point(1, 1, 1)),
                "TC05: Constructor should throw exception when all three points are identical");

        // TC06: All three points are collinear → should throw exception
        assertThrows(IllegalArgumentException.class,
                () -> new Plane(new Point(0, 0, 0), new Point(1, 1, 1), new Point(2, 2, 2)),
                "TC06: Constructor should throw exception when all points are collinear");
    }

    /**
     * Test method for {@link geometries.Plane#getNormal(primitives.Point)}.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partition Test ==============

        // TC01: Plane defined by three non-collinear points in 3D space
        Point v1 = new Point(1, 0, 0);
        Point v2 = new Point(0, 1, 0);
        Point v3 = new Point(0, 0, 1);

        Plane plane = new Plane(v1, v2, v3);
        Vector normal = plane.getNormal(v1);

        // Calculate two vectors lying on the plane
        Vector vec1 = v2.subtract(v1);
        Vector vec2 = v3.subtract(v1);

        // Check that the normal is orthogonal to both vectors on the plane
        assertEquals(0, normal.dotProduct(vec1), 1e-10, "Normal is not perpendicular to vec1");
        assertEquals(0, normal.dotProduct(vec2), 1e-10, "Normal is not perpendicular to vec2");

        // Check that the normal is normalized (length == 1)
        assertEquals(1, normal.length(), 1e-10, "Normal is not a unit vector");
    }
}