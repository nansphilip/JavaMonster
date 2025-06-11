package com.fantasyhospital.util;
import javafx.scene.image.*;

/**
 * Utility class for removing a specific background color (magenta) from PNG images.
 * It replaces the magenta color with transparency, effectively removing it from the image.
 */
public class RemovePngBackgroundUtils {

    /**
     * Removes the magenta background from the given image.
     *
     * @param image the image from which to remove the magenta background
     * @return a new image with the magenta background removed
     */
    public static Image removePngBackground(Image image) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        WritableImage transparentImage = new WritableImage(width, height);
        PixelReader reader = image.getPixelReader();
        PixelWriter writer = transparentImage.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int argb = reader.getArgb(x, y);
                int red = (argb >> 16) & 0xFF;
                int green = (argb >> 8) & 0xFF;
                int blue = argb & 0xFF;

                // Fond violet (magenta) à supprimer
                if (red == 255 && green == 0 && blue == 255) {
                    writer.setArgb(x, y, 0x00000000); // transparent
                } else {
                    writer.setArgb(x, y, argb);
                }
            }
        }
        return transparentImage;
    }
}
