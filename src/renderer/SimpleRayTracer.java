package renderer;
import geometries.Intersectable.Intersection;
import primitives.Vector;
import scene.Scene;
import primitives.Color;
import primitives.Point;
import primitives.Ray;
import static primitives.Util.alignZero;
import java.util.List;
import material.Material;
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
        if (preprocessIntersection(intersection, ray.getDirection()))return Color.BLACK;
        return scene
                .ambientLight.getIntensity().scale(intersection.material.kA).add(calcLocalEffects(intersection));

    }


}




