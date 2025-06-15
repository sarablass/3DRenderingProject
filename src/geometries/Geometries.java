package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * The Geometries class represents a collection of intersectable geometries in a 3D Cartesian coordinate system.
 * It implements the Composite Design Pattern, allowing multiple geometries to be treated as a single entity.
 * This class also implements the Intersectable interface to find intersections of a ray with the geometries.
 */
public class Geometries extends Intersectable{


    /**
     * A list containing all the geometries in the collection.
     */
    List<Intersectable> geometries= new LinkedList<Intersectable>();
    /**
     * Default constructor that initializes an empty collection of geometries.
     */
    public Geometries(){}
    /**
     * Constructor that initializes the collection with the given geometries.
     *
     * @param geometries one or more geometries to add to the collection
     */
    public Geometries(Intersectable... geometries) {
        add(geometries);
    }

    /**
     * Adds one or more geometries to the collection.
     *
     * @param geometries one or more geometries to add
     */
    public void add(Intersectable... geometries) {
        Collections.addAll(this.geometries, geometries); //add all the geometries to the list
    }

    @Override
    public List<Intersection> calculateIntersectionsHelper(Ray ray) {
        List<Intersection> intersections = null;
        for (Intersectable geometry : geometries) {
            List<Intersection> geoIntersections = geometry.calculateIntersectionsHelper(ray);
            if (geoIntersections != null) {
                if (intersections == null) {
                    intersections = new LinkedList<>();
                }
                intersections.addAll(geoIntersections);
            }
        }
        return intersections;
    }
}



