package lighting;

import primitives.Color;
import primitives.Double3;

public class AmbientLight extends Light {
    public static final AmbientLight NONE = new AmbientLight(Color.BLACK);

    /**
     * Constructs an AmbientLight object with the given intensity.
     * The intensity is calculated by scaling the given color by the factor `ka`.
     *
     * @param Ia The base color of the ambient light.
     */
    public AmbientLight(Color Ia) {
          super(Ia);
    }

}
