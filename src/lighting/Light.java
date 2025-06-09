package lighting;

import primitives.Color;

/**
 * Abstract class representing a light source in a scene.
 */
abstract class Light {
    /**
     * The intensity of the light source.
     */
    protected final Color intensity;

    /**
     * Constructs a light source with the given intensity.
     *
     * @param intensity The intensity of the light source.
     */
    protected Light(Color intensity) {
        this.intensity = intensity;
    }

    /**
     * Getter for the intensity of the light source.
     *
     * @return The intensity of the light source.
     */
    public Color getIntensity() {
        return intensity;
    }
}
