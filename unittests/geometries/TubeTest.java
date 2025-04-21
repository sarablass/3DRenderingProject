package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Unit tests for the {@link geometries.Tube} class.
 */

class TubeTest {
    /**
     * Test method for {@link geometries.Tube#getNormal(primitives.Point)}.
     * This method tests the normal vector calculation for a Tube.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: There is a simple single test here - using a tube
        Tube tube = new Tube(1.0, new Ray(new Point(0, 0, 0), new Vector(0, 0, 1)));
        final Point point = new Point(1, 0, 0);
        // ensure the point is on the surface of the tube
        assertEquals(1.0, point.distance(tube.axis.getHead()), 1e-10, "Point is not on the surface of the tube");
        // ensure there are no exceptions

        // generate the test result
        Vector result = tube.getNormal(point);
        // ensure |result| = 1
        assertEquals(1, result.length(), 1e-10, "Tube's normal is not a unit vector");
        // ensure the result is orthogonal to the axis
        assertTrue(result.dotProduct(tube.axis.getDirection()) == 0, "Normal is not orthogonal to the axis");

        // =============== Boundary Values Tests ==================
        // TC11: Point is directly opposite the head of the axis ray
        final Point point2 = new Point(0, 1, 0);
        result = tube.getNormal(point2);
        assertFalse(result.equals(Vector.ZERO), "Normal vector should not be the zero vector");
        assertEquals(1, result.length(), 1e-10, "Tube's normal is not a unit vector");
        assertTrue(result.dotProduct(tube.axis.getDirection()) == 0, "Normal is not orthogonal to the axis");
    }
}