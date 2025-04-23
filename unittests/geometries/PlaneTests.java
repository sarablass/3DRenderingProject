package geometries;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import primitives.Util;

import java.util.List;

class PlaneTests {

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
        assertEquals(0, normal.dotProduct(vec1), DELTA, "Normal is not perpendicular to vec1");
        assertEquals(0, normal.dotProduct(vec2), DELTA, "Normal is not perpendicular to vec2");

        // Check that the normal is normalized (length == 1)
        assertEquals(1, normal.length(), DELTA, "Normal is not a unit vector");
    }

    /** A point used in some tests */
    private final Point p001 = new Point(0, 0, 1);
    /** A point used in some tests */
    private final Point p100 = new Point(1, 0, 0);
    /** A point used in some tests */
    private final Vector v001 = new Vector(0, 0, 1);
    /**
     * Test method for {@link geometries.Plane#findIntersections(primitives.Ray)}.
     */
    @Test
    public void testFindIntersections() {
        // ================ EP: The Ray must be neither orthogonal nor parallel to the plane ==================
        Plane plane = new Plane(new Point(1,0,1),
                new Point(0,1,1),
                new Point(1,1,1));
        //TC01: Ray intersects the plane
        assertEquals(List.of(new Point(1,0.5,1)),
                plane.findIntersections(new Ray(new Point(0,0.5,0),
                        new Vector(1,0,1))),
                "Ray does not intersects the plane");

        //TC02: Ray does not intersect the plane
        assertNull(plane.findIntersections(new Ray(new Point(1,0.5,2),
                        new Vector(1,2,5))),
                "Ray intersects the plane");


        // ====================== Boundary Values Tests =======================//
        // **** Group: Ray is parallel to the plane
        //TC10: The ray included in the plane
        assertNull(plane.findIntersections(new Ray(new Point(1,2,1), new Vector(1,0,0))),
                "Does not return null- when ray included in the plane");

        //TC11: The ray not included in the plane
        assertNull(plane.findIntersections(new Ray(new Point(1,2,2),
                        new Vector(1,0,0))),
                "Does not return null- when ray not included in the plane");

        // **** Group: Ray is orthogonal to the plane
        //TC12: before the plane
        assertEquals(List.of(new Point(1,1,1)),
                plane.findIntersections(new Ray(new Point(1,1,0),
                        new Vector(0,0,1))),
                "Ray is orthogonal to the plane, before the plane");

        //TC13: on the plane
        assertNull(plane.findIntersections(new Ray(new Point(1,2,1),
                        new Vector(0,0,1))),
                "Does not return null- when ray is orthogonal to the plane, on the plane");

        //TC14: after the plane
        assertNull(plane.findIntersections(new Ray(new Point(1,2,2),
                        new Vector(0,0,1))),
                "Does not return null- when ray is orthogonal to the plane, after the plane");

        // **** Group: Ray is neither orthogonal nor parallel to
        //TC15: Ray begins at the plane
        assertNull(plane.findIntersections(new Ray(new Point(2,4,1),
                        new Vector(2,3,5))),
                "Does not return null- when ray is neither orthogonal nor parallel to ray and begin at the plane");

        //TC16: Ray begins in the same point which appears as reference point in the plane
        assertNull(plane.findIntersections(new Ray(new Point(1,0,1), new Vector(2,3,5))),
                "Does not return null- when ray begins in the same point which appears as reference point in the plane");
    }
}