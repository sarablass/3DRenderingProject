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
 * SimpleRayTracer is a basic ray tracing engine that computes the color of rays
 * by finding intersections and applying local lighting models (diffuse and specular).
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

    /**
     * Preprocesses the intersection by setting the view vector and calculating
     * the dot product between the view vector and the surface normal.
     * This is used to verify that the surface is visible from the camera's perspective.
     *
     * @param intersection the intersection to process
     * @param v the direction of the ray
     * @return true if the surface is visible (dot product ≠ 0), false otherwise
     */
    private boolean preprocessIntersection(Intersection intersection, Vector v) {
        intersection.v = v;
        intersection.normal = intersection.geometry.getNormal(intersection.point);
        intersection.vNormal = alignZero(intersection.v.dotProduct(intersection.normal));
        return intersection.vNormal != 0;
    }

    /**
     * Initializes the lighting parameters at the intersection point for a given light source.
     * Calculates the direction from the point to the light and checks if the light contributes
     * to the shading (i.e., both light and view are on the same side of the surface).
     *
     * @param intersection the intersection to update with lighting data
     * @param light the light source
     * @return true if the light contributes to the shading, false otherwise
     */
    private boolean setLightSource(Intersection intersection, LightSource light) {
        intersection.light = light;
        intersection.l = light.getL(intersection.point);
        intersection.lNormal = alignZero(intersection.l.dotProduct(intersection.normal));
        return intersection.lNormal * intersection.vNormal > 0;
    }

    /**
     * Calculates the color contribution from local lighting effects (diffuse and specular).
     * Iterates over all light sources and sums their influence on the given intersection.
     *
     * @param intersection the intersection data containing geometry, normal, and material
     * @return the total local color contribution
     */
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

    /**
     * Calculates the diffusive reflection component at the intersection point
     * using Lambert's cosine law. The result is proportional to the cosine
     * of the angle between the surface normal and the light direction.
     *
     * @param intersection the intersection point and associated data
     * @return the diffusive component as a {@link Double3} value
     */
    private Double3 calcDiffusive(Intersection intersection){
        if (intersection.lNormal < 0) {
            return intersection.material.kD.scale(intersection.lNormal * -1);
        }
        return intersection.material.kD.scale(intersection.lNormal);
    }
}




