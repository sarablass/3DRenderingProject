package primitives;

import org.junit.jupiter.api.Test;
import java.util.LinkedList;
import java.util.List;

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

    @Test
    void testFindClosestPoint() {
        List<Point> pointList = new LinkedList<>();

        Point p1 = new Point(1, 1, 1);
        Point p2 = new Point(2, 2, 2);
        Point p3 = new Point(3, 3, 3);

        pointList.add(p1);
        pointList.add(p2);
        pointList.add(p3);

        Vector vector = new Vector(0, -0.5, 0);

        // ============ Equivalence Partitions Tests ==============
        //TC01: The closest point is in the middle of the list
        Ray ray1 = new Ray(new Point(2, 2.5, 2), vector);
        assertEquals(p2, ray1.findClosestPoint(pointList), "The point in the middle");

        // =============== Boundary Values Tests ==================
        //TC10: The closest point is the first point in the list
        Ray ray2 = new Ray(new Point(1, 1.25, 1), vector);
        assertEquals(p1, ray2.findClosestPoint(pointList), "The point is the first one");

        //TC11: The closest point is the last point in the list
        Ray ray3 = new Ray(new Point(3, 3.5, 3), vector);
        assertEquals(p3, ray3.findClosestPoint(pointList), "The point is the last one");

        //TC12: The list is null
        pointList.clear();
        assertNull(ray3.findClosestPoint(pointList), "The list is empty");

    }
}