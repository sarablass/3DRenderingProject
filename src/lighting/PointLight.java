package lighting;
import primitives.*;

public class PointLight extends Light implements LightSource {
    /**
     * The position of the light source.
     */
    private final Point position;
    /**
     * The constant attenuation coefficient.
     */
    private double kC=1, kL=0, kQ=0;

    /**
     * Constructs a PointLight object with the given intensity and position.
     * @param intensity The intensity of the light source.
     * @param position The position of the light source.
     */
    public PointLight(Color intensity, Point position) {
        super(intensity);
        this.position = position;
    }

    /**
     * Setter for kC- the constant attenuation coefficient.
     * @param kC
     * @return
     */
    public PointLight setKc(double kC) {
        this.kC = kC;
        return this;
    }

    /**
     * Setter for kL- the linear attenuation coefficient.
     * @param kL
     * @return
     */
    public PointLight setKl(double kL) {
        this.kL = kL;
        return this;
    }

    /**
     * Setter for kQ- the quadratic attenuation coefficient.
     * @param kQ
     * @return
     */
    public PointLight setKq(double kQ) {
        this.kQ = kQ;
        return this;
    }

    @Override
    public Color getIntensity(Point p) {
        double d = position.distance(p); // Calculate the distance to the point
        return intensity.scale(1 / (kC + kL * d + kQ * d * d)); // Apply attenuation
    }

    @Override
    public Vector getL(Point p) {
        // Return the normalized vector from the light source to the point
        return p.subtract(position).normalize();
    }


}
