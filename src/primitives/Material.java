package primitives;

public class Material {
    public Double3 kA= Double3.ONE;

    /**
     * setter for kD- the diffuse reflection coefficient.
     * @param kA
     * @return Material after setting kD
     */
    public Material setKa(Double3 kA) {
        this.kA = kA;
        return this;
    }

    /**
     * Setter for kD- the diffuse reflection coefficient.
     * @param kA
     * @return Material after setting kD
     */
    public Material setKa(double kA) {
        this.kA = new Double3(kA);
        return this;
    }

}
