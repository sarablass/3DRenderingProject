package renderer;
import scene.Scene;
import primitives.Color;
import primitives.Point;
import primitives.Ray;

import java.util.List;

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
//        List<Point> intersections  = this.scene.geometries.findIntersections(ray);
//
//        if (intersections == null)
//            return this.scene.background;
//
//        Point closestPoint = ray.findClosestPoint(intersections);
//
//        return calcColor(closestPoint);
    }
//    /**
//     * Get the color of an intersection point
//     * @param point point of intersection
//     * @return Color of the intersection point
//     */
//    private Color calcColor(Point point) {
//        return this.scene.ambientLight.getIntensity();
//    }
}




