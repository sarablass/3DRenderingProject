package renderer;

import org.junit.jupiter.api.Test;
import primitives.Color;

class ImageWriterTests {
    //==== the size of the view plane =====//
    int nX = 800;
    int nY = 500;

    //Color yellowColor = new Color(java.awt.Color.YELLOW);

    Color redColor = new Color(java.awt.Color.RED); // if r=255 the color is red (for the net)
    Color yellowColor = new Color(java.awt.Color.YELLOW); // if r=255 the color is red (for the net)

    /**
     * Test method for {@link renderer.ImageWriter#writeToImage(String)}.
     */
    @Test
    void testWriteToImage() {
        ImageWriter imageWriter = new ImageWriter(nX, nY);
        //=== running on the view plane===//
        for (int i = 0; i < nX; i++) {
            for (int j = 0; j < nY; j++) {
                //=== create the net ===//
                if (i % 50 == 0 || j % 50 == 0) { //we want the squares to be 10x16 so every 50 pixels we change the color- 50*10=500, 50*16=800
                    imageWriter.writePixel(i, j, redColor);
                } else {
                    imageWriter.writePixel(i, j, yellowColor);
                }
            }


        }
        imageWriter.writeToImage("redYellow"); //write the image
    }
}