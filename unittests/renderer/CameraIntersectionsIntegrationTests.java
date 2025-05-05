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



    private void helpCountsIntersections(Camera camera, Geometry geometry, int expected) {
        int count = 0;
        List<Point> intersections;

        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 3; i++) {
                intersections = geometry.findIntersections(camera.constructRay(3, 3, j, i));
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
        helpCountsIntersections(camera1, new Sphere( new Point(0, 0, -3),1d),2 );


        //TC02: Sphere r=2.5 (18 intersections)
        helpCountsIntersections(camera2, new Sphere( new Point(0, 0, -2.5),2.5d),18 );


        //TC03: Sphere r=2 (10 intersections)
        helpCountsIntersections(camera2, new Sphere( new Point(0, 0, -2),2d),10 );


        //TC04: Sphere r=4 (9 intersections)
        helpCountsIntersections(camera2, new Sphere( new Point(0, 0, 1),4d),9 );

        //TC05: Sphere r=0.5 (0 intersections)
        helpCountsIntersections(camera1, new Sphere( new Point(0, 0, 1),0.5d),0 );

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
        helpCountsIntersections(camera1, new Triangle(new Point(1, -1, -2), new Point(-1, -1, -2), new Point(0, 1, -2)) ,1 );


        //TC02: Large triangle (2 intersection)
        helpCountsIntersections(camera1, new Triangle(new Point(1, -1, -2), new Point(-1, -1, -2), new Point(0, 20, -2)) ,2 );


    }



    @Test
    public void CameraRayPlaneIntegration() {
        Camera camera1 = cameraBuilder.setLocation(Point.ZERO).setDirection(new Vector(0, 0, 1), new Vector(0, -1, 0)).setVpDistance(1d).setVpSize(3d, 3d).build();




        //TC01: The plane parallel to the View Plane (9 intersections)
        helpCountsIntersections(camera1, new Plane(new Point(0, 0, 5),
                new Vector(0, 0, 1)) ,9 );


        //TC02: Diagonal plane to the View Plane (9 intersections)
        helpCountsIntersections(camera1, new Plane(new Point(0, 0, 5),
                new Vector(0, -1, 2)) ,9 );


        ////TC03: Diagonal plane with an obtuse angle to the View Plane (6 intersections)
        helpCountsIntersections(camera1, new Plane(new Point(0, 0, 2),
                new Vector(1, 1, 1)) ,6 );


        // TC04:The plane behind the view plane (0 intersections)
        helpCountsIntersections(camera1, new Plane(new Point(0, 0, -4),
                new Vector(0, 0, 1)) ,0 );

    }
}
