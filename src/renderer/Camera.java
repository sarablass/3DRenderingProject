package renderer;

import renderer.PixelManager.Pixel;

import static primitives.Util.isZero;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import primitives.Point2D;
import scene.Scene;

import java.util.LinkedList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.stream.IntStream;

/**
 * The {@code Camera} class represents a virtual camera in 3D space,
 * defining its position, orientation, and the view plane properties.
 * It uses the Builder design pattern to allow flexible construction.
 */
public class Camera implements Cloneable {

    private Point p0;
    private Vector vTo, vUp, vRight;
    private double width = 0.0, height = 0.0, distance = 0.0;
    private ImageWriter imageWriter;
    private RayTracerBase rayTracer;
    private int Nx = 1;
    private int Ny = 1;

    // Anti-aliasing fields
    private boolean antiAliasingEnabled = false;
    private Blackboard blackboard;

    // Adaptive Super Sampling fields
    private boolean adaptiveSuperSamplingEnabled = false;
    private int adaptiveSuperSamplingDepth = 0;
    private double colorTolerance = 1.0;

    // Multithreading fields
    private int threadsCount = 0; // -2 auto, -1 range/stream, 0 no threads, 1+ number of threads
    private static final int SPARE_THREADS = 2; // Spare threads if trying to use all the cores
    private double printInterval = 0; // printing progress percentage interval (0 – no printing)
    private PixelManager pixelManager; // pixel manager object


    /**
     * Private constructor to enforce the use of the Builder pattern.
     */
    private Camera() {
    }

    /**
     * Gets a new instance of the {@link Builder} to create a {@code Camera}.
     *
     * @return a new Builder instance
     */
    public static Builder getBuilder() {
        return new Builder();
    }

    /**
     * Constructs a ray through a specific pixel (i, j) in the view plane.
     *
     * @param nX number of horizontal pixels
     * @param nY number of vertical pixels
     * @param j  column index (pixel)
     * @param i  row index (pixel)
     * @return the constructed ray through the pixel
     */
    public Ray constructRay(int nX, int nY, int j, int i) {
        double Ry = height / nY;
        double Rx = width / nX;
        Point pIJ = p0;

        double Yi = -(i - (nY - 1) / 2d) * Ry;
        double Xj = (j - (nX - 1) / 2d) * Rx;

        if (!isZero(Xj)) {
            pIJ = pIJ.add(vRight.scale(Xj));
        }
        if (!isZero(Yi)) {
            pIJ = pIJ.add(vUp.scale(Yi));
        }

        pIJ = pIJ.add(vTo.scale(distance));

        return new Ray(p0, pIJ.subtract(p0).normalize());
    }

    /**
     * This function renders image's pixel color map from the scene
     * included in the ray tracer object
     *
     * @return the camera object itself
     */
    public Camera renderImage() {
        pixelManager = new PixelManager(Nx, Ny, printInterval);
        return switch (threadsCount) {
            case 0 -> renderImageNoThreads();
            case -1 -> renderImageStream();
            default -> renderImageRawThreads();
        };
    }

    /**
     * Render image using multi-threading by parallel streaming
     *
     * @return the camera object itself
     */
    private Camera renderImageStream() {
        IntStream.range(0, Ny).parallel()
                .forEach(i -> IntStream.range(0, Nx).parallel()
                        .forEach(j -> castRay(j, i)));
        return this;
    }

    /**
     * Render image without multi-threading
     *
     * @return the camera object itself
     */
    private Camera renderImageNoThreads() {
        for (int i = 0; i < Nx; i++) {
            for (int j = 0; j < Ny; j++) {
                castRay(j, i);
            }
        }
        return this;
    }

    /**
     * Renders the image using raw threads for explicit multithreading.
     * This method creates a specified number of threads, each responsible for processing
     * a subset of the pixels. The threads are managed manually, providing more control
     * over resource allocation and execution.
     *
     * <p><b>Advantages:</b></p>
     * <ul>
     *     <li>Fine-grained control over thread management.</li>
     *     <li>Can be optimized for specific hardware or workloads.</li>
     * </ul>
     *
     * <p><b>Disadvantages:</b></p>
     * <ul>
     *     <li>Increased code complexity compared to parallel streams.</li>
     *     <li>Requires careful handling of thread synchronization and resource sharing.</li>
     * </ul>
     *
     * @return the camera object itself for method chaining
     */
    private Camera renderImageRawThreads() {
        var threads = new LinkedList<Thread>();
        while (threadsCount-- > 0)
            threads.add(new Thread(() -> {
                PixelManager.Pixel pixel;
                while ((pixel = pixelManager.nextPixel()) != null)
                    castRay(pixel.col(), pixel.row());
            }));
        for (var thread : threads) thread.start();
        try {
            for (var thread : threads) thread.join();
        } catch (InterruptedException ignored) {}
        return this;
    }


    /**
     * Prints a grid on the image with the specified interval and color.
     *
     * @param interval the interval at which to print the grid lines
     * @param color    the color of the grid lines
     * @return this Camera object
     */
     public Camera printGrid(int interval, Color color) {
        for (int i = 0; i < Nx; i++) {
            for (int j = 0; j < Ny; j++) {
                if (i % interval == 0 || j % interval == 0) {
                    imageWriter.writePixel(i, j, color);
                }
            }
        }
        return this;
    }

    /**
     * Writes the image to a file with the specified name.
     *
     * @param fileName the name of the file to write the image to
     * @return this Camera object
     */
    public Camera writeToImage(String fileName) {
        imageWriter.writeToImage(fileName);
        return this;
    }

    /**
     * Writes the color to the pixel.
     */
    private void castRay(int i, int j) {
        Color finalColor;

        if (adaptiveSuperSamplingEnabled) {
            // Use Adaptive Super Sampling
            finalColor = castRayAdaptive(j, i, 0, 0, 0, 1, 1);
        } else if (antiAliasingEnabled && blackboard != null) {
            // Use regular Anti-Aliasing
            List<Point2D> samples = blackboard.generateSamples();
            Color accumulatedColor = Color.BLACK;

            // Cast multiple rays per pixel and average the colors
            for (Point2D sample : samples) {
                Ray ray = constructRay(i, j, sample.getX(), sample.getY());
                Color sampleColor = rayTracer.traceRay(ray);
                accumulatedColor = accumulatedColor.add(sampleColor);
            }

            // Average the accumulated color
            finalColor = accumulatedColor.scale((double) 1 / (samples.size()));
        } else {
            // Single ray
            Ray ray = constructRay(Nx, Ny, j, i);
            finalColor = rayTracer.traceRay(ray);
        }

        imageWriter.writePixel(j, i, finalColor);
        if (pixelManager != null) {
            pixelManager.pixelDone();
        }
    }

    /**
     * Recursively casts rays for adaptive super sampling to determine the color of a pixel.
     * This method refines pixel sampling by dividing the pixel into sub-pixels and checking
     * color similarity to decide whether further subdivision is needed.
     *
     * @param j              the column index of the pixel
     * @param i              the row index of the pixel
     * @param currentDepth   the current recursion depth
     * @param subPixelX      the relative x-coordinate of the sub-pixel (0 to 1)
     * @param subPixelY      the relative y-coordinate of the sub-pixel (0 to 1)
     * @param subPixelWidth  the width of the sub-pixel
     * @param subPixelHeight the height of the sub-pixel
     * @return the color calculated for the pixel or sub-pixel
     */
    private Color castRayAdaptive(int j, int i, int currentDepth,
                                  double subPixelX, double subPixelY,
                                  double subPixelWidth, double subPixelHeight) {
        if (currentDepth >= adaptiveSuperSamplingDepth) {
            return sampleSubPixel(j, i, subPixelX + subPixelWidth / 2, subPixelY + subPixelHeight / 2);
        }

        Color topLeft = sampleSubPixel(j, i, subPixelX, subPixelY);
        Color topRight = sampleSubPixel(j, i, subPixelX + subPixelWidth, subPixelY);
        Color bottomLeft = sampleSubPixel(j, i, subPixelX, subPixelY + subPixelHeight);
        Color bottomRight = sampleSubPixel(j, i, subPixelX + subPixelWidth, subPixelY + subPixelHeight);

        if (colorsAreSimilar(topLeft, topRight, bottomLeft, bottomRight)) {
            return topLeft.add(topRight).add(bottomLeft).add(bottomRight).scale(0.25);
        }

        double halfWidth = subPixelWidth / 2;
        double halfHeight = subPixelHeight / 2;

        Color sub1 = castRayAdaptive(j, i, currentDepth + 1, subPixelX, subPixelY, halfWidth, halfHeight);
        Color sub2 = castRayAdaptive(j, i, currentDepth + 1, subPixelX + halfWidth, subPixelY, halfWidth, halfHeight);
        Color sub3 = castRayAdaptive(j, i, currentDepth + 1, subPixelX, subPixelY + halfHeight, halfWidth, halfHeight);
        Color sub4 = castRayAdaptive(j, i, currentDepth + 1, subPixelX + halfWidth, subPixelY + halfHeight, halfWidth, halfHeight);

        return sub1.add(sub2).add(sub3).add(sub4).scale(0.25);
    }

    /**
     * Samples the color of a sub-pixel at the specified coordinates.
     * This method calculates the color by constructing a ray through the sub-pixel
     * and tracing it using the ray tracer.
     *
     * @param j         the column index of the pixel
     * @param i         the row index of the pixel
     * @param subPixelX the relative x-coordinate within the pixel (0 to 1)
     * @param subPixelY the relative y-coordinate within the pixel (0 to 1)
     * @return the color sampled from the sub-pixel
     */
    private Color sampleSubPixel(int j, int i, double subPixelX, double subPixelY) {
        double Ry = height / Ny;
        double Rx = width / Nx;

        double Yi = -(i - (Ny - 1) / 2d) * Ry;
        double Xj = (j - (Nx - 1) / 2d) * Rx;

        double offsetX = (subPixelX - 0.5) * Rx;
        double offsetY = (subPixelY - 0.5) * Ry;

        Point pIJ = p0.add(vTo.scale(distance));
        if (!isZero(Xj + offsetX)) pIJ = pIJ.add(vRight.scale(Xj + offsetX));
        if (!isZero(Yi + offsetY)) pIJ = pIJ.add(vUp.scale(Yi + offsetY));

        Ray ray = new Ray(p0, pIJ.subtract(p0).normalize());
        return rayTracer.traceRay(ray);
    }

    /**
     * Checks if the given colors are similar based on the color distance.
     * The method calculates the average color and compares the distance
     * of each color to the average against the defined color threshold.
     *
     * @param c1 the first color
     * @param c2 the second color
     * @param c3 the third color
     * @param c4 the fourth color
     * @return true if all colors are similar, false otherwise
     */
    private boolean colorsAreSimilar(Color c1, Color c2, Color c3, Color c4) {
        return c1.colorDistance(c2) < colorTolerance &&
                c1.colorDistance(c3) < colorTolerance &&
                c1.colorDistance(c4) < colorTolerance &&
                c2.colorDistance(c3) < colorTolerance &&
                c2.colorDistance(c4) < colorTolerance &&
                c3.colorDistance(c4) < colorTolerance;
    }

    /**
     * Builder class to construct {@link Camera} instances using chained methods.
     */
    public static class Builder {

        private final Camera camera = new Camera();

        /**
         * Sets the camera's location in 3D space.
         *
         * @param p0 the camera location
         * @return the builder instance
         */
        public Builder setLocation(Point p0) {
            camera.p0 = p0;
            return this;
        }

        /**
         * Sets the direction of the camera using orthogonal vectors.
         *
         * @param vTo the "to" direction
         * @param vUp the "up" direction
         * @return the builder instance
         * @throws IllegalArgumentException if vectors are not orthogonal
         */
        public Builder setDirection(Vector vTo, Vector vUp) {
            if (!isZero(vTo.dotProduct(vUp))) {
                throw new IllegalArgumentException("the vectors must be orthogonal");
            }
            camera.vTo = vTo.normalize();
            camera.vUp = vUp.normalize();
            return this;
        }

        /**
         * Sets the direction of the camera based on a target point and an up vector.
         *
         * @param target the point the camera looks at
         * @param up     the up vector
         * @return the builder instance
         */
        public Builder setDirection(Point target, Vector up) {
            Vector vTo = target.subtract(camera.p0).normalize();
            Vector vRight = vTo.crossProduct(up).normalize();
            Vector vUp = vRight.crossProduct(vTo).normalize();

            camera.vTo = vTo;
            camera.vUp = vUp;
            camera.vRight = vRight;
            return this;
        }

        /**
         * Sets the direction of the camera using a target point and a default up vector (Y-axis).
         *
         * @param target the point the camera looks at
         * @return the builder instance
         * @throws IllegalArgumentException if the calculated direction is invalid
         */
        public Builder setDirection(Point target) {
            Vector defaultUp = new Vector(0, 1, 0);
            Vector vTo = target.subtract(camera.p0).normalize();

            if (isZero(vTo.dotProduct(defaultUp))) {
                throw new IllegalArgumentException("vTo cannot be parallel to the default up vector");
            }

            Vector vRight = vTo.crossProduct(defaultUp).normalize();
            Vector vUp = vRight.crossProduct(vTo).normalize();

            camera.vTo = vTo;
            camera.vUp = vUp;
            camera.vRight = vRight;

            return this;
        }

        /**
         * Sets the view plane size.
         *
         * @param width  the width of the view plane
         * @param height the height of the view plane
         * @return the builder instance
         * @throws IllegalArgumentException if width or height is not positive
         */
        public Builder setVpSize(double width, double height) {
            if (width <= 0 || height <= 0) {
                throw new IllegalArgumentException("width and height must be positive");
            }
            camera.width = width;
            camera.height = height;
            return this;
        }

        /**
         * Sets the distance from the camera to the view plane.
         *
         * @param distance the view plane distance
         * @return the builder instance
         * @throws IllegalArgumentException if distance is not positive
         */
        public Builder setVpDistance(double distance) {
            if (distance <= 0) {
                throw new IllegalArgumentException("distance must be positive");
            }
            camera.distance = distance;
            return this;
        }

        /**
         * Set multi-threading <br>
         * Parameter value meaning:
         * <ul>
         * <li>-2 - number of threads is number of logical processors less 2</li>
         * <li>-1 - stream processing parallelization (implicit multi-threading) is used</li>
         * <li>0 - multi-threading is not activated</li>
         * <li>1 and more - literally number of threads</li>
         * </ul>
         *
         * @param threads number of threads
         * @return builder object itself
         */
        public Builder setMultithreading(int threads) {
            if (threads < -3)
                throw new IllegalArgumentException("Multithreading parameter must be -2 or higher");
            if (threads == -2) {
                int cores = Runtime.getRuntime().availableProcessors() - SPARE_THREADS;
                camera.threadsCount = cores <= 2 ? 1 : cores;
            } else
                camera.threadsCount = threads;
            return this;
        }

        /**
         * Set debug printing interval. If it's zero - there won't be printing at all
         *
         * @param interval printing interval in %
         * @return builder object itself
         */
        public Builder setDebugPrint(double interval) {
            if (interval < 0) throw new IllegalArgumentException("interval parameter must be non-negative");
            camera.printInterval = interval;
            return this;
        }

        /**
         * Sets the resolution of the camera.
         *
         * @param nX the number of pixels in the x direction
         * @param nY the number of pixels in the y direction
         */
        public Builder setResolution(int nX, int nY) {
            camera.imageWriter = new ImageWriter(nX, nY);
            camera.Nx = nX;
            camera.Ny = nY;
            return this;
        }

        public Builder setRayTracer(Scene scene, RayTracerType rayTracerType) {
            switch (rayTracerType) {
                case SIMPLE:
                    camera.rayTracer = new SimpleRayTracer(scene);
                    break;
                default:
                    camera.rayTracer = null;
                    //throw new IllegalArgumentException("Invalid ray tracer type");
            }
            return this;
        }

        public Builder setAntiAliasing(SamplingType samplingType, int resolution) {
            if (resolution <= 0) {
                throw new IllegalArgumentException("Anti-aliasing resolution must be positive");
            }
            camera.antiAliasingEnabled = true;
            camera.blackboard = new Blackboard(samplingType, resolution);
            return this;
        }

        /**
         * Disable anti-aliasing
         *
         * @return Builder instance for chaining
         */
        public Builder disableAntiAliasing() {
            camera.antiAliasingEnabled = false;
            camera.blackboard = null;
            return this;
        }

        /**
         * Enables adaptive super sampling for the camera and sets the depth and color threshold.
         * Adaptive super sampling improves image quality by refining pixel sampling
         * based on color differences within sub-pixels.
         *
         * @param depth     the maximum depth of adaptive sampling recursion
         * @param threshold the color difference threshold for adaptive sampling
         * @return the camera object itself for method chaining
         */
        public Builder setAdaptiveSuperSampling(int depth, double threshold) {

            camera.adaptiveSuperSamplingEnabled = depth > 0;
            camera.adaptiveSuperSamplingDepth = depth;
            camera.colorTolerance = threshold;
            return this;
        }

        /**
         * Enables adaptive super sampling for the camera with a specified depth.
         * Uses the default color difference threshold to refine pixel sampling
         * and improve image quality.
         *
         * @param depth the maximum depth of adaptive sampling recursion
         * @return the camera object itself for method chaining
         */
        public Builder setAdaptiveSuperSampling(int depth) {
            return setAdaptiveSuperSampling(depth, camera.colorTolerance);
        }

        /**
         * Finalizes the camera construction after validation.
         *
         * @return the constructed Camera instance
         * @throws MissingResourceException if any required field is missing
         * @throws IllegalArgumentException if direction vectors are not valid
         */
        public Camera build() {
            final String description = "Missing rendering data";
            final String className = "Camera";

            if (camera.p0 == null) throw new MissingResourceException(description, className, "p0");
            if (camera.vTo == null) throw new MissingResourceException(description, className, "vTo");
            if (camera.vUp == null) throw new MissingResourceException(description, className, "vUp");
            if (isZero(camera.width)) throw new MissingResourceException(description, className, "width");
            if (isZero(camera.height)) throw new MissingResourceException(description, className, "height");
            if (isZero(camera.distance)) throw new MissingResourceException(description, className, "distance");

            if (camera.width < 0) throw new IllegalArgumentException("width must be positive");
            if (camera.height < 0) throw new IllegalArgumentException("height must be positive");
            if (camera.distance < 0) throw new IllegalArgumentException("distance must be positive");

            camera.vRight = (camera.vTo.crossProduct(camera.vUp)).normalize();

            if (!isZero(camera.vTo.dotProduct(camera.vRight)) ||
                    !isZero(camera.vTo.dotProduct(camera.vUp)) ||
                    !isZero(camera.vUp.dotProduct(camera.vRight))) {
                throw new IllegalArgumentException("The 3 vectors must be orthogonal");
            }

            if (!isZero(camera.vTo.length() - 1) ||
                    !isZero(camera.vRight.length() - 1) ||
                    !isZero(camera.vUp.length() - 1)) {
                throw new IllegalArgumentException("The 3 vectors must be normalized");
            }
            if (camera.Nx <= 0) throw new MissingResourceException(description, className, "nX");
            if (camera.Ny <= 0) throw new MissingResourceException(description, className, "ny");
            if (camera.rayTracer == null) {
                camera.rayTracer = new SimpleRayTracer(null);
            }

            try {
                return (Camera) camera.clone();
            } catch (CloneNotSupportedException exception) {
                throw new RuntimeException(exception);
            }
        }

    }

    /**
     * Constructs a ray from the camera through a sub-pixel position on the view plane.
     *
     * @param i       pixel row index
     * @param j       pixel column index
     * @param offsetX relative horizontal offset within the pixel (0 to 1)
     * @param offsetY relative vertical offset within the pixel (0 to 1)
     * @return        a ray passing through the specified sub-pixel point
     */
    public Ray constructRay(int i, int j, double offsetX, double offsetY) {
        // Pixel dimensions
        double rY = height / Ny;
        double rX = width / Nx;

        // Center of the view plane
        Point pc = p0.add(vTo.scale(distance));

        // Calculate pixel position with sub-pixel offset
        double xJ = (j - (Nx - 1) / 2.0 + offsetX - 0.5) * rX;
        double yI = -(i - (Ny - 1) / 2.0 + offsetY - 0.5) * rY;

        Point pij = pc;
        if (!isZero(xJ)) {
            pij = pij.add(vRight.scale(xJ));
        }
        if (!isZero(yI)) {
            pij = pij.add(vUp.scale(yI));
        }

        Vector Vij = pij.subtract(p0);
        return new Ray(p0, Vij);
    }
}
