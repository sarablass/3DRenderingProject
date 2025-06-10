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
     * Sets the constant attenuation factor of the light.
     *
     * @param kC the constant attenuation factor
     * @return the current SpotLight instance
     */
    @Override
    public SpotLight setKc(double kC) {
        super.setKc(kC);
        return this;
    }

    /**
     * Sets the linear attenuation factor of the light.
     *
     * @param kL the linear attenuation factor
     * @return the current SpotLight instance
     */
    @Override
    public SpotLight setKl(double kL) {
        super.setKl(kL);
        return this;
    }

    /**
     * Sets the quadratic attenuation factor of the light.
     *
     * @param kQ the quadratic attenuation factor
     * @return the current SpotLight instance
     */
    @Override
    public SpotLight setKq(double kQ) {
        super.setKq(kQ);
        return this;
    }

    /**
     * set the narrow beam of the light
     * @param narrowBeam the narrow beam of the light
     * @return the light source
     */
    public SpotLight setNarrowBeam(double narrowBeam) {
        this.narrowBeam = narrowBeam;
        return this;
    }

    /**
     * get intensity of the light at a specific point
     * @param color color of the light
     * @param direction direction of the light
     * @param position position of the light source
     */
    public SpotLight(Color color, Point position,Vector direction) {
        super(color, position);
        this.direction = direction.normalize();
    }

    /**
     * Calculates the intensity of the light at a specific point.
     * The intensity is affected by the direction of the light and the narrow beam factor.
     *
     * @param point the point at which to calculate the light intensity
     * @return the color representing the light intensity at the specified point
     */
    @Override
    public Color getIntensity(Point point) {
        Vector l = getL(point);
//        double dirDot = l.dotProduct(direction.scale(-1));
        double dirDot = l.dotProduct(direction);
        double intensityFactor =Math.max(0d, dirDot);
        intensityFactor = Math.pow(intensityFactor, narrowBeam);
        return super.getIntensity(point).scale(intensityFactor);

    }

}