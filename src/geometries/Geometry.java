package geometries;

import primitives.Point;
import primitives.Vector;
import primitives.Color;
import primitives.Material;

/**
 * The Geometry interface represents a geometric shape in 3D space.
 */
public abstract class Geometry extends Intersectable {

    protected Color emission= new Color(java.awt.Color.BLACK);
    private Material material= new Material();
    /**
     * Computes and returns the normal vector to the geometry at a given point.
     * @param p The point at which to compute the normal vector.
     * @return The normal vector to the geometry at the given point.
     */
    public abstract Vector getNormal(Point p);

    /**
     * Getter for the emission color of the geometry
     * @return The emission color of the geometry
     */
    public Color getEmission() {
        return emission;
    }

    /**
     * Setter for the emission color of the geometry
     * @param emission The emission color of the geometry
     * @return The geometry object
     */
    public Geometry setEmission(Color emission) {
        this.emission = emission;
        return this;
    }

    /**
     * Getter for the material of the geometry
     * @return The material of the geometry
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * Setter for the material of the geometry
     * @param material The material of the geometry
     * @return The geometry object
     */
    public Geometry setMaterial(Material material) {
        this.material = material;
        return this;
    }
}