package primitives;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link primitives.Point} class.
 */
class PointTests {

    /**
     * Test method for {@link primitives.Point#subtract(primitives.Point)}.
     */
    @Test
    void testSubtract() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Subtracting two points results in the correct vector
        Point p1 = new Point(3, 5, 7);
        Point p2 = new Point(1, 2, 3);
        assertEquals(new Vector(2, 3, 4), p1.subtract(p2), "ERROR: Point subtraction gives incorrect vector");

        // =============== Boundary Values Tests ==================

        // TC02: Subtracting a point from itself should throw an exception
        assertThrows(IllegalArgumentException.class, () -> p1.subtract(p1), "ERROR: Subtracting a point from itself should throw an exception");
    }

    /**
     * Test method for {@link primitives.Point#add(primitives.Vector)}.
     */
    @Test
    void testAdd() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Adding a vector to a point results in the correct point
        Point p = new Point(1, 2, 3);
        Vector v = new Vector(3, 4, 5);
        assertEquals(new Point(4, 6, 8), p.add(v), "ERROR: Point addition gives incorrect result");
    }

    /**
     * Test method for {@link primitives.Point#distanceSquared(primitives.Point)}.
     */
    @Test
    void testDistanceSquared() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Computing squared distance between two points
        Point p1 = new Point(1, 2, 3);
        Point p2 = new Point(4, 6, 8);
        assertEquals(50, p1.distanceSquared(p2), "ERROR: distanceSquared() wrong value");

        // =============== Boundary Values Tests ==================

        // TC02: Distance squared from a point to itself should be zero
        assertEquals(0, p1.distanceSquared(p1), "ERROR: Distance squared to itself should be 0");
    }

    /**
     * Test method for {@link primitives.Point#distance(primitives.Point)}.
     */
    @Test
    void testDistance() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Computing distance between two points
        Point p1 = new Point(1, 2, 3);
        Point p2 = new Point(4, 6, 8);
        assertEquals(Math.sqrt(50), p1.distance(p2), 0.00001, "ERROR: distance() wrong value");

        // =============== Boundary Values Tests ==================

        // TC02: Distance from a point to itself should be zero
        assertEquals(0, p1.distance(p1), "ERROR: Distance to itself should be 0");
    }
}

