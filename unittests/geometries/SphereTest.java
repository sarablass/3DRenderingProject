package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static primitives.Util.isZero;

class SphereTest {
    final Point gp1 = new Point(0.0651530771650466, 0.355051025721682, 0);
    final Point gp2 = new Point(1.53484692283495, 0.844948974278318, 0);
    final List<Point> exp = List.of(gp1, gp2);
    final Vector v310 = new Vector(3, 1, 0);
    final Vector v110 = new Vector(1, 1, 0);
    final Point p01 = new Point(-1, 0, 0);
    /**
     * Test method for {@link geometries.Sphere#getNormal(primitives.Point)}.
     */

    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Simple test case - using a sphere
        Sphere sphere = new Sphere(new Point(0, 0, 0), 1.0);
        Point point = new Point(1, 0, 0);

        // Ensure the point is on the surface of the sphere
        assertEquals(1.0, point.distance(sphere.center), 1e-10, "Point is not on the surface of the sphere");

        // Ensure there are no exceptions
        assertDoesNotThrow(() -> sphere.getNormal(point), "");

        // Generate the test result
        Vector result = sphere.getNormal(point);

        // Ensure |result| = 1
        assertEquals(1, result.length(), 1e-10, "Sphere's normal is not a unit vector");
    }
}