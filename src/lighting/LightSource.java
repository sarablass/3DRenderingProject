package lighting;

import primitives.*;

/**
 * Interface representing a light source in the scene.
 * Provides methods to calculate the intensity and direction of light at a given point.
 */
public interface LightSource {
    /**
     * Calculates the intensity of the light at a specific point.
     *
     * @param p The point where the light intensity is calculated.
     * @return The color representing the intensity of the light.
     */
    Color getIntensity(Point p);

    /**
     * Calculates the direction vector from the light source to a specific point.
     *
     * @param p The point where the direction vector is calculated.
     * @return The vector pointing from the light source to the given point.
     */
    Vector getL(Point p);

    /**
     * Calculates the distance from the light source to a specific point.
     *
     * @param point The point to measure the distance to.
     * @return The distance from the light source to the given point.
     */
    double getDistance(Point point);
}
