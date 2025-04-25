package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RayTests {

    @Test
    void testGetPoint() {
        // ============ Equivalence Partitions Tests ==============
        Ray ray = new Ray(new Point(1, 1, 1), new Vector(1, 1, 1));
        // TC01: A point on the ray at a positive distance from the head
        assertEquals(new Point(1+2*(1/Math.sqrt(12)), 1+2*(1/Math.sqrt(12)), 1+2*(1/Math.sqrt(12))),
                ray.getPoint(1),
                "Failed test: A point on the ray at a positive distance of the head");
        // TC02: A point on the ray at a negative distance from the head
        assertEquals(new Point(1-1*(1/Math.sqrt(3)), 1-1*(1/Math.sqrt(3)), 1-1*(1/Math.sqrt(3))),
                ray.getPoint(-1),
                "Failed test: A point on the ray at a negative distance of the head");

        // =============== Boundary Values Tests ==================
        // TC03: A point on the ray at a distance of 0 from the head

        assertEquals(new Point(1, 1, 1),
                ray.getPoint(0),
                "Failed test: A point on the ray at a distance of 0 from the head");
    }
}