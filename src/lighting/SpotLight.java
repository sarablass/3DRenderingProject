package lighting;
import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * The SpotLight class represents a light source with a specific position and direction in the scene.
 * It extends the PointLight class and adds functionality for directional lighting and a narrow beam effect.
 */
public class SpotLight extends PointLight{
    /**
     * The direction of the spotlight.
     */
    private final Vector direction;
    /**
     * The narrow beam of the spotlight, which determines how focused the light is.
     * A value of 1 means a wide beam, while higher values create a narrower beam.
     */
    private Double narrowBeam = 1d;

    /**
     * get intensity of the light at a specific point
     * @param color color of the light
     * @param direction direction of the light
     * @param position position of the light source
     */
    public SpotLight(Color color, Point position,Vector direction) {
        super(color, position);
        this.direction = direction;
    }

    /**
     * Sets the constant attenuation factor.
     *
     * @param kC The constant attenuation factor.
     * @return The current `SpotLight` instance (for method chaining).
     */
    @Override
    public SpotLight setKc(double kC) {
        return (SpotLight) super.setKc(kC); // Call the parent method and cast the result
    }


    /**
     * Sets the linear attenuation factor.
     *
     * @param kL The linear attenuation factor.
     * @return The current `SpotLight` instance (for method chaining).
     */
    @Override
    public SpotLight setKl(double kL) {
        return (SpotLight) super.setKl(kL); // Call the parent method and cast the result
    }

    /**
     * Sets the quadratic attenuation factor.
     *
     * @param kQ The quadratic attenuation factor.
     * @return The current `SpotLight` instance (for method chaining).
     */
    @Override
    public SpotLight setKq(double kQ) {
        return (SpotLight) super.setKq(kQ); // Call the parent method and cast the result
    }

    /**
     * Returns the intensity of the light at a given point.
     * <p>
     * The intensity is calculated based on the distance from the light source to the point,
     * the attenuation factors, and the angle between the spotlight's direction and the direction
     * to the point.
     *
     * @param p The point at which the light intensity is calculated.
     * @return The intensity (color) of the light at the given point.
     */
    @Override
    public Color getIntensity(Point p) {
        // Calculate the factor based on the angle between the spotlight's direction and the direction to the point
        double factor = Math.max(0, direction.normalize().dotProduct(getL(p)));
        // Scale the parent class's intensity by the factor
        return super.getIntensity(p).scale(factor);
    }
}