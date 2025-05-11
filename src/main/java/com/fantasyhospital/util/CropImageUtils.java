package com.fantasyhospital.util;
import javafx.scene.image.*;

public class CropImageUtils {

    public static Image cropImage(Image transparentImage) {

        WritableImage writableImage = new WritableImage((int) transparentImage.getWidth(), (int) transparentImage.getHeight());
        PixelReader reader = transparentImage.getPixelReader();
        for (int x = 0; x < writableImage.getWidth(); x++) {
            for (int y = 0; y < writableImage.getHeight(); y++) {
                writableImage.getPixelWriter().setArgb(x, y, reader.getArgb(x, y));
            }
        }


        int left = (int) writableImage.getWidth(), right = 0, top = (int) writableImage.getHeight(), bottom = 0;

        for (int x = 0; x < writableImage.getWidth(); x++) {
            for (int y = 0; y < writableImage.getHeight(); y++) {
                if (writableImage.getPixelReader().getArgb(x, y) != 0x00000000) {
                    left = Math.min(left, x);
                    right = Math.max(right, x);
                    top = Math.min(top, y);
                    bottom = Math.max(bottom, y);
                }
            }
        }

        int width = right - left;
        int height = bottom - top;


        if (width > 0 && height > 0) {
            WritableImage croppedWritableImage = new WritableImage(width, height);
            PixelWriter writer = croppedWritableImage.getPixelWriter();
            for (int x = left; x < right; x++) {
                for (int y = top; y < bottom; y++) {
                    writer.setArgb(x - left, y - top, writableImage.getPixelReader().getArgb(x, y));
                }
            }

            return croppedWritableImage;
        } else {
            return writableImage;
        }
    }
}
