package lighting;

import primitives.Color;
import primitives.Double3;

public class AmbientLight extends Light {
    public static final AmbientLight NONE = new AmbientLight(Color.BLACK);

    /**
     * Constructor for AmbientLight.
     * @param IA the intensity of the ambient light
     */
    public AmbientLight(Color IA) {
        super(IA);
    }

}
