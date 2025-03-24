package geometries;

import primitives.Point;
import primitives.Vector;

public class Plane extends Geometry {
    public Plane(Point v0, Point v1, Point v2) {
    }

    public Vector getNormal() {
        return null;
    }

    @Override
    public Vector getNormal(Point point) {
        return null;
    }
}