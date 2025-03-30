package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link primitives.Vector} class.
 */
class VectorTest {

    /**
     * Unit tests for {@link primitives.Vector#Vector(double, double, double)}
     */
    @Test
    void testConstructorWithDoubles() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Creating a valid vector using three double values
        assertDoesNotThrow(() -> new Vector(1, 2, 3), "ERROR: Failed to create a valid vector with three doubles");

        // =============== Boundary Values Tests ==================

        // TC11: Creating a zero vector with three double values should throw an exception
        assertThrows(IllegalArgumentException.class,
                () -> new Vector(0, 0, 0),
                "ERROR: Zero vector with three doubles does not throw an exception");
    }

    /**
     * Unit tests for {@link primitives.Vector#Vector(Double3)}
     */
    @Test
    void testConstructorWithDouble3() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Creating a valid vector using a Double3 object
        assertDoesNotThrow(() -> new Vector(new Double3(1, 2, 3)), "ERROR: Failed to create a valid vector with Double3");

        // =============== Boundary Values Tests ==================

        // TC11: Creating a zero vector with a Double3 object should throw an exception
        assertThrows(IllegalArgumentException.class,
                () -> new Vector(Double3.ZERO),
                "ERROR: Zero vector with Double3 does not throw an exception");
    }

    /**
     * Test method for {@link primitives.Vector#add(primitives.Vector)}.
     */
    @Test
    void testAdd() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Test adding two general vectors
        Vector v1 = new Vector(1, 2, 3);
        Vector v2 = new Vector(1, 0, 0);
        assertEquals(new Vector(2, 2, 3), v1.add(v2));

        // =============== Boundary Values Tests ==================
        // TC11: Test adding a vector and its negative (zero vector should not be created)
        assertThrows(
                IllegalArgumentException.class,
                () -> v1.add(new Vector(-1, -2, -3)),
                "ERROR: Vector + -itself does not throw an exception"
        );
    }

    /**
     * Test method for {@link primitives.Vector#subtract(primitives.Vector)}.
     */
    @Test
    void testSubtract() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Test subtracting two general vectors
        Vector v1 = new Vector(5, 6, 7);
        Vector v2 = new Vector(1, 2, 3);
        assertEquals(new Vector(4, 4, 4), v1.subtract(v2));

        // =============== Boundary Values Tests ==================
        // TC11: Test subtracting a vector from itself (should give a zero vector)
        Vector v3 = new Vector(2, 3, 4);
        assertEquals(new Vector(0, 0, 0), v3.subtract(v3), "ERROR: Vector minus itself should result in a zero vector");

        // TC12: Test subtracting from a zero vector
        Vector zeroVector = new Vector(0, 0, 0);
        assertEquals(new Vector(1, 2, 3), zeroVector.subtract(new Vector(-1, -2, -3)), "ERROR: Subtracting negative values from zero vector should give positive results");
    }

    /**
     * Test method for {@link primitives.Vector#scale(double)}.
     */
    @Test
    void testScale() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Test scaling a vector by a positive scalar
        Vector v = new Vector(1, -2, 3);
        assertEquals(new Vector(2, -4, 6), v.scale(2));

        // TC02: Test scaling a vector by a negative scalar
        assertEquals(new Vector(-1.5, 3, -4.5), v.scale(-1.5));

        // =============== Boundary Values Tests ==================
        // TC11: Test scaling a vector by zero (should throw an exception)
        assertThrows(IllegalArgumentException.class, () -> v.scale(0), "ERROR: Scaling by zero does not throw an exception");
    }

    /**
     * Test method for {@link primitives.Vector#dotProduct(primitives.Vector)}.
     */
    @Test
    void testDotProduct() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Test dot product of two general vectors
        Vector v1 = new Vector(1, 2, 3);
        Vector v2 = new Vector(-2, -4, -6);
        assertEquals(-28, v1.dotProduct(v2), "ERROR: dotProduct() result is incorrect for general vectors");

        // =============== Boundary Values Tests ==================

        // TC11: Dot product of a vector with a zero vector should be 0
        Vector zeroVector = new Vector(0, 0, 0);
        assertEquals(0, v1.dotProduct(zeroVector), "ERROR: dotProduct() result is incorrect for zero vector");

        // TC12: Dot product of a vector with a vector of ones
        Vector onesVector = new Vector(1, 1, 1);
        assertEquals(6, v1.dotProduct(onesVector), "ERROR: dotProduct() result is incorrect for vector of ones");

        // TC13: Dot product of a vector with itself should be the squared length
        assertEquals(v1.lengthSquared(), v1.dotProduct(v1), "ERROR: dotProduct() result is incorrect for self dot product");
    }

    /**
     * Test method for
     * {@link primitives.Vector#crossProduct(primitives.Vector)}.
     */
    @Test
    void testCrossProduct() {
        /**
         * ============ Equivalence Partitions Tests ==============
         *
         * Scenario: Testing the cross-product of two orthogonal vectors.
         * The length of the cross product should be the product of the lengths of the two vectors.
         */
        Vector v1 = new Vector(1, 2, 3);
        Vector v2 = new Vector(0, -3, 2);
        Vector v3 = new Vector(-2, -4, -6);

        // TC01: Testing that the length of the cross product is correct
        Vector vr = v1.crossProduct(v2);
        assertEquals(v1.length() * v2.length(), vr.length(), 0.00001, "crossProduct() wrong result length");
        assertEquals(0, vr.dotProduct(v1), "crossProduct() result is not orthogonal to 1st operand");
        assertEquals(0, vr.dotProduct(v2), "crossProduct() result is not orthogonal to 2nd operand");

        /**
         * =============== Boundary Values Tests ==================
         *
         * TC11: Testing cross product for parallel vectors
         * Scenario: When the vectors are parallel, the cross product should be a zero vector.
         */
        assertThrows(IllegalArgumentException.class, () -> v1.crossProduct(v3), "crossProduct() for parallel vectors does not throw an exception");
    }

    /**
     * Test method for {@link primitives.Vector#lengthSquared()}.
     */
    @Test
    void testLengthSquared() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Correct squared length calculation
        Vector v1 = new Vector(1, 2, 3);
        assertEquals(14, v1.lengthSquared(), 0.00001, "ERROR: lengthSquared() wrong value");
    }

    /**
     * Test method for {@link primitives.Vector#length()}.
     */
    @Test
    void testLength() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Correct length calculation
        Vector v = new Vector(0, 3, 4);
        assertEquals(5, v.length(), 0.00001, "ERROR: length() wrong value");
    }

    /**
     * Test method for {@link primitives.Vector#normalize()}.
     */
    @Test
    void testNormalize() {
        Vector v = new Vector(0, 3, 4);
        Vector normalized = v.normalize();
        // ============ Equivalence Partitions Tests ==============
        // TC01: Normalization of a general vector
        assertEquals(1, normalized.length(), 0.00001, "ERROR: normalize() does not produce a unit vector");
        assertThrows(IllegalArgumentException.class, () -> normalized.crossProduct(v), "ERROR: normalize() result is not parallel to the original vector");
    }
}