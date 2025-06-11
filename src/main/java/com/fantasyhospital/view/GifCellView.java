package com.fantasyhospital.view;

import com.fantasyhospital.controller.DoomController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.Random;

/**
 * GifCellView is a utility class that provides a method to display a looping GIF animation in the DoomController's doom box.
 * It randomly selects a GIF from a predefined list and updates the ImageView at regular intervals.
 */
public class GifCellView {

    private static final Random random = new Random();
    private static Timeline gifLoop;

    private static final String[] gifPaths = {
            "/images/gif/Creature1.gif",
            "/images/gif/Creature2.gif",
            "/images/gif/Creature3.gif",
            "/images/gif/Creature4.gif",
            "/images/gif/Creature5.gif",
            "/images/gif/Creature6.gif",
            "/images/gif/Creature8.gif",
            "/images/gif/Heal.gif",
    };

    /**
     * Starts a looping GIF animation in the DoomController's doom box.
     * It randomly selects a GIF from the predefined list and updates the ImageView every 3 seconds.
     *
     * @param doomController the DoomController instance to update the doom box
     */
    public static void startLoop(DoomController doomController) {
        ImageView imageView = new ImageView();
        imageView.setFitWidth(128);
        imageView.setFitHeight(128);
        imageView.setPreserveRatio(true);

        VBox content = new VBox(10, imageView);
        content.setAlignment(Pos.CENTER);

        doomController.getDoomBox().getChildren().clear();
        doomController.getDoomBox().getChildren().add(content);

        gifLoop = new Timeline(
                new KeyFrame(Duration.ZERO, event -> {
                    String randomGif = gifPaths[random.nextInt(gifPaths.length)];
                    Image gif = new Image(GifCellView.class.getResource(randomGif).toExternalForm());
                    imageView.setImage(gif);
                }),
                new KeyFrame(Duration.seconds(3))
        );
        gifLoop.setCycleCount(Timeline.INDEFINITE);
        gifLoop.play();
    }

    public static void stopLoop() {
        if (gifLoop != null) {
            gifLoop.stop();
        }
    }
}