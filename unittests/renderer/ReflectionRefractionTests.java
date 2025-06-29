package renderer;

import static java.awt.Color.*;

import org.junit.jupiter.api.Test;

import geometries.*;
import lighting.*;
import primitives.*;
import scene.Scene;
import renderer.SamplingType;

/**
 * Tests for reflection and transparency functionality, test for partial
 * shadows
 * (with transparency)
 * @author Dan Zilberstein
 */
class ReflectionRefractionTests {
   /** Default constructor to satisfy JavaDoc generator */
   ReflectionRefractionTests() { /* to satisfy JavaDoc generator */ }

   /** Scene for the tests */
   private final Scene          scene         = new Scene("Test scene");
   /** Camera builder for the tests with triangles */
   private final Camera.Builder cameraBuilder = Camera.getBuilder()     //
      .setRayTracer(scene, RayTracerType.SIMPLE);

   /** Produce a picture of a sphere lighted by a spot light */
   @Test
   void twoSpheres() {
      scene.geometries.add( //
                           new Sphere(new Point(0, 0, -50), 50d).setEmission(new Color(BLUE)) //
                              .setMaterial(new Material().setKD(0.4).setKS(0.3).setShininess(100).setKT(0.3)), //
                           new Sphere(new Point(0, 0, -50), 25d).setEmission(new Color(RED)) //
                              .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(100))); //
      scene.lights.add( //
                       new SpotLight(new Color(1000, 600, 0), new Point(-100, -100, 500), new Vector(-1, -1, -2)) //
                          .setKl(0.0004).setKq(0.0000006));

      cameraBuilder
         .setLocation(new Point(0, 0, 1000)) //
         .setDirection(Point.ZERO, Vector.AXIS_Y) //
         .setVpDistance(1000).setVpSize(150, 150) //
         .setResolution(500, 500) //
         .build() //
         .renderImage() //
         .writeToImage("refractionTwoSpheres");
   }

   /** Produce a picture of a sphere lighted by a spot light */
   @Test
   void twoSpheresOnMirrors() {
      scene.geometries.add( //
                           new Sphere(new Point(-950, -900, -1000), 400d).setEmission(new Color(0, 50, 100)) //
                              .setMaterial(new Material().setKD(0.25).setKS(0.25).setShininess(20) //
                                 .setKT(new Double3(0.5, 0, 0))), //
                           new Sphere(new Point(-950, -900, -1000), 200d).setEmission(new Color(100, 50, 20)) //
                              .setMaterial(new Material().setKD(0.25).setKS(0.25).setShininess(20)), //
                           new Triangle(new Point(1500, -1500, -1500), new Point(-1500, 1500, -1500), //
                                        new Point(670, 670, 3000)) //
                              .setEmission(new Color(20, 20, 20)) //
                              .setMaterial(new Material().setKR(1)), //
                           new Triangle(new Point(1500, -1500, -1500), new Point(-1500, 1500, -1500), //
                                        new Point(-1500, -1500, -2000)) //
                              .setEmission(new Color(20, 20, 20)) //
                              .setMaterial(new Material().setKR(new Double3(0.5, 0, 0.4))));
      scene.setAmbientLight(new AmbientLight(new Color(26, 26, 26)));
      scene.lights.add(new SpotLight(new Color(1020, 400, 400), new Point(-750, -750, -150), new Vector(-1, -1, -4)) //
         .setKl(0.00001).setKq(0.000005));

      cameraBuilder
         .setLocation(new Point(0, 0, 10000)) //
         .setDirection(Point.ZERO, Vector.AXIS_Y) //
         .setVpDistance(10000).setVpSize(2500, 2500) //
         .setResolution(500, 500) //
         .build() //
         .renderImage() //
         .writeToImage("reflectionTwoSpheresMirrored");
   }

   /**
    * Produce a picture of a two triangles lighted by a spot light with a
    * partially
    * transparent Sphere producing partial shadow
    */
   @Test
   void trianglesTransparentSphere() {
      scene.geometries.add(
                           new Triangle(new Point(-150, -150, -115), new Point(150, -150, -135),
                                        new Point(75, 75, -150))
                              .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(60)),
                           new Triangle(new Point(-150, -150, -115), new Point(-70, 70, -140), new Point(75, 75, -150))
                              .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(60)),
                           new Sphere(new Point(60, 50, -50), 30d).setEmission(new Color(BLUE))
                              .setMaterial(new Material().setKD(0.2).setKS(0.2).setShininess(30).setKT(0.6)));
      scene.setAmbientLight(new AmbientLight(new Color(38, 38, 38)));
      scene.lights.add(
                       new SpotLight(new Color(700, 400, 400), new Point(60, 50, 0), new Vector(0, 0, -1))
                          .setKl(4E-5).setKq(2E-7));

      cameraBuilder
         .setLocation(new Point(0, 0, 1000)) //
         .setDirection(Point.ZERO, Vector.AXIS_Y) //
         .setVpDistance(1000).setVpSize(200, 200) //
         .setResolution(600, 600) //
         .build() //
         .renderImage() //
         .writeToImage("refractionShadow");
   }

    /**
     * Produce a picture of a house scene with various effects
     * including reflections, refractions, and shadows.
     */
   @Test
   void renderEnhancedHouseSceneWithAllEffects() {
      scene.geometries.add(
              // Grass (plane) – slightly reflective
              new Plane(new Point(0, -100, 0), new Vector(0, 1, 0))
                      .setEmission(new Color(34, 139, 34)) // Green color for grass
                      .setMaterial(new Material().setKD(0.6).setKS(0.2).setShininess(20).setKR(0.1)), // slight reflection

              // Sky (plane)
              new Plane(new Point(0, 0, -500), new Vector(0, 0, 1))
                      .setEmission(new Color(135, 206, 250)) // Light blue color for sky
                      .setMaterial(new Material().setKD(0.8).setKS(0.1).setShininess(10)),

              // Mirror lake/pond – strongly reflective surface
              new Triangle(new Point(-80, -99, -120), new Point(80, -99, -120), new Point(-80, -99, -200))
                      .setEmission(new Color(30, 30, 60)) // Dark blue
                      .setMaterial(new Material().setKD(0.1).setKS(0.3).setShininess(100).setKR(0.8)), // strong reflection
              new Triangle(new Point(80, -99, -120), new Point(80, -99, -200), new Point(-80, -99, -200))
                      .setEmission(new Color(30, 30, 60))
                      .setMaterial(new Material().setKD(0.1).setKS(0.3).setShininess(100).setKR(0.8)),

              // House walls (yellow cube made of triangles)
              new Triangle(new Point(-50, -100, -150), new Point(50, -100, -150), new Point(-50, 0, -150))
                      .setEmission(new Color(255, 255, 0)) // Yellow color for walls
                      .setMaterial(new Material().setKD(0.5).setKS(0.3).setShininess(20)),
              new Triangle(new Point(50, -100, -150), new Point(50, 0, -150), new Point(-50, 0, -150))
                      .setEmission(new Color(255, 255, 0))
                      .setMaterial(new Material().setKD(0.5).setKS(0.3).setShininess(20)),

              // Roof (red triangle) – with slight reflection
              new Triangle(new Point(-60, 0, -150), new Point(60, 0, -150), new Point(0, 50, -150))
                      .setEmission(new Color(200, 0, 0)) // Red color for roof
                      .setMaterial(new Material().setKD(0.4).setKS(0.4).setShininess(50).setKR(0.2)), // reflective roof

              // Tree trunk (cylinder)
              new Cylinder(
                      10, // Radius
                      new Ray(new Point(120, -100, -150), new Vector(0, 1, 0).normalize()), // Axis
                      50 // Height
              )
                      .setEmission(new Color(139, 69, 19)) // Brown color for trunk
                      .setMaterial(new Material().setKD(0.6).setKS(0.2).setShininess(20)),
              // Tree trunk (rectangular prism made of triangles)
              new Triangle(new Point(115, -100, -145), new Point(125, -100, -145), new Point(115, -50, -145))
                      .setEmission(new Color(139, 69, 19)) // Brown color for trunk
                      .setMaterial(new Material().setKD(0.6).setKS(0.2).setShininess(20)),
              new Triangle(new Point(125, -100, -145), new Point(125, -50, -145), new Point(115, -50, -145))
                      .setEmission(new Color(139, 69, 19))
                      .setMaterial(new Material().setKD(0.6).setKS(0.2).setShininess(20)),
              new Triangle(new Point(125, -100, -145), new Point(125, -100, -155), new Point(125, -50, -145))
                      .setEmission(new Color(139, 69, 19))
                      .setMaterial(new Material().setKD(0.6).setKS(0.2).setShininess(20)),
              new Triangle(new Point(125, -100, -155), new Point(125, -50, -155), new Point(125, -50, -145))
                      .setEmission(new Color(139, 69, 19))
                      .setMaterial(new Material().setKD(0.6).setKS(0.2).setShininess(20)),

              // Tree leaves (sphere) – basic
              new Sphere(new Point(120, -40, -150), 25)
                      .setEmission(new Color(0, 128, 0)) // Green color for leaves
                      .setMaterial(new Material().setKD(0.6).setKS(0.2).setShininess(30)),

              // Sun (sphere) – moderate self-emission
              new Sphere(new Point(-200, 200, -400), 40)
                      .setEmission(new Color(200, 200, 80)) // Softer yellow
                      .setMaterial(new Material().setKD(0.3).setKS(0.1).setShininess(10))
      );

      // Define ambient light for better overall effects
      scene.setAmbientLight(new AmbientLight(new Color(40, 40, 40))); // Soft background light

      scene.lights.add(
              // Main light from the sun – reduced intensity
              new SpotLight(new Color(400, 300, 200), new Point(-150, 150, 0), new Vector(1, -1, -1))
                      .setKl(0.00001).setKq(0.000001)
                      .setNarrowBeam(15)
      );

      cameraBuilder
              .setLocation(new Point(0, 50, 800)) // Slightly higher location for better angle
              .setDirection(new Point(0, -50, -150), Vector.AXIS_Y) // Pointing to scene center
              .setVpDistance(800).setVpSize(400, 400)
              .setResolution(1000, 1000)
              .setAntiAliasing(SamplingType.GRID, 3) // Using jittered sampling for better quality
              .build()
              .renderImage()
              .writeToImage("enhancedHouseSceneWithAllEffects");
   }

}
