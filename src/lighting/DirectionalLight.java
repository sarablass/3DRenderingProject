package lighting;
import primitives.*;

public class DirectionalLight extends Light implements LightSource {
    private final Vector direction;

    /**
     * Constructs a DirectionalLight with the specified intensity and direction.
     *
     * @param intensity The color intensity of the light.
     * @param direction The direction vector of the light.
     */
    public DirectionalLight(Color intensity, Vector direction) {
        super(intensity);
        this.direction = direction.normalize();
    }

    @Override
    public Color getIntensity(Point p) {
        return this.intensity;
    }

    @Override
    public Vector getL(Point p) {
        return this.direction.normalize();
    }

}
