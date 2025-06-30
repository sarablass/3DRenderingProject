package renderer;

import static java.awt.Color.*;

import org.junit.jupiter.api.Test;

import geometries.*;
import lighting.*;
import primitives.*;
import scene.Scene;

class FeaturesTests {
    FeaturesTests(){}
    /** Scene for the tests */
    private final Scene          scene         = new Scene("Test scene");
    /** Camera builder for the tests with triangles */
    private final Camera.Builder cameraBuilder = Camera.getBuilder()     //
            .setRayTracer(scene, RayTracerType.SIMPLE);
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
                .writeToImage("enhancedHouseSceneWithAllEffects+AntiAliasing");
    }


}
