package com.fantasyhospital.util;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageUtils {

    /**
     * Charge une image depuis les ressources.
     *
     * @param path Le chemin relatif dans le dossier resources (ex : "/images/room/CloseDoor.png")
     * @return Une instance de Image
     */
    public static Image loadImage(String path) {
        return new Image(ImageUtils.class.getResource(path).toExternalForm());
    }

    /**
     * Crée un ImageView configuré à partir du chemin d'une image.
     *
     * @param path Le chemin relatif dans les ressources
     * @param width Largeur souhaitée
     * @param height Hauteur souhaitée
     * @return ImageView configuré
     */
    public static ImageView createImageView(String path, double width, double height) {
        Image image = loadImage(path);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        imageView.setPreserveRatio(true);
        return imageView;
    }

    public static ImageView createEmptyImageView(double width, double height, boolean visible) {
        ImageView imageView = new ImageView();
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        imageView.setPreserveRatio(true);
        imageView.setVisible(visible);
        return imageView;
    }

    public static void setImage(ImageView imageView, String path) {
        Image image = loadImage(path);
        imageView.setImage(image);
    }
}