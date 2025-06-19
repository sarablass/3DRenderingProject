package primitives;

public class Material {
    /**
     * The ambient reflection coefficient.
     * <p>
     * This determines the proportion of ambient light reflected by the material.
     * Ambient light is uniform and does not depend on the direction of the light source.
     */
    public Double3 kA = Double3.ONE;

    /**
     * The diffuse reflection coefficient.
     * <p>
     * This determines the proportion of diffuse light reflected by the material.
     * Diffuse reflection depends on the angle between the light source and the surface normal.
     */
    public Double3 kD = Double3.ZERO;

    /**
     * The specular reflection coefficient.
     * <p>
     * This determines the proportion of specular light reflected by the material.
     * Specular reflection creates highlights and depends on the angle between the viewer and the reflection direction.
     */
    public Double3 kS = Double3.ZERO;

    /**
     * The shininess factor.
     * <p>
     * This determines the sharpness of specular highlights. Higher values result in smaller, sharper highlights.
     */
    public int nShininess = 0;

    /**
     * The transparency coefficient.
     * <p>
     * This determines the proportion of light that passes through the material (refraction).
     * A value of 0 means the material is opaque, while higher values indicate greater transparency.
     */
    public Double3 kT = Double3.ZERO;

    /**
     * The reflection coefficient.
     * <p>
     * This determines the proportion of light that is reflected by the material (mirror-like reflection).
     * A value of 0 means no reflection, while higher values indicate greater reflectivity.
     */
    public Double3 kR = Double3.ZERO;

    /**
     * setter for kA- the ambient reflection coefficient.
     * @param kA
     * @return Material after setting kA
     */
    public Material setKA(Double3 kA) {
        this.kA = kA;
        return this;
    }

    /**
     * Setter for kA- the ambient reflection coefficient.
     * @param kA
     * @return Material after setting kA
     */
    public Material setKA(double kA) {
        this.kA = new Double3(kA);
        return this;
    }
    /**
     * setter for kD- the diffuse reflection coefficient.
     * @param kD
     * @return Material after setting kD
     */
    public Material setKD(Double3 kD) {
        this.kD = kD;
        return this;
    }

    /**
     * Setter for kD- the diffuse reflection coefficient.
     * @param kD
     * @return Material after setting kD
     */
    public Material setKD(double kD) {
        this.kD = new Double3(kD);
        return this;
    }
    /**
     * Setter for kS- the specular reflection coefficient.
     * @param kS
     * @return Material after setting kS
     */
    public Material setKS(Double3 kS) {
        this.kS = kS;
        return this;
    }

    /**
     * Setter for kS- the specular reflection coefficient.
     * @param kS
     * @return Material after setting kS
     */
    public Material setKS(double kS) {
        this.kS = new Double3(kS);
        return this;
    }

    /**
     * Setter for nShininess- the shininess coefficient.
     * @param nShininess
     * @return Material after setting nShininess
     */
    public Material setShininess(int nShininess) {
        this.nShininess = nShininess;
        return this;
    }

    /**
     * Sets the transparency coefficient using a `Double3` value.
     *
     * @param kT The transparency coefficient.
     * @return The current `Material` instance (for method chaining).
     */
    public Material setKT(Double3 kT) {
        this.kT = kT;
        return this;
    }

    /**
     * Sets the transparency coefficient using a single double value.
     *
     * @param value The transparency coefficient.
     * @return The current `Material` instance (for method chaining).
     */
    public Material setKT(double value) {
        this.kT = new Double3(value);
        return this;
    }

    /**
     * Sets the reflection coefficient using a `Double3` value.
     *
     * @param kR The reflection coefficient.
     * @return The current `Material` instance (for method chaining).
     */
    public Material setKR(Double3 kR) {
        this.kR = kR;
        return this;
    }

    /**
     * Sets the reflection coefficient using a single double value.
     *
     * @param value The reflection coefficient.
     * @return The current `Material` instance (for method chaining).
     */
    public Material setKR(double value) {
        this.kR = new Double3(value);
        return this;
    }
}
