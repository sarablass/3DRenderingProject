package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Geometries implements Intersectable{


    List<Intersectable> geometries= new LinkedList<Intersectable>();
    public Geometries(){}
    public Geometries(Intersectable... geometries) {
        add(geometries);
    }

    public void add(Intersectable... geometries) {
        Collections.addAll(this.geometries, geometries); //add all the geometries to the list
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        List<Point> result = null;
        for (Intersectable geometry : geometries) {
            if(geometry.findIntersections(ray)!=null){
                List<Point> tempIntersections = geometry.findIntersections(ray);
                if (tempIntersections != null) {
                    if (result == null) {
                        result = new LinkedList<>();
                    }
                    result.addAll(tempIntersections);
                }
            }
        }


        return result;
    }
}
