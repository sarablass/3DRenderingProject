package renderer;

import org.junit.jupiter.api.*;
import primitives.*;
import geometries.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CameraIntersectionsIntegrationTests {
    private final Vector vUp = new Vector(0, -1, 0);
    private final Vector vTo = new Vector(0, 0, -1);

    private final Camera.Builder cameraBuilder = Camera.getBuilder()
            .setDirection(vUp, vTo)
            .setVpDistance(1)
            .setVpSize(3, 3);



    private void assertCountsIntersections(int expected, Camera camera, Geometry geometry, int nX, int nY) {
        int count = 0;
        List<Point> intersections;

        for (int j = 0; j < nX; j++) {
            for (int i = 0; i < nY; i++) {
                intersections = geometry.findIntersections(camera.constructRay(nX, nY, j, i));
                count += intersections == null ? 0 : intersections.size();
            }
        }
        assertEquals(expected, count, "wrong number of intersections");
    }


    @Test
    public void CameraRaySphereIntegration() {

        Camera camera1 = cameraBuilder.setLocation(Point.ZERO).setDirection(new Vector(0, 0, -1), new Vector(0, -1, 0)).setVpDistance(1d).setVpSize(3d, 3d).build();
        Camera camera2= cameraBuilder.setLocation(new Point(0, 0, 0.5)).setDirection(new Vector(0, 0, -1), new Vector(0, -1, 0)).setVpDistance(1d).setVpSize(3d, 3d).build();


        //TC01: Sphere r=1 (2 intersections)
        assertCountsIntersections(2, camera1, new Sphere( new Point(0, 0, -3),1d), 3, 3);


        //TC02: Sphere r=2.5 (18 intersections)
        assertCountsIntersections(18, camera2, new Sphere( new Point(0, 0, -2.5),2.5d), 3, 3);


        //TC03: Sphere r=2 (10 intersections)
        assertCountsIntersections(10, camera2, new Sphere( new Point(0, 0, -2),2d), 3, 3);


        //TC04: Sphere r=4 (9 intersections)
        assertCountsIntersections(9, camera2, new Sphere( new Point(0, 0, 1),4d), 3, 3);

        //TC05: Sphere r=0.5 (0 intersections)
        assertCountsIntersections(0, camera1, new Sphere( new Point(0, 0, 1),0.5d), 3, 3);

    }

    /**
     * Test method for
     * {@link Camera#constructRay(int, int, int, int)}
     * and {@link geometries.Triangle#findIntersections(Ray)}.
     */
    @Test
    public void CameraRayTriangleIntegration() {
        Camera camera1 = cameraBuilder.setLocation(Point.ZERO).setDirection(new Vector(0, 0, -1), new Vector(0, -1, 0)).setVpDistance(1d).setVpSize(3d, 3d).build();



        //TC01: Small triangle (1 intersection)
        assertCountsIntersections(1, camera1, new Triangle(new Point(1, -1, -2), new Point(-1, -1, -2), new Point(0, 1, -2)), 3, 3);


        //TC02: Large triangle (2 intersection)
        assertCountsIntersections(2, camera1, new Triangle(new Point(1, -1, -2), new Point(-1, -1, -2), new Point(0, 20, -2)), 3, 3);


    }



    @Test
    public void CameraRayPlaneIntegration() {
        Camera camera1 = cameraBuilder.setLocation(Point.ZERO).setDirection(new Vector(0, 0, 1), new Vector(0, -1, 0)).setVpDistance(1d).setVpSize(3d, 3d).build();




        //TC01: The plane parallel to the View Plane (9 intersections)
        assertCountsIntersections(9, camera1, new Plane(new Point(0, 0, 5),
                new Vector(0, 0, 1)), 3, 3);


        //TC02: Diagonal plane to the View Plane (9 intersections)
        assertCountsIntersections(9, camera1, new Plane(new Point(0, 0, 5),
                new Vector(0, -1, 2)), 3, 3);


        ////TC03: Diagonal plane with an obtuse angle to the View Plane (6 intersections)
        assertCountsIntersections(6, camera1, new Plane(new Point(0, 0, 2),
                new Vector(1, 1, 1)), 3, 3);


        // TC04:The plane behind the view plane (0 intersections)
        assertCountsIntersections(0, camera1, new Plane(new Point(0, 0, -4),
                new Vector(0, 0, 1)), 3, 3);

    }
}
