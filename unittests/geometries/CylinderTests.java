package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CylinderTests {
    /**
     * Delta value for accuracy when comparing the numbers of type 'double' in
     * assertEquals
     */
    private final double DELTA = 0.000001;
    /**
     * Test method for {@link geometries.Cylinder#getNormal(Point)(geometries.Cylinder)}.
     */
    @Test
    void testGetNormal() {



        // ============ Equivalence Partitions Tests ==============


        Cylinder cylinder = new Cylinder(1, new Ray(new Point(0, 0, 0), new Vector(0, 0, 1)), 5);

        // Test normal to a point on the lateral surface

        // TC01: ensure there are no exceptions
        assertDoesNotThrow(() -> cylinder.getNormal(new Point(1, 0, 3)), "Failed test: exception thrown");
        // generate the test result
        Vector result = cylinder.getNormal(new Point(1, 0, 3));
        // TC02: ensure |result| = 1
        assertEquals(1,
                result.length(),
                DELTA,
                "Cylinder's normal is not a unit vector");
        //TC03: check it is the right normal
        assertEquals((new Vector(1, 0, 0)),
                result,
                "Cylinder's normal at (1,1,1) is incorrect");


        // Test normal on the bottom base of the cylinder

        // TC04: ensure there are no exceptions
        assertDoesNotThrow(() -> cylinder.getNormal(new Point(0.5, 0.5, 0)), "Failed test: exception thrown");
        // generate the test result when the point is on the
        result = cylinder.getNormal(new Point(0.5, 0.5, 0));
        // TC05: ensure |result| = 1
        assertEquals(1,
                result.length(),
                DELTA,
                "Cylinder's normal is not a unit vector");
        //TC06: check it is the right normal
        assertEquals(new Vector(0, 0, -1),
                result,
                "ERROR: Cylinder's normal wrong result for bottom base");

        // Test normal on the top base of the cylinder

        // TC07: ensure there are no exceptions
        assertDoesNotThrow(() -> cylinder.getNormal(new Point(0.5, 0.5, 5)), "Failed test: exception thrown");
        // generate the test result
        result = cylinder.getNormal(new Point(0.5, 0.5, 5));
        // TC08: ensure |result| = 1
        assertEquals(1,
                result.length(),
                DELTA,
                "Cylinder's normal is not a unit vector");
        //TC09: check it is the right normal
        assertEquals(new Vector(0, 0, 1),
                result,
                "ERROR: Cylinder's normal wrong result for top base");


        // =============== Boundary Values Tests ==================

        //Test normal at the center of the bottom base of the cylinder

        // TC10: ensure there are no exceptions
        assertDoesNotThrow(() -> cylinder.getNormal(new Point(0, 0, 0)), "Failed test: exception thrown");
        // generate the test result
        result = cylinder.getNormal(new Point(0, 0, 0));
        // TC11: ensure |result| = 1
        assertEquals(1,
                result.length(),
                DELTA,
                "Cylinder's normal is not a unit vector");
        //TC12: check it is the right normal
        assertEquals(new Vector(0, 0, -1),
                result,
                "ERROR: Cylinder's normal wrong result for bottom base");

        // Test normal at the center of the top base of the cylinder
        // TC13: ensure there are no exceptions
        assertDoesNotThrow(() -> cylinder.getNormal(new Point(0, 0, 5)), "Failed test: exception thrown");
        // generate the test result
        result = cylinder.getNormal(new Point(0, 0, 5));
        // TC14: ensure |result| = 1
        assertEquals(1,
                result.length(),
                DELTA,
                "Cylinder's normal is not a unit vector");
        //TC15: check it is the right normal
        assertEquals(new Vector(0, 0, 1),
                result,
                "ERROR: Cylinder's normal wrong result for top base");


    }
}