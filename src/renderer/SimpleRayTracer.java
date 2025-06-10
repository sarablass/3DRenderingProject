package renderer;
import geometries.Intersectable.Intersection;
import scene.Scene;
import primitives.*;
import primitives.Point;
import lighting.LightSource;
import static primitives.Util.alignZero;
import java.util.List;
import primitives.Material;
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

    /**
     * Get color of the intersection of the ray with the scene
     * @param ray Ray to trace
     * @return Color of intersection
     */
    @Override
    public Color traceRay(Ray ray) {
        var intersections = scene.geometries.calculateIntersections(ray);return intersections == null
                ? scene.background
                : calcColor(ray.findClosestIntersection(intersections), ray);
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


    private Double3 calcSpecular(Intersection intersection){
        Vector n = intersection.normal.normalize();
        Vector l = intersection.l.normalize();
        Vector v = intersection.normal.normalize(); // inverse of ray direction

        // Calculate reflection vector R = L - 2 * (N·L) * N
        Vector r = l.subtract(n.scale(2 * intersection.lNormal)).normalize();

        // Calculate R·V (viewer direction)
        double rv = r.dotProduct(v) * -1;
        if (rv <= 0)
            return Double3.ZERO; // no specular if angle > 90 degrees

        // Calculate specular component: kS * (R·V)^nShininess
        return intersection.material.kS.scale(Math.pow(rv, intersection.material.nShininess));
    }
    private Double3 calcDiffusive(Intersection intersection){
        if (intersection.lNormal < 0) {
            return intersection.material.kD.scale(intersection.lNormal * -1);
        }
        return intersection.material.kD.scale(intersection.lNormal);
    }
}




