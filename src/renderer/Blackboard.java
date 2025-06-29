package renderer;

import primitives.Point2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Blackboard {
    private final int resolution;
    private final SamplingType type;
    private final Random rand = new Random();

    public Blackboard(SamplingType type, int resolution) {
        this.type = type;
        this.resolution = resolution;
    }

    public List<Point2D> generateSamples() {
        List<Point2D> samples = new ArrayList<>();
        int totalSamples = resolution * resolution;

        switch (type) {
            case GRID:
                // Regular grid - samples at center of each sub-pixel
                for (int i = 0; i < resolution; i++) {
                    for (int j = 0; j < resolution; j++) {
                        double x = (j + 0.5) / resolution;
                        double y = (i + 0.5) / resolution;
                        samples.add(new Point2D(x, y));
                    }
                }
                break;

            case RANDOM:
                // Purely random sampling
                for (int i = 0; i < totalSamples; i++) {
                    double x = rand.nextDouble();
                    double y = rand.nextDouble();
                    samples.add(new Point2D(x, y));
                }
                break;

            case JITTERED:
                // Stratified/jittered sampling - best quality
                for (int i = 0; i < resolution; i++) {
                    for (int j = 0; j < resolution; j++) {
                        double x = (j + rand.nextDouble()) / resolution;
                        double y = (i + rand.nextDouble()) / resolution;
                        samples.add(new Point2D(x, y));
                    }
                }
                break;
        }

        return samples;
    }

//    public int getTotalSamples() {
//        return resolution * resolution;
//    }
}