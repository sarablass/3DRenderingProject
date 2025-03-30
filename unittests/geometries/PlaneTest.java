package geometries;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

class PlaneTest {

    @Test
    void testConstructor() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Creating a plane with three non-collinear points - normal should be correct and unit length
        Point p1 = new Point(0, 0, 0);
        Point p2 = new Point(1, 0, 0);
        Point p3 = new Point(0, 1, 0);

        Plane plane = new Plane(p1, p2, p3);
        Vector normal = plane.getNormal(p1);

        // Check that the normal is perpendicular to both vectors formed by the points
        Vector v1 = p2.subtract(p1);
        Vector v2 = p3.subtract(p1);
        assertEquals(0, normal.dotProduct(v1), "TC01: Normal is not perpendicular to v1");
        assertEquals(0, normal.dotProduct(v2), "TC01: Normal is not perpendicular to v2");

        // Check that the normal is a unit vector
        assertEquals(1, normal.length(), 0.0000000001, "TC01: Normal is not a unit vector");

        // Compute the cross product of the two vectors
        Vector crossProduct = v1.crossProduct(v2);

        // Check that the cross product is not a unit vector (not exactly length 1)
        assertNotEquals(1, crossProduct.length(), 0.0000000001, "TC01: Cross product should not be a unit vector");

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

    @Test
    void testGetNormal() {
    }
}
