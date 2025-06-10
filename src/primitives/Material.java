package primitives;

public class Material {
    //למה היה את המתשנה הזה קיים?
    public Double3 kA= Double3.ONE,kD= Double3.ZERO, kS= Double3.ZERO;
    public int nShininess=0;


    /**
     * setter for kA- the ambient reflection coefficient.
     * @param kA
     * @return Material after setting kA
     */
    public Material setKa(Double3 kA) {
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
}
