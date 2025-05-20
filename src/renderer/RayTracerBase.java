package renderer;
import scene.Scene;
import primitives.Color;
import primitives.Ray;

/**
 * RayTracerBase class represents a base class for ray tracers.
 * A ray tracer is a class that renders a scene by tracing rays from the camera to the scene.
 */
public abstract class  RayTracerBase  {
    protected final Scene scene;
    public RayTracerBase(Scene scene) {
        this.scene = scene;
    }
    /**
     * Traces a ray through the scene and returns the color of the intersection point.
     * @param ray The ray to trace.
     * @return The color of the intersection point.
     */
    public abstract Color traceRay(Ray ray);
}
