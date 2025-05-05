package renderer;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.MissingResourceException;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;
public class Camera implements Cloneable {
    private Point p0;
    private Vector vTo, vUp, vRight;
    private double width = 0.0, height = 0.0, distance = 0.0;
    private Camera() {}
    public static Builder getBuilder() {
        return new Builder();
    }
    public Ray constructRay(int nX,int nY, int j, int i){
        return null;
    }
    public static class Builder {

        private final Camera camera = new Camera();


        public Builder setLocation(Point p0) {
        camera.p0 = p0;
        return this;
    }

        public Builder setDirection(Vector vTo, Vector vUp) {
        if (!isZero(vTo.dotProduct(vUp))) {
            throw new IllegalArgumentException("the vectors must be orthogonal");
        }
        camera.vTo = vTo.normalize();
        camera.vUp = vUp.normalize();
        return this;
    }

        public Builder setVpSize(double width, double height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("width and height must be positive");
        }
        camera.width = width;
        camera.height = height;
        return this;
    }

        public Builder setVpDistance(double distance) {
            if (distance <= 0) {
                throw new IllegalArgumentException("distance must be positive");
            }
            camera.distance= distance;
            return this;
        }
        public Builder setResolution(int nX  ,int  nY ) {

            return null;
        }

        public Camera build() {

        final String description = "Missing rendering data";
        final String className = "Camera";
        if (camera.p0 == null) {
            throw new MissingResourceException(description, className, "p0");
        }
        if (camera.vTo == null) {
            throw new MissingResourceException(description, className, "vTo");
        }
        if (camera.vUp == null) {
            throw new MissingResourceException(description, className, "vUp");
        }
        if (isZero(camera.width)) {
            throw new MissingResourceException(description, className, "width");
        }
        if (isZero(camera.height)) {
            throw new MissingResourceException(description, className, "height");
        }
        if (isZero(camera.distance)) {
            throw new MissingResourceException(description, className, "distance");
        }

        if (camera.width < 0) {
            throw new IllegalArgumentException("width must be positive");
        }
        if (camera.height < 0) {
            throw new IllegalArgumentException("height must be positive");
        }
        if (camera.distance < 0) {
            throw new IllegalArgumentException("distance must be positive");
        }
        camera.vRight = (camera.vTo.crossProduct(camera.vUp)).normalize();

        if (!isZero(camera.vTo.dotProduct(camera.vRight)) || !isZero(camera.vTo.dotProduct(camera.vUp)) || !isZero(camera.vUp.dotProduct(camera.vRight))) {
            throw new IllegalArgumentException("The 3 vectors must be orthogonal");
        }
        if (camera.vTo.length() != 1 || camera.vRight.length() != 1 || camera.vUp.length() != 1) {
            throw new IllegalArgumentException("The 3 vectors must be normalized");
        }
        try {
            return (Camera) camera.clone();
        } catch (CloneNotSupportedException exception) {
            throw new RuntimeException(exception);
        }

    }

    }
}
