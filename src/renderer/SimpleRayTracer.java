package renderer;
import geometries.Intersectable.Intersection;
import scene.Scene;
import primitives.*;
import lighting.LightSource;
import static primitives.Util.alignZero;
import java.util.List;

import primitives.Material;

import static java.lang.Math.*;

/**
 * SimpleRayTracer class represents a simple ray tracer.
 * A simple ray tracer is a ray tracer that does not perform any ray tracing.
 */
public class SimpleRayTracer extends RayTracerBase{
    /**
     * Constructs a SimpleRayTracer object with the given scene.
     * @param scene The scene to render.
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    @Override
    public Color traceRay(Ray ray) {
        List<Intersection> intersections = scene.geometries.calculateIntersectionsHelper(ray);
        if (intersections == null || intersections.isEmpty()) {
            return scene.background;
        }
        Intersection in = ray.findClosestIntersection(intersections);
        return calcColor(in,ray);
    }



    /**
     * Get the color of an intersection point
     * @param intersection point of intersection
     * @return Color of the intersection point
     */
    private Color calcColor(Intersection intersection, Ray ray) {
        if (!preprocessIntersection(intersection, ray.getDirection())) {
            return Color.BLACK;
        }
        return scene
                .ambientLight.getIntensity().scale(intersection.material.kA)
                .add(calcColorLocalEffects(intersection));
    }

    private boolean preprocessIntersection(Intersection intersection, Vector v) {
        intersection.v = v;
        intersection.normal = intersection.geometry.getNormal(intersection.point);
        intersection.vNormal = alignZero(intersection.v.dotProduct(intersection.normal));
        return intersection.vNormal != 0;
    }
    private boolean setLightSource(Intersection intersection, LightSource light) {
        intersection.light = light;
        intersection.l = light.getL(intersection.point);
        intersection.lNormal = alignZero(intersection.l.dotProduct(intersection.normal));
        return intersection.lNormal * intersection.vNormal > 0;
    }
    private Color calcColorLocalEffects(Intersection intersection){
        if (intersection == null) {
            return scene.background;
        }
        Material material = intersection.material;
        Color color = intersection.geometry.getEmission();
        for (LightSource lightSource : scene.lights) {
            {
                if(!setLightSource(intersection, lightSource)) {
                    continue;
                }
                // Compute light intensity at the intersection point
                Color iL = lightSource.getIntensity(intersection.point);
                // Add contribution from diffusive and specular effects
                color = color.add(iL.scale(calcDiffusive(intersection))).add(iL.scale(calcSpecular(intersection)));
            }
        }
        return color;
    }

    /**
     * Calculates the specular component of the lighting at the intersection point.
     *
     * @param intersection the intersection point and its associated data
     * @return the specular component as a {@link Double3} value
     */
    private Double3 calcSpecular(Intersection intersection) {
        Material material = intersection.geometry.getMaterial();
        double nDotL = intersection.normal.dotProduct(intersection.l);
        Vector r = intersection.l.subtract(intersection.normal.scale(2 * nDotL));
        double rDotV = max(0, r.dotProduct((intersection.v).scale(-1)));
        return material.kS.scale(pow(rDotV, material.nShininess));
    }

    private Double3 calcDiffusive(Intersection intersection){
        if (intersection.lNormal < 0) {
            return intersection.material.kD.scale(intersection.lNormal * -1);
        }
        return intersection.material.kD.scale(intersection.lNormal);
    }


//    /**
//     * Constructor for the SimpleRayTracer.
//     *
//     * @param scene the 3D scene to be rendered
//     */
//    public SimpleRayTracer(Scene scene) {
//        super(scene);
//    }
//
//    /**
//     * Traces a ray and calculates the color at the intersection point.
//     * <p>
//     * This method checks for intersections of the ray with the geometries in the scene.
//     * If no intersections are found, the background color is returned. Otherwise, the
//     * color at the closest intersection point is calculated.
//     *
//     * @param ray the ray to trace
//     * @return the color at the intersection point or the background color if no intersection
//     */
//    @Override
//    public Color traceRay(Ray ray) {
//        // Check if the scene has geometries
//        if (Objects.equals(scene.geometries, new Geometries())) {
//            return scene.background; // Return background color if no geometries
//        }
//        // Check if the ray intersects with any geometries in the scene
//        if (scene.geometries.calculateIntersections(ray) == null) {
//            return scene.background; // Return background color if no intersection
//        }
//        // Find the closest intersection point
//        Intersection closestIntersection = ray.findClosestIntersection(scene.geometries.calculateIntersections(ray));
//        if (closestIntersection == null) {
//            return scene.background; // Return background color if no intersection
//        }
//        // Calculate the color at the intersection point
//        return calcColor(closestIntersection, ray);
//    }
//
//    /**
//     * Calculates the color at a given intersection point in the scene.
//     * <p>
//     * This method determines the color at the intersection point by combining the ambient light
//     * intensity and the local effects of lighting, such as diffuse and specular reflections.
//     * <p>
//     * If the intersection is invalid (e.g., the normal and view direction are not aligned properly),
//     * the method returns black.
//     *
//     * @param intersection the intersection point and its associated data (geometry, normal, etc.)
//     * @param ray the ray that intersects the geometry at the given point
//     * @return the calculated color at the intersection point
//     */
//    private Color calcColor(Intersection intersection, Ray ray) {
//        // Check if the intersection is valid
//        if (!preprocessIntersection(intersection, ray.getDirection())) {
//            return Color.BLACK; // Return black if the intersection is invalid
//        }
//        // Combine ambient light intensity with local lighting effects
//        return scene.ambientLight.getIntensity()
//                .scale(intersection.geometry.getMaterial().kA) // Scale by ambient reflection coefficient
//                .add(calcColorLocalEffects(intersection)); // Add local lighting effects
//    }
//
//    /**
//     * Preprocesses the intersection by calculating and validating the normal and view direction.
//     *
//     * @param intersection the intersection point and its associated data
//     * @param v the direction of the ray
//     * @return true if the intersection is valid, false otherwise
//     */
//    public Boolean preprocessIntersection(Intersection intersection, Vector v) {
//        intersection.v = v;
//        intersection.normal = intersection.geometry.getNormal(intersection.point);
//        intersection.vNormal = alignZero(intersection.v.dotProduct(intersection.normal));
//        return intersection.vNormal != 0;
//    }
//
//    /**
//     * Sets the light source properties for the given intersection.
//     *
//     * @param intersection the intersection point and its associated data
//     * @param light the light source affecting the intersection point
//     * @return true if the light source contributes to the intersection, false otherwise
//     */
//    public Boolean setLightSource(Intersection intersection, LightSource light) {
//        intersection.light = light;
//        intersection.l = light.getL(intersection.point);
//        intersection.lNormal = alignZero(intersection.l.dotProduct(intersection.normal));
//        return intersection.lNormal * intersection.vNormal > 0;
//    }
//
//    /**
//     * Calculates the diffusive component of the lighting at the intersection point.
//     *
//     * @param intersection the intersection point and its associated data
//     * @return the diffusive component as a {@link Double3} value
//     */
//    private Double3 calcDiffusive(Intersection intersection) {
//        Material material = intersection.geometry.getMaterial();
//        double nDotL = abs(intersection.normal.dotProduct(intersection.l));
//        return material.kD.scale(nDotL);
//    }
//
//    /**
//     * Calculates the specular component of the lighting at the intersection point.
//     *
//     * @param intersection the intersection point and its associated data
//     * @return the specular component as a {@link Double3} value
//     */
//    private Double3 calcSpecular(Intersection intersection) {
//        Material material = intersection.geometry.getMaterial();
//        double nDotL = intersection.normal.dotProduct(intersection.l);
//        Vector r = intersection.l.subtract(intersection.normal.scale(2 * nDotL));
//        double rDotV = max(0, r.dotProduct((intersection.v).scale(-1)));
//        return material.kS.scale(pow(rDotV, material.nShininess));
//    }
//
//    /**
//     * Calculates the local lighting effects (diffuse and specular) at the intersection point.
//     *
//     * @param intersection the intersection point and its associated data
//     * @return the calculated color including local lighting effects
//     */
//    private Color calcColorLocalEffects(Intersection intersection) {
//        Color color = intersection.geometry.getEmission();
//
//        for (LightSource lightSource : scene.lights) {
//            if (!setLightSource(intersection, lightSource)) {
//                continue;
//            }
//            Color iL = lightSource.getIntensity(intersection.point);
//            Double3 diff = calcDiffusive(intersection);
//            Double3 spec = calcSpecular(intersection);
//            color = color.add(iL.scale(diff.add(spec)));
//        }
//        return color;
//    }
}




