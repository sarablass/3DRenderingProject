package renderer;

import static java.awt.Color.*;

import org.junit.jupiter.api.Test;

import geometries.*;
import lighting.*;
import primitives.*;
import scene.Scene;
/**
 * Unit tests for rendering scenes with various geometric compositions and lighting effects.
 * This class contains multiple test methods to validate the rendering engine's behavior
 * under different scenarios, including soft shadows, reflections, and transparency.
 */
public class minipPictureTest {

    /**
     * Default constructor for the minipPictureTest class.
     * Initializes the test environment and prepares the scene for rendering.
     */
    public minipPictureTest(){}
    /**
     * Scene for the tests
     */
    private final Scene scene = new Scene("Test scene");
    /**
     * Camera builder for the tests with triangles
     */
    private final Camera.Builder cameraBuilder = Camera.getBuilder()     //
            .setRayTracer(scene, RayTracerType.SIMPLE);

    @Test  /* 1: ADAPTIVE OFF ; MT OFF */
    void Adaptive_Off_MT_Off() {
        renderSceneConfiguration(
                0,
                0,
                "house_Adaptive_Off_MT_Off"
        );
    }

    @Test  /* 2: ADAPTIVE OFF ; MT ON  */
    void Adaptive_Off_MT_On() {
        renderSceneConfiguration(
                0,
                3,
                "house_Adaptive_Off_MT_On"
        );
    }

    @Test  /* 3: ADAPTIVE ON  ; MT OFF */
    void Adaptive_On_MT_Off() {
        renderSceneConfiguration(
                6,
                0,
                "house_Adaptive_On_MT_Off"
        );
    }

    @Test  /* 4: ADAPTIVE ON  ; MT ON  */
    void Adaptive_On_MT_On() {
        renderSceneConfiguration(
                6,
                3,
                "house_Adaptive_On_MT_On"
        );
    }

    void renderSceneConfiguration(int adaptiveDepth, int mtAmount, String fileName) {
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

                // Tree trunk2 (cylinder)
                new Cylinder(
                        5, // Radius
                        new Ray(new Point(80, -100, -130), new Vector(0, 1, 0).normalize()), // Axis
                        25 // Height
                )
                        .setEmission(new Color(139, 69, 19)) // Brown color for trunk
                        .setMaterial(new Material().setKD(0.6).setKS(0.2).setShininess(20)),
                // Tree trunk (rectangular prism made of triangles)
                new Triangle(new Point(167, -90, -120), new Point(173, -90, -120), new Point(167, -60, -120))
                        .setEmission(new Color(139, 69, 19)) // Brown color for trunk
                        .setMaterial(new Material().setKD(0.6).setKS(0.2).setShininess(20)),
                new Triangle(new Point(173, -90, -120), new Point(173, -60, -120), new Point(167, -60, -120))
                        .setEmission(new Color(139, 69, 19))
                        .setMaterial(new Material().setKD(0.6).setKS(0.2).setShininess(20)),
                new Triangle(new Point(173, -90, -120), new Point(173, -90, -126), new Point(173, -60, -120))
                        .setEmission(new Color(139, 69, 19))
                        .setMaterial(new Material().setKD(0.6).setKS(0.2).setShininess(20)),
                new Triangle(new Point(173, -90, -126), new Point(173, -60, -126), new Point(173, -60, -120))
                        .setEmission(new Color(139, 69, 19))
                        .setMaterial(new Material().setKD(0.6).setKS(0.2).setShininess(20)),
                // Tree leaves (sphere) – basic

                new Sphere(new Point(170, -45, -120), 15)
                        .setEmission(new Color(0, 128, 0)) // Green color for leaves
                        .setMaterial(new Material().setKD(0.6).setKS(0.2).setShininess(30)),
                // Small blue sphere on the left side, closer to the camera
                new Sphere(new Point(-130, -90, -100), 10)
                        .setEmission(new Color(0, 0, 255)) // Strong blue
                        .setMaterial(new Material().setKD(0.5).setKS(0.3).setShininess(40)),

// Small orange sphere further left and deeper into the scene
                new Sphere(new Point(-100, -90, -150), 10)
                        .setEmission(new Color(255, 100, 0)) // Strong orange
                        .setMaterial(new Material().setKD(0.5).setKS(0.3).setShininess(40)),
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

        long t0 = System.nanoTime();
        cameraBuilder
                .setLocation(new Point(0, 50, 800)) // Slightly higher location for better angle
                .setDirection(new Point(0, -50, -150), Vector.AXIS_Y) // Pointing to scene center
                .setVpDistance(800).setVpSize(400, 400)
                .setResolution(1000, 1000)
                .setAdaptiveSuperSampling(adaptiveDepth) // Set adaptive super sampling depth
                .setMultithreading(mtAmount)
                .build()
                .renderImage()
                .writeToImage(fileName);

        double seconds = (System.nanoTime() - t0) / 1e9;

        System.out.printf("%-15s  depth=%d  MT=%s  ➜  %.3f sec%n",
                fileName, adaptiveDepth, mtAmount == 0 ? "Off" : "On", seconds);


    }
}