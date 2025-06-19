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
    private static final int MAX_CALC_COLOR_LEVEL = 10;
    private static final double MIN_CALC_COLOR_K = 0.001;
    private static final Double3 INITIAL_K = Double3.ONE;
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
        Intersection in = findClosestIntersection(ray);
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
                .ambientLight.getIntensity().scale(intersection.geometry.getMaterial().kA)
                .add(calcColor(intersection, MAX_CALC_COLOR_LEVEL, INITIAL_K));
    }

    /**
     * Calculates the color at an intersection point, including both local and global effects.
     * <p>
     * This method handles recursive ray tracing for global effects (reflection and refraction)
     * up to a specified recursion level. For the base case (level = 1), it computes only
     * local effects. For higher levels, it adds global effects by recursively tracing
     * reflected and refracted rays. The method uses an attenuation factor ({@code k})
     * to control the contribution of recursive calls, terminating early if the contribution
     * falls below {@code MIN_CALC_COLOR_K}.
     * </p>
     *
     * @param intersection the intersection point, including geometry, normal, and material properties
     * @param level the current recursion level for global effects
     * @param k the cumulative attenuation factor for recursive ray contributions
     * @return the calculated color, including local and global effects
     */
    private Color calcColor(Intersection intersection, int level, Double3 k) {
        Color color = calcColorLocalEffects(intersection, k);
        return level == 1 ? color : color.add(calcGlobalEffects(intersection, level, k));
    }

    /**
     * Constructs a refracted ray at the intersection point.
     * <p>
     * This method creates a refracted ray based on the intersection point and the view direction,
     * assuming no change in direction due to refraction (simple transparency model). The ray
     * is offset slightly along the normal to avoid self-intersection, following the ray tracing
     * pattern for handling transparent surfaces.
     * </p>
     *
     * @param intersection the intersection point, including the point, normal, and view direction
     * @return the refracted ray
     */
    private Ray constructRefractedRay(Intersection intersection) {
        return new Ray(intersection.point, intersection.v, intersection.normal);
    }

    /**
     * Constructs a reflected ray at the intersection point.
     * <p>
     * This method computes the reflected ray using the reflection formula: R = V - 2(V·N)N,
     * where V is the view direction and N is the surface normal. The ray is offset slightly
     * along the normal to avoid self-intersection, following the ray tracing pattern for
     * handling reflective surfaces.
     * </p>
     *
     * @param intersection the intersection point, including the point, normal, and view direction
     * @return the reflected ray
     */
    private Ray constructReflectedRay(Intersection intersection) {
        Vector v = intersection.v;
        Vector n = intersection.normal;
        double vn = v.dotProduct(n);
        Vector r = v.subtract(n.scale(2 * vn));
        return new Ray(intersection.point, r, n);
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
     * Iterates over all light sources and sums their influence on the given intersection,
     * attenuated by transparency and cumulative factor k.
     *
     * @param intersection the intersection data containing geometry, normal, and material
     * @param k the cumulative attenuation factor (used for recursion depth and transparency)
     * @return the total local color contribution
     */
    private Color calcColorLocalEffects(Intersection intersection, Double3 k) {
        if (intersection == null) {
            return scene.background;
        }

        Material material = intersection.material;
        Color color = intersection.geometry.getEmission();

        for (LightSource lightSource : scene.lights) {
            if (!setLightSource(intersection, lightSource)) {
                continue;
            }

            // Compute transparency toward the light
            Double3 ktr = transparency(intersection);

            // If the light's contribution is negligible, skip it
            if (ktr.product(k).lowerThan(MIN_CALC_COLOR_K)) {
                continue;
            }

            // Compute light intensity at the intersection point, scaled by transparency
            Color iL = lightSource.getIntensity(intersection.point).scale(ktr);

            // Add contribution from diffusive and specular effects
            Double3 diff = calcDiffusive(intersection);
            Double3 spec = calcSpecular(intersection);
            color = color.add(iL.scale(diff.add(spec)));
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

    /**
     * Determines whether the intersection point is unshaded by checking if any geometry blocks the light source.
     * <p>
     * This method casts a shadow ray from the intersection point toward the light source and checks
     * for intersections with other geometries. If an opaque geometry is found within the light's distance,
     * the point is considered shaded. Otherwise, it is unshaded.
     * </p>
     *
     * @param intersection The intersection point to check for shading.
     * @return true if the point is unshaded (no opaque object blocks the light), false otherwise.
     */
    private boolean unshaded(Intersection intersection) {
        // Calculate the direction from the intersection point toward the light source
        Vector lightDirection = intersection.l.scale(-1);

        // Create the shadow ray using the offset constructor to avoid self-intersections
        Ray shadowRay = new Ray(intersection.point, lightDirection, intersection.normal);

        // Find all intersections of the shadow ray with the scene's geometries
        List<Intersection> shadowIntersections = scene.geometries.calculateIntersectionsHelper(shadowRay);
        if (shadowIntersections == null) {
            return true; // No objects intersect the shadow ray — point is illuminated
        }

        double lightDistance = intersection.light.getDistance(intersection.point);

        // Iterate through the intersections to check for opaque obstacles
        for (Intersection shadowIntersection : shadowIntersections) {
            double distanceToShadowPoint = shadowIntersection.point.distance(intersection.point);

            // Only consider intersections that are between the point and the light source
            if (alignZero(distanceToShadowPoint - lightDistance) <= 0) {
                // If the object is sufficiently opaque, it blocks the light
                if (shadowIntersection.geometry.getMaterial().kT.lowerThan(MIN_CALC_COLOR_K )) {
                    return false; // Light is blocked by an opaque object — point is in shadow
                }
            }
        }

        return true; // No sufficiently opaque object blocks the light — point is illuminated
    }

    /**
     * Calculates the transparency factor for an intersection point relative to a light source.
     * <p>
     * This method casts a shadow ray from the intersection point to the light source and
     * accumulates the transparency coefficients ({@code kT}) of any intersecting geometries.
     * If an opaque geometry (with {@code kT < MIN_CALC_COLOR_K}) is found, the transparency
     * factor is zero. Otherwise, it returns the product of all transparency coefficients.
     * This method supports soft shadows and partial transparency in the ray tracing pipeline.
     * </p>
     *
     * @param intersection the intersection point, including point, light direction, and normal
     * @return the transparency factor as a {@link Double3} value (1 for fully transparent, 0 for fully opaque)
     */
    private Double3 transparency(Intersection intersection) {
        // Calculate the ray from the intersection point towards the light source
        Vector pointToLight = intersection.l.scale(-1);
        Ray shadowRay = new Ray(intersection.point, pointToLight, intersection.normal);
        double maxDistance = intersection.light.getDistance(intersection.point);

        // Find all intersections with the shadow ray
        List<Intersection> allIntersections = scene.geometries.calculateIntersections(shadowRay);

        // Filter intersections that are within the distance to the light source
        List<Intersection> shadowIntersections = null;
        if (allIntersections != null) {
            shadowIntersections = allIntersections.stream()
                    .filter(inter -> intersection.point.distance(inter.point) < maxDistance)
                    .toList();
            if (shadowIntersections.isEmpty()) {
                shadowIntersections = null;
            }
        }

        // If no intersection, the object is fully transparent (no shadow)
        if (shadowIntersections == null) return Double3.ONE;

        // Initialize transparency factor to 1 (fully transparent)
        Double3 ktr = Double3.ONE;

        // For each intersection point between the object and light source
        for (Intersection shadowInter : shadowIntersections) {
            // Multiply the transparency factor by the material's transparency
            ktr = ktr.product(shadowInter.geometry.getMaterial().kT);

            // If the accumulated transparency is too low, consider it opaque
            if (ktr.lowerThan(MIN_CALC_COLOR_K)) {
                return Double3.ZERO;
            }
        }

        return ktr;
    }

    /**
     * Calculates the global lighting effects (reflection and refraction) at the intersection point.
     * <p>
     * This method combines the contributions of reflected and refracted rays, recursively
     * tracing them to compute their colors. The contributions are scaled by the material's
     * reflection ({@code kR}) and transmission ({@code kT}) coefficients. This implements
     * the recursive ray tracing pattern for global illumination effects.
     * </p>
     *
     * @param intersection the intersection point, including geometry, normal, and material properties
     * @param level the current recursion level
     * @param k the cumulative attenuation factor
     * @return the combined color from reflection and refraction
     */
    private Color calcGlobalEffects(Intersection intersection, int level, Double3 k) {
        return calcGlobalEffect(constructRefractedRay(intersection), level, k, intersection.geometry.getMaterial().kT)
                .add(calcGlobalEffect(constructReflectedRay(intersection), level, k, intersection.geometry.getMaterial().kR));
    }

    /**
     * Calculates the color contribution of a single global effect (reflection or refraction).
     * <p>
     * This method traces a ray (either reflected or refracted) and computes its color contribution,
     * scaled by the material's transmission or reflection coefficient ({@code kx}). If the
     * cumulative attenuation ({@code kkx}) falls below {@code MIN_CALC_COLOR_K}, the contribution
     * is ignored to optimize performance. If no intersection is found, the background color is
     * scaled by {@code kx}. This method follows the recursive ray tracing pattern.
     * </p>
     *
     * @param ray the ray to trace (reflected or refracted)
     * @param level the current recursion level
     * @param k the cumulative attenuation factor
     * @param kx the material's reflection or transmission coefficient
     * @return the color contribution of the global effect
     */
    private Color calcGlobalEffect(Ray ray, int level, Double3 k, Double3 kx) {
        Double3 kkx = k.product(kx);
        if (kkx.lowerThan(MIN_CALC_COLOR_K)) return Color.BLACK;
        Intersection intersection = findClosestIntersection(ray);
        if (intersection == null) return scene.background.scale(kx);
        return preprocessIntersection(intersection, ray.getDirection())
                ? calcColor(intersection, level - 1, kkx).scale(kx) : Color.BLACK;
    }

    /**
     * Finds the closest intersection of a ray with the scene's geometries.
     * <p>
     * This method retrieves all intersections of the ray with the scene's geometries and
     * returns the closest one using the ray's {@code findClosestIntersection} method.
     * If no intersections are found, it returns null. This is a key component of the
     * ray tracing algorithm, determining which geometry contributes to the pixel's color.
     * </p>
     *
     * @param ray the ray to check for intersections
     * @return the closest intersection, or null if no intersections are found
     */
    private Intersection findClosestIntersection(Ray ray) {
        List<Intersection> intersections = scene.geometries.calculateIntersections(ray);
        return intersections == null ? null : ray.findClosestIntersection(intersections);
    }
}




