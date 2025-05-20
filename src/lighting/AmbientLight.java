package lighting;

import primitives.Color;
import primitives.Double3;

public class AmbientLight {
    private final Color intensity; // The light color
    public static final AmbientLight NONE = new AmbientLight(Color.BLACK);

    /**
     * Constructs an AmbientLight object with the given intensity.
     * The intensity is calculated by scaling the given color by the factor `ka`.
     *
     * @param Ia The base color of the ambient light.
     */
    public AmbientLight(Color Ia) {
        this.intensity = Ia;
    }

    /**
     * Returns the light intensity factor.
     * @return The light intensity factor.
     */
    public Color getIntensity() {
        return intensity;
    }
}
